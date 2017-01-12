package de.tunetown.roommap.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents one measurement at one spatial point in the room, givven 
 * by x, y and z coordinaties parsed from the file name.
 * 
 * @author tweber
 *
 */
public class Measurement {
	
	// Measurement data
	private List<Double> frequencies = null;
	private List<Double> spl = null;
	
	// Coordinates of the measurement
	private double x = Double.NaN;
	private double y = Double.NaN;
	private double z = Double.NaN;
	
	private Measurements parent;
	
	public Measurement(Measurements parent) {
		this.parent = parent;
	}
	
	public void load(File file) {
		// Load file content
		String out = getFileContent(file);
		
		// Parse metadata
		if (!parseMeta(file)) return;

		// Parse data
		parse(out);
		
		System.out.println ("SUCCESS: Parsed " + frequencies.size() + " data points at " + x + " " + y + " " + z);
	}

	public boolean isValid() {
		return frequencies != null &&
			   frequencies.size() > 0 && 
			   spl != null &&
			   spl.size() == frequencies.size() &&
			   x != Double.NaN &&
			   y != Double.NaN &&
			   z != Double.NaN;
	}
	
	/**
	 * Get coordinates from the file name
	 * 
	 * @param file
	 * @return
	 */
	private boolean parseMeta(File file) {
		String name = file.getName();
		String bd = name.substring(0, name.length()-4);
		String[] c = bd.split(" ");
		if (c.length != 3) {
			System.out.println("ERROR: No coordinates in file name: " + name);
			return false;
		}
		
		try {
			x = Double.parseDouble(c[0]);
			y = Double.parseDouble(c[1]);
			z = Double.parseDouble(c[2]);
		} catch (NumberFormatException e) {
			System.out.println("ERROR: Could not parse coordinates from " + name);
			return false;
		}
		
		return true;
	}

	/**
	 * Loads file content as string
	 * 
	 * @param file
	 * @return
	 */
	private String getFileContent(File file) {
		String out = "";
		try {
			out = new String(Files.readAllBytes(file.toPath()));
			
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return out;
	}

	/**
	 * Parses the file data into the instance attributes
	 * 
	 * @param out
	 */
	private void parse(String out) {
		// Initialize
		frequencies = new ArrayList<Double>(); 
		spl = new ArrayList<Double>(); 
		
		// Parse
		String lines[] = out.split("\\r?\\n");
		
		for(int i=0; i<lines.length; i++) {
			// Skip comments
			if (lines[i].startsWith("*")) continue;
			
			// Parse values
			String line[] = lines[i].split(" ");
			frequencies.add(Double.parseDouble(line[0]));
			spl.add(Double.parseDouble(line[1]));
		}
	}
	
	/**
	 * For any frequency, this approximates the SPL value.
	 * 
	 * NOTE: Currently, this returns the next bigger frequency´s SPL, or
	 * NaN if frequency is too high. Interpolation is not necessary as long as the 
	 * data is precise enough. REW export files usually are.
	 * 
	 * @param freq
	 */
	public double getSplStepped(double freq) {
		if (freq < getMinFrequency() || freq > getMaxFrequency()) return Double.NaN;
		
		int i = 0;
		while(i < frequencies.size() && frequencies.get(i) < freq) i++;
		if (i < frequencies.size()) {
			return spl.get(i);
		} else {
			return Double.NaN;
		}
	}

	/**
	 * For any frequency, this approximates the SPL value, averaged between the next two frequencies available. 
	 * 
	 * @param freq
	 */
	public double getSpl(double freq) {
		if (Math.floor(freq)+1 < getMinFrequency() || Math.floor(freq) > getMaxFrequency()) return Double.NaN;
		
		int high = 0;
		while(high < frequencies.size()-1 && frequencies.get(high) < freq) high++;

		int low = frequencies.size()-1;
		while(low > 0 && frequencies.get(low) > freq) low--;

		// TODO interpolate cleanly
		return (spl.get(high) + spl.get(low)) / 2;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	private double minSplBuffer = Double.NaN;

	public double getMinSpl() {
		synchronized(parent) {
			if (Double.isNaN(minSplBuffer)) {
				minSplBuffer = Double.MAX_VALUE;
				for(double s : spl) {
					if (s < minSplBuffer) minSplBuffer = s;
				}
			}
		}
		return minSplBuffer;
	}
	
	private double maxSplBuffer = Double.NaN;

	public double getMaxSpl() {
		synchronized(parent) {
			if (Double.isNaN(maxSplBuffer)) {
				maxSplBuffer = -Double.MAX_VALUE;
				for(double s : spl) {
					if (s > maxSplBuffer) maxSplBuffer = s;
				}
			}
		}
		return maxSplBuffer;
	}

	private double minFreqBuffer = Double.NaN;

	public double getMinFrequency() {
		synchronized(parent) {
			if (Double.isNaN(minFreqBuffer)) {
				minFreqBuffer = Double.MAX_VALUE;
				for(double f : frequencies) {
					if (f < minFreqBuffer) minFreqBuffer = f;
				}
			}
		}
		return minFreqBuffer;
	}
	
	private double maxFreqBuffer = Double.NaN;

	public double getMaxFrequency() {
		synchronized(parent) {
			if (Double.isNaN(maxFreqBuffer)) {
				maxFreqBuffer = -Double.MAX_VALUE;
				for(double f : frequencies) {
					if (f > maxFreqBuffer) maxFreqBuffer = f;
				}
			}
		}
		return maxFreqBuffer;
	}
	
	public long getSize() {
		return spl.size();
	}
}
