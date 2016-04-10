package hgm.gef.util;

import java.awt.geom.Point2D;
import java.util.Collection;

import hgm.gef.fig.Bounded;
import hgm.gef.fig.Bounds;

public class GEFUtil {
	
//	public static Rectangle2D round(Rectangle2D r) {
//		if (r == null) {
//			return null;
//		}
//		double x = Math.round(r.getX());
//		double y = Math.round(r.getY());
//		double w = Math.ceil(r.getWidth());
//		double h = Math.ceil(r.getHeight());
//		return new Rectangle2D.Double(x, y, w, h);
//	}
	
//	public static Rectangle roundInt(Rectangle2D r) {
//		if (r == null) {
//			return null;
//		}
//		return round(r).getBounds();
//	}
	
	public static Bounds addBounds(Collection<? extends Bounded> list) {
		if (list.isEmpty()) {
			return null;
		}
		
		return new Bounds(getMinCorner(list), getMaxCorner(list));
	}

	public static Point2D getMinCorner(Collection<? extends Bounded> list) {
		Point2D result = null;
		
		for (Bounded b : list) {
			Point2D corner = getMinCorner(b);
			
			if (result == null) {
				result = corner;
			} else {
				result.setLocation(Math.min(result.getX(), corner.getX()), Math.min(result.getY(), corner.getY()));
			}
		}
		
		return result;
	}
	
	public static Point2D getMaxCorner(Collection<? extends Bounded> list) {
		Point2D result = null;
		
		for (Bounded b : list) {
			Point2D corner = getMaxCorner(b);
			
			if (result == null) {
				result = corner;
			} else {
				result.setLocation(Math.max(result.getX(), corner.getX()), Math.max(result.getY(), corner.getY()));
			}
		}
		
		return result;
	}

	public static Point2D getMinCorner(Bounded b) {
		Point2D c1 = b.getCorner1();
		Point2D c2 = b.getCorner2();
		return new Point2D.Double(Math.min(c1.getX(), c2.getX()), Math.min(c1.getY(), c2.getY()));
	}
	
	public static Point2D getMaxCorner(Bounded b) {
		Point2D c1 = b.getCorner1();
		Point2D c2 = b.getCorner2();
		return new Point2D.Double(Math.max(c1.getX(), c2.getX()), Math.max(c1.getY(), c2.getY()));
	}

}
