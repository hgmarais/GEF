package hgm.gef.property;

public class DirectPropertyLink extends AbstractPropertyLink {

	public DirectPropertyLink(PropertyOwner target, String targetPropertyName, PropertyOwner source,
			String sourcePropertyName) {
		super(target, targetPropertyName, source, sourcePropertyName);
		apply();
	}
	
	@Override
	protected void apply() {
		target.setProperty(targetPropertyName, source.getProperty(sourcePropertyName));
	}

}
