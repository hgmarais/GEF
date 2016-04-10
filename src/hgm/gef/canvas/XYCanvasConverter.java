package hgm.gef.canvas;

public class XYCanvasConverter {
	
	private CanvasConverter xConverter;
	
	private CanvasConverter yConverter;
	
	public XYCanvasConverter(Canvas canvas) {
		this.xConverter = new CanvasConverter();
		this.yConverter = new CanvasConverter();
	}
	
	public CanvasConverter getX() {
		return xConverter;
	}
	
	public CanvasConverter getY() {
		return yConverter;
	}
	
}
