package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

public class NormalizeByFreqCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public NormalizeByFreqCheckboxControl(Main main, Controls parent) {
		super(parent);
		this.main = main;
	}

	@Override
	public void updateValue() {
		setValue(main.getNormalizeByFrequency());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		main.setNormalizeByFrequency(value);
		main.repaint();
	}

	@Override
	protected String getLabelText() {
		return "Normalize SPL range for selected frequency";
	}
}
