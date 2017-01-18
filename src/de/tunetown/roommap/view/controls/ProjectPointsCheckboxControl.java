package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

public class ProjectPointsCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	public ProjectPointsCheckboxControl(Main main, Controls parent) {
		super(parent, main);
	}

	@Override
	public void updateValue() {
		setValue(getMain().getPointProjection());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		getMain().setPointProjection(value);
	}

	@Override
	protected String getLabelText() {
		return "3D projection of data points";
	}
}
