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
	
	private List<Measurement> measurements;

	private double interpolatorFrequency = 0;
	private ThinPlateSplineInterpolation interpolator = null;
	
	public Measurements() {
	}
	
	/**
	 * Load and parse REW data files
	 * 
	 * @param files
	 */
	public void load(File[] files) {
		measurements = new ArrayList<Measurement>();
		for(File file : files) {
			Measurement m = new Measurement(this);
			m.load(file);
			if (m.isValid()) measurements.add(m);
		}
		System.out.println ("Parsed " + getDataSize() + " data points"); 
	}
	
	/**
	 * Returns the overall amount of data points
	 * 
	 * @return
	 */
	public long getDataSize() {
		long ret = 0;
		for(Measurement m : measurements) {
			ret+= m.getSize();
		}
		return ret;
	}
	
	/**
	 * Get measurements container
	 * 
	 * @return
	 */
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
		return getInterpolator(freq).interpolate(new PointND(x, y, z)).getElement(0);
	}
	
	/**
	 * Get buffered interpolator instance
	 * 
	 * @param freq
	 * @return
	 */
	private ThinPlateSplineInterpolation getInterpolator(double freq) {
		synchronized(this) {
			if (interpolator == null || freq != interpolatorFrequency) {
				ArrayList<PointND> points = new ArrayList<PointND>();
				ArrayList<PointND> values = new ArrayList<PointND>();
				
				for(Measurement m : measurements) {
					points.add(new PointND(m.getX(), m.getY(), m.getZ()));
					values.add(new PointND(m.getSpl(freq)));
				}
				interpolator = new ThinPlateSplineInterpolation(3, points, values);
				interpolatorFrequency = freq;
			}
		}
		return interpolator;
	}
	
	private double maxXBuffer = Double.NaN;
	
	public double getMaxX() {
		synchronized(this) {
			if (Double.isNaN(maxXBuffer)) {
				maxXBuffer = -Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getX() > maxXBuffer) maxXBuffer = m.getX();
				}
			}
		}
		return maxXBuffer;
	}
	
	private double maxYBuffer = Double.NaN;

	public double getMaxY() {
		synchronized(this) {
			if (Double.isNaN(maxYBuffer)) {
				maxYBuffer = -Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getY() > maxYBuffer) maxYBuffer = m.getY();
				}
			}
		}
		return maxYBuffer;
	}
	
	private double maxZBuffer = Double.NaN;

	public double getMaxZ() {
		synchronized(this) {
			if (Double.isNaN(maxZBuffer)) {
				maxZBuffer = -Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getZ() > maxZBuffer) maxZBuffer = m.getZ();
				}
			}
		}
		return maxZBuffer;
	}

	private double minXBuffer = Double.NaN;

	public double getMinX() {
		synchronized(this) {
			if (Double.isNaN(minXBuffer)) {
				minXBuffer = Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getX() < minXBuffer) minXBuffer = m.getX();
				}
			}
		}
		return minXBuffer;
	}
	
	private double minYBuffer = Double.NaN;
	
	public double getMinY() {
		synchronized(this) {
			if (Double.isNaN(minYBuffer)) {
				minYBuffer = Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getY() < minYBuffer) minYBuffer = m.getY();
				}
			}
		}
		return minYBuffer; 
	}
	
	private double minZBuffer = Double.NaN;
	
	public double getMinZ() {
		synchronized(this) {
			if (Double.isNaN(minZBuffer)) {
				minZBuffer = Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getZ() < minZBuffer) minZBuffer = m.getZ();
				}
			}
		}
		return minZBuffer; 
	}

	private double minSPLBuffer = Double.NaN;

	/**
	 * Returns minimum SPL over all frequencies
	 * 
	 * @return
	 */
	public double getMinSpl() {
		synchronized(this) {
			if (Double.isNaN(minSPLBuffer)) {
				minSPLBuffer = Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getMinSpl() < minSPLBuffer) minSPLBuffer = m.getMinSpl();
				}
			}
		}
		return minSPLBuffer;
	}
	
	private double minSPLFBuffer = Double.NaN;
	private double minSPLFBufferFreq = 0;

	public double getMinSpl(double freq) {
		synchronized(this) {
			if (Double.isNaN(Double.NaN) || minSPLFBufferFreq != freq) {
				minSPLFBuffer = Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getSpl(freq) < minSPLFBuffer) minSPLFBuffer = m.getSpl(freq);
				}
				minSPLFBufferFreq = freq;
			}
		}
		return minSPLFBuffer;
	}

	private double maxSPLBuffer = Double.NaN;
	
	/**
	 * Returns maximum SPL over all frequencies
	 * 
	 * @return
	 */
	public double getMaxSpl() {
		synchronized(this) {
			if (Double.isNaN(maxSPLBuffer)) {
				maxSPLBuffer = -Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getMaxSpl() > maxSPLBuffer) maxSPLBuffer = m.getMaxSpl();
				}
			}
		}
		return maxSPLBuffer;
	}

	private double maxSPLFBuffer = Double.NaN;
	private double maxSPLFBufferFreq = 0;

	public double getMaxSpl(double freq) {
		synchronized(this) {
			if (Double.isNaN(maxSPLFBuffer) || maxSPLFBufferFreq != freq) {
				maxSPLFBuffer = -Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getSpl(freq) > maxSPLFBuffer) maxSPLFBuffer = m.getSpl(freq);
				}
				maxSPLFBufferFreq = freq;
			}
		}
		return maxSPLFBuffer;
	}
	
	private double minFreqBuffer = Double.NaN;
	
	public double getMinFrequency() {
		synchronized(this) {
			if (Double.isNaN(minFreqBuffer)) {
				minFreqBuffer = Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getMinFrequency() < minFreqBuffer) minFreqBuffer = m.getMinFrequency();
				}
			}
		}
		return minFreqBuffer; 
	}

	private double maxFreqBuffer = Double.NaN;
	
	public double getMaxFrequency() {
		synchronized(this) {
			if (Double.isNaN(maxFreqBuffer)) {
				maxFreqBuffer = -Double.MAX_VALUE;
				for(Measurement m : measurements) {
					if (m.getMaxFrequency() > maxFreqBuffer) maxFreqBuffer = m.getMaxFrequency();
				}
			}
		}
		return maxFreqBuffer; 
	}
}
