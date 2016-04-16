package hgm.gef.selection;

import java.util.LinkedList;
import java.util.List;

import hgm.gef.fig.Bounds;
import hgm.gef.fig.Fig;
import hgm.gef.property.PropertyListener;

public abstract class AbstractSelection implements Selection {
	
	protected final SelectionManager selectionManager;
	
	protected final Fig figure;
	
	private LinkedList<PropertyListener> propertyListeners = new LinkedList<>();

	public AbstractSelection(SelectionManager selectionManager, Fig figure) {
		this.selectionManager = selectionManager;
		this.figure = figure;
	}
	
	@Override
	public SelectionManager getSelectionManager() {
		return selectionManager;
	}
	
	@Override
	public Fig getFigure() {
		return figure;
	}
	
	@Override
	public List<PropertyListener> getPropertyListeners() {
		return propertyListeners;
	}
	
	@Override
	public boolean contains(double mx, double my) {
		return figure.contains(mx, my);
	}

	@Override
	public Bounds getBounds() {
		return figure.getBounds();
	}
	
}
