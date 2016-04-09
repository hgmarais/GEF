package hgm.gef.editor;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.LinkedList;

import hgm.gef.Style;
import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.XYCanvasConverter;

public class Painter {
	
	public final Canvas canvas;
	
	public final Graphics2D g;

	private Style topStyle = null;
	
	private LinkedList<Style> styles = new LinkedList<>();

	public Painter(Canvas canvas, Graphics2D g, Style style) {
		this.canvas = canvas;
		this.g = g;
		pushStyle(style);
		canvas.getConverter().apply(g);
	}
	
	public XYCanvasConverter getConverter() {
		return canvas.getConverter();
	}
	
	public void pushStyle(Style style) {
		if (style == null) {
			return;
		}
		
		topStyle = style.prepare(canvas);
		styles.add(topStyle);
	}
	
	public void popStyle() {
		if (!styles.isEmpty()) {
			styles.removeLast();
		}
		
		if (styles.isEmpty()) {
			topStyle = null;
		} else {
			topStyle = styles.getLast();
		}
	}
	
	public void paint(Shape shape) {
		if ((topStyle == null) || (shape == null)) {
			return;
		}
		
		if (topStyle.getFillPaint() != null) {
			g.setPaint(topStyle.getFillPaint());
			g.fill(shape);
		}
		
		if ((topStyle.getStroke() != null) && (topStyle.getStrokeColor() != null)) {
			g.setStroke(topStyle.getStroke());
			g.setColor(topStyle.getStrokeColor());
			g.draw(shape);
		}
	}
	
}
