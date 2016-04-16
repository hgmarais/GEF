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
	
	default Map<String, Unit> getPropertyUnitMap() {
		return getPropertyOwner().getPropertyUnitMap();
	}
	
	default PropertySupplier<?> getPropertySupplier(String name) {
		return new PropertySupplier<>(this, name);
	}
	
	default <T> PropertySupplier<T> getPropertySupplier(Class<T> type, String name) {
		return new PropertySupplier<T>(this, name);
	}
	
	default void setProperty(String name, Object value) {
	}
	
	default void setProperty(String name, Object value, Unit unit) {
		setProperty(name, value);
		setPropertyUnit(name, unit);
	}
	
	default Object getProperty(String name) {
		return null;
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
		for (PropertyListener listener : getPropertyListeners()) {
			listener.propertyChanged(getPropertyOwner(), name);
		}
	}
	
}
