package hgm.gef.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import hgm.gef.BasicStyle;
import hgm.gef.canvas.Axis;
import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.CanvasPanel;
import hgm.gef.canvas.CoordSystem;
import hgm.gef.canvas.ScreenCoordSystem;
import hgm.gef.canvas.ScrollBarPolicy;
import hgm.gef.fig.Bounds;
import hgm.gef.fig.XYRectangleFig;
import hgm.gef.function.PropertyLinkBuilder;
import hgm.gef.layer.AxisLayer;
import hgm.gef.layer.BasicLayer;
import hgm.gef.layer.LayerManager;
import hgm.gef.property.AbsoluteConstraint;
import hgm.gef.util.Unit;

public class App {
	
	public static CanvasPanel createCanvasPanel(CoordSystem coordSystem) {
		BasicLayer layer = new BasicLayer();
		AxisLayer axisLayer = new AxisLayer(Axis.BOTH);
		
		Canvas canvas = new Canvas(coordSystem);
		canvas.setBounds(new Bounds(-100, -100, 100, 100));
		
		LayerManager layerManager = canvas.getLayerManager();
		layerManager.addToTop(layer);
		layerManager.addToBottom(axisLayer);
		
		return new CanvasPanel(canvas);
	}
	
	private static CanvasPanel createXCanvasPanel(Canvas plotCanvas) {
		XYRectangleFig fig = new XYRectangleFig(-5, 0, 5, 5);
		fig.setPropertyUnit(XYRectangleFig.X1, Unit.SCREEN);
		fig.setPropertyUnit(XYRectangleFig.Y1, Unit.SCREEN);
		fig.setPropertyUnit(XYRectangleFig.X2, Unit.SCREEN);
		fig.setPropertyUnit(XYRectangleFig.Y2, Unit.SCREEN);
		fig.setRelative(new Point2D.Double(0, 0));
		fig.setStyle(BasicStyle.line(false, Color.BLUE));
		
		BasicLayer layer = new BasicLayer();
		AxisLayer axisLayer = new AxisLayer(Axis.HORIZONTAL);
		layer.addFigure(fig);
		
		Canvas canvas = new Canvas(new ScreenCoordSystem());
		
		LayerManager layerManager = canvas.getLayerManager();
		layerManager.addToTop(layer);
		layerManager.addToBottom(axisLayer);
		
		canvas.setPropertyConstraint(Canvas.TOP, new AbsoluteConstraint(0.0));
		PropertyLinkBuilder.from(plotCanvas, Canvas.LEFT, Double.class).to(canvas, Canvas.LEFT).link();
		PropertyLinkBuilder.from(plotCanvas, Canvas.ZOOM, Double.class).to(canvas, Canvas.ZOOM).link();
		
//		PropertyLinkBuilder.from(fig, XYRectangleFig.X1, Double.class).via(x -> canvas.xOffsetToModel(x, Unit.MODEL, 10, Unit.SCREEN)).to(fig, XYRectangleFig.X2).link();
//		PropertyLinkBuilder.from(fig, XYRectangleFig.Y1, Double.class).via(y -> canvas.yOffsetToModel(y, Unit.MODEL, 10, Unit.SCREEN)).to(fig, XYRectangleFig.Y2).link();
		
		CanvasPanel panel = new CanvasPanel(canvas);
		panel.setBackground(Color.GRAY);
		panel.setScrollable(false);
		panel.setScrollBarPolicy(ScrollBarPolicy.HIDDEN);
		return panel;
	}

	public static void main(String[] args) {
		JPanel canvasPanel = new JPanel(new BorderLayout());
		JPanel controlPanel = new JPanel(new GridLayout(1, 2));
		
		CanvasPanel plotPanel = createCanvasPanel(new ScreenCoordSystem());
		Canvas canvas = plotPanel.getCanvas();
		canvasPanel.add(plotPanel, BorderLayout.CENTER);
		
		CanvasPanel xPanel = createXCanvasPanel(canvas);
		canvasPanel.add(xPanel, BorderLayout.SOUTH);
		
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
