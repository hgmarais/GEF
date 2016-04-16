package hgm.gef.layer;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hgm.gef.Paintable;
import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.Painter;
import hgm.gef.fig.Bounded;
import hgm.gef.fig.Bounds;
import hgm.gef.fig.LayerFig;
import hgm.gef.property.PropertyListener;
import hgm.gef.util.GEFUtil;

public class LayerManager implements Bounded, Paintable {

	private Canvas canvas;
	
	private List<Layer> layers = new ArrayList<>();
	
	private LinkedList<LayerManagerListener> listeners = new LinkedList<>();
	
	private LinkedList<PropertyListener> propertyListeners = new LinkedList<>();

	public LayerManager(Canvas canvas) {
		this.canvas = canvas;
	}
	
	@Override
	public Bounds getBounds() {
		return GEFUtil.addBounds(layers);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public void addToTop(Layer layer) {
		add(0, layer);
	}
	
	public void addToBottom(Layer layer) {
		add(layers.size(), layer);
	}
	
	public void add(int index, Layer layer) {
		if (index < 0) {
			index = 0;
		} else if (index > layers.size()) {
			index = layers.size();
		}
		
		layers.add(index, layer);
		layer.setLayerManager(this);
		canvas.layerAdded(layer);
		fireLayerAdded(layer);
		repaint();
	}
	
	public void remove(Layer layer) {
		if (layers.remove(layer)) {
			layer.setLayerManager(null);
			canvas.layerRemoved(layer);
			fireLayerRemoved(layer);
			repaint();
		}
	}
	
	public List<LayerFig> getLayerFigures(double mx, double my) {
		List<LayerFig> result = new LinkedList<>();
		
		for (Layer layer : layers) {
			result.addAll(layer.getFigures(mx, my));
		}
		
		return result;
	}
	
	private void fireLayerAdded(Layer layer) {
		for (LayerManagerListener listener : cloneListeners()) {
			listener.layerAdded(layer);
		}
	}
	
	private void fireLayerRemoved(Layer layer) {
		for (LayerManagerListener listener : cloneListeners()) {
			listener.layerRemoved(layer);
		}
	}
	
	@SuppressWarnings("unchecked")
	private List<LayerManagerListener> cloneListeners() {
		return (List<LayerManagerListener>) listeners.clone();
	}

	public void addListener(LayerManagerListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(LayerManagerListener listener) {
		listeners.remove(listener);
	}
	
	public void repaint() {
		canvas.repaint();
	}

	public void repaint(Bounds mb) {
		canvas.repaint(mb);
	}
	
	@Override
	public void paint(Painter p) {
		for (int i = layers.size() - 1; i >= 0; i--) {
			layers.get(i).paint(p);
		}
	}

	public Point2D getCorner1() {
		return GEFUtil.getMinCorner(layers);
	}
	
	public Point2D getCorner2() {
		return GEFUtil.getMaxCorner(layers);
	}

	public void figureAdded(Layer layer, LayerFig figure) {
		canvas.figureAdded(layer, figure);
	}
	
	public void figureRemoved(Layer layer, LayerFig figure) {
		canvas.figureRemoved(layer, figure);
	}

	@Override
	public List<PropertyListener> getPropertyListeners() {
		return propertyListeners;
	}

}
