package hgm.gef.fig;

import hgm.gef.Style;
import hgm.gef.canvas.Painter;
import hgm.gef.property.PropertyOwner;
import hgm.gef.property.ProxyPropertyOwner;
import hgm.gef.selection.Selection;

public abstract class AbstractFig implements Fig {
	
	private Style style;
	
	private Selection selection;
	
	private ProxyPropertyOwner propertyOwner = new ProxyPropertyOwner(this);
	
	@Override
	public PropertyOwner getPropertyOwner() {
		return propertyOwner;
	}
	
	@Override
	public void setSelection(Selection selection) {
		this.selection = selection;
	}
	
	@Override
	public void setStyle(Style style) {
		this.style = style;
	}
	
	@Override
	public Style getStyle() {
		return style;
	}
	
	@Override
	public void refresh() {
		if (selection != null) {
			selection.refresh();
		}
		
		repaint();
	}
	
	@Override
	public void setProperty(String name, Object value) {
	}
	
	@Override
	public Object getProperty(String name) {
		return null;
	}
	
	@Override
	public final void paint(Painter p) {
		p.pushStyle(style);
		
		try {
			paintImpl(p);
		} finally {
			p.popStyle();
		}
	}
	
	protected abstract void paintImpl(Painter p);


}
