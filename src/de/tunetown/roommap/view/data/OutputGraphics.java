package de.tunetown.roommap.view.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
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
	private double resolution = 0.2;       // Resolution (model units, not pixels!)
	private int maxSize = 800;             // Initial max. Size of data panel (pixels)
	private double projectionDepth = 100;  // Depth of 3d data point projection

	public OutputGraphics(Main main) {
		this.main = main;
		
		Dimension dim = getPaintDimension(maxSize, maxSize);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);
	}

	/**
	 * Returns the size for the drawing stage
	 *  
	 * @return
	 */
	private Dimension getPaintDimension() {
		// TODO buffer
		return getPaintDimension(getWidth(), getHeight());
	}
	
	/**
	 * As getPaintDimension(), but has inputs to set the view width/height desired
	 * 
	 * @param viewWidth
	 * @param viewHeight
	 * @return
	 */
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

	/**
	 * Paint method
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.WHITE);//TODO
		g.fillRect(0, 0, getWidth(), getHeight());
		
		if (main.getPooledInterpolation()) {
			paintDataPooled(g);
		} else {
			paintData(g);
		}
		paintPoints(g);
	}

	/**
	 * Paint data visualization
	 * 
	 * @param g
	 */
	private void paintData(Graphics g) {
		double modelZ = main.getViewZ();
		int resY = convertModelToViewX(resolution);
		int resX = convertModelToViewX(resolution);
		Dimension d = getPaintDimension();

		for(int x=0; x<d.getWidth()+resX/2; x+=resX) {
			double x1 = convertViewToModelX(x);
			for(int y=0; y<d.getHeight()+resY/2; y+=resY) {
				double rx = x1 - main.getMargin() + main.getMeasurements().getMinX(); 
				double ry = convertViewToModelY(y) - main.getMargin() + main.getMeasurements().getMinY();
				
				double spl = main.getMeasurements().getSpl(rx, ry, modelZ, main.getFrequency());
				
				g.setColor(getOutColor(spl));
				g.fillRect(x - resX/2, y - resY/2, resX, resY);
			}
		}
	}

	/**
	 * Paint data visualization (multithreaded)
	 * 
	 * @param g
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void paintDataPooled(Graphics g) {
		int resX = convertModelToViewX(resolution);
		Dimension d = getPaintDimension();

		// Create new thread pool
		List<Future> futures = new ArrayList<Future>();
		ExecutorService pool = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors() - 2);
		
		// Add workers to the pool
		for(int x=0; x<d.getWidth()+resX/2; x+=resX) {
			futures.add(pool.submit(new PaintExecutor(this, x)));
		}

		try {
			// Wait until the work is done
			pool.shutdown();
			if (!pool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS)) {
				// Error
				System.out.println("Error: Threads not terminated correctly.");
				System.exit(8);
			}
			
			// Get thread results and paint them
			for(Future f : futures) {
				ArrayList<PaintBuffer> buffers = (ArrayList<PaintBuffer>)(f.get());
				for(PaintBuffer buf : buffers) {
					g.setColor(buf.getColor());
					g.fillRect(buf.getX(), buf.getY(), buf.getW(), buf.getH());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<PaintBuffer> calculateX(int x) {
		List<PaintBuffer> ret = new ArrayList<PaintBuffer>();
		
		double modelZ = main.getViewZ();
		Dimension d = getPaintDimension();
		int resX = convertModelToViewX(resolution);
		int resY = convertModelToViewX(resolution);
		double x1 = convertViewToModelX(x);

		for(int y=0; y<d.getHeight()+resY/2; y+=resY) {
			double rx = x1 - main.getMargin() + main.getMeasurements().getMinX(); 
			double ry = convertViewToModelY(y) - main.getMargin() + main.getMeasurements().getMinY();
			
			double spl = main.getMeasurements().getSpl(rx, ry, modelZ, main.getFrequency());
			
			ret.add(new PaintBuffer(getOutColor(spl), x - resX/2, y - resY/2, resX, resY));
		}
		return ret;
	}
	
	/**
	 * Paint points visualization
	 * 
	 * @param g
	 */
	private void paintPoints(Graphics g) {
		int diaX = convertModelToViewX(resolution);
		int diaY = convertModelToViewY(resolution);
		
		for(Measurement m : main.getMeasurements().getMeasurements()) {
			double z = main.getViewZ() - m.getZ(); 
			int x = getProjectionX(convertModelToViewX(m.getX() + main.getMargin() - main.getMeasurements().getMinX()), z);
			int y = getProjectionY(convertModelToViewY(m.getY() + main.getMargin() - main.getMeasurements().getMinY()), z);
			
			g.setColor(new Color(0, 0, 0, getAlpha(z)));
			g.fillOval(x - diaX/2, y - diaY/2, diaX, diaY);
		}
	}

	/**
	 * Simple 3d projection of room data points
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	private int getProjectionX(int x, double z) {
		if (!main.getPointProjection()) return x; 
		Dimension d = getPaintDimension();
		int x0 = x - (int)(d.getWidth() / 2);
		x0 = x0 - (int)(z * x0 / projectionDepth) ;
		return x0 + (int)(d.getWidth() / 2);
	}
	
	/**
	 * Simple 3d projection of room data points
	 * 
	 * @param y
	 * @param z
	 * @return
	 */
	private int getProjectionY(int y, double z) {
		if (!main.getPointProjection()) return y;
		Dimension d = getPaintDimension();
		int y0 = y - (int)(d.getHeight() / 2);
		y0 = y0 - (int)(z * y0 / projectionDepth) ;
		return y0 + (int)(d.getHeight() / 2);
	}

	/**
	 * Alpha value for 3d projection
	 * 
	 * @param z
	 * @return
	 */
	private int getAlpha(double z) {
		if (!main.getPointProjection()) return 255;
		int minAlpha = 30;
		int maxAlpha = 255;

		if (Math.abs(z) <= 0.5) {
			return (int)(minAlpha + (maxAlpha - minAlpha) * (0.5 - Math.abs(z)) * 2);
		} else {
			return minAlpha;
		}
	}

	/**
	 * Get visualization color for a given SPL level
	 * 
	 * @param spl
	 * @return
	 */
	public Color getOutColor(double spl) {
		if (Double.isNaN(spl)) return Color.BLACK;//TODO
		
		double minSpl;
		double maxSpl;
		
		if (main.getNormalizeByFrequency()){ 
			minSpl = main.getMeasurements().getMinSpl(main.getFrequency());
			maxSpl = main.getMeasurements().getMaxSpl(main.getFrequency());
		} else {
			minSpl = main.getMeasurements().getMinSpl();
			maxSpl = main.getMeasurements().getMaxSpl();
		}

		double val = (spl - minSpl) / (maxSpl - minSpl);

		Rainbow rainbow = new Rainbow();
		return rainbow.colourAt(100 - (int)(val * 100));
	}
	
	/**
	 * Coordinate conversion
	 * 
	 * @param x
	 * @return
	 */
	private double convertViewToModelX(int x) {
		return ((double)x / getPaintDimension().getWidth()) * (main.getMeasurements().getMaxX() - main.getMeasurements().getMinX() + 2*main.getMargin());
	}

	/**
	 * Coordinate conversion
	 * 
	 * @param y
	 * @return
	 */
	private double convertViewToModelY(int y) {
		return ((double)y / getPaintDimension().getHeight()) * (main.getMeasurements().getMaxY() - main.getMeasurements().getMinY() + 2*main.getMargin());
	}

	/**
	 * Coordinate conversion
	 * 
	 * @param x
	 * @return
	 */
	private int convertModelToViewX(double x) {
		return (int)((x / (main.getMeasurements().getMaxX() - main.getMeasurements().getMinX() + 2*main.getMargin())) * getPaintDimension().getWidth());
	}
	
	/**
	 * Coordinate conversion
	 * 
	 * @param y
	 * @return
	 */
	private int convertModelToViewY(double y) {
		return (int)((y / (main.getMeasurements().getMaxY() - main.getMeasurements().getMinY() + 2*main.getMargin())) * getPaintDimension().getHeight());
	}
}
