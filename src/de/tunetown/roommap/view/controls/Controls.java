package de.tunetown.roommap.view.controls;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

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
	
	private Control freqControl;
	private Control heightControl;
	private Control marginControl;
	private Control resolutionControl;
	private JCheckBox normalizeToFreqSwitch;
	private JCheckBox projectionOfPointsSwitch;
	private JCheckBox pooledInterpolationSwitch;
	private JCheckBox showGridSwitch;
	
	public Controls(Main main) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		this.main = main;
		
		init();
	}

	/**
	 * Initialize all controls
	 */
	private void init() {
		freqControl = new FrequencyControl(main, this);
		add(freqControl);

		heightControl = new HeightControl(main, this);
		add(heightControl);
		
		marginControl = new MarginControl(main, this);
		add(marginControl);

		resolutionControl = new ResolutionControl(main, this);
		add(resolutionControl);

		normalizeToFreqSwitch = new JCheckBox("Normalize SPL range for selected frequency");
		normalizeToFreqSwitch.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				main.setNormalizeByFrequency(e.getStateChange() == ItemEvent.SELECTED);
				main.repaint();
			}
		});
		add(normalizeToFreqSwitch);

		showGridSwitch = new JCheckBox("Show grid");
		showGridSwitch.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				main.setShowGrid(e.getStateChange() == ItemEvent.SELECTED);
				main.repaint();
			}
		});
		add(showGridSwitch);

		projectionOfPointsSwitch = new JCheckBox("3D projection of data points");
		projectionOfPointsSwitch.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				main.setPointProjection(e.getStateChange() == ItemEvent.SELECTED);
				main.repaint();
			}
		});
		add(projectionOfPointsSwitch);

		pooledInterpolationSwitch = new JCheckBox("Multithreading (interpolation)");
		pooledInterpolationSwitch.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				main.setPooledInterpolation(e.getStateChange() == ItemEvent.SELECTED);
				main.repaint();
			}
		});
		add(pooledInterpolationSwitch);

		// Initial update. This sets the initial values of all controls
		updateControls();
	}
	
	/**
	 * Update all controls
	 */
	public void updateControls() {
		if (freqControl != null) freqControl.update();
		if (marginControl != null) marginControl.update();
		if (heightControl != null) heightControl.update();
		if (normalizeToFreqSwitch != null) normalizeToFreqSwitch.setSelected(main.getNormalizeByFrequency());
		if (projectionOfPointsSwitch != null) projectionOfPointsSwitch.setSelected(main.getPointProjection());
		if (pooledInterpolationSwitch != null) pooledInterpolationSwitch.setSelected(main.getPooledInterpolation());
		if (showGridSwitch != null) showGridSwitch.setSelected(main.getShowGrid());
	}
}
