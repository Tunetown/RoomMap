package de.tunetown.roommap.view.controls;

import java.text.DecimalFormat;

import de.tunetown.roommap.main.Main;

public class FrequencySliderControl extends SliderControl {
	private static final long serialVersionUID = 1L;
	
	public FrequencySliderControl(Main main, Controls parent) {
		super(parent, main, 3);
	}

	@Override
	protected void changeValue(double val) {
		getMain().setFrequency(val);
	}

	@Override
	protected double determineValue() {
		return getMain().getFrequency();
	}

	@Override
	public void updateValue() {
		setValue(getMain().getFrequency());
	}

	@Override
	public double getMin() {
		return getMain().getMeasurements().getMinFrequency();
	}

	@Override
	public double getMax() {
		return getMain().getMeasurements().getMaxFrequency();
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
