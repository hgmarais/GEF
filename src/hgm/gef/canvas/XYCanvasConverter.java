package hgm.gef.canvas;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class XYCanvasConverter {
	
	private CanvasConverter xConverter;
	
	private CanvasConverter yConverter;
	
	private double zoom = 1.0;

	public XYCanvasConverter() {
		this.xConverter = new CanvasConverter();
		this.yConverter = new CanvasConverter();
		setZoom(1.0);
	}
	
	public CanvasConverter getX() {
		return xConverter;
	}
	
	public CanvasConverter getY() {
		return yConverter;
	}
	
	public double xPixelToModel(int p) {
		return getX().pixelToModel(p);
	}
	
	public double yPixelToModel(int p) {
		return getY().pixelToModel(p);
	}
	
	public void adjustOffset(double mx, double my) {
		xConverter.adjustOffset(mx);
		yConverter.adjustOffset(my);
	}
	
	public double getXOffset() {
		return xConverter.getOffset();
	}
	
	public double getYOffset() {
		return yConverter.getOffset();
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public void setZoom(double zoom) {
		if (zoom > 0) {
			this.zoom = zoom;
			xConverter.setZoom(zoom);
			yConverter.setZoom(zoom);
		}
	}
	
	public void adjustZoom(double dz) {
		setZoom(zoom + dz);
	}
	
	public Rectangle2D modelToPixel(Rectangle2D model) {
		if (model == null) {
			return null;
		}
		
		double x = xConverter.modelToPixel(model.getX());
		double y = yConverter.modelToPixel(model.getY());
		double w = xConverter.modelToPixel(model.getWidth());
		double h = yConverter.modelToPixel(model.getHeight());
		return new Rectangle2D.Double(x, y, w, h);
	}
	
	public Rectangle2D pixelToModel(Rectangle2D pixel) {
		if (pixel == null) {
			return null;
		}
		
		double x = xConverter.pixelToModel(pixel.getX());
		double y = yConverter.pixelToModel(pixel.getY());
		double w = xConverter.pixelToModel(pixel.getWidth());
		double h = yConverter.pixelToModel(pixel.getHeight());
		return new Rectangle2D.Double(x, y, w, h);
	}

	public void setOffset(double mx, double my) {
		xConverter.setOffset(mx);
		yConverter.setOffset(my);
	}

	public void apply(Graphics2D g) {
		g.scale(getZoom(), getZoom());
		g.translate(-getXOffset(), -getYOffset());
	}

}
