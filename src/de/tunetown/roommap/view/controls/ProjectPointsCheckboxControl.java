package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

public class ProjectPointsCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public ProjectPointsCheckboxControl(Main main, Controls parent) {
		super(parent);
		this.main = main;
	}

	@Override
	public void updateValue() {
		setValue(main.getPointProjection());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		main.setPointProjection(value);
		main.repaint();
	}

	@Override
	protected String getLabelText() {
		return "3D projection of data points";
	}
}
