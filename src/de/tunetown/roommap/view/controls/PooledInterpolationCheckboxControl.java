package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;
import de.tunetown.roommap.main.ThreadManagement;

public class PooledInterpolationCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public PooledInterpolationCheckboxControl(Main main, Controls parent) {
		super(parent);
		this.main = main;
	}

	@Override
	public void updateValue() {
		setValue(main.getPooledInterpolation());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		main.setPooledInterpolation(value);
		main.repaint();
	}

	@Override
	protected String getLabelText() {
		ThreadManagement m = new ThreadManagement(); 
		return "Multithreaded interpolation with " + m.getNumOfProcessorsInterpolation() + " Threads";
	}

}
