package hgm.gef.property;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import hgm.gef.util.Unit;

public class ProxyPropertyOwner implements PropertyOwner {
	
	private PropertyOwner owner;

	private Map<String, Unit> unitMap = new HashMap<>();
	
	private Map<String, Object> valueMap = new HashMap<>();
	
	private Map<String, Constraint> constraintMap = new HashMap<>();
	
	private LinkedList<PropertyListener> listeners = new LinkedList<>();
	
	public ProxyPropertyOwner(PropertyOwner owner) {
		this.owner = owner;
	}
	
	@Override
	public PropertyOwner getPropertyOwner() {
		return owner;
	}

	@Override
	public List<PropertyListener> getPropertyListeners() {
		return listeners;
	}

	@Override
	public Map<String, Unit> getPropertyUnitMap() {
		return unitMap;
	}
	
	@Override
	public Map<String, Object> getPropertyValueMap() {
		return valueMap;
	}
	
	@Override
	public Map<String, Constraint> getPropertyConstraintMap() {
		return constraintMap;
	}
	
	@Override
	public void propertyChanged(String name) {
	}

}
