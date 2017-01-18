package de.tunetown.roommap.view.controls;

import java.util.ArrayList;

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
	
	private ArrayList<Control> controls = new ArrayList<Control>();

	public Controls(Main main) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.main = main;
		init();
	}
	
	public void updateControlLabels() {
		for(Control c : controls) c.updateLabel();
	}

	/**
	 * Initialize all controls
	 */
	private void init() {
		controls.add(new FrequencySliderControl(main, this));
		controls.add(new HeightSliderControl(main, this));
		controls.add(new MarginSliderControl(main, this));
		controls.add(new ResolutionSliderControl(main, this));
		controls.add(new NormalizeByFreqCheckboxControl(main, this));
		controls.add(new ProjectPointsCheckboxControl(main, this));
		controls.add(new GridCheckboxControl(main, this));
		controls.add(new PrecalculationCheckboxControl(main, this));
		controls.add(new PooledInterpolationCheckboxControl(main, this));

		// Add registered controls to the panel
		addControls();
		
		// Initial update. This sets the initial values of all controls
		updateControlValues();
	}
	
	private void addControls() {
		for(Control c : controls) {
			c.init();
			add(c);
		}
	}

	/**
	 * Update all controls
	 */
	public void updateControlValues() {
		for(Control c : controls) c.updateValue();
	}
	
	@SuppressWarnings("rawtypes")
	public int getMaxLabelWidth(Class cla) {
		int ret = 0;
		for(Control c : controls) {
			if (cla.isInstance(c) && c.getLabelWidth() > ret) ret = c.getLabelWidth();
		}
		return ret;
	}
}
