package hgm.gef.function;

import java.awt.geom.Point2D;
import java.util.function.Function;

public class XPointToDouble implements Function<Point2D, Double> {

	@Override
	public Double apply(Point2D t) {
		return t.getX();
	}

}
