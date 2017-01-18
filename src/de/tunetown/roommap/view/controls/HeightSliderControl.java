package de.tunetown.roommap.view.controls;

import java.text.DecimalFormat;

import de.tunetown.roommap.main.Main;

public class HeightSliderControl extends SliderControl {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public HeightSliderControl(Main main, Controls parent) {
		super(parent, 4);
		this.main = main;
	}

	@Override
	protected void changeValue(double val) {
		main.setViewZ(val);
		main.repaint();
	}

	@Override
	protected double determineValue() {
		return main.getViewZ();
	}

	@Override
	public void updateValue() {
		setValue(main.getViewZ());
		updateSliderAttributes();
	}

	@Override
	public double getMin() {
		return main.getMeasurements().getMinZ() - main.getMargin();
	}

	@Override
	public double getMax() {
		return main.getMeasurements().getMaxZ() + main.getMargin();
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
		return "Height (m):";
	}
	
	
}
