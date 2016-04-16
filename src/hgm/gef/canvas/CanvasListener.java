package hgm.gef.canvas;

import hgm.gef.fig.Bounds;

public interface CanvasListener {
	
	void boundsChanged(Canvas source);

	void zoomChanged(Canvas source);

	void visibleBoundsChanged(Canvas source);

	void repaintRequested(Canvas source);

	void repaintRequested(Canvas source, Bounds mb);

	void converterChanged(Canvas source);

	void offsetChanged(Canvas canvas, double dx, double dy);

}
