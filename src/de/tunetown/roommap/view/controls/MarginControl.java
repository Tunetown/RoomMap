package de.tunetown.roommap.view.controls;

import java.text.DecimalFormat;

import de.tunetown.roommap.main.Main;

public class MarginControl extends Control {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public MarginControl(Main main, Controls parent) {
		super(parent, "Margin (m):", 4);
		this.main = main;
		init();
	}
	
	@Override
	protected void changeValue(double val) {
		main.setMargin(val);
		parent.updateControls(); // On a mragin change, we also update the height slider value and labels
		main.repaint();
	}

	@Override
	protected double determineValue() {
		return main.getMargin();
	}

	@Override
	public void update() {
		setValue(main.getMargin());
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

}
