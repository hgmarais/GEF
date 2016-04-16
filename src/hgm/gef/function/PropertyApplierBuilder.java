package hgm.gef.function;

import java.util.function.Consumer;
import java.util.function.Function;

import hgm.gef.property.PropertyOwner;

public class PropertyApplierBuilder<F, T> {
	
	private PropertySupplier<F> supplier;
	
	private Function<F, T> function;

	public PropertyApplierBuilder(PropertySupplier<F> supplier) {
		this.supplier = supplier;
	}
	
	public PropertyApplierBuilder(PropertySupplier<F> supplier, Function<F, T> function) {
		this.supplier = supplier;
		this.function = function;
	}
	
	public <X> PropertyApplierBuilder<F, X> via(Function<F, X> function) {
		return new PropertyApplierBuilder<F, X>(supplier, function);
	}
	
	public Applier<F, T> to(Consumer<T> consumer) {
		return new Applier<F, T>(supplier, function, consumer);
	}
	
	public PropertyApplier<F, T> to(PropertyOwner owner, String name) {
		return new PropertyApplier<F, T>(supplier, function, new PropertyConsumer<T>(owner, name));
	}
	
	public static <F> PropertyApplierBuilder<F, F> from(PropertyOwner owner, String name, Class<F> type) {
		return new PropertyApplierBuilder<F, F>(new PropertySupplier<F>(owner, name));
	}

}
