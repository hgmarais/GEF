package hgm.gef.fig;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.Painter;

public class WHRectangleFig extends LayerFig {
	
	public static final String POINT = "POINT";
	
	public static final String X = "X";
	
	public static final String Y = "Y";
	
	public static final String W = "W";
	
	public static final String H = "H";
	
	private Rectangle2D shape;
	
	public WHRectangleFig(double x, double y, double w, double h) {
		setProperty(X, x);
		setProperty(Y, y);
		setProperty(W, w);
		setProperty(H, h);
	}
	
	public double getX() {
		return (Double)getProperty(X);
	}
	
	public double getY() {
		return (Double)getProperty(Y);
	}
	
	public double getW() {
		return (Double)getProperty(W);
	}
	
	public double getH() {
		return (Double)getProperty(H);
	}
	
	public void refreshShape() {
		Canvas canvas = getCanvas();
		
		if (canvas == null) {
			return;
		}
		
		double mx = canvas.xToModel(getX(), getPropertyUnit(X));
		double my = canvas.yToModel(getY(), getPropertyUnit(Y));
		double mw = canvas.wToModel(getW(), getPropertyUnit(W));
		double mh = canvas.hToModel(getH(), getPropertyUnit(H));
		
		shape = new Rectangle2D.Double(mx, my, mw, mh);
	}
	
	@Override
	public void propertyChanged(String name) {
		switch (name) {
		case X: 
			refresh();
			firePropertyChanged(POINT);
			break;
		case Y: 
			refresh();
			firePropertyChanged(POINT);
			break;
		case W: 
			refresh();
			break;
		case H: 
			refresh();
			break;
		case POINT:
			refresh();
			break;
		}
	}
	
	public void setPoint(Point2D value) {
		setProperty(X, value.getX());
		setProperty(Y, value.getY());
	}
	
	public Point2D getPoint() {
		return new Point2D.Double(getX(), getY());
	}
	
	@Override
	public Object getProperty(String name) {
		switch (name) {
		case POINT: return getPoint();
		}
		
		return super.getProperty(name);
	}

	@Override
	public Bounds getBounds() {
		double x = getX();
		double y = getY();
		// TODO : Calculate bounds based on zoom factor.
		return new Bounds(x, y, x + getW(), y + getY());
	}

	@Override
	protected void paintImpl(Painter p) {
		refreshShape();
		
		if (shape != null) {
			p.paint(shape);
		}
	}

}
