package hgm.gef.fig;

import java.util.LinkedList;
import java.util.List;

import hgm.gef.Style;
import hgm.gef.canvas.Painter;
import hgm.gef.property.PropertyListener;
import hgm.gef.selection.Selection;

public abstract class AbstractFig implements Fig {
	
	private Style style;
	
	private Selection selection;
	
	private List<PropertyListener> propertyListeners = new LinkedList<>();
	
	@Override
	public List<PropertyListener> getPropertyListeners() {
		return propertyListeners;
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
