package hgm.gef.canvas;

import java.util.LinkedList;
import java.util.List;

import hgm.gef.property.PropertyLink;

public class CanvasGroup {
	
	public List<PropertyLink> propertyLinks = new LinkedList<>();
	
	public List<CanvasLink> links = new LinkedList<>();
	
	public void addZoomLink(Canvas target, Canvas source) {
		links.add(new ZoomLink(target, source));
	}
	
	public void addOffsetLink(boolean absolute, Axis axis, Canvas target, Canvas source) {
		links.add(new OffsetLink(absolute, axis, target, source));
	}
	
	public void addPropertyLink(PropertyLink propertyLink) {
		propertyLinks.add(propertyLink);
	}
	
}
