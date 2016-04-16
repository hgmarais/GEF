package hgm.gef.canvas;

import javax.swing.DefaultBoundedRangeModel;

import hgm.gef.fig.Bounds;

public class ScrollBarModel extends DefaultBoundedRangeModel {

	/***/
	private static final long serialVersionUID = 1L;
	
	private Canvas canvas;
	
	private boolean horizontal;
	
	private ScrollBarPolicy policy = ScrollBarPolicy.SHOWN;
	
	private boolean canBeShown = true;
	
	private boolean refreshing = false;

	private boolean adjusting;

	private boolean applying;
	
	private Bounds mLastVisibleBounds;
	
	private double currentMin;
	
	private double currentMax;
	
	private double currentExtent;

	private double currentValue;

	public ScrollBarModel(Canvas canvas, boolean horizontal) {
		this.canvas = canvas;
		this.horizontal = horizontal;
		refresh();
	}
	
	public void setPolicy(ScrollBarPolicy policy) {
		this.policy = policy;
	}
	
	public ScrollBarPolicy getPolicy() {
		return policy;
	}
	
	public boolean canBeShown() {
		return canBeShown;
	}
	
	public boolean shouldBeShown() {
		switch (policy) {
		case AS_NEEDED : return canBeShown();
		case SHOWN : return true;
		case HIDDEN : return false;
		}
		
		return false;
	}

	public void refresh() {
//		if (refreshing) {
//			return;
//		}
		
		refreshing = true;
		
		try {
			if (horizontal) {
				refreshHorizontal();
			} else {
				refreshVertical();
			}
			
			canBeShown = getExtent() < (getMaximum() - getMinimum());
		} finally {
			refreshing = false;
		}
	}
	
	private void refreshHorizontal() {
		if (!refreshLastValue()) {
			return;
		}
		
		Bounds mCanvasBounds = canvas.getBounds();
		Bounds mVisibleBounds = canvas.getVisibleBounds();
		
		double pvx = canvas.xModelToScreen(mVisibleBounds.getMinX());
		double pvw = canvas.wModelToScreen(mVisibleBounds.getWidth());
		double pcx = canvas.xModelToScreen(mCanvasBounds.getMinX());
		double pcw = canvas.wModelToScreen(mCanvasBounds.getWidth());
		
		CoordSystem coordSystem = canvas.getCoordSystem();

		double hMin = Math.min(pvx, pcx);
		double hMax = Math.max(pcx + coordSystem.horizontal(pcw), pvx + coordSystem.horizontal(pvw));
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
		if (!refreshLastValue()) {
			return;
		}
		
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
	
	private boolean refreshLastValue() {
		Bounds b = canvas.getVisibleBounds();
		
		if (b == null) {
			return false;
		}
		
		if ((mLastVisibleBounds == null) || !mLastVisibleBounds.equals(b)) {
			mLastVisibleBounds = b;
			return true;
		}
		
		return false;
	}

	private void refresh(double min, double max, double value, double extent, int size) {
		currentMin = min;
		currentMax = max;
		currentValue = value;
		currentExtent = extent;
		
		double diff = Math.abs(max - min);
		value = Math.abs(value - min);
		
		int sMin = 0;
		int sMax = size;
		int sValue = (int)((double)size * value / diff);
		int sExtent = (int)((double)size * extent / diff);
		
		if ((getMinimum() != sMin) || (getMaximum() != sMax) || (getValue() != sValue) || (getExtent() != sExtent)) {
			setRangeProperties(sValue, sExtent, sMin, sMax, getValueIsAdjusting());
		}
	}
	
	public void applyToCanvas() {
		
//		if (adjusting) {
//			return;
//		}
//		
		if (applying) {
			return;
		}
//		
//		if (refreshing) {
//			return;
//		}
		
		applying = true;
		
		try {
			if (horizontal) {
				applyHorizontal();
			} else {
				applyVertical();
			}
		} finally {
			applying = false;
		}
	}

	private void applyHorizontal() {
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
		
		adjustOffset(newValue, 0.0);
	}

	private void adjustOffset(double mdx, double mdy) {
//		if (adjusting) {
//			return;
//		}
		
		adjusting = true;
		
		try {
			canvas.adjustOffset(mdx, mdy);
		} finally {
			adjusting = false;
		}
	}

	private void applyVertical() {
		CoordSystem coordSystem = canvas.getCoordSystem();
		Bounds mCanvasBounds = canvas.getBounds();
		Bounds mVisibleBounds = canvas.getVisibleBounds();
		
		int sMin = getMinimum();
		int sMax = getMaximum();
		int sValue = getValue();
		
//		System.out.println("sMin : "+sMin);
//		System.out.println("sMax : "+sMax);
//		System.out.println("sValue : "+sValue);
//		System.out.println("sExtent : "+getExtent());
//		
//		if (coordSystem.getYDirection() == -1) {
//			sValue = sMin + (sMax - sValue);
//		}
//		
//		System.out.println("sValue2 : "+sValue);
//		
//		double percentage = (double)sValue / (double)(Math.abs(sMax - sMin));
//		double diff = Math.abs(currentMax - currentMin);
//		double newValue;
//		
//		if (coordSystem.getYDirection() == 1) {
//			newValue = currentMin + (percentage * diff);
//		} else { 
//			newValue = currentMax - (percentage * diff);
//		}
//		
//		adjustOffset(0.0, newValue - currentValue);
//		
//		System.out.println("currentMin : "+currentMin);
//		System.out.println("currentValue : "+currentValue);
//		System.out.println("newValue : "+newValue);
		
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
		
		adjustOffset(0.0, newValue);
	}

	public void adjust(int d) {
		if (horizontal) {
			double diff = canvas.wScreenToModel(canvas.getScreenWidth()) * d / canvas.getScreenWidth();
			adjustOffset(diff, 0.0);
		} else {
			double diff = canvas.hScreenToModel(canvas.getScreenHeight()) * d / canvas.getScreenHeight();
			adjustOffset(0.0, diff);
		}
//		setValue(getValue() + d);
	}
	
}
