package de.tunetown.roommap.view;

import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.tunetown.roommap.main.Main;

public class Controls extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	private JSlider heightSlider;
	private JSlider freqSlider;
	
	public Controls(Main main) {
		super();
		this.main = main;
		
		init();
	}

	private void init() {
		freqSlider = new JSlider(JSlider.HORIZONTAL, 20, 400, (int)main.getFrequency());
		Hashtable labelTable = new Hashtable();
		for(int i=20; i<=400; i+=20) {
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
		
		heightSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500);
		Hashtable labelTable2 = new Hashtable();
		DecimalFormat df = new DecimalFormat("#.##");
		for(int i=0; i<=1000; i+=200) {
			double value = (double)i/1000;
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
