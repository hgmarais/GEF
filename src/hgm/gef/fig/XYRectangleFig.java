package hgm.gef.fig;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.Painter;
import hgm.gef.util.Unit;

public class XYRectangleFig extends LayerFig {
	
	public static final String POINT1 = "POINT1";
	
	public static final String POINT2 = "POINT2";
	
	public static final String X1 = "X1";
	
	public static final String Y1 = "Y1";
	
	public static final String X2 = "X2";
	
	public static final String Y2 = "Y2";
	
	private Rectangle2D shape;
	
	private Point2D relative;
	
	private Unit relativeUnit;
	
	public XYRectangleFig(double x1, double y1, double x2, double y2) {
		setProperty(X1, x1);
		setProperty(Y1, y1);
		setProperty(X2, x2);
		setProperty(Y2, y2);
	}
	
	public void setRelative(Point2D relative) {
		this.relative = relative;
		refresh();
	}
	
	public void setRelativeUnit(Unit relativeUnit) {
		this.relativeUnit = relativeUnit;
		refresh();
	}
	
	public double getX1() {
		return (Double)getProperty(X1);
	}
	
	public double getY1() {
		return (Double)getProperty(Y1);
	}
	
	public double getX2() {
		return (Double)getProperty(X2);
	}
	
	public double getY2() {
		return (Double)getProperty(Y2);
	}
	
	public void refreshShape() {
		Canvas canvas = getCanvas();
		
		if (canvas == null) {
			return;
		}
		
		if (relative == null) {
			double mx1 = canvas.xToModel(getX1(), getPropertyUnit(X1));
			double my1 = canvas.yToModel(getY1(), getPropertyUnit(Y1));
			double mx2 = canvas.xToModel(getX2(), getPropertyUnit(X2));
			double my2 = canvas.yToModel(getY2(), getPropertyUnit(Y2));
			shape = new Bounds(mx1, my1, mx2, my2).toRectangle();
		} else {
			double mx1 = canvas.xRelative(relative.getX(), relativeUnit, getX1(), getPropertyUnit(X1));
			double my1 = canvas.yRelative(relative.getY(), relativeUnit, getY1(), getPropertyUnit(Y1));
			double mx2 = canvas.xRelative(relative.getX(), relativeUnit, getX2(), getPropertyUnit(X2));
			double my2 = canvas.yRelative(relative.getY(), relativeUnit, getY2(), getPropertyUnit(Y2));
			shape = new Bounds(mx1, my1, mx2, my2).toRectangle();
		}
	}
	
	@Override
	public void propertyChanged(String name) {
		switch (name) {
		case X1: 
			refresh();
			firePropertyChanged(POINT1);
			break;
		case Y1: 
			refresh();
			firePropertyChanged(POINT1);
			break;
		case X2: 
			refresh();
			firePropertyChanged(POINT2);
			break;
		case Y2: 
			refresh();
			firePropertyChanged(POINT2);
			break;
		case POINT1:
			refresh();
			break;
		case POINT2:
			refresh();
			break;
		}
	}
	
	public void setPoint1(Point2D value) {
		setProperty(X1, value.getX());
		setProperty(Y1, value.getY());
		refresh();
		firePropertyChanged(POINT1);
	}
	
	public void setPoint2(Point2D value) {
		setProperty(X2, value.getX());
		setProperty(Y2, value.getY());
		refresh();
		firePropertyChanged(POINT2);
	}
	
	public Point2D getPoint1() {
		return new Point2D.Double(getX1(), getY1());
	}
	
	public Point2D getPoint2() {
		return new Point2D.Double(getX2(), getY2());
	}
	
	@Override
	public Object getProperty(String name) {
		switch (name) {
		case POINT1: return getPoint1();
		case POINT2: return getPoint2();
		}
		
		return super.getProperty(name);
	}

	@Override
	public Bounds getBounds() {
		return new Bounds(getX1(), getY1(), getX2(), getY2());
	}

	@Override
	protected void paintImpl(Painter p) {
		refreshShape();
		
		if (shape != null) {
			p.paint(shape);
		}
	}

}
