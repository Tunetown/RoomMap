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
		
		// Create controls
		initControls();

		// Add registered controls to the panel
		addControls();
		
		// Initial update. This sets the initial values of all controls to the current values in the program.
		updateControlValues();

	}
	
	/**
	 * Initialize all controls as instances first
	 */
	private void initControls() {
		controls.add(new FrequencySliderControl(main, this));
		
		Control hsl = new HeightSliderControl(main, this);
		controls.add(hsl);
		
		Control msl = new MarginSliderControl(main, this);
		msl.addDependentControl(hsl);
		controls.add(msl);
		
		controls.add(new ResolutionSliderControl(main, this));
		controls.add(new NormalizeByFreqCheckboxControl(main, this));
		controls.add(new ProjectPointsCheckboxControl(main, this));
		controls.add(new GridCheckboxControl(main, this));
		controls.add(new WavelengthCheckboxControl(main, this));
		controls.add(new HideSenselessDataCheckboxControl(main, this));
		controls.add(new PooledInterpolationCheckboxControl(main, this));
		controls.add(new PrecalculationCheckboxControl(main, this));
	}
	
	/**
	 * Add all initialized controls to the panel
	 * 
	 */
	private void addControls() {
		for(Control c : controls) {
			c.init();
			add(c);
		}
	}
	
	/**
	 * Returns the first control of a given class type
	 * 
	 * @param cl
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Control getControl(Class cl) {
		for(Control c : controls) {
			if (cl.isInstance(c)) return c;
		}
		return null;
	}

	/**
	 * Update all control values
	 */
	public void updateControlValues() {
		for(Control c : controls) c.updateValue();
	}
	
	/**
	 * Update all control labels
	 */
	public void updateControlLabels() {
		for(Control c : controls) c.updateLabel();
	}

	/**
	 * Returns the width of the largest label of all controls of a given class type
	 * 
	 * @param cla
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int getMaxLabelWidth(Class cla) {
		int ret = 0;
		for(Control c : controls) {
			if (cla.isInstance(c) && c.getLabelWidth() > ret) ret = c.getLabelWidth();
		}
		return ret;
	}
}
