package hgm.gef.function;

import java.util.function.Function;

import hgm.gef.canvas.Canvas;

public class XScreenToModel implements Function<Double, Double> {
	
	private Canvas canvas;

	public XScreenToModel(Canvas canvas) {
		this.canvas = canvas;
	}

	@Override
	public Double apply(Double t) {
		return canvas.xScreenToModel(t);
	}

}
