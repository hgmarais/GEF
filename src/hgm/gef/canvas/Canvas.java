package hgm.gef.canvas;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import hgm.gef.Style;
import hgm.gef.fig.Bounds;
import hgm.gef.fig.LayerFig;
import hgm.gef.layer.Layer;
import hgm.gef.layer.LayerManager;
import hgm.gef.property.PropertyOwner;
import hgm.gef.property.ProxyPropertyOwner;
import hgm.gef.selection.SelectionManager;
import hgm.gef.util.Unit;

public class Canvas implements PropertyOwner {
	
	public static final String ZOOM = "ZOOM";
	
	public static final String SCREEN_WIDTH = "SWIDTH";
	
	public static final String SCREEN_HEIGHT = "SHEIGHT";
	
	public static final String LEFT = "LEFT";
	
	public static final String TOP = "TOP";
	
	public static final String MOUSE_POSITION = "MOUSE_POSITION";
	
	private LayerManager layerManager;
	
	private SelectionManager selectionManager;
	
	private Bounds mCanvasBounds = new Bounds(0, 0, 0, 0);
	
//	private double mLeft = 0.0;
//	
//	private double mTop = 0.0;
	
	private CoordSystem coordSystem;
	
//	private int sWidth;
//	
//	private int sHeight;
//	
//	private double zoom = 1.0;
	
	private double xPixelsPerModel = 1.0;
	
	private double yPixelsPerModel = 1.0;
	
	private boolean applyingBehaviours = false;
	
	private Point mousePosition;
	
	private List<Behaviour> behaviours = new LinkedList<>();
	
	private LinkedList<CanvasListener> listeners = new LinkedList<>();
	
	private ProxyPropertyOwner propertyOwner = new ProxyPropertyOwner(this);
	
	public Canvas(CoordSystem coordSystem) {
		this.coordSystem = coordSystem;
		layerManager = new LayerManager(this);
		selectionManager = new SelectionManager(this);
		setProperty(ZOOM, 1.0);
		setProperty(LEFT, 0.0);
		setProperty(TOP, 0.0);
		setProperty(SCREEN_WIDTH, 1);
		setProperty(SCREEN_HEIGHT, 1);
	}
	
	@Override
	public PropertyOwner getPropertyOwner() {
		return propertyOwner;
	}
	
	public void addBehaviour(Behaviour behaviour) {
		behaviours.add(behaviour);
		applyBehaviours();
	}
	
	public void applyBehaviours() {
		if (applyingBehaviours) {
			return;
		}
		
		applyingBehaviours = true;
		
		try {
			behaviours.forEach(b -> b.apply(this));
		} finally {
			applyingBehaviours = false;
		}
	}
	
	public void setXPixelsPerModel(double xPixelsPerModel) {
		this.xPixelsPerModel = xPixelsPerModel;
		fireConverterChanged();
		applyBehaviours();
	}
	
	public void setYPixelsPerModel(double yPixelsPerModel) {
		this.yPixelsPerModel = yPixelsPerModel;
		fireConverterChanged();
		applyBehaviours();
	}
	
	
	public CoordSystem getCoordSystem() {
		return coordSystem;
	}
	
	public LayerManager getLayerManager() {
		return layerManager;
	}
	
	public SelectionManager getSelectionManager() {
		return selectionManager;
	}
	
	public void setBounds(Bounds mBounds) {
		if ((mBounds == null) || mCanvasBounds.equals(mBounds)) {
			return;
		}
		
		mCanvasBounds = mBounds;
		fireCanvasBoundsChanged();
		applyBehaviours();
	}
	
	public void setMousePosition(Point mousePosition) {
		this.mousePosition = mousePosition;
		firePropertyChanged(MOUSE_POSITION);
	}
	
	public Point getMousePosition() {
		return mousePosition;
	}
	
	public void setScreenSize(int sWidth, int sHeight) {
		int sw = getScreenWidth();
		int sh = getScreenHeight();
		
		if ((sw == sWidth) && (sh == sHeight)) {
			return;
		}
		
		setProperty(SCREEN_WIDTH, sWidth);
		setProperty(SCREEN_HEIGHT, sHeight);
		
		fireVisibleBoundsChanged();
		applyBehaviours();
	}
	
	public int getScreenWidth() {
		return (Integer)getProperty(SCREEN_WIDTH);
	}
	
	public int getScreenHeight() {
		return (Integer)getProperty(SCREEN_HEIGHT);
	}
	
	public void setLeft(double mLeft) {
		setOffset(mLeft, getTop());
	}
	
	public void setTop(double mTop) {
		setOffset(getLeft(), mTop);
	}
	
	public double getLeft() {
		return (Double)getProperty(LEFT);
	}
	
	public double getTop() {
		return (Double)getProperty(TOP);
	}
	
	public Bounds getBounds() {
		return mCanvasBounds;
	}
	
	public Bounds getVisibleBounds() {
		double mLeft = getLeft();
		double mTop = getTop();
		return new Bounds(mLeft, mTop, mLeft + wScreenToModel(getScreenWidth()), mTop + hScreenToModel(getScreenHeight()));
	}
	
