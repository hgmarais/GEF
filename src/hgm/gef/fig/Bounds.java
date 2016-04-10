package hgm.gef.fig;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Bounds {
	
	private double minX;
	
	private double minY;
	
	private double maxX;
	
	private double maxY;

	public Bounds(Rectangle2D r) {
		minX = r.getMinX();
		minY = r.getMinY();
		maxX = r.getMaxX();
		maxY = r.getMaxY();
	}

	public Bounds(Point2D c1, Point2D c2) {
		this(c1.getX(), c1.getY(), c2.getX(), c2.getY());
	}
	
	public Bounds(double x1, double y1, double x2, double y2) {
		minX = Math.min(x1, x2);
		minY = Math.min(y1, y2);
		maxX = Math.max(x1, x2);
		maxY = Math.max(y1, y2);
	}

	public double getMinX() {
		return minX;
	}
	
	public double getMinY() {
		return minY;
	}
	
	public double getMaxX() {
		return maxX;
	}
	
	public double getMaxY() {
		return maxY;
	}
	
	public double getWidth() {
		return getMaxX() - getMinX();
	}
	
	public double getHeight() {
		return getMaxY() - getMinY();
	}
	
	public boolean contains(Point2D p) {
		return contains(p.getX(), p.getY());
	}
	
	public boolean contains(double x, double y) {
		return (x >= minX) && (x <= maxX) && (y >= minY) && (y <= maxY);
	}
	
	public Point2D getCenter() {
		return new Point2D.Double(minX + (maxX - minX) / 2.0, minY + (maxY - minY) / 2.0);
	}

	public Point2D getMin() {
		return new Point2D.Double(minX, minY);
	}
	
	public Point2D getMax() {
		return new Point2D.Double(maxX, maxY);
	}

	public Bounds adjust(double mx, double my) {
		return new Bounds(minX + mx, minY + my, maxX + mx, maxY + my);
	}

	public double getPercentageX(double percentage) {
		return getMinX() + percentage * getWidth();
	}
	
	public double getPercentageY(double percentage) {
		return getMinY() + percentage * getHeight();
	}
	
	@Override
	public String toString() {
		return String.format("%.2f, %.2f - %.2f, %.2f", getMinX(), getMinY(), getMaxX(), getMaxY());
	}
	
}
