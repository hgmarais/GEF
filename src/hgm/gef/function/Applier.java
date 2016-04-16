package hgm.gef.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Applier<F, T> {
	
	private Supplier<F> supplier;
	
	private Function<F, T> function;
	
	private Consumer<T> consumer;

	public Applier(Supplier<F> supplier, Function<F, T> function, Consumer<T> consumer) {
		this.supplier = supplier;
		this.function = function;
		this.consumer = consumer;
	}
	
	public Supplier<F> getSupplier() {
		return supplier;
	}
	
	public Function<F, T> getFunction() {
		return function;
	}
	
	public Consumer<T> getConsumer() {
		return consumer;
	}
	
	public void apply() {
		consumer.accept(function.apply(supplier.get()));
	}
	
}