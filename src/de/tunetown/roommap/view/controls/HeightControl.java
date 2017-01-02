package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

public class HeightControl extends Control {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public HeightControl(Main main, Controls parent) {
		super(parent, "Height (Z):", 4);
		this.main = main;
		
		init();
	}

	@Override
	protected void changeValue(double val) {
		main.setViewZ(val);
		main.repaint();
	}

	@Override
	public void update() {
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
}
