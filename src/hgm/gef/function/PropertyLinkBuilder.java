package hgm.gef.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import hgm.gef.property.PropertyOwner;

public class PropertyLinkBuilder<F, T> implements Supplier<T> {
	
	private PropertyOwner sourceOwner;

	private String sourceName;
	
	private Supplier<F> supplier;
	
	private Function<F, T> function;

	public PropertyLinkBuilder(PropertyOwner sourceOwner, String sourceName, Supplier<F> supplier, Function<F, T> function) {
		this.sourceOwner = sourceOwner;
		this.sourceName = sourceName;
		this.supplier = supplier;
		this.function = function;
	}
	
	public <X> PropertyLinkBuilder<T, X> via(Function<T, X> function) {
		return new PropertyLinkBuilder<T, X>(sourceOwner, sourceName, this, function);
	}
	
	public Applier<F, T> to(Consumer<T> consumer) {
		return new Applier<F, T>(supplier, function, consumer);
	}
	
	public PropertyApplier<F, T> to(PropertyOwner owner, String name) {
		return new PropertyApplier<F, T>(sourceOwner, sourceName, supplier, function, new PropertyConsumer<T>(owner, name));
	}
	
	@Override
	public T get() {
		return function.apply(supplier.get());
	}
	
	public static <F> PropertyLinkBuilder<F, F> from(PropertyOwner owner, String name, Class<F> type) {
		return new PropertyLinkBuilder<F, F>(owner, name, new PropertySupplier<F>(owner, name), f -> f);
	}

}
