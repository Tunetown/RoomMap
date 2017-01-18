package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

public class GridCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public GridCheckboxControl(Main main, Controls parent) {
		super(parent);
		this.main = main;
	}

	@Override
	public void updateValue() {
		setValue(main.getShowGrid());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		main.setShowGrid(value);
		main.repaint();
	}
	
	@Override
	protected String getLabelText() {
		return "Show Grid";
	}
}
