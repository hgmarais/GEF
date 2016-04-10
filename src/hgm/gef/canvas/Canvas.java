package hgm.gef.canvas;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import hgm.gef.Style;
import hgm.gef.editor.CoordSystem;
import hgm.gef.editor.LayerManager;
import hgm.gef.editor.Painter;
import hgm.gef.fig.Bounds;
import hgm.gef.selection.SelectionManager;

public class Canvas {
	
	private LayerManager layerManager;
	
	private SelectionManager selectionManager;
	
	private XYCanvasConverter converter;
	
	private Bounds mCanvasBounds = new Bounds(0, 0, 0, 0);
	
//	private Bounds mVisibleBounds = new Bounds(0, 0, 0, 0);
	
	private double mLeft = 0.0;
	
	private double mTop = 0.0;
	
	private LinkedList<CanvasListener> listeners = new LinkedList<>();
	
	private CoordSystem coordSystem;
	
	private int sWidth;
	
	private int sHeight;
	
	private double zoom = 1.0;
	
	public Canvas(CoordSystem coordSystem) {
		this.coordSystem = coordSystem;
		converter = new XYCanvasConverter(this);
		layerManager = new LayerManager(this);
		selectionManager = new SelectionManager(this);
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
	
	public XYCanvasConverter getConverter() {
		return converter;
	}
	
	public void setBounds(Bounds mBounds) {
		mCanvasBounds = mBounds;
		fireCanvasBoundsChanged();
	}

	public void setScreenSize(int sWidth, int sHeight) {
		this.sWidth = sWidth;
		this.sHeight = sHeight;
		fireVisibleBoundsChanged();
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
		double width = coordSystem.horizontal(sWidth) * xModelPerScreen();
		double height = coordSystem.vertical(sHeight) * yModelPerScreen();
		return new Bounds(mLeft, mTop, mLeft + width, mTop + height);
	}
	
	public void zoomFitCanvas() {
		zoomFit(getBounds());
	}
	
	public void zoomFitLayers() {
		zoomFit(layerManager.getBounds());
	}

	private void zoomFit(Bounds mb) {
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
		mLeft = mx;
		mTop = my;
		fireVisibleBoundsChanged();
	}
	
	public void setZoom(double zoom) {
		if (zoom > 0.0) {
			this.zoom = zoom;
			fireZoomChanged();
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
	
	public double yModelToScreen(double m) {
		return coordSystem.vertical((m - mTop) / yModelPerScreen());
	}
	
	public double xScreenToModel(double s) {
		return mLeft + coordSystem.horizontal(s) * xModelPerScreen();
	}
	
	public double yScreenToModel(double s) {
		return mTop +  coordSystem.vertical(s) * yModelPerScreen();
	}
	
	public double xModelToPixel(double m) {
		return getConverter().getX().modelToPixel(m);
	}
	
	public double yModelToPixel(double m) {
		return getConverter().getY().modelToPixel(m);
	}
	
	public double xPixelToModel(int p) {
		return getConverter().getX().pixelToModel(p);
	}
	
	public double yPixelToModel(int p) {
		return getConverter().getY().pixelToModel(p);
	}
	
	private void fireZoomChanged() {
		for (CanvasListener listener : cloneListeners()) {
			listener.zoomChanged();
		}
	}
	
	private void fireVisibleBoundsChanged() {
		for (CanvasListener listener : cloneListeners()) {
			listener.visibleBoundsChanged();
		}
	}
	
	private void fireCanvasBoundsChanged() {
		for (CanvasListener listener : cloneListeners()) {
			listener.boundsChanged();
		}
	}
	
	private void fireRepaintRequested() {
		for (CanvasListener listener : cloneListeners()) {
			listener.repaintRequested();
		}
	}
	
	private void fireRepaintRequested(Bounds mb) {
		for (CanvasListener listener : cloneListeners()) {
			listener.repaintRequested(mb);
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

}
