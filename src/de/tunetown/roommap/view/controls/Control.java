package de.tunetown.roommap.view.controls;

import java.util.ArrayList;

import javax.swing.JPanel;

import de.tunetown.roommap.main.Main;

/**
 * Generic base class for all controls 
 * 
 * @author tweber
 *
 */
public abstract class Control extends JPanel {
	private static final long serialVersionUID = 1L;

	private Main main;
	private ArrayList<Control> dependentControls = new ArrayList<Control>();
	
	public Control(Main main) {
		this.main = main;
	}
	
	/**
	 * Initialize control
	 * 
	 */
	public abstract void init();

	/**
	 * Has to update the control instance with the current value of the target parameter
	 */
	public abstract void updateValue();

	/**
	 * Has to set the correct label text
	 */
	protected abstract void updateLabel();

	/**
	 * Returns the width of the label
	 * 
	 * @return
	 */
	protected abstract int getLabelWidth();

	/**
	 * Returns the label text
	 */
	protected abstract String getLabelText();

	/**
	 * Adds one control as a dependent control, meaning that when a control changes value, 
	 * all its dependent controls also will be updated
	 * 
	 * @param c
	 */
	public void addDependentControl(Control c) {
		dependentControls.add(c);
	}
	
	/**
	 * Update dependent controls. These have to be registered before with addDependentControl().
	 * 
	 */
	protected void updateDependentControls() {
		for(Control c : dependentControls) c.updateValue();
	}
	
	protected void repaintControls() {
		main.repaint();
	}
	
	protected Main getMain() {
		return main;
	}
}