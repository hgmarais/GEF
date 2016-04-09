package hgm.gef.app;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.CanvasListener;

public class ControlPanel extends JPanel implements CanvasListener {
	
	/***/
	private static final long serialVersionUID = 1L;
	
	private Canvas canvas;
	
	private JSpinner wSpinner;
	
	private JSpinner hSpinner;
	
	private JSpinner zSpinner;
	
	private SpinnerNumberModel wModel;
	
	private SpinnerNumberModel hModel;
	
	private SpinnerNumberModel zModel;
	
	private JButton fitLayers;
	
	private JButton fitCanvas;
	
	public ControlPanel(Canvas canvas) {
		this.canvas = canvas;

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		createComponents();
		layoutComponents();
		
		refreshZoom();
		refreshBounds();
		canvas.addListener(this);
	}
	
	private void createComponents() {
		wModel = new SpinnerNumberModel(1.0, Double.MIN_VALUE, Double.MAX_VALUE, 1.0);
		hModel = new SpinnerNumberModel(1.0, Double.MIN_VALUE, Double.MAX_VALUE, 1.0);
		zModel = new SpinnerNumberModel(1.0, Double.MIN_VALUE, Double.MAX_VALUE, 0.1);
		
		wSpinner = new JSpinner(wModel);
		hSpinner = new JSpinner(hModel);
		zSpinner = new JSpinner(zModel);
		
		wModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				canvas.setWidth((Double)wModel.getValue());
			}
		});
		
		hModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				canvas.setHeight((Double)hModel.getValue());
			}
		});
		
		zModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				canvas.setZoomAroundVisibleCenter((Double)zModel.getValue());
			}
		});
		
		fitLayers = new JButton("Fit Layers");
		fitCanvas = new JButton("Fit Canvas");
		
		fitLayers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.zoomFitLayers();
			}
		});
		
		fitCanvas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.zoomFitCanvas();
			}
		});
	}

	private void layoutComponents() {
		setLayout(new GridLayout(3, 2));

		add(new JLabel("w:"));
		add(wSpinner);
		add(new JLabel("h:"));
		add(hSpinner);
		add(new JLabel("z:"));
		add(zSpinner);
		add(fitLayers);
		add(fitCanvas);
	}

	private void refreshZoom() {
		zModel.setValue(canvas.getConverter().getZoom());
	}
	
	private void refreshBounds() {
		Rectangle2D b = canvas.getCanvasBounds();
		wModel.setValue(b.getWidth());
		hModel.setValue(b.getHeight());
	}

	@Override
	public void boundsChanged(Rectangle2D mCanvasBounds) {
		refreshBounds();
	}

	@Override
	public void zoomChanged(double zoom) {
		refreshZoom();
	}

	@Override
	public void visibleBoundsChanged() {
	}

	@Override
	public void repaintRequested() {
	}

	@Override
	public void repaintRequested(Rectangle2D mr) {
	}

}
