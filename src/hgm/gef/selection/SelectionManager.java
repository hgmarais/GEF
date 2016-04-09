package hgm.gef.selection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import hgm.gef.Paintable;
import hgm.gef.canvas.Canvas;
import hgm.gef.editor.LayerManager;
import hgm.gef.editor.Painter;
import hgm.gef.fig.LayerFig;
import hgm.gef.util.GEFUtil;

public class SelectionManager implements Paintable {
	
	private Canvas canvas;
	
	private List<Selectable> selectables = new LinkedList<>();
	
	private List<Selection> selections = new LinkedList<>();

	private LayerManager layerManager;
	
	public SelectionManager(Canvas canvas) {
		this.canvas = canvas;
		layerManager = canvas.getLayerManager();
	}
	
	public void refreshSelectables(double mx, double my) {
		List<LayerFig> figures = layerManager.getLayerFigures(mx, my);
		selectables = figures.stream().map(figure -> new DefaultSelectable(this, figure)).collect(Collectors.toList());
		canvas.repaint(GEFUtil.addBounds(selectables));
	}

	public void clearSelectables() {
		selectables = new ArrayList<>(0);
		canvas.repaint();
	}
	
	@Override
	public void paint(Painter p) {
		for (Selection selection : selections) {
			selection.paint(p);
		}
		
		for (Selectable selectable : selectables) {
			selectable.paint(p);
		}
	}

}
