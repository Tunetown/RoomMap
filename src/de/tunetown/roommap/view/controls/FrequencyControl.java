package de.tunetown.roommap.view.controls;

import java.text.DecimalFormat;

import de.tunetown.roommap.main.Main;

public class FrequencyControl extends Control {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public FrequencyControl(Main main, Controls parent) {
		super(parent, "Frequency (Hz):", 3);
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
		return main.getMeasurements().getMinFrequency();
	}

	@Override
	public double getMax() {
		return main.getMeasurements().getMaxFrequency();
	}

	@Override
	protected String formatValue(double value) {
		DecimalFormat df = new DecimalFormat("#");
		return df.format(value);
	}
	
	
}
