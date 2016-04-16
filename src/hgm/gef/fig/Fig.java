package hgm.gef.fig;

import hgm.gef.Paintable;
import hgm.gef.canvas.Canvas;
import hgm.gef.property.PropertyOwner;
import hgm.gef.selection.Selection;

public interface Fig extends PropertyOwner, Bounded, Paintable, Styleable {
	
	Canvas getCanvas();
	
	void repaint();
	
	void refresh();
	
	void setSelection(Selection selection);
	
}
