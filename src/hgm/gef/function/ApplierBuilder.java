package hgm.gef.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ApplierBuilder<F, T> {
	
	private Supplier<F> supplier;
	
	private Function<F, T> function;

	public ApplierBuilder(Supplier<F> supplier) {
		this.supplier = supplier;
	}
	
	public ApplierBuilder(Supplier<F> supplier, Function<F, T> function) {
		this.supplier = supplier;
		this.function = function;
	}
	
	public <X> ApplierBuilder<F, X> via(Function<F, X> function) {
		return new ApplierBuilder<F, X>(supplier, function);
	}
	
	public Applier<F, T> to(Consumer<T> consumer) {
		return new Applier<F, T>(supplier, function, consumer);
	}
	
	public static <F, T> ApplierBuilder<F, T> from(Supplier<F> supplier) {
		return new ApplierBuilder<F, T>(supplier);
	}

}
