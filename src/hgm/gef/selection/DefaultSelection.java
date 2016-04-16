package hgm.gef.selection;

import java.awt.Color;

import hgm.gef.BasicStyle;
import hgm.gef.Style;
import hgm.gef.canvas.Painter;
import hgm.gef.fig.Fig;

public class DefaultSelection extends AbstractSelection {
	
	public static final Style STYLE = BasicStyle.dashedLine(false, Color.RED);

	public DefaultSelection(SelectionManager selectionManager, Fig figure) {
		super(selectionManager, figure);
	}
	
	@Override
	public void refresh() {
	}

	@Override
	public void paint(Painter p) {
		p.pushStyle(STYLE);
		p.paint(getBounds());
		p.popStyle();
	}

}
