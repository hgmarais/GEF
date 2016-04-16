package hgm.gef.function;

import hgm.gef.property.PropertyLink;
import hgm.gef.property.PropertyListener;
import hgm.gef.property.PropertyOwner;

public class FPropertyLink<F, T> implements PropertyLink, PropertyListener {
	
	private PropertyOwner source;

	private String name;
	
	private Applier<?, ?> transformer;

	public FPropertyLink(PropertyOwner source, String name, Applier<F, T> transformer) {
		this.source = source;
		this.name = name;
		this.transformer = transformer;
		transformer.apply();
		source.addPropertyListener(this);
	}

	@Override
	public void propertyChanged(PropertyOwner owner, String name) {
		if (this.name.equals(name)) {
			transformer.apply();
		}
	}

}
