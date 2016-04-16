package hgm.gef.canvas;

public class ZoomLink extends AbstractCanvasLink {
	
	public ZoomLink(Canvas target, Canvas source) {
		super(target, source);
		apply();
	}

	@Override
	public void zoomChanged(Canvas source) {
		apply();
	}
	
	@Override
	protected void applyImpl() {
		target.setZoom(source.getZoom());
	}


}
