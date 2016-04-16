package hgm.gef.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import hgm.gef.BasicStyle;
import hgm.gef.canvas.Axis;
import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.CanvasPanel;
import hgm.gef.canvas.CoordSystem;
import hgm.gef.canvas.ScreenCoordSystem;
import hgm.gef.fig.Bounds;
import hgm.gef.fig.RectangleFig;
import hgm.gef.fig.ShapeFig;
import hgm.gef.function.PropertyLinkBuilder;
import hgm.gef.layer.AxisLayer;
import hgm.gef.layer.BasicLayer;
import hgm.gef.layer.LayerManager;
import hgm.gef.util.Unit;

public class App {
	
	public static CanvasPanel createCanvasPanel(CoordSystem coordSystem) {
//		ShapeFig fig1 = new ShapeFig(new Rectangle(0, 0, 200, 200));
//		ShapeFig fig2 = new ShapeFig(new Rectangle(200, 200, 200, 200));
//		ShapeFig fig3 = new ShapeFig(new Rectangle(-200, -200, 200, 200));
//		ShapeFig fig4 = new ShapeFig(new Rectangle(-400, -400, 200, 200));
		RectangleFig fig5 = new RectangleFig(100, 100, 200, 200);
		RectangleFig fig6 = new RectangleFig(250, 250, 300, 300);
		
		fig5.setPropertyUnit(RectangleFig.X1, Unit.SCREEN);
		fig5.setPropertyUnit(RectangleFig.Y1, Unit.SCREEN);
		fig5.setPropertyUnit(RectangleFig.X2, Unit.SCREEN);
		fig5.setPropertyUnit(RectangleFig.Y2, Unit.SCREEN);
		
		ShapeFig xLine = new ShapeFig(new Line2D.Double(0, 0, 500, 0));
		ShapeFig yLine = new ShapeFig(new Line2D.Double(0, 0, 0, 500));
		
//		fig1.setStyle(new BasicStyle(null, Color.RED));
//		fig2.setStyle(new BasicStyle(null, Color.GREEN));
//		fig3.setStyle(new BasicStyle(null, Color.BLUE));
//		fig4.setStyle(new BasicStyle(null, Color.ORANGE));
		fig5.setStyle(new BasicStyle(null, Color.BLUE));
		fig6.setStyle(new BasicStyle(null, Color.ORANGE));
		
		xLine.setStyle(BasicStyle.dashedLine(false, Color.MAGENTA));
		yLine.setStyle(BasicStyle.dashedLine(false, Color.CYAN));
		
		BasicLayer layer = new BasicLayer();
		AxisLayer axisLayer = new AxisLayer(Axis.BOTH);
		
		layer.addFigure(xLine);
		layer.addFigure(yLine);
//		layer.addFigure(fig1);
//		layer.addFigure(fig2);
//		layer.addFigure(fig3);
//		layer.addFigure(fig4);
		layer.addFigure(fig5);
		layer.addFigure(fig6);
		
		Canvas canvas = new Canvas(coordSystem);
		canvas.setBounds(new Bounds(-100, -100, 100, 100));
//		canvas.centerOnOrigin();

//		PropertyApplier<Double, Double> xApplier = PropertyApplierBuilder
//			.from(fig5, RectangleFig.X2, Double.class)
//			.via(d -> d+20)
//			.to(fig6, RectangleFig.X1);
		
//		PropertyApplier<Double, Double> yApplier = PropertyApplierBuilder
//				.from(fig5, RectangleFig.Y2, Double.class)
//				.via(d -> d+10)
//				.to(fig6, RectangleFig.Y1);
		
		PropertyLinkBuilder
			.from(canvas, Canvas.MOUSE_POSITION, Point2D.class)
			.via(p -> p == null ? new Point2D.Double(0, 0) : p)
			.via(p -> new Point2D.Double(p.getX() + 10, p.getY() + 10))
			.to(fig5, RectangleFig.POINT1).toLink();
		
		PropertyLinkBuilder
			.from(fig5, RectangleFig.POINT1, Point2D.class)
			.via(p -> new Point2D.Double(p.getX() + 10, p.getY() + 10))
			.to(fig5, RectangleFig.POINT2).toLink();
		
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
