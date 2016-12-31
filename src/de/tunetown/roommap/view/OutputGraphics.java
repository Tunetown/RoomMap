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
	private double resolution = 0.1;  // Resolution (model units, not pixels!)
	private int maxSize = 800;        // Initial max. Size of data panel (pixels)
	private int gridWidth = 3;        // Grid width (pixels)

	public OutputGraphics(Main main) {
		this.main = main;
		
		Dimension dim = getPaintDimension(maxSize, maxSize);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);
	}

	private Dimension getPaintDimension() {
		return getPaintDimension(getWidth(), getHeight());
	}
	
	private Dimension getPaintDimension(int viewWidth, int viewHeight) {
		double modelSizeX = main.getMeasurements().getMaxX() - main.getMeasurements().getMinX() + 2*main.getMargin();
		double modelSizeY = main.getMeasurements().getMaxY() - main.getMeasurements().getMinY() + 2*main.getMargin();
		double modelRatio = modelSizeX / modelSizeY;
		double panelRatio = (double)viewWidth / (double)viewHeight;

		int w;
		int h;
		if (modelRatio > panelRatio) {
			w = viewWidth;
			h = (int)(w / modelRatio);
		} else {
			h = viewHeight;
			w = (int)(h * modelRatio);
		}
		return new Dimension(w, h);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);//TODO
		g.fillRect(0, 0, getWidth(), getHeight());
		
		paintData(g);
		paintGrid(g);
		paintPoints(g);
	}
	
	private void paintGrid(Graphics g) {
		g.setColor(Color.LIGHT_GRAY); // TODO
	}

	private void paintPoints(Graphics g) {
		g.setColor(Color.DARK_GRAY);//TODO

		for(Measurement m : main.getMeasurements().getMeasurements()) {
			int x = convertModelToViewX(m.getX() + main.getMargin() - main.getMeasurements().getMinX());
			int y = convertModelToViewY(m.getY() + main.getMargin() - main.getMeasurements().getMinY());
			// TODO optimize
			int diaX = convertModelToViewX(resolution);
			int diaY = convertModelToViewY(resolution);
			g.fillOval(x - diaX/2, y - diaY/2, diaX, diaY);
		}
	}

	private void paintData(Graphics g) {
		double modelZ = main.getViewZ();
		
		Dimension d = getPaintDimension();
		int resX = convertModelToViewX(resolution);
		int resY = convertModelToViewX(resolution);
		
		for(int x=0; x<d.getWidth()+resX/2; x+=resX) {
			for(int y=0; y<d.getHeight()+resY/2; y+=resY) {
				double rx = convertViewToModelX(x) - main.getMargin() + main.getMeasurements().getMinX(); 
				double ry = convertViewToModelY(y) - main.getMargin() + main.getMeasurements().getMinY();
				
				double spl = main.getMeasurements().getSpl(rx, ry, modelZ, main.getFrequency());
				
				g.setColor(getOutColor(spl));
				g.fillRect(x - resX/2, y - resY/2, resX, resY);
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
	
	private double convertViewToModelX(int x) {
		return ((double)x / getPaintDimension().getWidth()) * (main.getMeasurements().getMaxX() - main.getMeasurements().getMinX() + 2*main.getMargin());
	}

	private double convertViewToModelY(int y) {
		return ((double)y / getPaintDimension().getHeight()) * (main.getMeasurements().getMaxY() - main.getMeasurements().getMinY() + 2*main.getMargin());
	}

	private int convertModelToViewX(double x) {
		return (int)((x / (main.getMeasurements().getMaxX() - main.getMeasurements().getMinX() + 2*main.getMargin())) * getPaintDimension().getWidth());
	}
	
	private int convertModelToViewY(double y) {
		return (int)((y / (main.getMeasurements().getMaxY() - main.getMeasurements().getMinY() + 2*main.getMargin())) * getPaintDimension().getHeight());
	}
}
