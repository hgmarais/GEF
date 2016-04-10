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
	
	private JButton fitLayers;
	
	private JButton fitCanvas;

	public ControlPanel(CanvasPanel canvasPanel) {
		this.canvas = canvasPanel.getCanvas();

		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		createComponents();
		layoutComponents();
		
		refreshZoom();
		refreshBounds();
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
		add(fitLayers);
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
	public void boundsChanged() {
		refreshBounds();
	}

	@Override
	public void zoomChanged() {
		refreshZoom();
	}

	@Override
	public void visibleBoundsChanged() {
	}

	@Override
	public void repaintRequested() {
	}

	@Override
	public void repaintRequested(Bounds mb) {
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

}
