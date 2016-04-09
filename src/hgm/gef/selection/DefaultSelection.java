package hgm.gef.selection;

import java.awt.Color;

import hgm.gef.BasicStyle;
import hgm.gef.Style;
import hgm.gef.editor.Painter;
import hgm.gef.fig.Fig;

public class DefaultSelection implements Selection {
	
	public static final Style STYLE = BasicStyle.dashedLine(false, Color.ORANGE);

	private SelectionManager selectionManager;
	
	private Fig figure;

	public DefaultSelection(SelectionManager selectionManager, Fig figure) {
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
	public void refresh() {
	}

	@Override
	public void paint(Painter p) {
		p.pushStyle(STYLE);
		p.paint(figure.getBounds());
		p.popStyle();
	}

}
