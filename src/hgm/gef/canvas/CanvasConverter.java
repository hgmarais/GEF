package hgm.gef.canvas;

import hgm.gef.editor.Converter;
import hgm.gef.editor.PixelConverter;

public class CanvasConverter {

	private Converter converter;

	public CanvasConverter() {
		this.converter = new PixelConverter();
	}
	
	public void setConverter(Converter converter) {
		this.converter = converter;
	}
	
	public Converter getConverter() {
		return converter;
	}
	
	public double pixelToModel(double pixel) {
		return converter.toModel(pixel);
	}
	
	public double modelToPixel(double model) {
		return converter.toPixel(model);
	}

}
