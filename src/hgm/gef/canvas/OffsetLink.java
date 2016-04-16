package hgm.gef.canvas;

public class OffsetLink extends AbstractCanvasLink {

	private boolean absolute;
	
	private Axis axis;
	
	private double dx;
	
	private double dy;

	public OffsetLink(boolean absolute, Axis axis, Canvas target, Canvas source) {
		super(target, source);
		this.absolute = absolute;
		this.axis = axis;
		apply();
	}
	
	@Override
	public void visibleBoundsChanged(Canvas source) {
		apply();
	}
	
	@Override
	public void offsetChanged(Canvas canvas, double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
		super.offsetChanged(canvas, dx, dy);
	}
	
	@Override
	protected void applyImpl() {
		double top = source.getTop();
		double left = source.getLeft();
		
		if (absolute) {
			switch (axis) {
			case BOTH :
				target.setOffset(left, top);
				break;
			case HORIZONTAL :
				target.setOffset(left, target.getTop());
				break;
			case VERTICAL :
				target.setOffset(target.getLeft(), top);
				break;
			}
		} else {
			switch (axis) {
			case BOTH :
				target.adjustOffset(dx, dy);
				break;
			case HORIZONTAL :
				target.adjustOffset(dx, 0.0);
				break;
			case VERTICAL :
				target.adjustOffset(0.0, dy);
				break;
			}
		}
	}

}
