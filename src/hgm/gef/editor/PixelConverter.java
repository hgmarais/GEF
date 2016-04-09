package hgm.gef.editor;

public class PixelConverter implements Converter {

	@Override
	public double toPixel(double model) {
		return model;
	}

	@Override
	public double toModel(double pixel) {
		return pixel;
	}

}
