package de.tunetown.roommap.view.controls;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import de.tunetown.roommap.main.Main;

/**
 * Generic base class for simple text output
 * 
 * @author tweber
 *
 */
public abstract class TextControl extends Control {
	private static final long serialVersionUID = 1L;

	protected Controls parent;
	
	private JLabel label;
	
	public TextControl(Controls parent, Main main) {
		super(main);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.parent = parent;
	}

	/**
	 * Called when a change has been made to one of the control inputs.
	 * 
	 * @param value new value
	 */
	protected void changeValue(boolean value) {
		// Nothing to do
	}
	
	@Override
	public void updateValue() {
		// Nothing to do	
	}
	
	/**
	 * Initialize content (must be called by child classes at the end of the constructor)
	 * 
	 */
	@Override
	public void init() {
		label = new JLabel("");
		add(label);
	}
	
	@Override
	protected void updateLabel() {
		label.setText(getLabelText());
	}
}
