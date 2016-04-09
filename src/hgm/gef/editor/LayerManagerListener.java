package hgm.gef.editor;

import hgm.gef.layer.Layer;

public interface LayerManagerListener {

	void layerAdded(Layer layer);

	void layerRemoved(Layer layer);
	
}
