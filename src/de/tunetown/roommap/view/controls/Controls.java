package de.tunetown.roommap.view.controls;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
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
	private JCheckBox normalizeToFreqSwitch;
	
	public Controls(Main main) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.main = main;
		
		init();
	}

	/**
	 * Initialize all controls
	 */
	private void init() {
		freqControl = new FrequencyControl(main, this);
		add(freqControl);

		heightControl = new HeightControl(main, this);
		add(heightControl);
		
		marginControl = new MarginControl(main, this);
		add(marginControl);

		normalizeToFreqSwitch = new JCheckBox("Normalize SPL range for selected frequency");
		normalizeToFreqSwitch.setSelected(true);
		normalizeToFreqSwitch.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				main.setNormalizeByFrequency(e.getStateChange() == ItemEvent.SELECTED);
				main.repaint();
			}
		});
		add(normalizeToFreqSwitch);
		
		// Initial update. This sets the initial values of all controls
		updateControls();
	}
	
	/**
	 * Update all controls
	 */
	public void updateControls() {
		if (freqControl != null) freqControl.update();
		if (marginControl != null) marginControl.update();
		if (heightControl != null) heightControl.update();
		if (normalizeToFreqSwitch != null) normalizeToFreqSwitch.setSelected(main.getNormalizeByFrequency());
	}
}
