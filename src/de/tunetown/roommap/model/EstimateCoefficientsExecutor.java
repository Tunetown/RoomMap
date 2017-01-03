package de.tunetown.roommap.model;

import java.util.ArrayList;
import java.util.concurrent.Callable;

@SuppressWarnings("rawtypes")
public class EstimateCoefficientsExecutor implements Callable {

	private ThinPlateSplineInterpolationPooled parent;
	private ArrayList<Integer> indices;
	
	public EstimateCoefficientsExecutor(ThinPlateSplineInterpolationPooled parent, ArrayList<Integer> indices) {
		this.parent = parent;
		this.indices = indices;
	}
	
	@Override
	public Object call() throws Exception {
		return parent.calculateCoefficients(indices);
	}

}
