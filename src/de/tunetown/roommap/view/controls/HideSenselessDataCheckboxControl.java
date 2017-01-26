package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

public class HideSenselessDataCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	public HideSenselessDataCheckboxControl(Main main, Controls parent) {
		super(parent, main);
	}

	@Override
	public void updateValue() {
		setValue(getMain().getHideSenselessData());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		getMain().setHideSenselessData(value);
	}
	
	@Override
	protected String getLabelText() {
		return "Only show accurate data (sampling theorem)";
	}
}
