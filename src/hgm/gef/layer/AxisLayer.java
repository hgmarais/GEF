package hgm.gef.layer;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.List;

import hgm.gef.BasicStyle;
import hgm.gef.canvas.Axis;
import hgm.gef.canvas.Painter;
import hgm.gef.fig.Bounds;
import hgm.gef.fig.LayerFig;

public class AxisLayer extends AbstractLayer {
	
	private Axis axis;
	
	private BasicStyle style;

	public AxisLayer(Axis axis) {
		this.axis = axis;
		style = BasicStyle.line(false, Color.BLACK);
	}

	@Override
	public void addFigure(LayerFig figure) {
	}

	@Override
	public void removeFigure(LayerFig figure) {
	}

	@Override
	public List<LayerFig> getFigures() {
		return Collections.emptyList();
	}

	@Override
	public List<LayerFig> getFigures(double mx, double my) {
		return Collections.emptyList();
	}

	@Override
	public void paint(Painter p) {
		p.pushStyle(style);
		
		switch (axis) {
		case HORIZONTAL:
			paintHorizontal(p);
			break;
		case VERTICAL: 
			paintVertical(p);
			break;
		case BOTH:
			paintHorizontal(p);
			paintVertical(p);
			break;
		}
		
		p.popStyle();
	}

	private void paintHorizontal(Painter p) {
		Bounds bounds = p.canvas.getVisibleBounds();
		p.paint(new Line2D.Double(bounds.getMinX(), 0.0, bounds.getMaxX(), 0.0));
	}

	private void paintVertical(Painter p) {
		Bounds bounds = p.canvas.getVisibleBounds();
		p.paint(new Line2D.Double(0.0, bounds.getMinY(), 0.0, bounds.getMaxY()));		
	}

}
