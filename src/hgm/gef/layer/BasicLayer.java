package hgm.gef.layer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hgm.gef.canvas.Painter;
import hgm.gef.fig.LayerFig;

public class BasicLayer extends AbstractLayer {
	
	private List<LayerFig> figures = new LinkedList<>();
	
	@Override
	public void addFigure(LayerFig figure) {
		figures.add(figure);
		figure.setLayer(this);
		fireFigureAdded(figure);
	}
	
	@Override
	public void removeFigure(LayerFig figure) {
		if (figures.remove(figure)) {
			figure.setLayer(null);
			fireFigureRemoved(figure);
		}
	}

	@Override
	public List<LayerFig> getFigures() {
		return Collections.unmodifiableList(figures);
	}
	
	@Override
	public List<LayerFig> getFigures(double mx, double my) {
		List<LayerFig> result = new LinkedList<>();
		
		for (LayerFig figure : figures) {
			if (figure.contains(mx, my)) {
				result.add(figure);
			}
		}
		
		return result;
	}
	
	@Override
	public void paint(Painter p) {
		for (LayerFig figure : figures) {
			figure.paint(p);
		}
	}
	
	@Override
	public void refresh() {
		figures.forEach(figure -> figure.refresh());
	}

}
