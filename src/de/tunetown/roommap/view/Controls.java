package de.tunetown.roommap.view;

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

import de.tunetown.roommap.main.Main;

/**
 * Panel for the controls
 * 
 * @author tweber
 *
 */
public class Controls extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	private JSlider heightSlider;
	private JSlider freqSlider;
	private JTextField freqInput;
	private JSlider marginSlider;
	
	public Controls(Main main) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.main = main;
		
		init();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void init() {
		// Frequency Label
		JLabel freqLabel = new JLabel("Frequency:");
		add(freqLabel);
		
		// Frequency Slider
		freqSlider = new JSlider(JSlider.HORIZONTAL, 20, 420, 20);
		Hashtable labelTable = new Hashtable();
		for(int i=20; i<=420; i+=100) {
			labelTable.put(i, new JLabel(""+i));
		}
		freqSlider.setLabelTable( labelTable );
		freqSlider.setPaintLabels(true);
		freqSlider.setMinorTickSpacing(20);
		freqSlider.setPaintTicks(true);
		
		freqSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = ((JSlider)e.getSource()).getValue();
				setFrequency((double)value);
			}
		});
		add(freqSlider);
		
		// Frequency Input
		freqInput = new JTextField("", 8);
		freqInput.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = 1L;
			
			@Override
		    public void actionPerformed(ActionEvent e) {
				double val;
				try {
					val = Double.parseDouble(e.getActionCommand());
				} catch (NumberFormatException ex) {
					return;
				}
				setFrequency(val); // TODO update routine
		    }
		});
		add(freqInput);
		
		// Height Label
		JLabel heightLabel = new JLabel("Z (Height):");
		add(heightLabel);

		// Height Slider
		heightSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
		setHeightSliderAttribs();
		
		heightSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = ((JSlider)e.getSource()).getValue();
				setViewZ(convertSliderToZ(value));
			}
		});
		add(heightSlider);
		
		// Margin Label
		JLabel marginLabel = new JLabel("Margin (Zoom):");
		add(marginLabel);

		// Margin Slider
		marginSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
		Hashtable labelTable3 = new Hashtable();
		DecimalFormat df = new DecimalFormat("#.##");
		for(int i=0; i<=1000; i+=200) {
			double value = ((double)i/1000);
			labelTable3.put(i, new JLabel(df.format(value)));
		}
		marginSlider.setLabelTable(labelTable3);
		marginSlider.setPaintLabels(true);
		marginSlider.setMinorTickSpacing(200);
		marginSlider.setPaintTicks(true);
		
		marginSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = ((JSlider)e.getSource()).getValue();
				setMargin((double)value / 1000);
			}
		});
		add(marginSlider);
		
		updateControls();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setHeightSliderAttribs() {
		Hashtable labelTable2 = new Hashtable();
		DecimalFormat df = new DecimalFormat("#.#");
		for(int i=0; i<=1000; i+=250) {
			double value = convertSliderToZ(i); 
			labelTable2.put(i, new JLabel(df.format(value)));
		}
		heightSlider.setLabelTable(labelTable2);
		heightSlider.setPaintLabels(true);
		heightSlider.setMinorTickSpacing(200);
		heightSlider.setPaintTicks(true);
	}

	private double convertSliderToZ(int i) {
		return (double)i * (main.getMeasurements().getMaxZ() - main.getMeasurements().getMinZ() + 2*main.getMargin()) / 1000 - main.getMargin() + main.getMeasurements().getMinZ();
	}

	private int convertZToSlider(double z) {
		return (int)((z + main.getMargin() - main.getMeasurements().getMinZ()) * 1000 / (main.getMeasurements().getMaxZ() - main.getMeasurements().getMinZ() + 2*main.getMargin())) ;
	}
	
	public void updateControls() {
		freqSlider.setValue((int)main.getFrequency());
		freqInput.setText(""+main.getFrequency());
		heightSlider.setValue(convertZToSlider(main.getViewZ()));
		setHeightSliderAttribs();
		marginSlider.setValue((int)(main.getMargin() * 1000));
	}

	protected void setMargin(double d) {
		//main.setViewZ(main.getViewZ() - main.getMargin());
		main.setMargin(d);
		//main.setViewZ(main.getViewZ() + main.getMargin());

		updateControls();
		main.repaint();
	}

	protected void setFrequency(double value) {
		main.setFrequency(value);
		
		updateControls();
		main.repaint();
	}

	public void setViewZ(double h) {
		main.setViewZ(h);
		
		updateControls();
		main.repaint();
	}
}
