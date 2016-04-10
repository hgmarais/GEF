package hgm.gef.canvas;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import hgm.gef.BasicStyle;
import hgm.gef.Style;
import hgm.gef.editor.CoordSystem;
import hgm.gef.editor.LayerManagerListener;
import hgm.gef.fig.Bounds;
import hgm.gef.layer.Layer;

public class CanvasPanel extends JPanel implements CanvasListener, LayerManagerListener, MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener, AdjustmentListener {
	
	/***/
	private static final long serialVersionUID = 1L;
	
	private ViewportPanel viewportPanel;
	
	private Style style;

	private Canvas canvas;

	private JScrollBar hBar;

	private JScrollBar vBar;
	
	private JPanel corner;

	private class ViewportPanel extends JPanel {
		
		/***/
		private static final long serialVersionUID = 1L;

		public ViewportPanel() {
			setOpaque(true);
			setDoubleBuffered(true);
		}
		
		@Override
		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setBackground(getBackground());
			g2.setColor(getBackground());
			g2.fillRect(0, 0, getWidth(), getHeight());
			canvas.paint(g2, style);
//			g2.setColor(Color.RED);
//			g2.draw(canvas.getCanvasBounds());
		}
		
	}
	
	public CanvasPanel(Canvas canvas) {
		this.canvas = canvas;
		this.style = new BasicStyle(new BasicStroke(1.0f), Color.WHITE, null);
		viewportPanel = new ViewportPanel();
		
		canvas.addListener(this);
		canvas.getLayerManager().addListener(this);

		hBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 0);
		vBar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, 0);
		corner = new JPanel();
		
		JPanel hPanel = new JPanel(new BorderLayout());
		hPanel.add(hBar, BorderLayout.CENTER);
		hPanel.add(corner, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(viewportPanel, BorderLayout.CENTER);
		add(hPanel, BorderLayout.SOUTH);
		add(vBar, BorderLayout.EAST);
		
		viewportPanel.addMouseListener(this);
		viewportPanel.addMouseWheelListener(this);
		viewportPanel.addMouseMotionListener(this);
		hBar.addAdjustmentListener(this);
		vBar.addAdjustmentListener(this);
		addComponentListener(this);
		
		setBackground(Color.WHITE);
	}
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public JPanel getViewportPanel() {
		return viewportPanel;
	}
	
	@Override
	public void setBackground(Color bg) {
		if (viewportPanel != null) {
			viewportPanel.setBackground(bg);
		}
		super.setBackground(bg);
	}
	
	private void refreshVisibleSize() {
		canvas.setScreenSize(getWidth(), getHeight());
//		double x1 = canvas.xScreenToModel(0);
//		double y1 = canvas.yScreenToModel(0);
//		double x2 = canvas.xScreenToModel(getWidth());
//		double y2 = canvas.yScreenToModel(getHeight());
//		double x1 = canvas.getConverter().xPixelToModel(0);
//		double y1 = canvas.getConverter().yPixelToModel(0);
//		double x2 = canvas.getConverter().xPixelToModel(viewportPanel.getWidth());
//		double y2 = canvas.getConverter().yPixelToModel(viewportPanel.getHeight());
//		canvas.setVisibleBounds(new Bounds(x1, y1, x2, y2));
	}
	
	public void setStyle(Style style) {
		this.style = style;
	}
	
	public Style getStyle() {
		return style;
	}

	@Override
	public void layerAdded(Layer layer) {
		repaint();
	}

	@Override
	public void layerRemoved(Layer layer) {
		repaint();
	}
	
	@Override
	public void repaintRequested() {
		repaint();
	}

	@Override
	public void repaintRequested(Bounds mb) {
		// TODO : Limit to bounds.
		repaint();
	}

	private void refreshSelectables(int sx, int sy) {
		double mx = canvas.xScreenToModel(sx);
		double my = canvas.yScreenToModel(sy);
		canvas.getSelectionManager().refreshSelectables(mx, my);
	}
	
	private void select(boolean toggle, int sx, int sy) {
		double mx = canvas.xScreenToModel(sx);
		double my = canvas.yScreenToModel(sy);
		canvas.getSelectionManager().select(toggle, mx, my);
		refreshSelectables(sx, sy);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (e.isControlDown()) {
			double mx = canvas.xScreenToModel(e.getX());
			double my = canvas.yScreenToModel(e.getY());
			canvas.adjustZoomAround(-e.getPreciseWheelRotation(), mx, my);
		} else if (e.isAltDown()) {
//			if (e.isShiftDown()) {
//				canvas.adjustDimension(-e.getPreciseWheelRotation(), 0.0);
//			} else {
//				canvas.adjustDimension(0.0, -e.getPreciseWheelRotation());
//			}
		} else {
			if (e.isShiftDown()) {
				canvas.adjustOffset(e.getPreciseWheelRotation(), 0.0);
			} else {
				canvas.adjustOffset(0.0, e.getPreciseWheelRotation());
			}
		}
		
		mouseMoved(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		select(e.isControlDown(), e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		refreshSelectables(e.getX(), e.getY());
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		refreshSelectables(e.getX(), e.getY());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		canvas.getSelectionManager().clearSelectables();
	}
	
	@Override
	public void visibleBoundsChanged() {
		refreshScrollBars();
		repaint();
	}
	
	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
//		if (e.getValueIsAdjusting()) {
//			return;
//		}
//		
//		BoundedRangeModel hModel = hBar.getModel();
//		BoundedRangeModel vModel = vBar.getModel();
//		
//		int hMin = hModel.getMinimum();
//		int hMax = hModel.getMaximum();
//		int hValue = hModel.getValue();
//		int hExtent = hModel.getExtent();
//		
//		int vMin = vModel.getMinimum();
//		int vMax = vModel.getMaximum();
//		int vValue = vModel.getValue();
//		int vExtent = vModel.getExtent();
		
//		System.out.println("vMin : "+vMin);
//		System.out.println("vMax : "+vMax);
//		System.out.println("vValue : "+vValue);
//		System.out.println("vExtent : "+vExtent);
		
//		CoordSystem coordSystem = canvas.getCoordSystem();
		
//		if (coordSystem.getXDirection() == -1) {
//			hValue = hMin + (hMax - hValue) + hExtent;
//		}
		
//		if (coordSystem.getYDirection() == -1) {
//			vValue = vMin + (vMax - vValue);
//		}
//		
//		double mx = canvas.xPixelToModel(hValue);
//		double my = canvas.yPixelToModel(vValue);
//		
//		canvas.adjustOffset(mx-canvas.getLeft(), coordSystem.vertical(my - canvas.getTop()));
	}
	
	private void refreshScrollBars() {
		Bounds mCanvasBounds = canvas.getBounds();
		Bounds mVisibleBounds = canvas.getVisibleBounds();
		
		double pvx = canvas.xModelToPixel(mVisibleBounds.getMinX());
		double pvy = canvas.yModelToPixel(mVisibleBounds.getMinY());
		double pvw = canvas.xModelToPixel(mVisibleBounds.getWidth());
		double pvh = canvas.yModelToPixel(mVisibleBounds.getHeight());
		double pcx = canvas.xModelToPixel(mCanvasBounds.getMinX());
		double pcy = canvas.yModelToPixel(mCanvasBounds.getMinY());
		double pcw = canvas.xModelToPixel(mCanvasBounds.getWidth());
		double pch = canvas.yModelToPixel(mCanvasBounds.getHeight());
		
//		double pvx = canvas.xModelToScreen(mVisibleBounds.getMinX());
//		double pvy = canvas.yModelToScreen(mVisibleBounds.getMinY());
//		double pvw = canvas.xModelToScreen(mVisibleBounds.getWidth());
//		double pvh = canvas.yModelToScreen(mVisibleBounds.getHeight());
//		double pcx = canvas.xModelToScreen(mCanvasBounds.getMinX());
//		double pcy = canvas.yModelToScreen(mCanvasBounds.getMinY());
//		double pcw = canvas.xModelToScreen(mCanvasBounds.getWidth());
//		double pch = canvas.yModelToScreen(mCanvasBounds.getHeight());
		
		int hMin = (int) Math.min(pvx, pcx);
		int hMax = (int) Math.max(pcx + pcw, pvx + pvw);
		int hExtent = (int) pvw;
		int hValue = (int) pvx;
		
		int vMin = (int) Math.min(pvy, pcy);
		int vMax = (int) Math.max(pcy + pch, pvy + pvh);
		int vExtent = (int) pvh;
		int vValue = (int) pvy;
		
//		System.out.println("*************************");
//		System.out.println("pvx : "+pvx);
//		System.out.println("pvw : "+pvw);
//		System.out.println("pcx : "+pcx);
//		System.out.println("pcw : "+pcw);
//		System.out.println("hMin : "+hMin);
//		System.out.println("hMax : "+hMax);
//		System.out.println("hExtent : "+hExtent);
//		System.out.println("hValue : "+hValue);
//		System.out.println("hValue+hExtent : "+(hValue+hExtent));
//		System.out.println("------------------------");
//		System.out.println("pvy : "+pvy);
//		System.out.println("pvh : "+pvh);
//		System.out.println("pcy : "+pcy);
//		System.out.println("pch : "+pch);
//		System.out.println("vMin : "+vMin);
//		System.out.println("vMax : "+vMax);
//		System.out.println("vExtent : "+vExtent);
//		System.out.println("vValue : "+vValue);
//		System.out.println("vValue+vExtent : "+(vValue+vExtent));
		
		if ((hValue + hExtent) > hMax) {
			hExtent = hMax - hValue;
		}
		
		if ((vValue + vExtent) > vMax) {
			vExtent = vMax - vValue;
		}
		
		CoordSystem coordSystem = canvas.getCoordSystem();
		
		if (coordSystem.getXDirection() == -1) {
			hValue = hMax - hValue + hMin - hExtent;
		}
		
		if (coordSystem.getYDirection() == -1) {
			vValue = vMax - vValue + vMin - vExtent;
		}
		
		DefaultBoundedRangeModel hModel = new DefaultBoundedRangeModel(hValue, hExtent, hMin, hMax);
		DefaultBoundedRangeModel vModel = new DefaultBoundedRangeModel(vValue, vExtent, vMin, vMax);
		
		hBar.setModel(hModel);
		vBar.setModel(vModel);
		
		boolean hVisible = hModel.getExtent() < (hModel.getMaximum() - hModel.getMinimum());
		boolean vVisible = vModel.getExtent() < (vModel.getMaximum() - vModel.getMinimum());
		
		hBar.setVisible(hVisible);
		vBar.setVisible(vVisible);
		
		corner.setPreferredSize(new Dimension(hBar.getHeight(), vBar.getWidth()));
		corner.setVisible(hVisible && vVisible);
		
		hBar.repaint();
		vBar.repaint();
	}

	@Override
	public void boundsChanged() {
		refreshScrollBars();
		repaint();
	}

	@Override
	public void zoomChanged() {
		refreshVisibleSize();
		repaint();
	}
	
	@Override
	public void componentShown(ComponentEvent e) {
		refreshVisibleSize();
		refreshScrollBars();
	}
	
	@Override
	public void componentResized(ComponentEvent e) {
		refreshVisibleSize();
		refreshScrollBars();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

}