	public Point2D getVisibleCenter() {
		return getVisibleBounds().getCenter();
	}
	
	public void setVisibleCenter(Point2D mPoint) {
		setVisibleCenter(mPoint.getX(), mPoint.getY());
	}
	
	public void setVisibleCenter(Double mx, Double my) {
		Point2D center = getVisibleCenter();
		double dx = 0.0;
		double dy = 0.0;
		
		if (mx != null) {
			dx = mx - center.getX();
		}
		
		if (my != null) {
			dy = my - center.getY();
		}
		
		adjustOffset(dx, dy);
	}
	
	public void centerOnOrigin() {
		setVisibleCenter(0.0, 0.0);
	}
	
	public void zoomFitCanvas() {
		zoomFit(getBounds());
	}
	
	public void zoomFitLayers() {
		zoomFit(layerManager.getBounds());
	}

	public void zoomFit(Bounds mb) {
		if (mb == null) {
			return;
		}
		
		Bounds mVisibleBounds = getVisibleBounds();
		double mvw = mVisibleBounds.getWidth();
		double mvh = mVisibleBounds.getHeight();
		double mw = mb.getWidth();
		double mh = mb.getHeight();
		
		if ((mvw == 0.0) || (mvh == 0.0) || (mw == 0.0) || (mh == 0.0)) {
			return;
		}
		
		double wRatio = mvw / mw;
		double hRatio = mvh / mh;
		
		setZoom(getZoom() * Math.min(wRatio, hRatio));
		setOffset(coordSystem.getLeft(mb), coordSystem.getTop(mb));
	}

	public void adjustOffset(double mx, double my) {
		setOffset(getLeft() + coordSystem.horizontal(mx), getTop() + coordSystem.vertical(my));
	}
	
	public void setOffset(double mx, double my) {
		double mLeft = getLeft();
		double mTop = getTop();
		
		if ((mLeft == mx) && (mTop == my)) {
			return;
		}
		
		double dx = mx - mLeft;
		double dy = my - mTop;
		
		setProperty(LEFT, mx);
		setProperty(TOP, my);
		
		fireOffsetChanged(dx, dy);
		fireVisibleBoundsChanged();
		applyBehaviours();
	}
	
	public void setZoom(double zoom) {
		double currentZoom = getZoom();
		if (currentZoom == zoom) {
			return;
		}
		
		if (zoom > 0.0) {
			setProperty(ZOOM, zoom);
			applyBehaviours();
		}
	}
	
	public void adjustZoom(double dz) {
		setZoom(getZoom() + dz);
	}
	
	public void adjustZoomAround(double dz, double mx, double my) {
		setZoomAround(getZoom() + dz, mx, my);
	}
	
	public double getZoom() {
		return (Double)getProperty(ZOOM);
	}
	
	public void setZoomAroundVisibleCenter(double zoom) {
		setZoomAround(zoom, getVisibleBounds().getCenter());
	}
	
	public void setZoomAround(double zoom, Point2D mp) {
		setZoomAround(zoom, mp.getX(), mp.getY());
	}
	
	public void setZoomAround(double zoom, double mx, double my) {
		double sx = xModelToScreen(mx);
		double sy = yModelToScreen(my);
		
		setZoom(zoom);
		
		double mx2 = xScreenToModel(sx);
		double my2 = yScreenToModel(sy);

		adjustOffset(coordSystem.horizontal(mx) - coordSystem.horizontal(mx2), coordSystem.vertical(my) - coordSystem.vertical(my2));
	}
	
	public double xScreenPerModel() {
		return xPixelToModel(1) * getZoom();
	}
	
	public double yScreenPerModel() {
		return yPixelToModel(1) * getZoom();
	}
	
	public double xModelPerScreen() {
		return 1.0 / xScreenPerModel();
	}
	
	public double yModelPerScreen() {
		return 1.0 / yScreenPerModel();
	}
	
	public double xModelToScreen(double m) {
		return coordSystem.horizontal((m - getLeft()) / xModelPerScreen());
	}
	
	public double wModelToScreen(double m) {
		return m * xScreenPerModel();
	}
		
	public double hModelToScreen(double m) {
		return m * yScreenPerModel();
	}
	
	public double wPixelToModel(double p) {
		return p / xPixelsPerModel;
	}
	
	public double hPixelToModel(double p) {
		return p / yPixelsPerModel;
	}
	
	public double wScreenToModel(double s) {
		return s * xModelPerScreen();
	}
	
	public double hScreenToModel(double s) {
		return s * yModelPerScreen();
	}
	
	public double yModelToScreen(double m) {
		return coordSystem.vertical((m - getTop()) / yModelPerScreen());
	}
	
	public double xScreenToModel(double s) {
		return getLeft() + (coordSystem.horizontal(s) * xModelPerScreen());
	}
	
	public double yScreenToModel(double s) {
		return getTop() + (coordSystem.vertical(s) * yModelPerScreen());
	}
	
