package hgm.gef.editor;

public interface Converter {
	
	double toPixel(double model);
	
	double toModel(double pixel);

}
