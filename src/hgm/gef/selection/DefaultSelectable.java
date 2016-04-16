package hgm.gef.selection;

import java.awt.Color;

import hgm.gef.BasicStyle;
import hgm.gef.Style;
import hgm.gef.canvas.Painter;
import hgm.gef.fig.Fig;

public class DefaultSelectable extends AbstractSelectable {
	
	public static final Style STYLE = BasicStyle.dashedLine(false, Color.BLUE);
	
	public DefaultSelectable(SelectionManager selectionManager, Fig figure) {
		super(selectionManager, figure);
	}
	
	@Override
	public void paint(Painter p) {
		p.pushStyle(STYLE);
		p.paint(figure.getBounds());
		p.popStyle();
	}

	@Override
	public Selection createSelection() {
		return new DefaultSelection(selectionManager, figure);
	}

}
