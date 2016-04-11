package hgm.gef.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hgm.gef.BasicStyle;
import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.CanvasPanel;
import hgm.gef.canvas.CartesianCoordSystem;
import hgm.gef.canvas.CoordSystem;
import hgm.gef.canvas.ScreenCoordSystem;
import hgm.gef.fig.Bounds;
import hgm.gef.fig.ShapeFig;
import hgm.gef.layer.DefaultLayer;
import hgm.gef.layer.LayerManager;

public class App {
	
	public static CanvasPanel createCanvasPanel(CoordSystem coordSystem) {
		ShapeFig fig1 = new ShapeFig(new Rectangle(0, 0, 200, 200));
		ShapeFig fig2 = new ShapeFig(new Rectangle(200, 200, 200, 200));
		ShapeFig fig3 = new ShapeFig(new Rectangle(-200, -200, 200, 200));
		ShapeFig fig4 = new ShapeFig(new Rectangle(-400, -400, 200, 200));
		
		ShapeFig xLine = new ShapeFig(new Line2D.Double(0, 0, 500, 0));
		ShapeFig yLine = new ShapeFig(new Line2D.Double(0, 0, 0, 500));
		
		fig1.setStyle(new BasicStyle(null, Color.RED));
		fig2.setStyle(new BasicStyle(null, Color.GREEN));
		fig3.setStyle(new BasicStyle(null, Color.BLUE));
		fig4.setStyle(new BasicStyle(null, Color.ORANGE));
		
		xLine.setStyle(BasicStyle.dashedLine(false, Color.MAGENTA));
		yLine.setStyle(BasicStyle.dashedLine(false, Color.CYAN));
		
		DefaultLayer layer = new DefaultLayer();
		
		layer.addFigure(xLine);
		layer.addFigure(yLine);
		layer.addFigure(fig1);
		layer.addFigure(fig2);
		layer.addFigure(fig3);
		layer.addFigure(fig4);
		
		Canvas canvas = new Canvas(coordSystem);
		canvas.setBounds(new Bounds(0, 0, 400, 400));
		
		LayerManager layerManager = canvas.getLayerManager();
		layerManager.addToTop(layer);
		
		return new CanvasPanel(canvas);
	}

	public static void main(String[] args) {
		JPanel canvasPanel = new JPanel(new GridLayout(1, 2));
		JPanel controlPanel = new JPanel(new GridLayout(1, 2));
		
		CanvasPanel canvasPanel1 = createCanvasPanel(new ScreenCoordSystem());
		canvasPanel.add(canvasPanel1);
		controlPanel.add(new ControlPanel(canvasPanel1));
		
		CanvasPanel canvasPanel2 = createCanvasPanel(new CartesianCoordSystem());
		canvasPanel.add(canvasPanel2);
		controlPanel.add(new ControlPanel(canvasPanel2));
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(canvasPanel, BorderLayout.CENTER);
		panel.add(controlPanel, BorderLayout.SOUTH);
		
		JFrame frame = new JFrame("GEF App");
		frame.setContentPane(panel);
		frame.setSize(1024, 600);
//		frame.setLocationRelativeTo(null);
		frame.setLocation(500, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
//				canvasPanel1.getCanvas().zoomFitCanvas();
//				canvasPanel2.getCanvas().zoomFitCanvas();
			}
		});
	}

}
