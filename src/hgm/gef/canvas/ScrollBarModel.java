package hgm.gef.canvas;

import javax.swing.DefaultBoundedRangeModel;

import hgm.gef.editor.CoordSystem;
import hgm.gef.fig.Bounds;

public class ScrollBarModel extends DefaultBoundedRangeModel {

	/***/
	private static final long serialVersionUID = 1L;
	
	private CanvasPanel canvasPanel;
	
	private boolean horizontal;
	
	private boolean requireVisible = true;

	private boolean adjusting;
	
	public ScrollBarModel(CanvasPanel canvasPanel, boolean horizontal) {
		this.canvasPanel = canvasPanel;
		this.horizontal = horizontal;
		refresh();
	}
	
	public boolean requireVisible() {
		return requireVisible;
	}

	public void refresh() {
		if (horizontal) {
			refreshHorizontal();
		} else {
			refreshVertical();
		}
		
		requireVisible = getExtent() < (getMaximum() - getMinimum());
	}
	
	private void refreshHorizontal() {
		Canvas canvas = canvasPanel.getCanvas();
		Bounds mCanvasBounds = canvas.getBounds();
		Bounds mVisibleBounds = canvas.getVisibleBounds();
		
		double pvx = canvas.xModelToScreen(mVisibleBounds.getMinX());
		double pvw = canvas.wModelToScreen(mVisibleBounds.getWidth());
		double pcx = canvas.xModelToScreen(mCanvasBounds.getMinX());
		double pcw = canvas.wModelToScreen(mCanvasBounds.getWidth());
		
		CoordSystem coordSystem = canvas.getCoordSystem();

		pvx = coordSystem.horizontal(pvx);
		pcx = coordSystem.horizontal(pcx);
		
		double hMin = Math.min(pvx, pcx);
		double hMax = Math.max(pcx + pcw, pvx + pvw);
		double hExtent = pvw;
		double hValue = pvx;
		
		if ((hValue + hExtent) > hMax) {
			hExtent = hMax - hValue;
		}
		
		if (coordSystem.getXDirection() == -1) {
			hValue = hMax - hValue + hMin - hExtent;
		}
		
		refresh(hMin, hMax, hValue, hExtent, canvas.getScreenWidth());
	}
	
	private void refreshVertical() {
		Canvas canvas = canvasPanel.getCanvas();
		Bounds mCanvasBounds = canvas.getBounds();
		Bounds mVisibleBounds = canvas.getVisibleBounds();
		
		double pvy = canvas.yModelToScreen(mVisibleBounds.getMinY());
		double pvh = canvas.hModelToScreen(mVisibleBounds.getHeight());
		double pcy = canvas.yModelToScreen(mCanvasBounds.getMinY());
		double pch = canvas.hModelToScreen(mCanvasBounds.getHeight());
		
		CoordSystem coordSystem = canvas.getCoordSystem();

		pvy = coordSystem.vertical(pvy);
		pcy = coordSystem.vertical(pcy);
		
		double vMin = Math.min(pvy, pcy);
		double vMax = Math.max(pcy + pch, pvy + pvh);
		double vExtent = pvh;
		double vValue = pvy;
		
		if ((vValue + vExtent) > vMax) {
			vExtent = vMax - vValue;
		}
		
		if (coordSystem.getYDirection() == -1) {
			vValue = vMax - vValue + vMin - vExtent;
		}
		
		refresh(vMin, vMax, vValue, vExtent, canvas.getScreenHeight());
	}
	
	private void refresh(double min, double max, double value, double extent, int size) {
		double diff = Math.abs(max - min);
		value = Math.abs(value - min);
		
		int sMin = 0;
		int sMax = size;
		int sValue = (int)((double)size * value / diff);
		int sExtent = (int)((double)size * extent / diff);
		
		setRangeProperties(sValue, sExtent, sMin, sMax, adjusting);
	}
	
	public void applyToCanvas() {
		if (adjusting) {
			return;
		}
		
		adjusting = true;
		
		try {
			if (horizontal) {
				applyHorizontal();
			} else {
				applyVertical();
			}
		} finally {
			adjusting = false;
		}
	}

	private void applyHorizontal() {
		Canvas canvas = canvasPanel.getCanvas();
		CoordSystem coordSystem = canvas.getCoordSystem();
		Bounds mCanvasBounds = canvas.getBounds();
		Bounds mVisibleBounds = canvas.getVisibleBounds();
		
		int sMin = getMinimum();
		int sMax = getMaximum();
		int sValue = getValue();
		
		if (coordSystem.getXDirection() == -1) {
			sValue = sMin + (sMax - sValue);
		}
		
		double percentage = (double)sValue / (double)(Math.abs(sMax - sMin)); 
		double pvx = canvas.xModelToScreen(mVisibleBounds.getMinX());
		double pvw = canvas.wModelToScreen(mVisibleBounds.getWidth());
		double pcx = canvas.xModelToScreen(mCanvasBounds.getMinX());
		double pcw = canvas.wModelToScreen(mCanvasBounds.getWidth());
		
		pvx = coordSystem.horizontal(pvx);
		pcx = coordSystem.horizontal(pcx);
		
		double hMin = Math.min(pvx, pcx);
		double hMax = Math.max(pcx + pcw, pvx + pvw);
		double diff = Math.abs(hMax - hMin);

		double newValue = hMin + (percentage * diff);
		newValue = canvas.wScreenToModel(newValue);
		newValue = coordSystem.horizontal(newValue);
		
		canvas.adjustOffset(newValue, 0.0);
	}

	private void applyVertical() {
		Canvas canvas = canvasPanel.getCanvas();
		CoordSystem coordSystem = canvas.getCoordSystem();
		Bounds mCanvasBounds = canvas.getBounds();
		Bounds mVisibleBounds = canvas.getVisibleBounds();
		
		int sMin = getMinimum();
		int sMax = getMaximum();
		int sValue = getValue();
		
		if (coordSystem.getYDirection() == -1) {
			sValue = sMin + (sMax - sValue);
		}
		
		double percentage = (double)sValue / (double)(Math.abs(sMax - sMin)); 
		double pvy = canvas.yModelToScreen(mVisibleBounds.getMinY());
		double pvh = canvas.hModelToScreen(mVisibleBounds.getHeight());
		double pcy = canvas.yModelToScreen(mCanvasBounds.getMinY());
		double pch = canvas.hModelToScreen(mCanvasBounds.getHeight());
		
		pvy = coordSystem.vertical(pvy);
		pcy = coordSystem.vertical(pcy);
		
		double hMin = Math.min(pvy, pcy);
		double hMax = Math.max(pcy + pch, pvy + pvh);
		double diff = Math.abs(hMax - hMin);

		double newValue = hMin + (percentage * diff);
		newValue = canvas.wScreenToModel(newValue);
		newValue = coordSystem.vertical(newValue);
		
		canvas.adjustOffset(0.0, newValue);		
	}
	
}
