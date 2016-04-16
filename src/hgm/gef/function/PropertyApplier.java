package hgm.gef.function;

import java.util.function.Consumer;
import java.util.function.Function;

import hgm.gef.property.PropertyOwner;

public class PropertyApplier<F, T> extends Applier<F, T> {
	
	public PropertyApplier(PropertySupplier<F> supplier, Function<F, T> function, Consumer<T> consumer) {
		super(supplier, function, consumer);
	}
	
	public PropertySupplier<F> getPropertySupplier() {
		return (PropertySupplier<F>) getSupplier();
	}
	
	public PropertyOwner getSourceOwner() {
		return getPropertySupplier().getOwner();
	}
	
	public String getSourceName() {
		return getPropertySupplier().getName();
	}
	
	public FPropertyLink<F, T> toPropertyLink() {
		PropertySupplier<F> propertySupplier = getPropertySupplier();
		return new FPropertyLink<F, T>(propertySupplier.getOwner(), propertySupplier.getName(), this);
	}
	
}
