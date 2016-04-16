package hgm.gef.layer;

import java.util.LinkedList;
import java.util.List;

import hgm.gef.canvas.Canvas;
import hgm.gef.fig.Bounds;
import hgm.gef.fig.LayerFig;
import hgm.gef.property.PropertyOwner;
import hgm.gef.property.ProxyPropertyOwner;
import hgm.gef.util.GEFUtil;

public abstract class AbstractLayer implements Layer {

	private LayerManager layerManager;
	
	private LinkedList<LayerListener> listeners = new LinkedList<>();
	
	private ProxyPropertyOwner propertyOwner = new ProxyPropertyOwner(this);
	
	@Override
	public PropertyOwner getPropertyOwner() {
		return propertyOwner;
	}
	
	@Override
	public void setLayerManager(LayerManager layerManager) {
		this.layerManager = layerManager;
		refresh();
	}

	@Override
	public LayerManager getLayerManager() {
		return layerManager;
	}
	
	public Canvas getCanvas() {
		if (layerManager != null) {
			return layerManager.getCanvas();
		}
		
		return null;
	}

	protected void fireFigureAdded(LayerFig figure) {
		if (layerManager != null) {
			layerManager.figureAdded(this, figure);
		}
		
		for (LayerListener listener : cloneListeners()) {
			listener.figureAdded(figure);
		}
	}
	
	protected void fireFigureRemoved(LayerFig figure) {
		if (layerManager != null) {
			layerManager.figureRemoved(this, figure);
		}
		
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
	public void repaint(Bounds mb) {
		if (layerManager != null) {
			layerManager.repaint(mb);
		}
	}
	
	@Override
	public Bounds getBounds() {
		return GEFUtil.addBounds(getFigures());
	}

	@Override
	public boolean contains(double mx, double my) {
		for (LayerFig figure : getFigures()) {
			if (figure.contains(mx, my)) {
				return true;
			}
		}
		
		return false;
	}
	
}
