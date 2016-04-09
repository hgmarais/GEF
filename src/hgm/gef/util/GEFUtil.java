package hgm.gef.util;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Collection;

import hgm.gef.fig.Bounded;

public class GEFUtil {
	
	public static Rectangle2D round(Rectangle2D r) {
		if (r == null) {
			return null;
		}
		double x = Math.round(r.getX());
		double y = Math.round(r.getY());
		double w = Math.ceil(r.getWidth());
		double h = Math.ceil(r.getHeight());
		return new Rectangle2D.Double(x, y, w, h);
	}
	
	public static Rectangle roundInt(Rectangle2D r) {
		if (r == null) {
			return null;
		}
		return round(r).getBounds();
	}
	
	public static Rectangle2D addBounds(Collection<? extends Bounded> list) {
		Rectangle2D result = null;
		
		for (Bounded bounded : list) {
			Rectangle2D bounds = bounded.getBounds();
			
			if (bounds == null) {
				continue;
			}
			
			if (result == null) {
				result = bounds;
			} else {
				result.add(bounds);
			}
		}
		
		return result;
	}

}
