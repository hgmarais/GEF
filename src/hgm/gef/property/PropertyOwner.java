package hgm.gef.property;

import java.util.List;
import java.util.Map;

import hgm.gef.function.PropertySupplier;
import hgm.gef.util.Unit;

public interface PropertyOwner {

	PropertyOwner getPropertyOwner();
	
	default List<PropertyListener> getPropertyListeners() {
		return getPropertyOwner().getPropertyListeners();
	}
	
	default Map<String, Object> getPropertyValueMap() {
		return getPropertyOwner().getPropertyValueMap();
	}
	
	default Map<String, Unit> getPropertyUnitMap() {
		return getPropertyOwner().getPropertyUnitMap();
	}
	
	default Map<String, Constraint> getPropertyConstraintMap() {
		return getPropertyOwner().getPropertyConstraintMap();
	}
	
	default Object constrainProperty(String name, Object value) {
		Constraint constraint = getPropertyConstraintMap().get(name);
		
		if (constraint == null) {
			return value;
		}
		
		return constraint.constrain(value);
	}
	
	default void setPropertyConstraint(String name, Constraint constraint) {
		Map<String, Constraint> map = getPropertyConstraintMap(); 
		
		if (constraint == null) {
			map.remove(name);
		} else {
			map.put(name, constraint);
			
		}
	}
	
	default PropertySupplier<?> getPropertySupplier(String name) {
		return new PropertySupplier<>(this, name);
	}
	
	default <T> PropertySupplier<T> getPropertySupplier(Class<T> type, String name) {
		return new PropertySupplier<T>(this, name);
	}
	
	default void setProperty(String name, Object value) {
		getPropertyValueMap().put(name, constrainProperty(name, value));
		firePropertyChanged(name);
	}
	
	default void setProperty(String name, Object value, Unit unit) {
		setProperty(name, value);
		setPropertyUnit(name, unit);
	}
	
	default Object getProperty(String name) {
		return getPropertyValueMap().get(name);
	}
	
	default void setPropertyUnit(String name, Unit unit) {
		Map<String, Unit> map = getPropertyUnitMap();
		
		if (unit == null) {
			map.remove(name);
		} else {
			map.put(name, unit);
		}
	}
	
	default Unit getPropertyUnit(String name) {
		Unit unit = getPropertyUnitMap().get(name);
		
		if (unit != null) {
			return unit;
		}
		
		return Unit.MODEL;
	}
	
	default void addPropertyListener(PropertyListener listener) {
		getPropertyListeners().add(listener);
	}
	
	default void removePropertyListener(PropertyListener listener) {
		getPropertyListeners().remove(listener);
	}
	
	default void firePropertyChanged(String name) {
		propertyChanged(name);
		for (PropertyListener listener : getPropertyListeners()) {
			listener.propertyChanged(getPropertyOwner(), name);
		}
	}
	
	default void propertyChanged(String name) {
		getPropertyOwner().propertyChanged(name);
	}
	
}
