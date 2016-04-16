package hgm.gef.canvas;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import hgm.gef.Style;
import hgm.gef.fig.Bounds;
import hgm.gef.fig.LayerFig;
import hgm.gef.layer.Layer;
import hgm.gef.layer.LayerManager;
import hgm.gef.selection.SelectionManager;

public class Canvas {
	
	private LayerManager layerManager;
	
	private SelectionManager selectionManager;
	
	private Bounds mCanvasBounds = new Bounds(0, 0, 0, 0);
	
	private double mLeft = 0.0;
	
	private double mTop = 0.0;
	
	private CoordSystem coordSystem;
	
	private int sWidth;
	
	private int sHeight;
	
	private double zoom = 1.0;
	
	private double xPixelsPerModel = 1.0;
	
	private double yPixelsPerModel = 1.0;
	
	private boolean applyingBehaviours = false;
	
	private List<Behaviour> behaviours = new LinkedList<>();
	
	private LinkedList<CanvasListener> listeners = new LinkedList<>();
	
	public Canvas(CoordSystem coordSystem) {
		this.coordSystem = coordSystem;
		layerManager = new LayerManager(this);
		selectionManager = new SelectionManager(this);
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
		if (mCanvasBounds.equals(mBounds)) {
			return;
		}
		
		mCanvasBounds = mBounds;
		fireCanvasBoundsChanged();
		applyBehaviours();
	}
	
	public void setScreenSize(int sWidth, int sHeight) {
		System.out.println("setScreenSize : "+sWidth+" "+sHeight+" "+getVisibleBounds());
		
		if ((this.sWidth == sWidth) && (this.sHeight == sHeight)) {
			System.out.println("return");
			return;
		}
		
		this.sWidth = sWidth;
		this.sHeight = sHeight;
		
		fireVisibleBoundsChanged();
		applyBehaviours();
	}
	
	public int getScreenWidth() {
		return sWidth;
	}
	
	public int getScreenHeight() {
		return sHeight;
	}
	
	public double getLeft() {
		return mLeft;
	}
	
	public double getTop() {
		return mTop;
	}
	
	public Bounds getBounds() {
		return mCanvasBounds;
	}
	
	public Bounds getVisibleBounds() {
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
		setOffset(mLeft + coordSystem.horizontal(mx), mTop + coordSystem.vertical(my));
	}
	
	public void setOffset(double mx, double my) {
		System.out.println("setOffset : "+mx+" "+my+" "+getVisibleBounds());
		
		if ((mLeft == mx) && (mTop == my)) {
			return;
		}
		
		double dx = mx - mLeft;
		double dy = my - mTop;
		
		mLeft = mx;
		mTop = my;
		
		fireOffsetChanged(dx, dy);
		fireVisibleBoundsChanged();
		applyBehaviours();
	}
	
	public void setZoom(double zoom) {
		System.out.println("setZoom : "+zoom+" "+getVisibleBounds());
		
		if (this.zoom == zoom) {
			return;
		}
		
		if (zoom > 0.0) {
			this.zoom = zoom;
			fireZoomChanged();
			applyBehaviours();
		}
	}
	
	public void adjustZoom(double dz) {
		setZoom(zoom + dz);
	}
	
	public void adjustZoomAround(double dz, double mx, double my) {
		setZoomAround(zoom + dz, mx, my);
	}
	
	public double getZoom() {
		return zoom;
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
		return xPixelToModel(1) * zoom;
	}
	
	public double yScreenPerModel() {
		return yPixelToModel(1) * zoom;
	}
	
	public double xModelPerScreen() {
		return 1.0 / xScreenPerModel();
	}
	
	public double yModelPerScreen() {
		return 1.0 / yScreenPerModel();
	}
	
	public double xModelToScreen(double m) {
		return coordSystem.horizontal((m - mLeft) / xModelPerScreen());
	}
	
	public double wModelToScreen(double m) {
		return m * xScreenPerModel();
	}
	
	public double wScreenToModel(double s) {
		return s * xModelPerScreen();
	}
	
	public double hScreenToModel(double s) {
		return s * yModelPerScreen();
	}
	
	public double hModelToScreen(double m) {
		return m * yScreenPerModel();
	}
	
	public double yModelToScreen(double m) {
		return coordSystem.vertical((m - mTop) / yModelPerScreen());
	}
	
	public double xScreenToModel(double s) {
		return mLeft + (coordSystem.horizontal(s) * xModelPerScreen());
	}
	
	public double yScreenToModel(double s) {
		return mTop + (coordSystem.vertical(s) * yModelPerScreen());
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
	
	private void fireZoomChanged() {
		for (CanvasListener listener : cloneListeners()) {
			listener.zoomChanged(this);
		}
	}
	
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
			setZoom(zoom * mBounds.getHeight() / mHeight);
		}
	}
	
	public void zoomFitHorizontal(double mWidth) {
		if (mWidth > 0.0) {
			Bounds mBounds = getVisibleBounds();
			setZoom(zoom * mBounds.getWidth() / mWidth);
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

}
