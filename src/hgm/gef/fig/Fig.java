package hgm.gef.fig;

import hgm.gef.Paintable;
import hgm.gef.selection.Selection;

public interface Fig extends Bounded, Paintable, Styleable {
	
	void repaint();
	
	void refresh();
	
	void setSelection(Selection selection);
	
}
