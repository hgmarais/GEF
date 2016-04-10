package hgm.gef.fig;

import java.awt.geom.Point2D;

public interface Bounded {
	
	default Point2D getCorner1() {
		return getBounds().getMin();
	}
	
	default Point2D getCorner2() {
		return getBounds().getMax();
	}
	
	default boolean contains(double mx, double my) {
		return getBounds().contains(mx, my);
	}
	
	default Bounds getBounds() {
		return new Bounds(getCorner1(), getCorner2());
	}
	
}
