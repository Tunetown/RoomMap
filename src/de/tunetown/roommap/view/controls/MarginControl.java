package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

public class MarginControl extends Control {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public MarginControl(Main main, Controls parent) {
		super(parent, "Margin (Zoom):", 4);
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
}
