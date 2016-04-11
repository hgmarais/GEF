package hgm.gef.canvas;

import hgm.gef.fig.Bounds;

public interface CanvasListener {
	
	void boundsChanged();

	void zoomChanged();

	void visibleBoundsChanged();

	void repaintRequested();

	void repaintRequested(Bounds mb);

	void converterChanged();

}
