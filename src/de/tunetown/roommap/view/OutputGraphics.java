package de.tunetown.roommap.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class OutputGraphics extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainPanel mainPanel;
	
	public OutputGraphics(MainPanel mainPanel) {
		this.mainPanel = mainPanel;
		
		Dimension dim = new Dimension(500, 500);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.yellow);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	}
}
