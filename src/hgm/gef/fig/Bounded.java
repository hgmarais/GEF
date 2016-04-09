package hgm.gef.fig;

import java.awt.geom.Rectangle2D;

public interface Bounded {
	
	boolean contains(double mx, double my);
	
	Rectangle2D getBounds();

}
