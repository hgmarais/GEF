package hgm.gef.app;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

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
import hgm.gef.canvas.CanvasPanel;
import hgm.gef.fig.Bounds;

public class ControlPanel extends JPanel implements CanvasListener, MouseMotionListener {
	
	/***/
	private static final long serialVersionUID = 1L;
	
	private Canvas canvas;
	
	private JSpinner x1Spinner;
	
	private JSpinner y1Spinner;
	
	private JSpinner x2Spinner;
	
	private JSpinner y2Spinner;
	
	private JSpinner zSpinner;
	
	private SpinnerNumberModel x1Model;
	
	private SpinnerNumberModel y1Model;
	
	private SpinnerNumberModel x2Model;
	
	private SpinnerNumberModel y2Model;
	
	private SpinnerNumberModel zModel;
	
	private JLabel mouseLabel;
	
	private JLabel modelLabel;
	
	private JLabel offsetLabel;
	
	private JButton zoomLayers;
	
	private JButton zoomCanvas;
	
	private JButton fitCanvas;

	public ControlPanel(CanvasPanel canvasPanel) {
		this.canvas = canvasPanel.getCanvas();

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		createComponents();
		layoutComponents();
		
		canvas.addListener(this);
		canvasPanel.getViewportPanel().addMouseMotionListener(this);
	}
	
	private void createComponents() {
		x1Model = new SpinnerNumberModel(1.0, Double.MIN_VALUE, Double.MAX_VALUE, 1.0);
		y1Model = new SpinnerNumberModel(1.0, Double.MIN_VALUE, Double.MAX_VALUE, 1.0);
		x2Model = new SpinnerNumberModel(1.0, Double.MIN_VALUE, Double.MAX_VALUE, 1.0);
		y2Model = new SpinnerNumberModel(1.0, Double.MIN_VALUE, Double.MAX_VALUE, 1.0);
		zModel = new SpinnerNumberModel(1.0, Double.MIN_VALUE, Double.MAX_VALUE, 0.1);
		
		x1Spinner = new JSpinner(x1Model);
		y1Spinner = new JSpinner(y1Model);
		x2Spinner = new JSpinner(x2Model);
		y2Spinner = new JSpinner(y2Model);
		zSpinner = new JSpinner(zModel);
		
		ChangeListener changeListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				canvas.setBounds(getChosenBounds());
			}
		};
		
		refreshZoom();
		refreshBounds();
		
		x1Model.addChangeListener(changeListener);
		y1Model.addChangeListener(changeListener);
		x2Model.addChangeListener(changeListener);
		y2Model.addChangeListener(changeListener);
				
		zModel.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				canvas.setZoomAroundVisibleCenter((Double)zModel.getValue());
			}
		});
		
		zoomLayers = new JButton("Zoom Layers");
		zoomCanvas = new JButton("Zoom Canvas");
		fitCanvas = new JButton("Fit Canvas");
		
		zoomLayers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.zoomFitLayers();
			}
		});
		
		zoomCanvas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.zoomFitCanvas();
			}
		});
		
		fitCanvas.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				canvas.setBounds(canvas.getLayerManager().getBounds());
			}
		});
		
		offsetLabel = new JLabel();
		mouseLabel = new JLabel();
		modelLabel = new JLabel();
	}

	private void layoutComponents() {
		setLayout(new GridLayout(5, 4));

		add(new JLabel("x1:"));
		add(x1Spinner);
		add(new JLabel("y1:"));
		add(y1Spinner);
		add(new JLabel("x2:"));
		add(x2Spinner);
		add(new JLabel("y2:"));
		add(y2Spinner);
		add(new JLabel("z:"));
		add(zSpinner);
		add(new JLabel("mouse:"));
		add(mouseLabel);
		add(new JLabel("model:"));
		add(modelLabel);
		add(new JLabel("offset:"));
		add(offsetLabel);
		add(zoomLayers);
		add(zoomCanvas);
		add(fitCanvas);
	}
	
	private Bounds getChosenBounds() {
		return new Bounds((Double)x1Model.getValue(), (Double)y1Model.getValue(), (Double)x2Model.getValue(), (Double)y2Model.getValue());
	}

	private void refreshZoom() {
		zModel.setValue(canvas.getZoom());
	}
	
	private void refreshBounds() {
		Bounds b = canvas.getBounds();
		x1Model.setValue(b.getMinX());
		y1Model.setValue(b.getMinY());
		x2Model.setValue(b.getMaxX());
		y2Model.setValue(b.getMaxY());
	}

	@Override
	public void boundsChanged(Canvas source) {
		refreshBounds();
	}

	@Override
	public void zoomChanged(Canvas source) {
		refreshZoom();
	}

	@Override
	public void visibleBoundsChanged(Canvas source) {
	}

	@Override
	public void repaintRequested(Canvas source) {
	}

	@Override
	public void repaintRequested(Canvas source, Bounds mb) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseLabel.setText(e.getX()+", "+e.getY());
		modelLabel.setText((int)canvas.xScreenToModel(e.getX())+", "+(int)canvas.yScreenToModel(e.getY()));
		offsetLabel.setText((int)canvas.getLeft()+", "+(int)canvas.getTop());
	}

	@Override
	public void converterChanged(Canvas source) {
	}

	@Override
	public void offsetChanged(Canvas canvas, double dx, double dy) {
		// TODO Auto-generated method stub
		
	}

}
