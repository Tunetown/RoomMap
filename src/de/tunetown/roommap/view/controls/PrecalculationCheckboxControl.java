package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;
import de.tunetown.roommap.main.ThreadManagement;

public class PrecalculationCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	public PrecalculationCheckboxControl(Main main, Controls parent) {
		super(parent);
		this.main = main;
	}

	@Override
	public void updateValue() {
		setValue(main.getPrecalculation());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		main.setPrecalculation(value);
		main.repaint();
	}

	@Override
	protected String getLabelText() {
		ThreadManagement m = new ThreadManagement(); 
		String ret = "Precalc. in Background with " + m.getNumOfProcessorsInterpolatorPrecalculation() + " Threads";
		
		if (main.getMeasurements().getInterpolatorBuffer().isPrecalculationStarted()) {
			int perc = (int)(100 * main.getMeasurements().getInterpolatorBuffer().getLoadedPerc());
			if (perc == 100) {
				return ret + " (done)";
			} else {
				return ret + " (" + perc + "%)";
			}
		} else {
			return ret;
		}
	}
}
