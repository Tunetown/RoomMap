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
 * Dependencies to external libraries/sources: 
 * - CONRAD, a biomedical library which is being used here for thin plate spline interpolation in 3D. Just the necessary 
 *   classes are included as source, slightly modified to kill eclipse warnings.
 * - rainbowvis: This is also included as source, and used for Color interpolation. This has also been modified to produce
 *   Color instanes instead of CSS strings.
 * - ij.jar: Used by CONRAD, included as jar archive
 * - Jama-1.0.2.jar: Used by CONRAD, included as jar archive
 * - jpop.0.7.5.jar: Used by CONRAD, included as jar archive
 * 
 * TODO:
 * - 1.00 !!!! resize: keep ratio x/y stable
 * - 0.50 resolution bei resize konstant halten
 * 
 * - 1.00 Make resolution/point diameter adjustable (sliders)
 * - 0.25 !!!! add margin parameter
 * 
 * - 0.50 Determine freq range by data
 *		- Upper boundary: limit to <= 500Hz
 *		- Lower boundary: Limit to lowest data point
 * - 0.50 !!!! determine min height (Z) by data
 * 
 * - 0.25 test negative coords
 * 
 * - 0.25 add units to values
 * - 1.00 !!!! color legend
 * - 1.00 axis legends
 * 
 * - 3.00 !!!! Option: Show aggregated over function of frequency (ngauss, -tanh) 
 * 		- show on f axis also
 * - 3.00 simple rectangle vector-orientiert paint layer
 * 		- save in same folder with measurements
 * 
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
	
	private double frequency = 40; // get rid of TODO
	private double viewZ = 0;
	
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
		measurements = new Measurements();
		measurements.load(files);
		
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
}

