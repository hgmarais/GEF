package hgm.gef.canvas;

import java.util.LinkedList;
import java.util.List;

public class CanvasGroup {
	
	public List<CanvasLink> links = new LinkedList<>();
	
	public void addZoomLink(Canvas target, Canvas source) {
		links.add(new ZoomLink(target, source));
	}
	
	public void addOffsetLink(boolean absolute, Axis axis, Canvas target, Canvas source) {
		links.add(new OffsetLink(absolute, axis, target, source));
	}
	
}
