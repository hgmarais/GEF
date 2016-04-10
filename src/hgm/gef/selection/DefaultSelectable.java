package hgm.gef.selection;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import hgm.gef.BasicStyle;
import hgm.gef.Style;
import hgm.gef.editor.Painter;
import hgm.gef.fig.Fig;

public class DefaultSelectable implements Selectable {
	
	public static final Style STYLE = BasicStyle.dashedLine(false, Color.BLUE);
	
	private SelectionManager selectionManager;
	
	private Fig figure;

	public DefaultSelectable(SelectionManager selectionManager, Fig figure) {
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
	public void paint(Painter p) {
		p.pushStyle(STYLE);
		p.paint(figure.getBounds());
		p.popStyle();
	}

	@Override
	public Selection createSelection() {
		return new DefaultSelection(selectionManager, figure);
	}

	@Override
	public boolean contains(double mx, double my) {
		return figure.contains(mx, my);
	}

	@Override
	public Rectangle2D getBounds() {
		return figure.getBounds();
	}

}
