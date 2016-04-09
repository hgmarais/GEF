package hgm.gef.canvas;

import hgm.gef.editor.Converter;
import hgm.gef.editor.PixelConverter;

public class CanvasConverter {
	
	private double mOffset = 0.0;
	
	private double zoom = 1.0;
	
	private Converter converter;

	public CanvasConverter() {
		this.converter = new PixelConverter();
	}
	
	public void setOffset(double mOffset) {
		if (mOffset >= 0.0) {
			this.mOffset = mOffset;
		}
	}
	
	public void adjustOffset(double mx) {
		setOffset(mOffset + mx);
	}
	
	public double getOffset() {
		return mOffset;
	}
	
	public boolean setZoom(double zoom) {
		if (zoom > 0.0) {
			this.zoom = zoom;
			return true;
		}
		return false;
	}
	
	public boolean adjustZoom(double dz) {
		return setZoom(zoom + dz);
	}
	
	public double getZoom() {
		return zoom;
	}
	
	public void setConverter(Converter converter) {
		this.converter = converter;
	}
	
	public Converter getConverter() {
		return converter;
	}
	
	public double screenToModel(double screen) {
		return pixelToModel(screen) + mOffset;
	}
	
	public double modelToScreen(double model) {
		return modelToPixel(model - mOffset);
	}
	
	public double pixelToModel(double pixel) {
		return converter.toModel(pixel / zoom);
	}
	
	public double modelToPixel(double model) {
		return converter.toPixel(model) * zoom;
	}

}
