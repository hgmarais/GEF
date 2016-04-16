package hgm.gef.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import hgm.gef.BasicStyle;
import hgm.gef.canvas.Axis;
import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.CanvasPanel;
import hgm.gef.canvas.CoordSystem;
import hgm.gef.canvas.ScreenCoordSystem;
import hgm.gef.fig.Bounds;
import hgm.gef.fig.ShapeFig;
import hgm.gef.layer.AxisLayer;
import hgm.gef.layer.BasicLayer;
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
		
		BasicLayer layer = new BasicLayer();
		AxisLayer axisLayer = new AxisLayer(Axis.BOTH);
		
		layer.addFigure(xLine);
		layer.addFigure(yLine);
		layer.addFigure(fig1);
		layer.addFigure(fig2);
		layer.addFigure(fig3);
		layer.addFigure(fig4);
		
		Canvas canvas = new Canvas(coordSystem);
		canvas.setBounds(new Bounds(-400, -400, 400, 400));
//		canvas.centerOnOrigin();
		
		LayerManager layerManager = canvas.getLayerManager();
		layerManager.addToTop(layer);
		layerManager.addToBottom(axisLayer);
		
		return new CanvasPanel(canvas);
	}

	public static void main(String[] args) {
		JPanel canvasPanel = new JPanel(new BorderLayout());
		JPanel controlPanel = new JPanel(new GridLayout(1, 2));
		
//		CanvasPanel xPanel = createCanvasPanel(new ScreenCoordSystem());
//		canvasPanel.add(xPanel, BorderLayout.SOUTH);
//		CanvasPanel yPanel = createCanvasPanel(new ScreenCoordSystem());
//		canvasPanel.add(yPanel, BorderLayout.WEST);
//	Canvas xCanvas = xPanel.getCanvas();
//	Canvas yCanvas = yPanel.getCanvas();
//		CanvasGroup canvasGroup = new CanvasGroup();
//		canvasGroup.addOffsetLink(true, Axis.HORIZONTAL, xCanvas, pCanvas);
//		canvasGroup.addOffsetLink(true, Axis.VERTICAL, yCanvas, pCanvas);
//		canvasGroup.addZoomLink(xCanvas, pCanvas);
//		canvasGroup.addZoomLink(yCanvas, pCanvas);
//
//		xPanel.setBackground(Color.GRAY);
//		yPanel.setBackground(Color.GRAY);
//		xPanel.setScrollable(false);
//		yPanel.setScrollable(false);
//		xPanel.setScrollBarPolicy(ScrollBarPolicy.HIDDEN);
//		yPanel.setScrollBarPolicy(ScrollBarPolicy.HIDDEN);
		
		CanvasPanel plotPanel = createCanvasPanel(new ScreenCoordSystem());
		canvasPanel.add(plotPanel, BorderLayout.CENTER);
		controlPanel.add(new ControlPanel(plotPanel));

//		Canvas pCanvas = plotPanel.getCanvas();
		
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
	}

}
