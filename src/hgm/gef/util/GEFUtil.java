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
		
		Point2D min = getMinCorner(list);
		
		if (min == null) {
			return null;
		}
		
		return new Bounds(min, getMaxCorner(list));
	}

	public static Point2D getMinCorner(Collection<? extends Bounded> list) {
		Point2D result = null;
		
		for (Bounded b : list) {
			Bounds bounds = b.getBounds();
			
			if (bounds == null) {
				continue;
			}
			
			Point2D corner = bounds.getMin();
			
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
			Bounds bounds = b.getBounds();
			
			if (bounds == null) {
				continue;
			}
			
			Point2D corner = bounds.getMax();
			
			if (result == null) {
				result = corner;
			} else {
				result.setLocation(Math.max(result.getX(), corner.getX()), Math.max(result.getY(), corner.getY()));
			}
		}
		
		return result;
	}

}
