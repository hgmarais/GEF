package hgm.gef.fig;

import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import hgm.gef.editor.Painter;

public class ShapeFig extends LayerFig {

	private Shape shape;
	
	public ShapeFig() {
	}
	
	public ShapeFig(Shape shape) {
		this.shape = shape;
	}
	
	public void setShape(Shape shape) {
		this.shape = shape;
		refresh();
	}
	
	public Shape getShape() {
		return shape;
	}
	
	@Override
	public void paintImpl(Painter p) {
		if (shape == null) {
			return;
		}

		p.paint(shape);
	}

	@Override
	public boolean contains(double mx, double my) {
		if (shape == null) {
			return false;
		}
		
		return shape.contains(mx, my);
	}

	@Override
	public Rectangle2D getBounds() {
		if (shape == null) {
			return null;
		}
		
		return shape.getBounds2D();
	}

}