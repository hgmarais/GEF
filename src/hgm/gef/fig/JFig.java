package hgm.gef.fig;

import java.awt.Graphics2D;

import javax.swing.JComponent;

import hgm.gef.canvas.Painter;

public class JFig extends LayerFig {
	
	private JComponent component;
	
	private double x;
	
	private double y;
	
	private double width;
	
	private double height;

	public JFig(JComponent component) {
		this.component = component;
	}
	
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
		repaint();
	}
	
	public void setSize(double width, double height) {
		this.width = width;
		this.height = height;
		repaint();
	}

	@Override
	public Bounds getBounds() {
		return new Bounds(x, y, x + width, y + height);
	}

	@Override
	protected void paintImpl(Painter p) {
		Graphics2D g = p.gCopy();
		g.translate(x, y);
		component.paint(p.g);
	}

}
