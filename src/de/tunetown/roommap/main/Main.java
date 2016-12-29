package de.tunetown.roommap.main;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.tunetown.roommap.model.Measurements;
import de.tunetown.roommap.view.MainFrame;

/**
 * Application class for RoomMap.
 *
 * @author Thomas Weber, 2016/2017
 * @see www.tunetown.de
 * @version 0.1
 *
 */
public class Main {

	/**
	 * Temporary file (here, the last used data will be saved and reloaded on next startup)
	 */
	private static final File TEMP_FILE = new File(System.getProperty("user.home") + File.separator + "SE.tmp");
	
	private MainFrame frame;
	//private Menu menu;
	private Measurements data;
	
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
		JFileChooser j = new JFileChooser();
		int answer = j.showOpenDialog(frame);
		
		if (answer == JFileChooser.APPROVE_OPTION) {
			File file = j.getSelectedFile();
			
			data = new Measurements(file);
			data.load();
		} else {
			System.exit(0);
		}
		
		// Create and initialize application frame and menu. Order is critical here for proper display.
		frame = new MainFrame(this);
		//menu = new Menu(this, frame);
		
		//menu.init();
		frame.init();
	}
	
	public Measurements getMeasurements() {
		return data;
	}
}

