package hgm.gef.property;

public abstract class AbstractPropertyLink implements PropertyLink, PropertyListener {
	
	protected final PropertyOwner target;
	
	protected final PropertyOwner source;

	protected final String targetPropertyName;

	protected final String sourcePropertyName;

	public AbstractPropertyLink(PropertyOwner target, String targetPropertyName, PropertyOwner source, String sourcePropertyName) {
		this.target = target;
		this.source = source;
		this.targetPropertyName = targetPropertyName;
		this.sourcePropertyName = sourcePropertyName;
		source.addPropertyListener(this);
	}

	@Override
	public void propertyChanged(PropertyOwner owner, String name) {
		if (sourcePropertyName.equals(name)) {
			apply();
		}
	}
	
	protected abstract void apply();

}