	public double xModelToPixel(double m) {
		return m * xPixelsPerModel;
	}
	
	public double yModelToPixel(double m) {
		return m * yPixelsPerModel;
	}
	
	public double xPixelToModel(double p) {
		return p / xPixelsPerModel; 
	}
	
	public double yPixelToModel(double p) {
		return p / yPixelsPerModel;
	}
	
	private void fireConverterChanged() {
		for (CanvasListener listener : cloneListeners()) {
			listener.converterChanged(this);
		}
	}
	
//	private void fireZoomChanged() {
//		for (CanvasListener listener : cloneListeners()) {
//			listener.zoomChanged(this);
//		}
//	}
	
	private void fireOffsetChanged(double dx, double dy) {
		for (CanvasListener listener : cloneListeners()) {
			listener.offsetChanged(this, dx, dy);
		}
	}
	
	private void fireVisibleBoundsChanged() {
		for (CanvasListener listener : cloneListeners()) {
			listener.visibleBoundsChanged(this);
		}
	}
	
	private void fireCanvasBoundsChanged() {
		for (CanvasListener listener : cloneListeners()) {
			listener.boundsChanged(this);
		}
	}
	
	private void fireRepaintRequested() {
		for (CanvasListener listener : cloneListeners()) {
			listener.repaintRequested(this);
		}
	}
	
	private void fireRepaintRequested(Bounds mb) {
		for (CanvasListener listener : cloneListeners()) {
			listener.repaintRequested(this, mb);
		}		
	}
	
	@SuppressWarnings("unchecked")
	private List<CanvasListener> cloneListeners() {
		return (List<CanvasListener>) listeners.clone();
	}
	
	public void addListener(CanvasListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(CanvasListener listener) {
		listeners.remove(listener);
	}
	
	public void repaint() {
		fireRepaintRequested();
	}
	
	public void repaint(Bounds mb) {
		fireRepaintRequested(mb);
	}
	
	public void paint(Graphics2D g, Style style) {
		Painter painter = new Painter(this, g, style);
		layerManager.paint(painter);
		selectionManager.paint(painter);
	}

	public void zoomFitVertical(double mHeight) {
		if (mHeight > 0.0) {
			Bounds mBounds = getVisibleBounds();
			setZoom(getZoom() * mBounds.getHeight() / mHeight);
		}
	}
	
	public void zoomFitHorizontal(double mWidth) {
		if (mWidth > 0.0) {
			Bounds mBounds = getVisibleBounds();
			setZoom(getZoom() * mBounds.getWidth() / mWidth);
		}
	}

	public void figureAdded(Layer layer, LayerFig figure) {
		applyBehaviours();
	}

	public void figureRemoved(Layer layer, LayerFig figure) {
		applyBehaviours();		
	}

	public void layerAdded(Layer layer) {
		applyBehaviours();		
	}
	
	public void layerRemoved(Layer layer) {
		applyBehaviours();		
	}

	public Function<Point2D, Point2D> pointScreenToModel() {
		return (s) -> {
			if (s == null) {
				return null;
			}
			return new Point2D.Double(xScreenToModel(s.getX()), yScreenToModel(s.getY())); 
		};
	}

	public double xToModel(double x, Unit unit) {
		if (unit == null) {
			return x;
		}
		
		switch (unit) {
		case MODEL: return x;
		case PIXEL: return xPixelToModel(x);
		case SCREEN: return xScreenToModel(x);
		}
		
		return x;
	}
	
	public double yToModel(double y, Unit unit) {
		if (unit == null) {
			return y;
		}
		
		switch (unit) {
		case MODEL: return y;
		case PIXEL: return yPixelToModel(y);
		case SCREEN: return yScreenToModel(y);
		}
		
		return y;
	}
	
	public double wToModel(double w, Unit unit) {
		if (unit == null) {
			return w;
		}
		
		switch (unit) {
		case MODEL: return w;
		case PIXEL: return wPixelToModel(w);
		case SCREEN: return wScreenToModel(w);
		}
		
		return w;
	}
	
	public double hToModel(double h, Unit unit) {
		if (unit == null) {
			return h;
		}
		
		switch (unit) {
		case MODEL: return h;
		case PIXEL: return hPixelToModel(h);
		case SCREEN: return hScreenToModel(h);
		}
		
		return h;
	}
	
	public double xRelative(double x, Unit xUnit, double dx, Unit dxUnit) {
		double mx = xToModel(x, xUnit);
		double mdx = wToModel(dx, dxUnit);
		return mx + mdx;
	}
	
	public double yRelative(double y, Unit yUnit, double dy, Unit dyUnit) {
		double my = yToModel(y, yUnit);
		double mdy = hToModel(dy, dyUnit);
		return my + mdy;
	}
	
	@Override
	public void propertyChanged(String name) {
		switch (name) {
		case ZOOM:
		case LEFT:
		case TOP:
		case SCREEN_WIDTH:
		case SCREEN_HEIGHT:
			fireVisibleBoundsChanged();
			return;
		}

		PropertyOwner.super.propertyChanged(name);
	}

}
