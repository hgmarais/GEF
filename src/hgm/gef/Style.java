package hgm.gef;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import hgm.gef.canvas.Canvas;

public interface Style {
	
	Style prepare(Canvas canvas);
	
	Stroke getStroke();
	
	Color getStrokeColor();
	
	Paint getFillPaint();

}
