package de.tunetown.roommap.view.controls;

import java.text.DecimalFormat;

import de.tunetown.roommap.main.Main;

public class HeightSliderControl extends SliderControl {
	private static final long serialVersionUID = 1L;
	
	public HeightSliderControl(Main main, Controls parent) {
		super(parent, main, 4);
	}

	@Override
	protected void changeValue(double val) {
		getMain().setViewZ(val);
	}

	@Override
	protected double determineValue() {
		return getMain().getViewZ();
	}

	@Override
	public void updateValue() {
		setValue(getMain().getViewZ());
		updateSliderAttributes();
	}

	@Override
	public double getMin() {
		return getMain().getMeasurements().getMinZ() - getMain().getMargin();
	}

	@Override
	public double getMax() {
		return getMain().getMeasurements().getMaxZ() + getMain().getMargin();
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
		return "Height [m]";
	}
	
	
}
