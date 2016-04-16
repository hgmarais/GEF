package hgm.gef.fig;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.Painter;

public class RectangleFig extends LayerFig {
	
	public static final String POINT1 = "POINT1";
	
	public static final String POINT2 = "POINT2";
	
	public static final String X1 = "X1";
	
	public static final String Y1 = "Y1";
	
	public static final String X2 = "X2";
	
	public static final String Y2 = "Y2";
	
	private double x1;
	
	private double y1;
	
	private double x2;
	
	private double y2;

	private Rectangle2D shape;
	
	public RectangleFig(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public void refreshShape() {
		Canvas canvas = getCanvas();
		
		if (canvas == null) {
			return;
		}
		
		double mx1 = canvas.xConvertToModel(x1, getPropertyUnit(X1));
		double my1 = canvas.yConvertToModel(y1, getPropertyUnit(Y1));
		double mx2 = canvas.xConvertToModel(x2, getPropertyUnit(X2));
		double my2 = canvas.yConvertToModel(y2, getPropertyUnit(Y2));
		
		shape = new Bounds(mx1, my1, mx2, my2).toRectangle();
	}
	
	@Override
	public void setProperty(String name, Object value) {
		if (value == null) {
			return;
		}
		
		switch (name) {
		case X1: 
			x1 = ((Number) value).doubleValue();
			refresh();
			firePropertyChanged(X1);
			firePropertyChanged(POINT1);
			break;
		case Y1: 
			y1 = ((Number) value).doubleValue();
			refresh();
			firePropertyChanged(Y1);
			firePropertyChanged(POINT1);
			break;
		case X2: 
			x2 = ((Number) value).doubleValue();
			refresh();
			firePropertyChanged(X2);
			firePropertyChanged(POINT2);
			break;
		case Y2: 
			y2 = ((Number) value).doubleValue();
			refresh();
			firePropertyChanged(Y1);
			firePropertyChanged(POINT2);
			break;
		case POINT1:
			setPoint1((Point2D)value);
			break;
		case POINT2:
			setPoint2((Point2D)value);
			break;
		}
		
		super.setProperty(name, value);
	}
	
	public void setPoint1(Point2D value) {
		x1 = value.getX();
		y1 = value.getY();
		refresh();
		firePropertyChanged(X1);
		firePropertyChanged(Y1);
		firePropertyChanged(POINT1);
	}
	
	public Point2D getPoint1() {
		return new Point2D.Double(x1, y1);
	}
	
	public Point2D getPoint2() {
		return new Point2D.Double(x2, y2);
	}
	
	public void setPoint2(Point2D value) {
		x2 = value.getX();
		y2 = value.getY();
		refresh();
		firePropertyChanged(X2);
		firePropertyChanged(Y2);
		firePropertyChanged(POINT2);
	}

	@Override
	public Object getProperty(String name) {
		switch (name) {
		case X1: return x1;
		case Y1: return y1;
		case X2: return x2;
		case Y2: return y2;
		case POINT1: return getPoint1();
		case POINT2: return getPoint2();
		}
		
		return super.getProperty(name);
	}

	@Override
	public Bounds getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void paintImpl(Painter p) {
		refreshShape();
		
		if (shape != null) {
			p.paint(shape);
		}
	}

}
