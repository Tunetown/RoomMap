package de.tunetown.roommap.main;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import de.tunetown.roommap.model.Measurements;
import de.tunetown.roommap.view.MainFrame;

/**
 * Application class for RoomMap. This application is used to evaluate field measurements made with REW and
 * exported there as text files. The file name has to contain the coordinates of the measurement mic
 * position (x,y,z) separated by spaces (see examples folder).
 * 
 * The SPL distribution is shown at a specific height (adjust with slider) and at a specific frequency (adjust with slider 
 * or text input). The graph shows the pressure (SPL) distribution interpolated among the data points (shown as gray dots),
 * visualized from blue (lowest level) to red (highest level).
 * 
 * Just try it with the delivered examples: When the program asks you for files, select all
 * files inside one example folder and hit OK. 
 * 
 * Features:
 * - Mulithreaded interpolation 
 * 
 * Dependencies to external libraries/sources: 
 * - CONRAD, a biomedical library which is being used here for thin plate spline interpolation in 3D. Just the necessary 
 *   classes are included as source, slightly modified to kill eclipse warnings etc.
 * - rainbowvis: This is also included as source, and used for Color interpolation. This has also been modified to produce
 *   Color instances instead of CSS strings.
 * - ij.jar: Used by CONRAD, included as jar archive
 * - Jama-1.0.2.jar: Used by CONRAD, included as jar archive
 * - jpop.0.7.5.jar: Used by CONRAD, included as jar archive
 * 
 * TODO:
 * Finish current features:
 * - Split view classes up (data, points etc)
 * - Keep aspect ratio of window
 * - 3.00 Runtime at frequency change: Pre-determine coefficients for interpolation 
 * 				- Store one interpolator per frequency
 * 				- Only allow frequencies which do exist in measurement 0. If not found, take next higher (warn if delta > 0.5Hz) 
 * 					-> Stateful measurements.frequency attribute
 * 					-> Show correct frequency in text input
 * 
 * New features:
 * - 1.00 Do not paint any data points exceeding the current Z layer (do not extrapolate)
 * - 3.00 !!!! Option: Show aggregated over function of frequency (ngauss, -tanh) 
 * 		- show on f axis also
 * - 1.00 Import image PNG to lay over data
 * - 4.00 visualize in 3d like amroc (one color only, with alpha)
 * 
 * @author Thomas Weber, 2016/2017
 * @see www.tunetown.de
 * @version 0.1
 *
 */
public class Main {

	private MainFrame frame;
	private Measurements measurements;
	
	// Defaults
	private double frequency = 0;  
	private double viewZ = 0;       
	private double margin = 0.3; // TODO?
	private double resolution = 0.2;       // Resolution (model units, not pixels!)
	private boolean normalizeByFrequency = false;
	private boolean projectionOfPoints = false;
	private boolean showGrid = true;
	
	private boolean pooledInterpolation = true;
	private boolean pooledFreqChange = true;
	
	/**
	 * Main method
	 *  
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Use the native menu bar on mac os x
			System.setProperty("apple.laf.useScreenMenuBar", "true"); //$NON-NLS-1$
		
			// Set native look and feel 
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			
		} catch (Throwable t) {
			t.printStackTrace();
		} 
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				Main appl = new Main();
				appl.init();
			}
		});	
	}

	/**
	 * Initialize the application (called by main() method)
	 * 
	 */
	private void init() {
		// Load data
		JFileChooser j = new JFileChooser("/Users/tweber/git/RoomMap/examples"); // TODO remove
		j.setMultiSelectionEnabled(true);
		if (j.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) System.exit(0);
		
		File[] files = j.getSelectedFiles();
		measurements = new Measurements(this);
		measurements.load(files);
		
		// Get initial control values
		frequency = measurements.getMinFrequency();
		viewZ = measurements.getMinZ();
		
		// Create and initialize application frame and menu. Order is critical here for proper display.
		frame = new MainFrame(this);
		frame.init();
	}
	
	public Measurements getMeasurements() {
		return measurements;
	}

	public void setFrequency(double value) {
		this.frequency = value;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setViewZ(double z) {
		this.viewZ = z;
	}

	public double getViewZ() {
		return viewZ;
	}

	public void repaint() {
		frame.repaint();
	}

	public void setMargin(double d) {
		margin = d;
	}

	public double getMargin() {
		return margin;
	}
	
	public boolean getNormalizeByFrequency() {
		return normalizeByFrequency;
	}

	public void setNormalizeByFrequency(boolean b) {
		normalizeByFrequency = b;
	}

	public boolean getPointProjection() {
		return projectionOfPoints;
	}

	public void setPointProjection(boolean b) {
		projectionOfPoints = b;
	}
	
	public boolean getPooledInterpolation() {
		return pooledInterpolation;
	}

	public void setPooled(boolean b) {
		pooledInterpolation = b;
	}

	public void setPooledFreqChange(boolean b) {
		pooledFreqChange = b;
		measurements.resetInterpolator();
	}

	public boolean getPooledFreqChange() {
		return pooledFreqChange;
	}

	public void setShowGrid(boolean b) {
		showGrid = b;
	}

	public boolean getShowGrid() {
		return showGrid;
	}

	public void setResolution(double val) {
		resolution = val;
	}

	public double getResolution() {
		return resolution;
	}
}

