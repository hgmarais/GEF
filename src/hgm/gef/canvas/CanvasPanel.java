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
import java.awt.geom.Rectangle2D;

import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import hgm.gef.BasicStyle;
import hgm.gef.Style;
import hgm.gef.editor.LayerManagerListener;
import hgm.gef.layer.Layer;

public class CanvasPanel extends JPanel implements CanvasListener, LayerManagerListener, MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener, AdjustmentListener {
	
	/***/
	private static final long serialVersionUID = 1L;
	
	private ViewportView panel;
	
	private Style style;

	private Canvas canvas;

	private JScrollBar hBar;

	private JScrollBar vBar;
	
	private JPanel corner;

	private class ViewportView extends JPanel {
		
		/***/
		private static final long serialVersionUID = 1L;

		public ViewportView() {
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
		panel = new ViewportView();
		
		canvas.addListener(this);
		canvas.getLayerManager().addListener(this);

		hBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, 0, 0);
		vBar = new JScrollBar(JScrollBar.VERTICAL, 0, 0, 0, 0);
		corner = new JPanel();
		
		JPanel hPanel = new JPanel(new BorderLayout());
		hPanel.add(hBar, BorderLayout.CENTER);
		hPanel.add(corner, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		add(hPanel, BorderLayout.SOUTH);
		add(vBar, BorderLayout.EAST);
		
		panel.addMouseListener(this);
		panel.addMouseWheelListener(this);
		panel.addMouseMotionListener(this);
		hBar.addAdjustmentListener(this);
		vBar.addAdjustmentListener(this);
		addComponentListener(this);
		
		setBackground(Color.WHITE);
	}
	
	@Override
	public void setBackground(Color bg) {
		if (panel != null) {
			panel.setBackground(bg);
		}
		super.setBackground(bg);
	}
	
	private void refreshVisibleSize() {
		double w = canvas.getConverter().xPixelToModel(panel.getWidth());
		double h = canvas.getConverter().yPixelToModel(panel.getHeight());
		canvas.setVisibleSize(w, h);
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
	public void repaintRequested(Rectangle2D mr) {
		// TODO : Limit to rectangle.
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
			if (e.isShiftDown()) {
				canvas.adjustDimension(-e.getPreciseWheelRotation(), 0.0);								
			} else {
				canvas.adjustDimension(0.0, -e.getPreciseWheelRotation());
			}
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
		canvas.setOffset(canvas.xPixelToModel(hBar.getValue()), canvas.yPixelToModel(vBar.getValue()));
	}
	
	private void refreshScrollBars() {
		double pvx = canvas.xModelToPixel(canvas.getVisibleX());
		double pvy = canvas.yModelToPixel(canvas.getVisibleY());
		double pvw = canvas.xModelToPixel(canvas.getVisibleWidth());
		double pvh = canvas.yModelToPixel(canvas.getVisibleHeight());
		double pcw = canvas.xModelToPixel(canvas.getWidth());
		double pch = canvas.xModelToPixel(canvas.getHeight());
		
		double hMax = Math.max(pcw, pvx + pvw);
		double vMax = Math.max(pch, pvy + pvh);

		DefaultBoundedRangeModel hModel = new DefaultBoundedRangeModel((int)pvx, (int)pvw, 0, (int)(hMax));
		DefaultBoundedRangeModel vModel = new DefaultBoundedRangeModel((int)pvy, (int)pvh, 0, (int)(vMax));
		
		hBar.setModel(hModel);
		vBar.setModel(vModel);
		
		boolean hVisible = hModel.getExtent() < hModel.getMaximum();
		boolean vVisible = vModel.getExtent() < vModel.getMaximum();
		
		hBar.setVisible(hVisible);
		vBar.setVisible(vVisible);
		
		corner.setPreferredSize(new Dimension(hBar.getHeight(), vBar.getWidth()));
		corner.setVisible(hVisible && vVisible);
		
		hBar.repaint();
		vBar.repaint();
	}

	@Override
	public void boundsChanged(Rectangle2D mCanvasBounds) {
		refreshScrollBars();
		repaint();
	}

	@Override
	public void zoomChanged(double zoom) {
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
