package de.tunetown.roommap.view.data;

import java.util.concurrent.Callable;

/**
 * Pooled executor for multithreaded interpolation. Calls the interpolation algorithm implemented in OutputGraphics.
 * 
 * @author tweber
 *
 */
@SuppressWarnings("rawtypes")
public class PaintExecutor implements Callable {

	private OutputGraphics o;
	private int x;
	
	public PaintExecutor(OutputGraphics o, int x) {
		this.o = o;
		this.x = x;
	}
	
	@Override
	public Object call() throws Exception {
		Object ret = o.calculateX(x);
		return ret;
	}

}
