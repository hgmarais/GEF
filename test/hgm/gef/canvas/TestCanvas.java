package hgm.gef.canvas;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCanvas {

	@Test
	public void test() {
		Canvas canvas = new Canvas(new ScreenCoordSystem());
		canvas.setXPixelsPerModel(2.0);
		canvas.setOffset(0, 0);
		
		assertEquals(0, (int)canvas.xScreenToModel(0.0));
		assertEquals(20, (int)canvas.xScreenToModel(10.0));
		
		canvas.setOffset(10, 0);
		
		assertEquals(10, (int)canvas.xScreenToModel(0.0));
		assertEquals(30, (int)canvas.xScreenToModel(10.0));
		
		canvas.setOffset(-10, 0);
		
		assertEquals(-10, (int)canvas.xScreenToModel(0.0));
		assertEquals(10, (int)canvas.xScreenToModel(10.0));
		
	}

}
