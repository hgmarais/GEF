package hgm.gef.fig;

public interface Bounded {

	Bounds getBounds();
	
	default boolean contains(double mx, double my) {
		return getBounds().contains(mx, my);
	}
	
}
