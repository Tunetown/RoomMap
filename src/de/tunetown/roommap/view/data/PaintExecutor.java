package de.tunetown.roommap.view.data;

import java.util.concurrent.Callable;

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
		//System.out.println(x + " started");
		Object ret = o.calculateX(x);
		//System.out.println(x + " finished");
		return ret;
	}

}
