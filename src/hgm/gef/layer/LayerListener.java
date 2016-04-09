package hgm.gef.layer;

import hgm.gef.fig.LayerFig;

public interface LayerListener {
	
	void figureAdded(LayerFig figure);
	
	void figureRemoved(LayerFig figure);

}
