package hgm.gef.layer;

import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import hgm.gef.editor.LayerManager;
import hgm.gef.editor.Painter;
import hgm.gef.fig.LayerFig;
import hgm.gef.util.GEFUtil;

public class DefaultLayer implements Layer {
	
	private LayerManager editor;
	
	private List<LayerFig> figures = new LinkedList<>();
	
	private LinkedList<LayerListener> listeners = new LinkedList<>();
	
	@Override
	public void setLayerManager(LayerManager editor) {
		this.editor = editor;
	}

	@Override
	public LayerManager getEditor() {
		return editor;
	}

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
	
	private void fireFigureAdded(LayerFig figure) {
		for (LayerListener listener : cloneListeners()) {
			listener.figureAdded(figure);
		}
	}
	
	private void fireFigureRemoved(LayerFig figure) {
		for (LayerListener listener : cloneListeners()) {
			listener.figureRemoved(figure);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<LayerListener> cloneListeners() {
		return (List<LayerListener>) listeners.clone();
	}
	
	@Override
	public void addListener(LayerListener listener) {
		listeners.add(listener);
	}
	
	@Override
	public void removeListener(LayerListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void repaint(Rectangle2D mr) {
		if (editor != null) {
			editor.repaint(mr);
		}
	}
	
	@Override
	public Rectangle2D getBounds() {
		return GEFUtil.addBounds(figures);
	}

	@Override
	public boolean contains(double mx, double my) {
		for (LayerFig figure : figures) {
			if (figure.contains(mx, my)) {
				return true;
			}
		}
		
		return false;
	}

}
