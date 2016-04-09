package hgm.gef.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import hgm.gef.BasicStyle;
import hgm.gef.canvas.Canvas;
import hgm.gef.canvas.CanvasPanel;
import hgm.gef.editor.LayerManager;
import hgm.gef.fig.ShapeFig;
import hgm.gef.layer.DefaultLayer;

public class App {

	public static void main(String[] args) {
		ShapeFig fig1 = new ShapeFig(new Rectangle(0, 0, 200, 200));
		ShapeFig fig2 = new ShapeFig(new Rectangle(200, 200, 200, 200));
		
		fig1.setStyle(new BasicStyle(Color.WHITE, Color.BLUE));
		fig2.setStyle(new BasicStyle(Color.YELLOW, Color.GREEN));
		
		DefaultLayer layer1 = new DefaultLayer();
		DefaultLayer layer2 = new DefaultLayer();
		
		layer1.addFigure(fig1);
		layer2.addFigure(fig2);
		
		Canvas canvas = new Canvas();
		canvas.setCanvasBounds(new Rectangle2D.Double(0, 0, 400, 400));
		
		LayerManager layerManager = canvas.getLayerManager();
		layerManager.addToTop(layer2);
		layerManager.addToTop(layer1);
		
		CanvasPanel canvasPanel = new CanvasPanel(canvas);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(canvasPanel, BorderLayout.CENTER);
		panel.add(new ControlPanel(canvas), BorderLayout.SOUTH);
		
		JFrame frame = new JFrame("GEF App");
		frame.setContentPane(panel);
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
//		canvas.fitEditor();
//		canvas.resizeCanvasToEditor();
//		canvas.zoomFitCanvas();
	}

}
