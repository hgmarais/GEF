package hgm.gef.selection;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
	
	private void addSelection(Selection selection) {
		selections.add(selection);
		canvas.repaint(selection.getBounds());
	}
	
	private void removeSelection(Selection selection) {
		if (selections.remove(selection)) {
			canvas.repaint(selection.getBounds());
		}
	}
	
	private void clearSelections() {
		selections.clear();
		canvas.repaint();
	}
	
	public void refreshSelectables(double mx, double my) {
		List<LayerFig> figures = layerManager.getLayerFigures(mx, my);
		selectables.clear();
		
		for (LayerFig figure : figures) {
			if (isSelected(figure)) {
				continue;
			}
			
			selectables.add(new DefaultSelectable(this, figure));
		}
		
		canvas.repaint(GEFUtil.addBounds(selectables));
	}

	private boolean isSelected(LayerFig figure) {
		return getSelection(figure) != null;
	}

	private Selection getSelection(LayerFig figure) {
		for (Selection selection : selections) {
			if (selection.getFigure() == figure) {
				return selection;
			}
		}
		
		return null;
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

	public void select(boolean toggle, double mx, double my) {
		Selectable targetSelectable = null;
		
		for (Selectable selectable : selectables) {
			if (selectable.contains(mx, my)) {
				targetSelectable = selectable;
				break;
			}
		}
		
		if (targetSelectable != null) {
			if (!toggle) {
				clearSelections();
			}
			
			selectables.remove(targetSelectable);
			addSelection(targetSelectable.createSelection());
			return;
		}
		
		Selection targetSelection = null;
		
		for (Selection selection : selections) {
			if (selection.contains(mx, my)) {
				targetSelection = selection;
				break;
			}
		}
		
		if (toggle) {
			if (targetSelection != null) {
				removeSelection(targetSelection);
			}
		} else {
			clearSelections();
			
			if (targetSelection != null) {
				addSelection(targetSelection);
			}
		}
	}

}
