package hgm.gef.selection;

import hgm.gef.Paintable;
import hgm.gef.fig.Bounded;
import hgm.gef.fig.Fig;

public interface Selection extends Bounded, Paintable {
	
	SelectionManager getSelectionManager();
	
	Fig getFigure();
	
	void refresh();

}
