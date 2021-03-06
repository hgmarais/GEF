package hgm.gef.canvas;

import hgm.gef.fig.Bounds;
import hgm.gef.property.PropertyListener;
import hgm.gef.property.PropertyOwner;

public abstract class AbstractCanvasLink implements CanvasLink, CanvasListener, PropertyListener {
	
	private boolean applying = false;

	protected final Canvas source;

	protected final Canvas target;

	public AbstractCanvasLink(Canvas target, Canvas source) {
		this.target = target;
		this.source = source;
		
		source.addListener(this);
	}
	
	protected void apply() {
		if (applying) {
			return;
		}
		
		applying = true;
		
		try {
			applyImpl();
		} finally {
			applying = false;
		}
	}
	
	protected abstract void applyImpl();

	@Override
	public void boundsChanged(Canvas source) {
	}

	@Override
	public void offsetChanged(Canvas canvas, double dx, double dy) {
	}

	@Override
	public void visibleBoundsChanged(Canvas source) {
	}

	@Override
	public void repaintRequested(Canvas source) {
	}

	@Override
	public void repaintRequested(Canvas source, Bounds mb) {
	}

	@Override
	public void converterChanged(Canvas source ) {
	}
	
	@Override
	public void propertyChanged(PropertyOwner owner, String name) {
	}

}
