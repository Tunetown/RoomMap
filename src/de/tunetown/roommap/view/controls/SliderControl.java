package de.tunetown.roommap.view.controls;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.tunetown.roommap.main.Main;

public abstract class SliderControl extends Control {
	private static final long serialVersionUID = 1L;

	protected Controls parent;
	
	private JLabel label;
	private JSlider slider;
	private JTextField input;
	
	private int resolution = 1000;
	private int labelCount;
	
	private boolean temporarilyDisableSlider = false;
	
	public SliderControl(Controls parent, Main main) {
		this(parent, main, 2);
	}

	public SliderControl(Controls parent, Main main, int labelCount) {
		super(main);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		this.parent = parent;
		this.labelCount = labelCount;
		if (this.labelCount < 2) this.labelCount = 2;
	}

	/**
	 * This has to return the current model value to the control
	 * 
	 * @param value new value
	 */
	protected abstract double determineValue();

	/**
	 * Called when a change has been made to one of the control inputs.
	 * 
	 * @param value new value
	 */
	protected abstract void changeValue(double value);
	
	/**
	 * Formatting for output in text field
	 * 
	 * @param value
	 * @return
	 */
	protected abstract String formatValue(double value);
	
	/**
	 * Returns the minimum boundary
	 * 
	 * @return
	 */
	public abstract double getMin();
	
	/**
	 * Returns the maximum boundary
	 * 
	 * @return
	 */
	public abstract double getMax();

	/**
	 * Returns the width of the label
	 * 
	 * @return
	 */
	protected abstract int getLabelWidth();
	
	/**
	 * Returns the step for the control. Override this to implement stepping, 
	 * if needed also dependent on the current value.
	 * If NaN is returned, no stepping/rounding will be done.
	 * 
	 * @param value
	 * @return
	 */
	public double getStep(double value) {
		return Double.NaN;
	}

	/**
	 * Initialize content (must be called by child classes at the end of the constructor)
	 * 
	 */
	@Override
	public void init() {
		// Label
		label = new JLabel(getLabelText());
		label.setPreferredSize(new Dimension(parent.getMaxLabelWidth(SliderControl.class), 0));
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		add(label);
		
		// SPacer
		JLabel spacer = new JLabel("");
		spacer.setPreferredSize(new Dimension(10,0));
		add(spacer);
		
		// Frequency Slider
		slider = new JSlider(JSlider.HORIZONTAL, 0, resolution, 0);
		updateSliderAttributes();
		
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if (temporarilyDisableSlider) {
					temporarilyDisableSlider = false;
					return;
				}
				double value = convertFromSlider(((JSlider)e.getSource()).getValue());
				value = doStep(value);
				if (value == determineValue()) return;
				changeValue(value);
				input.setText(formatValue(value));
				updateDependentControls();
				repaintControls();
			}
		});
		add(slider);
		
		// Frequency Input
		input = new JTextField("", 8);
		input.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			@Override
		    public void actionPerformed(ActionEvent e) {
				double val;
				try {
					val = Double.parseDouble(e.getActionCommand());
				} catch (NumberFormatException ex) {
					return;
				}
				temporarilyDisableSlider = true;
				changeValue(val); 
				slider.setValue(convertToSlider(val));
				updateDependentControls();
				repaintControls();
		    }
		});
		input.setMaximumSize(new Dimension(100, 30));
		add(input);
		
		// Initial update
		updateValue();
	}
	
	/**
	 * Update slider labels etc.
	 * Must be called explicitly when something has changed
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void updateSliderAttributes() {
		Hashtable labelTable = new Hashtable();
		for(int i=0; i<=1000; i+=(1000 / labelCount)) {
			labelTable.put(i, new JLabel(formatValue(doStep(convertFromSlider(i)))));
		}
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
	}
	
	/**
	 * Set control value 
	 * 
	 * @param value
	 */
	protected void setValue(double value) {
		if (input != null) input.setText(formatValue(value));
		if (slider != null) slider.setValue(convertToSlider(value));
	}
	
	/**
	 * 
	 */
	protected double doStep(double value) {
		if (!Double.isNaN(getStep(value))) {
			return Math.floor(value / getStep(value)) * getStep(value);
		} else {
			return value;
		}
	}
	
	/**
	 * Returns current value
	 * 
	 * @return
	 */
	public double getValue() {
		return Double.parseDouble(input.getText());
	}
	
	private int convertToSlider(double value) {
		return (int)((value - getMin()) / (getMax() - getMin()) * resolution);
	}
	
	private double convertFromSlider(int sl) {
		return ((double)sl / resolution) * (getMax() - getMin()) + getMin();
	}
	
	@Override
	protected void updateLabel() {
		label.setText(getLabelText());
	}

}


