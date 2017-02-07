package de.tunetown.roommap.model;

import edu.stanford.rsl.tutorial.motion.estimation.ThinPlateSplineInterpolation;

/**
 * Pooled executor for pre-calculating interpolators
 * 
 * @author tweber
 *
 */
public class InterpolatorCalculationExecutor extends Thread {

	private InterpolatorBuffer buffer;
	private double freq;
	private ThinPlateSplineInterpolation interpolator;
	private boolean paused = false;
	
	public InterpolatorCalculationExecutor(InterpolatorBuffer buffer, double freq) {
		this.buffer = buffer;
		this.freq = freq;
		this.setPriority(Thread.MIN_PRIORITY);
	}

	@Override
	public void run() {
		if (paused) {
			synchronized(this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (buffer.hasInterpolator(freq)) return;
		interpolator = buffer.createInterpolator(freq);
		buffer.addInterpolator(freq, interpolator);
		System.out.println("Precalculated interpolator coefficients for " + freq + " Hz");
	}

	public void pauseExecution() {
		paused = true;
	}

	public void resumeExecution() {
		paused = false;
		synchronized(this) {
			notify();
		}
	}
}
