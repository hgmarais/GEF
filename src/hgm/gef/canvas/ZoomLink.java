package hgm.gef.canvas;

import hgm.gef.property.PropertyOwner;

public class ZoomLink extends AbstractCanvasLink {
	
	public ZoomLink(Canvas target, Canvas source) {
		super(target, source);
		apply();
	}

	@Override
	public void propertyChanged(PropertyOwner owner, String name) {
		if (Canvas.ZOOM.equals(name)) {
			apply();
		}
	}
	
	@Override
	protected void applyImpl() {
		target.setZoom(source.getZoom());
	}

}
