package de.tunetown.roommap.view;

import javax.swing.JFrame;

import de.tunetown.roommap.main.Main;

/**
 * Main frame for the application
 * 
 * @author xwebert
 *
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	private MainPanel mainPanel;
	
	public MainFrame(Main main) {
		super("RoomMap");
		this.main = main;
	}
	
	public void init() {
		mainPanel = new MainPanel(main, this);
		
		// Add this main GUI instance to the main frame (this contains all elements)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(mainPanel);

		// Do some size and location stuff
		pack();
		setLocationByPlatform(true);
		setVisible(true);
	}
}
