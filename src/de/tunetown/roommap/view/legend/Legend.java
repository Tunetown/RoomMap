package de.tunetown.roommap.view.legend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import de.tunetown.roommap.main.Main;
import de.tunetown.roommap.view.data.OutputGraphics;

public class Legend extends JPanel {
	private static final long serialVersionUID = 1L;

	private Main main;
	
	// TODO constants
	private int legendWidth = 100;       // Width of legend panel
	private int margin = 10;             // Panel margin
	private int resolution = 10;         // Resolution of legend colors
	private int textResolution = 50;     // Text resolution (determines in which intervals the SPL is shown)
	private int textWidth = 25;          // Width of text area
	private int fontSize = 10;           // Font size
	
	public Legend(Main main) {
		this.main = main;
		
		this.setPreferredSize(new Dimension(legendWidth, 0));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);//TODO
		g.fillRect(0, 0, getWidth(), getHeight());
		
		OutputGraphics o = new OutputGraphics(main);

		double min = main.getMeasurements().getMinSpl(main.getFrequency());
		double max = main.getMeasurements().getMaxSpl(main.getFrequency());
		
		// Bars
		double viewMax = this.getHeight()-2*margin;
		int lineWidth = getWidth()-2*margin-textWidth;
		for(int y=margin; y<this.getHeight()-margin; y+=resolution) {
			double spl = ((double)(y - margin) / viewMax) * (max - min) + min;
			g.setColor(o.getOutColor(spl));
			g.fillRect(margin, y, lineWidth, resolution);
		}
		
		// Labels
		Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Helvetica", Font.PLAIN, fontSize)); 
		g2.setColor(Color.BLACK); // TODO
		DecimalFormat df = new DecimalFormat("#.##");
        for(int y=margin; y<this.getHeight()-margin; y+=textResolution) {
        	double spl = ((double)(y - margin) / viewMax) * (max - min) + min;
        	g2.drawString(df.format(spl), margin + lineWidth + 3, y + fontSize/2);
        }
	}
}
