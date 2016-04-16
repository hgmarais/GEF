package hgm.gef.canvas;

import hgm.gef.fig.Bounds;

public class ZoomBehaviour implements Behaviour {
	
	private Axis axis;

	public ZoomBehaviour(Axis axis) {
		this.axis = axis;
	}

	@Override
	public void apply(Canvas canvas) {
		Bounds bounds = canvas.getLayerManager().getBounds();
		
		switch (axis) {
			case BOTH : 
				canvas.zoomFit(bounds);
				canvas.setVisibleCenter(0.0, 0.0);
				break;
			case VERTICAL :
				canvas.zoomFitVertical(bounds.getHeight());
				canvas.setVisibleCenter(null, 0.0);
				break;
			case HORIZONTAL :
				canvas.zoomFitHorizontal(bounds.getWidth());
				canvas.setVisibleCenter(0.0, null);
				break;
		}
	}

}
