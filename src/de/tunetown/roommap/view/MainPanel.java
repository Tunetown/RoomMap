package de.tunetown.roommap.view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.tunetown.roommap.main.Main;
import de.tunetown.roommap.view.controls.Controls;
import de.tunetown.roommap.view.data.OutputGraphics;
import de.tunetown.roommap.view.legend.Legend;

/**
 * Main UI panel, holding all components
 * 
 * @author xwebert
 *
 */
public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private Main main;
	
	private Controls controls;
	private OutputGraphics graphics;
	private Legend legend;

	public MainPanel(Main main) {
		super(new BorderLayout(3,3));
		this.main = main;
		init();
	}
	
	private void init() {
		controls = new Controls(main);
		add(controls, BorderLayout.EAST);
		
		graphics = new OutputGraphics(main);
		add(graphics, BorderLayout.CENTER);
		
		legend = new Legend(main);
		add(legend, BorderLayout.WEST);
	}
	
	public Controls getControls() {
		return controls;
	}
}
