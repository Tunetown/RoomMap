package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;
import de.tunetown.roommap.main.ThreadManagement;

/**
 * Option for multithreaded interpolation
 * 
 * @author tweber
 *
 */
public class PooledInterpolationCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	public PooledInterpolationCheckboxControl(Main main, Controls parent) {
		super(parent, main);
	}

	@Override
	public void updateValue() {
		setValue(getMain().getPooledInterpolation());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		getMain().setPooledInterpolation(value);
	}

	@Override
	protected String getLabelText() {
		ThreadManagement m = new ThreadManagement(); 
		return "Multithreaded interpolation with " + m.getNumOfProcessorsInterpolation() + " Threads";
	}

}
