package de.tunetown.roommap.view;

import java.text.DecimalFormat;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Controls extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private MainPanel mainPanel;
	private JSlider heightSlider;
	
	public Controls(MainPanel mainPanel) {
		super();
		this.mainPanel = mainPanel;
		
		init();
	}

	private void init() {
		heightSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 500);
		Hashtable labelTable = new Hashtable();
		DecimalFormat df = new DecimalFormat("#.##");
		for(int i=0; i<=1000; i+=200) {
			double value = (double)i/1000;
			labelTable.put(i, new JLabel(df.format(value)));
		}
		heightSlider.setLabelTable( labelTable );
		heightSlider.setPaintLabels(true);
		heightSlider.setMinorTickSpacing(200);
		heightSlider.setPaintTicks(true);
		
		heightSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				int value = ((JSlider)e.getSource()).getValue();
				setRoomHeight(value);
			}
		});
		add(heightSlider);
	}
	
	public void setRoomHeight(double h) {
		
	}
}
