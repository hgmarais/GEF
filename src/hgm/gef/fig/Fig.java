package hgm.gef.fig;

import hgm.gef.Paintable;
import hgm.gef.property.PropertyOwner;
import hgm.gef.selection.Selection;

public interface Fig extends PropertyOwner, Bounded, Paintable, Styleable {
	
	void repaint();
	
	void refresh();
	
	void setSelection(Selection selection);
	
}
