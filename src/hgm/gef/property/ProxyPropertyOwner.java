package hgm.gef.property;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hgm.gef.util.Unit;

public class ProxyPropertyOwner implements PropertyOwner {
	
	private PropertyOwner owner;

	private Map<String, Unit> propertyUnitMap = new HashMap<>(0);
	
	private LinkedList<PropertyListener> propertyListeners = new LinkedList<>();
	
	public ProxyPropertyOwner(PropertyOwner owner) {
		this.owner = owner;
	}
	
	@Override
	public PropertyOwner getPropertyOwner() {
		return owner;
	}

	@Override
	public List<PropertyListener> getPropertyListeners() {
		return propertyListeners;
	}

	@Override
	public Map<String, Unit> getPropertyUnitMap() {
		return propertyUnitMap;
	}

}
