package hgm.gef;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.Arrays;

import hgm.gef.canvas.Canvas;

public class BasicStyle implements Style {
	
	private static final BasicStroke BASIC_STROKE = new BasicStroke(1.0f);

	private boolean zoomStroke = false;
	
	private BasicStroke stroke;
	
	private Color strokeColor;
	
	private Paint fillPaint;
	
	public BasicStyle(Color strokeColor, Paint fillPaint) {
		this(BASIC_STROKE, strokeColor, fillPaint);
	}

	public BasicStyle(BasicStroke stroke, Color strokeColor, Paint fillPaint) {
		this(true, stroke, strokeColor, fillPaint);
	}
	
	public BasicStyle(boolean zoomStroke, Color strokeColor, Paint fillPaint) {
		this(zoomStroke, BASIC_STROKE, strokeColor, fillPaint);
	}
	
	public BasicStyle(boolean zoomStroke, BasicStroke stroke, Color strokeColor, Paint fillPaint) {
		this.zoomStroke = zoomStroke;
		this.stroke = stroke;
		this.strokeColor = strokeColor;
		this.fillPaint = fillPaint;
	}
	
	@Override
	public Style prepare(Canvas canvas) {
		if (zoomStroke || (stroke == null)) {
			return this;
		}
		
		float width = stroke.getLineWidth();
		float miter = stroke.getMiterLimit();
		float[] dash = stroke.getDashArray();
		float phase = stroke.getDashPhase();
		
		float zoom = (float) canvas.getZoom();
		
		width /= zoom;
		phase /= zoom;
		
		BasicStroke newStroke;
		
		if (dash != null) {
			dash = Arrays.copyOf(dash, dash.length);
			
			for (int i = 0; i < dash.length; i++) {
				dash[i] /= zoom;
			}
		
			newStroke = new BasicStroke(width, stroke.getEndCap(), stroke.getLineJoin(), miter, dash, phase);
		} else {
			newStroke = new BasicStroke(width, stroke.getEndCap(), stroke.getLineJoin(), miter);
		}
		
		return new BasicStyle(newStroke, strokeColor, fillPaint);
	}

	@Override
	public Stroke getStroke() {
		return stroke;
	}

	@Override
	public Color getStrokeColor() {
		return strokeColor;
	}

	@Override
	public Paint getFillPaint() {
		return fillPaint;
	}
	
	public static BasicStyle line(boolean zoomStroke, Color color) {
		return new BasicStyle(zoomStroke, color, null);
	}
	
	public static BasicStyle dashedLine(boolean zoomStroke, Color color) {
		BasicStroke s = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1.0f, new float[]{5.0f, 5.0f}, 5.0f);
		return new BasicStyle(zoomStroke, s, color, null);
	}

}
