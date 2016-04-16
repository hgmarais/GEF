package hgm.gef.fig;

import java.awt.geom.Point2D;

public class RectangleFig extends ShapeFig {
	
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
	
	public RectangleFig(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		refreshShape();
	}

	private void refreshShape() {
		setShape(new Bounds(x1, y1, x2, y2).toRectangle());
	}
	
	@Override
	public void setProperty(String name, Object value) {
		if (value == null) {
			return;
		}
		
		switch (name) {
		case X1: 
			x1 = ((Number) value).doubleValue();
			refreshShape();
			firePropertyChanged(X1);
			firePropertyChanged(POINT1);
			break;
		case Y1: 
			y1 = ((Number) value).doubleValue();
			refreshShape();
			firePropertyChanged(Y1);
			firePropertyChanged(POINT1);
			break;
		case X2: 
			x2 = ((Number) value).doubleValue();
			refreshShape();
			firePropertyChanged(X2);
			firePropertyChanged(POINT2);
			break;
		case Y2: 
			y2 = ((Number) value).doubleValue();
			refreshShape();
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
		refreshShape();
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
		refreshShape();
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

}
