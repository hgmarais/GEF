package hgm.gef.function;

import java.util.function.Supplier;

import hgm.gef.property.PropertyOwner;

public class PropertySupplier<T> implements Supplier<T> {
	
	private PropertyOwner owner;
	
	private String name;

	public PropertySupplier(PropertyOwner owner, String name) {
		this.owner = owner;
		this.name = name;
	}
	
	public PropertyOwner getOwner() {
		return owner;
	}
	
	public String getName() {
		return name;
	}

	@Override
	@SuppressWarnings("unchecked")
	public T get() {
		return (T) owner.getProperty(name);
	}

}
