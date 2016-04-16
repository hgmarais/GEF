package hgm.gef.fig;

import hgm.gef.layer.Layer;

public abstract class LayerFig extends AbstractFig {
	
	private Layer layer;

	public void setLayer(Layer layer) {
		this.layer = layer;
	}
	
	public Layer getLayer() {
		return layer;
	}
	
	@Override
	public void repaint() {
		if (layer != null) {
			layer.repaint(getBounds());
		}
	}

}
