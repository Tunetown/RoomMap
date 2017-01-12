package de.tunetown.roommap.view.controls;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public abstract class Control extends JPanel {
	private static final long serialVersionUID = 1L;

	protected Controls parent;
	
	private JLabel label;
	private JSlider slider;
	private JTextField input;
	
	private int resolution = 1000;
	private String labelText;
	private int labelCount;
	
	public Control(Controls parent, String labelText, int labelCount) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		this.parent = parent;
		this.labelText = labelText;
		this.labelCount = labelCount;
	}

	/**
	 * Called when a change has been made to one of the control inputs.
	 * 
	 * @param value new value
	 */
	protected abstract void changeValue(double value);
	
	/**
	 * Has to update the control instance with the current value of the target parameter
	 */
	public abstract void update();
	
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
	 * Initialize content (must be called by child classes)
	 * 
	 */
	protected void init() {
		// Label
		label = new JLabel(labelText);
		add(label);
		
		// Frequency Slider
		slider = new JSlider(JSlider.HORIZONTAL, 0, resolution, 0);
		updateSliderAttributes();
		
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double value = convertFromSlider(((JSlider)e.getSource()).getValue());
				changeValue(value);
				input.setText("" + value);
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
				changeValue(val); 
				slider.setValue(convertToSlider(val));
		    }
		});
		input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
		add(input);
		
		// Initial update
		update();
	}
	
	public double getStep(double value) {
		return Double.NaN;
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
			labelTable.put(i, new JLabel(formatValue(convertFromSlider(i))));
		}
		slider.setLabelTable(labelTable);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
	}
	
	protected String formatValue(double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		return df.format(value);
	}

	/**
	 * Set control value
	 * 
	 * @param value
	 */
	public void setValue(double value) {
		DecimalFormat df = new DecimalFormat("#.##");
		input.setText(df.format(value));
		
		//double valueSt = TODO getStep 
		slider.setValue(convertToSlider(value));
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
}
