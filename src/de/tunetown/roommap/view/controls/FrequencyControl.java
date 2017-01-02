package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

public class FrequencyControl extends Control {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public FrequencyControl(Main main, Controls parent) {
		super(parent, "Frequency:", 4);
		this.main = main;
		
		init();
	}

	@Override
	protected void changeValue(double val) {
		main.setFrequency(val);
		main.repaint();
	}

	@Override
	public void update() {
		setValue(main.getFrequency());
	}

	@Override
	public double getMin() {
		return 20;
	}

	@Override
	public double getMax() {
		return 420;
	}
}
