package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

public class NormalizeByFreqCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	public NormalizeByFreqCheckboxControl(Main main, Controls parent) {
		super(parent, main);
	}

	@Override
	public void updateValue() {
		setValue(getMain().getNormalizeByFrequency());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		getMain().setNormalizeByFrequency(value);
	}

	@Override
	protected String getLabelText() {
		return "Normalize SPL range for selected frequency";
	}
}
