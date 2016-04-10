package hgm.gef.editor;

import hgm.gef.fig.Bounds;

public abstract class CoordSystem {
	
	public CoordSystem() {
	}
	
	public double getLeft(Bounds bounds) {
		return getXDirection() == 1 ? bounds.getMinX() : bounds.getMaxX();
	}
	
	public double getTop(Bounds bounds) {
		return getYDirection() == 1 ? bounds.getMinY() : bounds.getMaxY();
	}
	
	public double xAdjust(double x, double dx) {
		return x + (double)getXDirection() * dx;
	}
	
	public double yAdjust(double y, double dy) {
		return y + (double)getYDirection() * dy;
	}
	
	public Bounds adjust(Bounds bounds, double dx, double dy) {
		return bounds.adjust(horizontal(dx), vertical(dy));
	}
	
	public double vertical(double value) {
		return value * (double)getYDirection();
	}
	
	public double horizontal(double value) {
		return value * (double)getXDirection();
	}
	
	public abstract int getXDirection();
	
	public abstract int getYDirection();

}
