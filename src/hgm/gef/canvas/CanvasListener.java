package hgm.gef.canvas;

import java.awt.geom.Rectangle2D;

public interface CanvasListener {
	
	void boundsChanged(Rectangle2D mCanvasBounds);

	void zoomChanged(double zoom);

	void visibleBoundsChanged();

	void repaintRequested();

	void repaintRequested(Rectangle2D mr);

}
