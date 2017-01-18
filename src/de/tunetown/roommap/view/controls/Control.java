package de.tunetown.roommap.view.controls;

import javax.swing.JPanel;

public abstract class Control extends JPanel {
	private static final long serialVersionUID = 1L;

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
}