package de.tunetown.roommap.view.controls;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import de.tunetown.roommap.main.Main;

public abstract class CheckboxControl extends Control {
	private static final long serialVersionUID = 1L;

	protected Controls parent;
	
	private JCheckBox checkbox;
	
	public CheckboxControl(Controls parent, Main main) {
		super(main);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.parent = parent;
	}

	/**
	 * Called when a change has been made to one of the control inputs.
	 * 
	 * @param value new value
	 */
	protected abstract void changeValue(boolean value);
	
	/**
	 * Initialize content (must be called by child classes at the end of the constructor)
	 * 
	 */
	@Override
	public void init() {
		checkbox = new JCheckBox(getLabelText());
		checkbox.setPreferredSize(new Dimension(parent.getMaxLabelWidth(CheckboxControl.class), 0));
		checkbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				changeValue(e.getStateChange() == ItemEvent.SELECTED);
				updateDependentControls();
				repaintControls();
			}
		});
		add(checkbox);

		// Initial update
		updateValue();
	}
	
	/**
	 * Set control value
	 * 
	 * @param value
	 */
	protected void setValue(boolean v) {
		if (checkbox != null) checkbox.setSelected(v);
	}
	
	/**
	 * Returns current value
	 * 
	 * @return
	 */
	public boolean getValue() {
		return checkbox.isSelected();
	}
	
	@Override
	protected void updateLabel() {
		checkbox.setText(getLabelText());
	}
}
