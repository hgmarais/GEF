package hgm.gef.selection;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import hgm.gef.BasicStyle;
import hgm.gef.Style;
import hgm.gef.editor.Painter;
import hgm.gef.fig.Fig;

public class DefaultSelection implements Selection {
	
	public static final Style STYLE = BasicStyle.dashedLine(false, Color.RED);

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
		p.paint(getBounds());
		p.popStyle();
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
