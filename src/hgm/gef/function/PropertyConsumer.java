package hgm.gef.function;

import java.util.function.Consumer;

import hgm.gef.property.PropertyOwner;

public class PropertyConsumer<T> implements Consumer<T> {
	
	private PropertyOwner owner;
	
	private String name;

	public PropertyConsumer(PropertyOwner owner, String name) {
		this.owner = owner;
		this.name = name;
	}

	@Override
	public void accept(T value) {
		owner.setProperty(name, value);
	}

}
