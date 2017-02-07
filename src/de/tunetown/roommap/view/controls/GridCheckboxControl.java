package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

/**
 * Grid enable checkbox
 * 
 * @author tweber
 *
 */
public class GridCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	public GridCheckboxControl(Main main, Controls parent) {
		super(parent, main);
	}

	@Override
	public void updateValue() {
		setValue(getMain().getShowGrid());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		getMain().setShowGrid(value);
	}
	
	@Override
	protected String getLabelText() {
		return "Show Grid";
	}
}
