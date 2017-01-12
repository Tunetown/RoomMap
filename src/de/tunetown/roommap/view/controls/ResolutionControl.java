package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

public class ResolutionControl extends Control {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public ResolutionControl(Main main, Controls parent) {
		super(parent, "Resolution (m):", 4);
		this.main = main;
		
		init();
	}
	
	@Override
	protected void changeValue(double val) {
		main.setResolution(val);
		main.repaint();
	}

	@Override
	public void update() {
		setValue(main.getResolution());
	}

	@Override
	public double getMin() {
		return 0.05;
	}

	@Override
	public double getMax() {
		return 0.5;
	}
}
