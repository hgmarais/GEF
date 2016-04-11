package hgm.gef.layer;

import java.util.List;

import hgm.gef.Paintable;
import hgm.gef.fig.Bounded;
import hgm.gef.fig.Bounds;
import hgm.gef.fig.LayerFig;

public interface Layer extends Paintable, Bounded {
	
	void setLayerManager(LayerManager editor);
	
	LayerManager getEditor();
	
	void addFigure(LayerFig figure);
	
	void removeFigure(LayerFig figure);
	
	List<LayerFig> getFigures();
	
	List<LayerFig> getFigures(double mx, double my);
	
	void addListener(LayerListener listener);
	
	void removeListener(LayerListener listener);
	
	void repaint(Bounds mb);

}
