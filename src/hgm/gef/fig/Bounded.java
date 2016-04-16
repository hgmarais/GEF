package hgm.gef.fig;

import hgm.gef.property.PropertyOwner;

public interface Bounded extends PropertyOwner {
	
	public static final String BOUNDS = "BOUNDS";

	Bounds getBounds();
	
	@Override
	default Object getProperty(String name) {
		if (BOUNDS.equals(name)) {
			return getBounds();
		}
		
		return null;
	}
	
	default boolean contains(double mx, double my) {
		Bounds bounds = getBounds();
		
		if (bounds == null) {
			return false;
		}
		
		return bounds.contains(mx, my);
	}
	
	default void fireBoundsChanged() {
		firePropertyChanged(BOUNDS);
	}
	
}
