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

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import hgm.gef.BasicStyle;
import hgm.gef.Style;
import hgm.gef.fig.Bounds;
import hgm.gef.layer.Layer;
import hgm.gef.layer.LayerManagerListener;

public class CanvasPanel extends JPanel implements CanvasListener, LayerManagerListener, MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {
	
	/***/
	private static final long serialVersionUID = 1L;
	
	private ViewportPanel viewportPanel;
	
	private Style style;

	private Canvas canvas;

	private JScrollBar hBar;

	private JScrollBar vBar;
	
	private ScrollBarModel hBarModel;
	
	private ScrollBarModel vBarModel;
	
	private JPanel corner;
	
	private boolean zoomable = true;
	
	private boolean hScrollable = true;
	
	private boolean vScrollable = true;

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
		
		createComponents();
		layoutComponents();
		
		canvas.addListener(this);
		canvas.getLayerManager().addListener(this);
		addComponentListener(this);
		setBackground(Color.WHITE);
	}
	
	public void setHorizontalScrollBarPolicy(ScrollBarPolicy policy) {
		hBarModel.setPolicy(policy);
		refreshScrollBars();
	}
	
	public void setScrollBarPolicy(ScrollBarPolicy policy) {
		hBarModel.setPolicy(policy);
		vBarModel.setPolicy(policy);
		refreshScrollBars();
	}
	
	public void setScrollBarPolicies(ScrollBarPolicy policy) {
		setScrollBarPolicies(policy, policy);
	}
	
	public void setZoomable(boolean zoomable) {
		this.zoomable = zoomable;
	}
	
	public boolean isZoomable() {
		return zoomable;
	}
	
	public void setHorizontalScrollable(boolean scrollable) {
		this.hScrollable = scrollable;
	}
	
	public boolean isHorizontalScrollable() {
		return hScrollable;
	}
	
	public void setVerticalScrollable(boolean scrollable) {
		this.vScrollable = scrollable;
	}
	
	public boolean isVerticalScrollable() {
		return vScrollable;
	}
	
	public void setScrollable(boolean scrollable) {
		setHorizontalScrollable(scrollable);
		setVerticalScrollable(scrollable);
	}
	
	public void setScrollBarPolicies(ScrollBarPolicy horizontal, ScrollBarPolicy vertical) {
		hBarModel.setPolicy(horizontal);
		vBarModel.setPolicy(vertical);
		refreshScrollBars();
	}
	
	private void createComponents() {
		viewportPanel = new ViewportPanel();
		hBar = new JScrollBar(JScrollBar.HORIZONTAL);
		vBar = new JScrollBar(JScrollBar.VERTICAL);
		corner = new JPanel();
		
		hBar.setVisible(false);
		vBar.setVisible(false);
		corner.setVisible(false);
		
		hBarModel = new ScrollBarModel(canvas, true);
		vBarModel = new ScrollBarModel(canvas, false);
		hBar.setModel(hBarModel);
		vBar.setModel(vBarModel);
		
		hBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (!e.getValueIsAdjusting()) {
					hBarModel.applyToCanvas();
				}
			}
		});
		
		vBar.addAdjustmentListener(new AdjustmentListener() {
			@Override
			public void adjustmentValueChanged(AdjustmentEvent e) {
				if (!e.getValueIsAdjusting()) {
					vBarModel.applyToCanvas();
				}
			}
		});
		
		viewportPanel.addMouseListener(this);
		viewportPanel.addMouseWheelListener(this);
		viewportPanel.addMouseMotionListener(this);
	}

	private void layoutComponents() {
		JPanel hPanel = new JPanel(new BorderLayout());
		hPanel.add(hBar, BorderLayout.CENTER);
		hPanel.add(corner, BorderLayout.EAST);
		
		setLayout(new BorderLayout());
		add(viewportPanel, BorderLayout.CENTER);
		add(hPanel, BorderLayout.SOUTH);
		add(vBar, BorderLayout.EAST);		
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
		int w = viewportPanel.getWidth();
		int h = viewportPanel.getHeight();
		
//		if (hBar.isVisible()) {
//			h -= hBar.getHeight();
//		}
//		
//		if (vBar.isVisible()) {
//			w -= vBar.getWidth();
//		}
		
		canvas.setScreenSize(w, h);
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
	public void repaintRequested(Canvas source) {
		repaint();
	}

	@Override
	public void repaintRequested(Canvas source, Bounds mb) {
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
			if (zoomable) {
				double mx = canvas.xScreenToModel(e.getX());
				double my = canvas.yScreenToModel(e.getY());
				canvas.adjustZoomAround(-e.getPreciseWheelRotation(), mx, my);
			}
		} else if (e.isAltDown()) {
//			if (e.isShiftDown()) {
//				canvas.adjustDimension(-e.getPreciseWheelRotation(), 0.0);
//			} else {
//				canvas.adjustDimension(0.0, -e.getPreciseWheelRotation());
//			}
		} else {
			int diff = (int) e.getPreciseWheelRotation();
			
			boolean horizontal = false;
			boolean vertical = false;
			
			if (e.isShiftDown()) {
				if (hScrollable) {
					horizontal = true;
				} else if (vScrollable) {
					vertical = true;
				}
			} else {
				if (vScrollable) {
					vertical = true;
				} else if (hScrollable) {
					horizontal = true;
				}
			}
			
			if (horizontal) {
				hBarModel.adjust(diff);
			} else if (vertical) {
				vBarModel.adjust(diff);
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
	public void visibleBoundsChanged(Canvas source) {
		refreshScrollBars();
		repaint();
	}
	
	private void refreshScrollBars() {
		System.out.println("refreshScrollBars");
		
		hBarModel.refresh();
		vBarModel.refresh();
		
		if ((hBar.isVisible() != hBarModel.shouldBeShown()) || (vBar.isVisible() != vBarModel.shouldBeShown())) {
			hBar.setVisible(hBarModel.shouldBeShown());
			vBar.setVisible(vBarModel.shouldBeShown());
			
			corner.setVisible(hBar.isVisible() && vBar.isVisible());
			corner.setPreferredSize(new Dimension(hBar.getHeight(), vBar.getWidth()));
		
			refreshVisibleSize();
		}
	}
	
	@Override
	public void boundsChanged(Canvas source) {
		refreshScrollBars();
		repaint();
	}
	
	@Override
	public void converterChanged(Canvas source) {
		refreshVisibleSize();
		repaint();
	}

	@Override
	public void zoomChanged(Canvas source) {
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

	@Override
	public void offsetChanged(Canvas canvas, double dx, double dy) {
		// TODO Auto-generated method stub
		
	}

}
