package de.tunetown.roommap.view.controls;

import java.text.DecimalFormat;

import de.tunetown.roommap.main.Main;

/**
 * Option to show/hide the wavelength circles when clicking on the data output panel
 * 
 * @author tweber
 *
 */
public class WavelengthCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	public WavelengthCheckboxControl(Main main, Controls parent) {
		super(parent, main);
	}

	@Override
	public void updateValue() {
		setValue(getMain().getShowWavelength());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		getMain().setShowWavelength(value);
	}

	@Override
	protected String getLabelText() {
		DecimalFormat df = new DecimalFormat("#.##");
		return "Show Wavelength Circles (lambda: " + df.format(getMain().getMeasurements().getWavelength(getMain().getFrequency())) + "m)";
	}
}
