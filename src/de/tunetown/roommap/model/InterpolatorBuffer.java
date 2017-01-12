package de.tunetown.roommap.model;

import java.util.ArrayList;
import java.util.HashMap;

import edu.stanford.rsl.conrad.geometry.shapes.simple.PointND;
import edu.stanford.rsl.tutorial.motion.estimation.ThinPlateSplineInterpolation;

public class InterpolatorBuffer {

	private Measurements measurements;
	private ArrayList<PointND> points;
	
	private HashMap<Double, ThinPlateSplineInterpolation> interpolators;

	public InterpolatorBuffer(Measurements measurements) {
		this.measurements = measurements;
	}
	
	private void createPoints() {
		points = new ArrayList<PointND>();
		for(Measurement m : measurements.getMeasurements()) {
			points.add(new PointND(m.getX(), m.getY(), m.getZ()));
		}
	}

	public ThinPlateSplineInterpolation getInterpolator(double freq) {
		if (interpolators.get(freq) != null) return interpolators.get(freq); 
		
		ThinPlateSplineInterpolation n = createInterpolator(freq);
		interpolators.put(freq, n);
		return n;
	}
	
	private ThinPlateSplineInterpolation createInterpolator(double freq) {
		ArrayList<PointND> values = new ArrayList<PointND>();
		
		for(Measurement m : measurements.getMeasurements()) {
			values.add(new PointND(m.getSpl(freq)));
		}
		return new ThinPlateSplineInterpolation(3, points, values);
	}

	public void initialize() {
		createPoints();
		interpolators = new HashMap<Double, ThinPlateSplineInterpolation>();
	}
}
