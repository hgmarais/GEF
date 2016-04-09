package hgm.gef.canvas;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;
import java.util.List;

import hgm.gef.Style;
import hgm.gef.editor.LayerManager;
import hgm.gef.editor.Painter;
import hgm.gef.selection.SelectionManager;

public class Canvas {
	
	private LayerManager layerManager;
	
	private SelectionManager selectionManager;
	
	private XYCanvasConverter converter = new XYCanvasConverter();
	
	private Rectangle2D mCanvasBounds = new Rectangle2D.Double(0, 0, 0, 0);
	
	private LinkedList<CanvasListener> listeners = new LinkedList<>();
	
	private double mVisibleWidth = 0.0;
	
	private double mVisibleHeight = 0.0;
	
	public Canvas() {
		layerManager = new LayerManager(this);
		selectionManager = new SelectionManager(this);
	}
	
	public LayerManager getLayerManager() {
		return layerManager;
	}
	
	public SelectionManager getSelectionManager() {
		return selectionManager;
	}
	
	public Rectangle2D getCanvasBounds() {
		return mCanvasBounds;
	}
	
	public XYCanvasConverter getConverter() {
		return converter;
	}

	public void setVisibleBounds(Rectangle2D mVisibleBounds) {
		setOffset(mVisibleBounds.getX(), mVisibleBounds.getY());
		setVisibleSize(mVisibleBounds.getWidth(), mVisibleBounds.getHeight());
	}
	
	public Rectangle2D getVisibleBounds() {
		double x = converter.getXOffset();
		double y = converter.getYOffset();
		return new Rectangle2D.Double(x, y, mVisibleWidth, mVisibleHeight);
	}
	
	public void setVisibleSize(double mVisibleWidth, double mVisibleHeight) {
		this.mVisibleWidth = mVisibleWidth;
		this.mVisibleHeight = mVisibleHeight;
		fireVisibleBoundsChanged();
	}

	public void setCanvasBounds(Rectangle2D mBounds) {
		if (mCanvasBounds == null) {
			this.mCanvasBounds = new Rectangle2D.Double(0, 0, 0, 0);
		} else {
			this.mCanvasBounds = mBounds;
		}
		
		fireCanvasBoundsChanged();
//		setOffset(mCanvasBounds.getX(), mCanvasBounds.getY());
	}
	
	public void adjustDimension(double dmw, double dmh) {
		double w = mCanvasBounds.getWidth() + dmw;
		double h = mCanvasBounds.getHeight() + dmh;
		w = Math.max(0.0, w);
		h = Math.max(0.0, h);
		setCanvasBounds(new Rectangle2D.Double(mCanvasBounds.getX(), mCanvasBounds.getY(), w, h));
	}
	
	public void setSize(double mw, double mh) {
		setWidth(mw);
		setHeight(mh);
	}
	
	public void setWidth(double mw) {
		if (mw < 0.0) {
			mw = 0.0;
		}
		setCanvasBounds(new Rectangle2D.Double(mCanvasBounds.getX(), mCanvasBounds.getY(), mw, mCanvasBounds.getHeight()));
	}
	
	public void setHeight(double mh) {
		if (mh < 0.0) {
			mh = 0.0;
		}
		setCanvasBounds(new Rectangle2D.Double(mCanvasBounds.getX(), mCanvasBounds.getY(), mCanvasBounds.getWidth(), mh));
	}
	
	public double getX() {
		return mCanvasBounds.getX();
	}
	
	public double getY() {
		return mCanvasBounds.getY();
	}
	
	public double getWidth() {
		return mCanvasBounds.getWidth();
	}
	
	public double getHeight() {
		return mCanvasBounds.getHeight();
	}
	
	public double getVisibleX() {
		return getConverter().getXOffset();
	}
	
	public double getVisibleY() {
		return getConverter().getYOffset();
	}
	
	public double getVisibleWidth() {
		return mVisibleWidth;
	}
	
