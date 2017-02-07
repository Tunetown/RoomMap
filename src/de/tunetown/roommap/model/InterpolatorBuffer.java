package de.tunetown.roommap.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.tunetown.roommap.main.Main;
import de.tunetown.roommap.main.ThreadManagement;
import edu.stanford.rsl.conrad.geometry.shapes.simple.PointND;
import edu.stanford.rsl.tutorial.motion.estimation.ThinPlateSplineInterpolation;

/**
 * This class manages the interpolator instances for each frequency. Each frequency 
 * has an own interpolator which is buffered to be reused next time the same 
 * frequency is selected.
 * 
 * @author tweber
 *
 */
public class InterpolatorBuffer {

	private Main main;
	
	private ArrayList<PointND> points;
	private ExecutorService pool = null;
	private ArrayList<InterpolatorCalculationExecutor> executors;
	private HashMap<Double, ThinPlateSplineInterpolation> interpolators;

	public InterpolatorBuffer(Main main) {
		this.main = main;
	}
	
	/**
	 * Create interpolator-compatible points array from the model instance
	 * 
	 */
	private void createPoints() {
		points = new ArrayList<PointND>();
		for(Measurement m : main.getMeasurements().getMeasurements()) {
			points.add(new PointND(m.getX(), m.getY(), m.getZ()));
		}
	}

	/**
	 * Returns the interpolator for a given frequency
	 * 
	 * @param freq
	 * @return
	 */
	public ThinPlateSplineInterpolation getInterpolator(double freq) {
		if (interpolators.get(freq) != null) return interpolators.get(freq); 
		
		ThinPlateSplineInterpolation n = createInterpolator(freq);
		interpolators.put(freq, n);
		main.updateControlLabels();
		return n;
	}
	
	/**
	 * Creates a new interpolator
	 * 
	 * @param freq
	 * @return
	 */
	public ThinPlateSplineInterpolation createInterpolator(double freq) {
		ArrayList<PointND> values = new ArrayList<PointND>();
		
		for(Measurement m : main.getMeasurements().getMeasurements()) {
			values.add(new PointND(m.getSpl(freq)));
		}
		return new ThinPlateSplineInterpolation(3, points, values);
	}

	/**
	 * Initialize point arrays etc.
	 * 
	 */
	public void initialize() {
		createPoints();
		interpolators = new HashMap<Double, ThinPlateSplineInterpolation>();
		if (pool != null) pool.shutdownNow();
		pool = null;
	}

	public boolean isPrecalculationStarted() {
		return pool != null;
	}

	/**
	 * Starts pre-calculation of interpolators
	 * 
	 * @param min minimum frequency
	 * @param max maximum frequency
	 * @param step frequency step
	 */
	public void startPrecalculation(double min, double max, double step) {
		ThreadManagement m = new ThreadManagement();
		if (m.getNumOfProcessorsInterpolatorPrecalculation() == 0) return;
		
		// Create new thread pool
		if (pool != null) pool.shutdownNow();
		executors = new ArrayList<InterpolatorCalculationExecutor>();
		pool = Executors.newFixedThreadPool(m.getNumOfProcessorsInterpolatorPrecalculation());
				
		// Add workers to the pool
		for(double f=min; f<=max; f+=step) {
			InterpolatorCalculationExecutor e = new InterpolatorCalculationExecutor(this, f);
			pool.submit(e);
			executors.add(e);
		}
	}
	
	public void addInterpolator(double freq, ThinPlateSplineInterpolation i) {
		synchronized(this) {
			interpolators.put(freq, i);
			main.updateControlLabels();
		}
	}

	public void stopPrecalculation() {
		if (pool == null || executors == null) return;
		for(InterpolatorCalculationExecutor e : executors) {
			e.pauseExecution();
		} 
	}

	public void resumePrecalculation() {
		if (pool == null || executors == null) return;
		for(InterpolatorCalculationExecutor e : executors) {
			e.resumeExecution();
		}
	}

	/**
	 * Returns the precalculation status in [0..1]. 0: Not started, 1: finished
	 * 
	 * @return
	 */
	public double getLoadedPerc() {
		if (executors == null) return 0;
		return (double)interpolators.size() / (double)executors.size();
	}
	
	/**
	 * Is there already a pre-calculated interpolator for a given frequency?
	 * 
	 * @param freq
	 * @return
	 */
	public boolean hasInterpolator(double freq) {
		return (interpolators.get(freq) != null);
	}
}
