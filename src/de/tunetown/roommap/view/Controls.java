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
		freqSlider = new JSlider(JSlider.HORIZONTAL, 20, 420, (int)main.getFrequency()); // TODO get from data
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
				freqInput.setText(""+main.getFrequency());
			}
		});
		add(freqSlider);
		
		// Frequency Input
		freqInput = new JTextField(""+main.getFrequency(), 15);
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
				freqSlider.setValue((int)main.getFrequency());
		    }
		});
		add(freqInput);
		
		// Height Label
		JLabel heightLabel = new JLabel("Z (Height):");
		add(heightLabel);

		// Height Slider
		heightSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500); // TODO values
		Hashtable labelTable2 = new Hashtable();
		DecimalFormat df = new DecimalFormat("#.##");
		for(int i=0; i<=1000; i+=200) {
			double value = ((double)i/1000) * main.getMeasurements().getMaxZ();
			labelTable2.put(i, new JLabel(df.format(value)));
		}
		heightSlider.setLabelTable(labelTable2);
		heightSlider.setPaintLabels(true);
		heightSlider.setMinorTickSpacing(200);
		heightSlider.setPaintTicks(true);
		
		heightSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = ((JSlider)e.getSource()).getValue();
				setRoomHeight((double)value / 1000);
			}
		});
		add(heightSlider);
		
		// Margin Label
		JLabel marginLabel = new JLabel("Margin:");
		add(marginLabel);

		// Margin Slider
		JSlider marginSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500); // TODO values
		Hashtable labelTable3 = new Hashtable();
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
		//add(marginSlider);
	}
	
	protected void setMargin(double d) {
		main.setMargin(d);
		main.repaint();
	}

	protected void setFrequency(double value) {
		main.setFrequency(value);
		main.repaint();
	}

	public void setRoomHeight(double h) {
		main.setViewZ(h);
		main.repaint();
	}
}
