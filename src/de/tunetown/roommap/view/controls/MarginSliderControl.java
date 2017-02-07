package de.tunetown.roommap.view.controls;

import java.text.DecimalFormat;

import de.tunetown.roommap.main.Main;

/**
 * Slider for the margin
 * 
 * @author tweber
 *
 */
public class MarginSliderControl extends SliderControl {
	private static final long serialVersionUID = 1L;
	
	public MarginSliderControl(Main main, Controls parent) {
		super(parent, main, 4);
	}
	
	@Override
	protected void changeValue(double val) {
		getMain().setMargin(val);
	}

	@Override
	protected double determineValue() {
		return getMain().getMargin();
	}

	@Override
	public void updateValue() {
		setValue(getMain().getMargin());
	}

	@Override
	public double getMin() {
		return 0;
	}

	@Override
	public double getMax() {
		return 1;
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
		return "Margin [m]";
	}
}
