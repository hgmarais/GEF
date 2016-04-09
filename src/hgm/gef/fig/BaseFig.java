package hgm.gef.fig;

import hgm.gef.Style;
import hgm.gef.editor.Painter;
import hgm.gef.selection.Selection;

public abstract class BaseFig implements Fig {
	
	private Style style;
	
	private Selection selection;
	
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
