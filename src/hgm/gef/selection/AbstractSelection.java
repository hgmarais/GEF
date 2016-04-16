package hgm.gef.selection;

import hgm.gef.fig.Bounds;
import hgm.gef.fig.Fig;
import hgm.gef.property.PropertyOwner;
import hgm.gef.property.ProxyPropertyOwner;

public abstract class AbstractSelection implements Selection {
	
	protected final SelectionManager selectionManager;
	
	protected final Fig figure;
	
	private ProxyPropertyOwner propertyOwner = new ProxyPropertyOwner(this);

	public AbstractSelection(SelectionManager selectionManager, Fig figure) {
		this.selectionManager = selectionManager;
		this.figure = figure;
	}
	
	@Override
	public PropertyOwner getPropertyOwner() {
		return propertyOwner;
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
	public boolean contains(double mx, double my) {
		return figure.contains(mx, my);
	}

	@Override
	public Bounds getBounds() {
		return figure.getBounds();
	}
	
}
