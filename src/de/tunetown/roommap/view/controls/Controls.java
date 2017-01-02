package de.tunetown.roommap.view.controls;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import de.tunetown.roommap.main.Main;

/**
 * Panel for the controls
 * 
 * @author tweber
 *
 */
public class Controls extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	private Control freqControl;
	private Control heightControl;
	private Control marginControl;
	
	public Controls(Main main) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.main = main;
		
		init();
	}

	private void init() {
		freqControl = new FrequencyControl(main, this);
		add(freqControl);

		heightControl = new HeightControl(main, this);
		add(heightControl);
		
		marginControl = new MarginControl(main, this);
		add(marginControl);

		updateControls();
	}
	
	public void updateControls() {
		if (freqControl != null) freqControl.update();
		if (marginControl != null) marginControl.update();
		if (heightControl != null) heightControl.update();
	}
}
