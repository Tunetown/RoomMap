package de.tunetown.roommap.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.rsl.conrad.geometry.shapes.simple.PointND;
import edu.stanford.rsl.tutorial.motion.estimation.ThinPlateSplineInterpolation;

/**
 * This represents the data model (all measurement points).
 * 
 * @author tweber
 *
 */
public class Measurements {
	
	private List<Measurement> measurements = new ArrayList<Measurement>();

	public Measurements() {
	}
	
	public void load(File[] files) {
		for(File file : files) {
			Measurement m = new Measurement();
			m.load(file);
			
			if (m.isValid()) measurements.add(m);
		}
		
		System.out.println ("Parsed " + measurements.size() + " data points"); // TODO determine data size
	}
	
	public List<Measurement> getMeasurements() {
		return measurements;
	}
	
	/**
	 * Returns the interpolated SPL level at a given point
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public double getSpl(double x, double y, double z, double freq) {
		// TODO optimize
		ArrayList<PointND> points = new ArrayList<PointND>();
		ArrayList<PointND> values = new ArrayList<PointND>();
		
		for(Measurement m : measurements) {
			points.add(new PointND(m.getX(), m.getY(), m.getZ()));
			values.add(new PointND(m.getSpl(freq)));
		}
		
		ThinPlateSplineInterpolation t = new ThinPlateSplineInterpolation(3, points, values);
		
		return t.interpolate(new PointND(x, y, z)).getElement(0);
	}
	
	public double getMaxX() {
		// TODO buffer
		double ret = -Double.MAX_VALUE;
		for(Measurement m : measurements) {
			if (m.getX() > ret) ret = m.getX();
		}
		return ret;
	}
	
	public double getMaxY() {
		// TODO buffer
		double ret = -Double.MAX_VALUE;
		for(Measurement m : measurements) {
			if (m.getY() > ret) ret = m.getY();
		}
		return ret;
	}
	
	public double getMaxZ() {
		// TODO buffer
		double ret = -Double.MAX_VALUE;
		for(Measurement m : measurements) {
			if (m.getZ() > ret) ret = m.getZ();
		}
		return ret;
	}

	public double getMinX() {
		// TODO buffer
		double ret = Double.MAX_VALUE;
		for(Measurement m : measurements) {
			if (m.getX() < ret) ret = m.getX();
		}
		return ret;
	}
	
	public double getMinY() {
		// TODO buffer
		double ret = Double.MAX_VALUE;
		for(Measurement m : measurements) {
			if (m.getY() < ret) ret = m.getY();
		}
		return ret; 
	}
	
	public double getMinZ() {
		// TODO buffer
		double ret = Double.MAX_VALUE;
		for(Measurement m : measurements) {
			if (m.getZ() < ret) ret = m.getZ();
		}
		return ret; 
	}

	/**
	 * Returns minimum SPL over all frequencies
	 * 
	 * @return
	 */
	public double getMinSpl() {
		// TODO buffer
		double ret = Double.MAX_VALUE;
		for(Measurement m : measurements) {
			if (m.getMinSpl() < ret) ret = m.getMinSpl();
		}
		return ret;
	}
	
	public double getMinSpl(double freq) {
		// TODO buffer
		double ret = Double.MAX_VALUE;
		for(Measurement m : measurements) {
			if (m.getSpl(freq) < ret) ret = m.getSpl(freq);
		}
		return ret;
	}

	/**
	 * Returns maximum SPL over all frequencies
	 * 
	 * @return
	 */
	public double getMaxSpl() {
		// TODO buffer
		double ret = -Double.MAX_VALUE;
		for(Measurement m : measurements) {
			if (m.getMaxSpl() > ret) ret = m.getMaxSpl();
		}
		return ret;
	}

	public double getMaxSpl(double freq) {
		// TODO buffer
		double ret = -Double.MAX_VALUE;
		for(Measurement m : measurements) {
			if (m.getSpl(freq) > ret) ret = m.getSpl(freq);
		}
		return ret;
	}
}
