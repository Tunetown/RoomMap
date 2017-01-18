package de.tunetown.roommap.view.controls;

import java.text.DecimalFormat;

import de.tunetown.roommap.main.Main;

public class FrequencySliderControl extends SliderControl {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public FrequencySliderControl(Main main, Controls parent) {
		super(parent, 3);
		this.main = main;
	}

	@Override
	protected void changeValue(double val) {
		main.setFrequency(val);
		main.repaint();
	}

	@Override
	protected double determineValue() {
		return main.getFrequency();
	}

	@Override
	public void updateValue() {
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
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(value);
	}

	@Override
	protected double getStep(double value) {
		return 0.5;
	}

	@Override
	protected int getLabelWidth() {
		return 130;
	}

	@Override
	protected String getLabelText() {
		return "Frequency (Hz):";
	}
}
