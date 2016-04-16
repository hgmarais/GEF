package hgm.gef.property;

import java.util.List;

import hgm.gef.function.PropertySupplier;

public interface PropertyOwner {

	List<PropertyListener> getPropertyListeners();

	default PropertySupplier<?> getPropertySupplier(String name) {
		return new PropertySupplier<>(this, name);
	}
	
	default <T> PropertySupplier<T> getPropertySupplier(Class<T> type, String name) {
		return new PropertySupplier<T>(this, name);
	}
	
	default void setProperty(String name, Object value) {
		
	}
	
	default Object getProperty(String name) {
		return null;
	}
	
	default void addPropertyListener(PropertyListener listener) {
		getPropertyListeners().add(listener);
	}
	
	default void removePropertyListener(PropertyListener listener) {
		getPropertyListeners().remove(listener);
	}
	
	default void firePropertyChanged(String name) {
		for (PropertyListener listener : getPropertyListeners()) {
			listener.propertyChanged(this, name);
		}
	}
	
}
