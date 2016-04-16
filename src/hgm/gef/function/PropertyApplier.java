package hgm.gef.function;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import hgm.gef.property.PropertyOwner;

public class PropertyApplier<F, T> extends Applier<F, T> {
	
	private PropertyOwner sourceOwner;
	
	private String sourceName;

	public PropertyApplier(PropertyOwner sourceOwner, String sourceName, Supplier<F> supplier, Function<F, T> function, Consumer<T> consumer) {
		super(supplier, function, consumer);
		this.sourceOwner = sourceOwner;
		this.sourceName = sourceName;
	}
	
	public PropertyOwner getSourceOwner() {
		return sourceOwner;
	}
	
	public String getSourceName() {
		return sourceName;
	}
	
	public FPropertyLink<F, T> toLink() {
		return new FPropertyLink<F, T>(sourceOwner, sourceName, this);
	}
	
}