	public double getVisibleHeight() {
		return mVisibleHeight;
	}

	public void zoomFitCanvas() {
		zoomFit(mCanvasBounds);
	}
	
	public void zoomFitLayers() {
		zoomFit(layerManager.getBounds());
	}

	private void zoomFit(Rectangle2D mr) {
		if (mr == null) {
			return;
		}
		
		Rectangle2D mVisibleBounds = getVisibleBounds();
		double mvw = mVisibleBounds.getWidth();
		double mvh = mVisibleBounds.getHeight();
		double mw = mr.getWidth();
		double mh = mr.getHeight();
		
		if ((mvw == 0.0) || (mvh == 0.0) || (mw == 0.0) || (mh == 0.0)) {
			return;
		}
		
		double wRatio = mvw / mw;
		double hRatio = mvh / mh;
		
		setZoom(getZoom() * Math.min(wRatio, hRatio));
		setOffset(mr.getX(), mr.getY());
	}

	public Rectangle2D modelToPixel(Rectangle2D model) {
		return converter.modelToPixel(model);
	}
	
	public Rectangle2D pixelToModel(Rectangle2D pixel) {
		return converter.pixelToModel(pixel);
	}
	
	public void adjustOffset(double mx, double my) {
		converter.adjustOffset(mx, my);
		fireVisibleBoundsChanged();
	}
	
	public void setOffset(double mx, double my) {
		converter.setOffset(mx, my);
		fireVisibleBoundsChanged();
	}
	
	public void setZoom(double zoom) {
		converter.setZoom(zoom);
		fireZoomChanged();
	}
	
	public void adjustZoom(double zoom) {
		converter.adjustZoom(zoom);
		fireZoomChanged();
	}
	
	public void adjustZoomAround(double dz, double mx, double my) {
		setZoomAround(converter.getZoom() + dz, mx, my);
	}
	
	public double getZoom() {
		return converter.getZoom();
	}
	
	public void setZoomAroundVisibleCenter(double zoom) {
		Rectangle2D mVisibleBounds = getVisibleBounds();
		setZoomAround(zoom, mVisibleBounds.getCenterX(), mVisibleBounds.getCenterY());
	}
	
	public void setZoomAround(double zoom, double mx, double my) {
		double sx = xModelToScreen(mx);
		double sy = yModelToScreen(my);
		
		setZoom(zoom);
		
		double mx2 = xScreenToModel(sx);
		double my2 = yScreenToModel(sy);
		adjustOffset(mx - mx2, my - my2);
	}
	
	public double xModelToScreen(double m) {
		return converter.getX().modelToScreen(m);
	}
	
	public double yModelToScreen(double m) {
		return converter.getY().modelToScreen(m);
	}
	
	public double xScreenToModel(double s) {
		return converter.getX().screenToModel(s);
	}
	
	public double yScreenToModel(double s) {
		return converter.getY().screenToModel(s);
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
			listener.zoomChanged(converter.getZoom());
		}
	}
	
	private void fireVisibleBoundsChanged() {
		for (CanvasListener listener : cloneListeners()) {
			listener.visibleBoundsChanged();
		}
	}
	
	private void fireCanvasBoundsChanged() {
		for (CanvasListener listener : cloneListeners()) {
			listener.boundsChanged(mCanvasBounds);
		}
	}
	
	private void fireRepaintRequested() {
		for (CanvasListener listener : cloneListeners()) {
			listener.repaintRequested();
		}
	}
	
	private void fireRepaintRequested(Rectangle2D mr) {
		for (CanvasListener listener : cloneListeners()) {
			listener.repaintRequested(mr);
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
	
	public void repaint(Rectangle2D mr) {
		fireRepaintRequested(mr);
	}
	
	public void paint(Graphics2D g, Style style) {
		Painter painter = new Painter(this, g, style);
		layerManager.paint(painter);
		selectionManager.paint(painter);
	}

}
