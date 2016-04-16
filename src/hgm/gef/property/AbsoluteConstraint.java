package hgm.gef.property;

public class AbsoluteConstraint implements Constraint {
	
	private Object value;

	public AbsoluteConstraint(Object value) {
		this.value = value;
	}
	
	@Override
	public Object constrain(Object value) {
		return this.value;
	}

}
