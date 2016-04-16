package hgm.gef.fig;

import hgm.gef.canvas.Canvas;
import hgm.gef.layer.Layer;

public abstract class LayerFig extends AbstractFig {
	
	private Layer layer;

	public void setLayer(Layer layer) {
		this.layer = layer;
		refresh();
	}
	
	public Layer getLayer() {
		return layer;
	}
	
	@Override
	public Canvas getCanvas() {
		if (layer == null) {
			return null;
		}
		
		return layer.getCanvas();
	}
	
	@Override
	public void repaint() {
		if (layer != null) {
			layer.repaint(getBounds());
		}
	}

}
