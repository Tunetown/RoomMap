package de.tunetown.roommap.main;

import java.io.File;

import javax.swing.JApplet;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.tunetown.roommap.model.Measurements;
import de.tunetown.roommap.view.MainFrame;
import de.tunetown.roommap.view.controls.FrequencySliderControl;
import de.tunetown.roommap.view.controls.SliderControl;

/** 
 * This application is used to evaluate field measurements made with REW and
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
 * - Mulithreaded thin plate spline interpolation 
 * - Precalculation of interpolation coefficients for each selectable frequency, in background (multithreaded)
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
 * - Applet release
 * - EXE release
 * 
 * Next new features:
 * - Publish to Applet (on server)
 * - 3.00 !!!! Option: Show aggregated over function of frequency (ngauss, -tanh) 
 * 		- show on f axis also
 * 
 * @author Thomas Weber, 2016/2017
 * @see www.tunetown.de
 * @version 0.1
 *
 */
public class Main extends JApplet {
	private static final long serialVersionUID = 1L;

	private MainFrame frame;
	private Measurements measurements;
	
	/**
	 * User controllable properties. These are all managed here centrally.
	 */
	private double frequency = 0;                   // Selected frequency
	private double viewZ = 0;                       // View Z coordinate (height slider; model units, not pixels!)       
	private double margin = 0.3;                    // Margin (model units, not pixels!)
	private double resolution = 0.2;                // Resolution (model units, not pixels!)
	
	private boolean normalizeByFrequency = false;   // Normalize SPL levels for the selected frequency
	private boolean projectionOfPoints = false;     // Projection of points with a very simple 3d "approximation"
	private boolean showGrid = true;                // Show coordinate grid
	private boolean showWavelength = false;         // Show circles of quarter wavelengths around a selected point, selectable with the mouse
	
	private boolean pooledInterpolation = true;     // Use multiple threads for interpolation
	private boolean precalculation = false;         // Pre-calculate interpolator coefficients for each frequency. NOTE: This must be false at program start!!
	private boolean hideInaccurateData = true;       // Hide data which is not accurate enough according to the Nyquist sampling theorem 
	
	/**
	 * Main method for standalone usage
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
				appl.initialize();
			}
		});	
	}
	
	/**
	 * Init method for applet usage
	 * 
	 */
	public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                	Main appl = new Main();
    				appl.initialize();
                }
            });
        } catch (Exception e) {
            System.err.println("createGUI didn't complete successfully");
        }
    }

	/**
	 * Initialize the application (called by main() method)
	 * 
	 */
	private void initialize() {
		// Load data
		JFileChooser j = new JFileChooser("/Users/tweber/git/RoomMap/examples"); // TODO remove
		j.setMultiSelectionEnabled(true);
		if (j.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) System.exit(0);
		
		File[] files = j.getSelectedFiles();
		measurements = new Measurements(this);
		measurements.load(files);
		
		// Get initial control values
		frequency = (measurements.getMaxFrequency() + measurements.getMinFrequency()) / 2;
		viewZ = measurements.getMinZ();
		
		// Create and initialize application frame and menu. Order is critical here for proper display.
		frame = new MainFrame(this);
		frame.init();
	}
	
	/**
	 * Update the control labels from the real application values
	 * 
	 */
	public void updateControlLabels() { 
		frame.getMainPanel().getControls().updateControlLabels();
	}

	/**
	 * Update the controls themselves from the real application values
	 * 
	 */
	public void updateControlValues() { 
		frame.getMainPanel().getControls().updateControlValues();
	}

	/**
	 * Enable/disable/resume pre-calculation of interpolators
	 * 
	 * @param b
	 */
	public void setPrecalculation(boolean b) {
		precalculation = b;
		if (precalculation) {
			if (measurements.getInterpolatorBuffer().isPrecalculationStarted()) {
				measurements.getInterpolatorBuffer().resumePrecalculation();
			} else {
				startPrecalculation();
			}
		} else {
			measurements.getInterpolatorBuffer().stopPrecalculation();
		}
	}

	/**
	 * Start pre-calculation of interpolators
	 * 
	 */
	private void startPrecalculation() {
		SliderControl fc = (SliderControl)frame.getMainPanel().getControls().getControl(FrequencySliderControl.class);
		if (fc != null) measurements.getInterpolatorBuffer().startPrecalculation(fc.getMin(), fc.getMax(), fc.getStep(0)); 
	}

	/**
	 * Stop pre-calculation of interpolators
	 * 
	 */
	public void stopPrecalculation() {
		if (!getPrecalculation()) return;
		measurements.getInterpolatorBuffer().stopPrecalculation();
	}

	/**
	 * Resume pre-calculation of interpolators
	 * 
	 */
	public void resumePrecalculation() {
		if (!getPrecalculation()) return;
		measurements.getInterpolatorBuffer().resumePrecalculation();
	}

	/**
	 * Is pre-calculation of interpolators being done?
	 * 
	 * @return
	 */
	public boolean getPrecalculation() {
		return precalculation;
	}

	/**
	 * Returns the model instance
	 * 
	 * @return
	 */
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

	public void setPooledInterpolation(boolean b) {
		pooledInterpolation = b;
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

	public boolean getShowWavelength() {
		return showWavelength;
	}

	public void setShowWavelength(boolean value) {
		showWavelength = value;
	}

	public void setHideInaccurateData(boolean b) {
		hideInaccurateData = b;
	}

	public boolean getHideInaccurateData() {
		return hideInaccurateData;
	}
}

