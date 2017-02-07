package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;
import de.tunetown.roommap.main.ThreadManagement;

/**
 * Option for interpolator pre-calculation
 * 
 * @author tweber
 *
 */
public class PrecalculationCheckboxControl extends CheckboxControl {
	private static final long serialVersionUID = 1L;
	
	public PrecalculationCheckboxControl(Main main, Controls parent) {
		super(parent, main);
	}

	@Override
	public void updateValue() {
		setValue(getMain().getPrecalculation());
	}

	@Override
	protected int getLabelWidth() {
		return 300;
	}

	@Override
	protected void changeValue(boolean value) {
		getMain().setPrecalculation(value);
	}

	@Override
	protected String getLabelText() {
		ThreadManagement m = new ThreadManagement(); 
		String ret = "Precalc. in Background with " + m.getNumOfProcessorsInterpolatorPrecalculation() + " Threads";
		
		if (getMain().getMeasurements().getInterpolatorBuffer().isPrecalculationStarted()) {
			int perc = (int)(100 * getMain().getMeasurements().getInterpolatorBuffer().getLoadedPerc());
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
