package de.tunetown.roommap.view.controls;

import java.text.DecimalFormat;

import de.tunetown.roommap.main.Main;

public class ResolutionSliderControl extends SliderControl {
	private static final long serialVersionUID = 1L;
	
	public ResolutionSliderControl(Main main, Controls parent) {
		super(parent, main, 4);
	}
	
	@Override
	protected void changeValue(double val) {
		getMain().setResolution(val);
	}

	@Override
	protected double determineValue() {
		return getMain().getResolution();
	}

	@Override
	public void updateValue() {
		setValue(getMain().getResolution());
	}

	@Override
	public double getMin() {
		return 0.05;
	}

	@Override
	public double getMax() {
		return 0.5;
	}

	@Override
	protected String formatValue(double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(value);
	}

	@Override
	public double getStep(double value) {
		return 0.05;
	}
	
	@Override
	protected int getLabelWidth() {
		return 130;
	}

	@Override
	protected String getLabelText() {
		return "Resolution [m]";
	}
}
