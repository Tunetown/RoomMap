package de.tunetown.roommap.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import rainbowvis.Rainbow;
import de.tunetown.roommap.main.Main;
import de.tunetown.roommap.model.Measurement;;

/**
 * SPL distribution visualization panel
 * 
 * @author tweber
 *
 */
public class OutputGraphics extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	// TODO constants
	private int resolution = 20;
	private int maxSize = 800;
	private int pointDiameter = 20;

	public OutputGraphics(Main main) {
		this.main = main;
		
		Dimension dim = getDimension();
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);
	}

	private Dimension getDimension() {
		int w;
		int h;
		if (main.getMeasurements().getMaxX() > main.getMeasurements().getMaxY()) {
			w = maxSize;
			h = (int)((main.getMeasurements().getMaxY() / main.getMeasurements().getMaxX()) * maxSize);
		} else {
			w = (int)((main.getMeasurements().getMaxX() / main.getMeasurements().getMaxY()) * maxSize);
			h = maxSize;
		}
			
		return new Dimension(w, h);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);//TODO
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		paintData(g);
		paintPoints(g);
	}
	
	private void paintPoints(Graphics g) {
		g.setColor(Color.DARK_GRAY);//TODO

		for(Measurement m : main.getMeasurements().getMeasurements()) {
			int x = convertModelToViewX(m.getX());
			int y = convertModelToViewY(m.getY());

			g.fillOval(x - pointDiameter/2, y - pointDiameter/2, pointDiameter, pointDiameter);
		}
	}

	private void paintData(Graphics g) {
		double modelZ = main.getViewZ() * main.getMeasurements().getMaxZ();
		
		for(int x=0; x<this.getWidth(); x+=resolution) {
			for(int y=0; y<this.getHeight(); y+=resolution) {
				double rx = convertViewToModelX(x+resolution/2); 
				double ry = convertViewToModelY(y+resolution/2); 
				double spl = main.getMeasurements().getSpl(rx, ry, modelZ, main.getFrequency());
				
				g.setColor(getOutColor(spl));
				g.fillRect(x, y, resolution, resolution);
			}
		}
	}

	private Color getOutColor(double spl) {
		if (spl == Double.NaN) return Color.BLACK;//TODO
		
		double minSpl = main.getMeasurements().getMinSpl(main.getFrequency());
		double maxSpl = main.getMeasurements().getMaxSpl(main.getFrequency());

		double val = (spl - minSpl) / (maxSpl - minSpl);

		Rainbow rainbow = new Rainbow();
		return rainbow.colourAt(100 - (int)(val * 100));
	}
	
	private double convertViewToModelY(int y) {
		return ((double)y / this.getHeight()) * main.getMeasurements().getMaxY();
	}

	private double convertViewToModelX(int x) {
		return ((double)x / this.getWidth()) * main.getMeasurements().getMaxX();
	}

	private int convertModelToViewX(double x) {
		return (int)((x / main.getMeasurements().getMaxX()) * this.getWidth());
	}
	
	private int convertModelToViewY(double y) {
		return (int)((y / main.getMeasurements().getMaxY()) * this.getHeight());
	}
}
