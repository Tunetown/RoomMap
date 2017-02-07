package de.tunetown.roommap.view.data;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import rainbowvis.Rainbow;
import de.tunetown.roommap.main.Main;
import de.tunetown.roommap.main.ThreadManagement;
import de.tunetown.roommap.model.Measurement;
import de.tunetown.roommap.view.ViewProperties;

/**
 * SPL distribution visualization panel
 * 
 * @author tweber
 *
 */
public class OutputGraphics extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	
	private int mouseX = 0;
	private int mouseY = 0;
	
	public OutputGraphics(Main main) {
		this.main = main;
		
		Dimension dim = getPaintDimension(ViewProperties.INITIAL_WINDOW_SIZE, ViewProperties.INITIAL_WINDOW_SIZE);
		this.setPreferredSize(dim);
		this.setMinimumSize(dim);

		addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseClicked(MouseEvent e) {
		        if (e.getButton() == MouseEvent.BUTTON1) {
		        	setWavelengthAnchor(e.getX(), e.getY());
		        }
		    }
		});
	}

	/**
	 * This sets the wavelength visualization circle center to the given point.
	 * 
	 * @param x
	 * @param y
	 */
	protected void setWavelengthAnchor(int x, int y) {
		this.mouseX = x;
		this.mouseY = y;
		
		if (!main.getShowWavelength()) {
			main.setShowWavelength(true);
			//main.updateControlValues();  // TODO update checkbox
			main.repaint();
		} else {
			repaint();
		}	
	}

	/**
	 * Returns the size for the drawing stage
	 *  
	 * @return
	 */
	private Dimension getPaintDimension() {
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
			w = viewWidth - ViewProperties.LABEL_WIDTH;
			h = (int)(w / modelRatio);
		} else {
			h = viewHeight - ViewProperties.LABEL_WIDTH;
			w = (int)(h * modelRatio);
		}
		return new Dimension(w, h);
	}

	/**
	 * Main paint method
	 * 
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(ViewProperties.BGCOLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		ThreadManagement m = new ThreadManagement();
		
		// Interpolated data
		if (main.getPooledInterpolation() && m.getNumOfProcessorsInterpolation() > 1) {
			paintDataPooled(g);
		} else {
			paintData(g);
		}
		
		// Axis labels and grid
		paintAxisLabels(g);
		
		// Data points
		paintPoints(g);
		
		// Wavelength circles
		paintWavelengthCircles(g);
	}

	/**
	 * Paint the wavelength circles if enabled. These circles are drawn with radius of 1/4 wavelength,
	 * 2/4, 3/4 and full wavelength to visualize the waveform properties roughly. 
	 * 
	 * @param g
	 */
	private void paintWavelengthCircles(Graphics g) {
		if (!main.getShowWavelength()) return;
		if (mouseX == -1 || mouseY == -1) return;
		
		int radX = this.convertModelToViewX(main.getMeasurements().getWavelength(main.getFrequency()));
		int radY = this.convertModelToViewY(main.getMeasurements().getWavelength(main.getFrequency()));
		
		g.setColor(ViewProperties.WAVELENGTH_CIRCLES_COLOR_HALF); 
		g.fillOval(mouseX, mouseY, ViewProperties.WAVELENGTH_CIRCLES_POINT_DIAMETER, ViewProperties.WAVELENGTH_CIRCLES_POINT_DIAMETER); 
		
		g.setColor(ViewProperties.WAVELENGTH_CIRCLES_COLOR_QUARTER); 
		g.drawOval(mouseX - radX/4, mouseY - radY/4, 2*radX/4, 2*radY/4);
		
		g.setColor(ViewProperties.WAVELENGTH_CIRCLES_COLOR_HALF); 
		g.drawOval(mouseX - radX/2, mouseY - radY/2, 2*radX/2, 2*radY/2);
		
		g.setColor(ViewProperties.WAVELENGTH_CIRCLES_COLOR_QUARTER);
		g.drawOval(mouseX - 3*radX/4, mouseY - 3*radY/4, 2*3*radX/4, 2*3*radY/4);

		g.setColor(ViewProperties.WAVELENGTH_CIRCLES_COLOR_HALF);
		g.drawOval(mouseX - radX, mouseY - radY, 2*radX, 2*radY);
	}

	/**
	 * Paints the axis labels and grid
	 * 
	 * @param g
	 */
	private void paintAxisLabels(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(new Font("Helvetica", Font.PLAIN, ViewProperties.FONT_SIZE)); 
        DecimalFormat df = new DecimalFormat("#.##");
        double margin = main.getMargin();
        double res = 1;
        
        if (main.getMeasurements().getMaxX() - main.getMeasurements().getMinX() >= 20) res = 2; 
        if (main.getMeasurements().getMaxX() - main.getMeasurements().getMinX() >= 100) res = 10; 
        int vy = getHeight() - ViewProperties.FONT_SIZE - 2;
        double x = new Double((int)main.getMeasurements().getMinX());
        while(x <= main.getMeasurements().getMaxX()) {
			int vx = convertModelToViewX(x + margin - main.getMeasurements().getMinX());
			
			g2.setColor(ViewProperties.FGCOLOR);
        	g2.drawString(df.format(x), vx - ViewProperties.FONT_SIZE/4, vy + ViewProperties.FONT_SIZE);
        	
        	if (main.getShowGrid()) {
	        	g2.setColor(ViewProperties.GRIDCOLOR);
	        	g2.drawLine(vx, 0, vx, vy - 2);
        	}
        	
        	x+=res;
        }

        res = 1;
        if (main.getMeasurements().getMaxY() - main.getMeasurements().getMinY() >= 20) res = 2; 
        if (main.getMeasurements().getMaxY() - main.getMeasurements().getMinY() >= 100) res = 10; 
        int vx = getWidth() - ViewProperties.FONT_SIZE/4 - 2;
        double y = new Double((int)main.getMeasurements().getMinY());
        while(y <= main.getMeasurements().getMaxY()) {
        	int vy2 = convertModelToViewY(y + margin - main.getMeasurements().getMinY());
        	
        	g2.setColor(ViewProperties.FGCOLOR);
        	g2.drawString(df.format(y), vx - ViewProperties.FONT_SIZE/4, vy2 + ViewProperties.FONT_SIZE/2);
        	
        	if (main.getShowGrid()) {
	        	g2.setColor(ViewProperties.GRIDCOLOR);
	        	g2.drawLine(0, vy2, vx - 2, vy2);
        	}
        	
        	y+=res;
        }
        
        g2.setColor(ViewProperties.FGCOLOR);
    	g2.drawString("m", getWidth() - ViewProperties.FONT_SIZE - 2, getHeight() - ViewProperties.FONT_SIZE - 2);
	}

	/**
	 * Paint data visualization, non-pooled. The same algorithm is also contained in calculateX, which is used
	 * when pooled interpolation is enabled. WHEN CHANGING THIS, YOU ALSO MUST CHANGE calculateX()!
	 * 
	 * @param g
	 */
	private void paintData(Graphics g) {
		double modelZ = main.getViewZ();
		int resY = convertModelToViewX(main.getResolution());
		int resX = convertModelToViewX(main.getResolution());
		Dimension d = getPaintDimension();

		for(int x=0; x<d.getWidth()+resX/2; x+=resX) {
			double x1 = convertViewToModelX(x);
			for(int y=0; y<d.getHeight()+resY/2; y+=resY) {
				double y1 = convertViewToModelY(y);

				double rx = x1 - main.getMargin() + main.getMeasurements().getMinX(); 
				double ry = y1 - main.getMargin() + main.getMeasurements().getMinY();
				double rel;
				if (main.getHideSenselessData()) {
					rel = main.getMeasurements().getPointAccuracy(rx, ry, modelZ, main.getFrequency());
				} else {
					rel = 1;
				}
				if (rel <= 0) continue; 
				
				double spl = main.getMeasurements().getSpl(rx, ry, modelZ, main.getFrequency());
				
				g.setColor(getOutColor(spl, rel));
				g.fillRect(x - resX/2, y - resY/2, resX, resY);
			}
		}
	}
	
	/**
	 * Calculation for pooled interpolation. The same algorithm is also contained in paintData, which is used
	 * when pooled interpolation is disabled. WHEN CHANGING THIS, YOU ALSO MUST CHANGE paintData()!
	 * 
	 * @param x
	 * @return
	 */
	public List<PaintBuffer> calculateX(int x) {
		List<PaintBuffer> ret = new ArrayList<PaintBuffer>();
		
		double modelZ = main.getViewZ();
		Dimension d = getPaintDimension();
		int resX = convertModelToViewX(main.getResolution());
		int resY = convertModelToViewX(main.getResolution());
		double x1 = convertViewToModelX(x);

		for(int y=0; y<d.getHeight()+resY/2; y+=resY) {
			double y1 = convertViewToModelY(y);

			double rx = x1 - main.getMargin() + main.getMeasurements().getMinX();
			double ry = y1 - main.getMargin() + main.getMeasurements().getMinY();
			double rel;
			if (main.getHideSenselessData()) {
				rel = main.getMeasurements().getPointAccuracy(rx, ry, modelZ, main.getFrequency());
			} else {
				rel = 1;
			}
			if (rel <= 0) continue;
			
			double spl = main.getMeasurements().getSpl(rx, ry, modelZ, main.getFrequency());

			ret.add(new PaintBuffer(getOutColor(spl, rel), x - resX/2, y - resY/2, resX, resY));
		}
		return ret;
	}

	/**
	 * Paint data visualization (multithreaded)
	 * 
	 * @param g
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void paintDataPooled(Graphics g) {
		int resX = convertModelToViewX(main.getResolution());
		Dimension d = getPaintDimension();

		// Stop interpolator precalculation
		main.stopPrecalculation();
		
		// Create new thread pool
		List<Future> futures = new ArrayList<Future>();
		ThreadManagement m = new ThreadManagement();
		ExecutorService pool = Executors.newWorkStealingPool(m.getNumOfProcessorsInterpolation());
		
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
		main.resumePrecalculation();
	}

	/**
	 * Paint data points visualization
	 * 
	 * @param g
	 */
	private void paintPoints(Graphics g) {
		int diaX = convertModelToViewX(main.getResolution()/2);
		int diaY = convertModelToViewY(main.getResolution()/2);
		if (diaX < ViewProperties.POINTS_MIN_DIAMETER) diaX = ViewProperties.POINTS_MIN_DIAMETER;
		if (diaX > ViewProperties.POINTS_MAX_DIAMETER) diaX = ViewProperties.POINTS_MAX_DIAMETER;
		if (diaY < ViewProperties.POINTS_MIN_DIAMETER) diaY = ViewProperties.POINTS_MIN_DIAMETER;
		if (diaY > ViewProperties.POINTS_MAX_DIAMETER) diaY = ViewProperties.POINTS_MAX_DIAMETER;
		
		for(Measurement m : main.getMeasurements().getMeasurements()) {
			double z = main.getViewZ() - m.getZ(); 
			int x = getProjectionX(convertModelToViewX(m.getX() + main.getMargin() - main.getMeasurements().getMinX()), z);
			int y = getProjectionY(convertModelToViewY(m.getY() + main.getMargin() - main.getMeasurements().getMinY()), z);
			
			Color c = ViewProperties.DATA_COLOR_POINTS;
			g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), getAlpha(z)));
			g.fillOval(x - diaX/2, y - diaY/2, diaX, diaY);
		}
	}

	/**
	 * Simple 3d projection of room data points (X)
	 * 
	 * @param x
	 * @param z
	 * @return
	 */
	private int getProjectionX(int x, double z) {
		if (!main.getPointProjection()) return x; 
		Dimension d = getPaintDimension();
		int x0 = x - (int)(d.getWidth() / 2);
		x0 = x0 - (int)(z * x0 / ViewProperties.POINT_PROJECTION_DEPTH) ;
		return x0 + (int)(d.getWidth() / 2);
	}
	
	/**
	 * Simple 3d projection of room data points (Y)
	 * 
	 * @param y
	 * @param z
	 * @return
	 */
	private int getProjectionY(int y, double z) {
		if (!main.getPointProjection()) return y;
		Dimension d = getPaintDimension();
		int y0 = y - (int)(d.getHeight() / 2);
		y0 = y0 - (int)(z * y0 / ViewProperties.POINT_PROJECTION_DEPTH) ;
		return y0 + (int)(d.getHeight() / 2);
	}

	/**
	 * Alpha value for simple 3d projection
	 * 
	 * @param z
	 * @return
	 */
	private int getAlpha(double z) {
		if (Math.abs(z) <= 0.5) {
			return (int)(ViewProperties.MIN_ALPHA + (ViewProperties.MAX_ALPHA - ViewProperties.MIN_ALPHA) * (0.5 - Math.abs(z)) * 2);
		} else {
			return ViewProperties.MIN_ALPHA;
		}
	}

	/**
	 * Get visualization color for a given SPL level
	 * 
	 * @param spl
	 * @return
	 */
	public Color getOutColor(double spl, double alpha) {
		if (Double.isNaN(spl)) return ViewProperties.DATA_COLOR_NAN;
		
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
		Color ret = rainbow.colourAt(100 - (int)(val * 100));
		return new Color(ret.getRed(), ret.getGreen(), ret.getBlue(), (int)(alpha*255)); // TODO protect alpha overflow? Test...
	}
	
	/**
	 * Coordinate conversion UI to model (X)
	 * 
	 * @param x
	 * @return
	 */
	private double convertViewToModelX(int x) {
		return ((double)x / getPaintDimension().getWidth()) * (main.getMeasurements().getMaxX() - main.getMeasurements().getMinX() + 2*main.getMargin());
	}

	/**
	 * Coordinate conversion UI to model (Y)
	 * 
	 * @param y
	 * @return
	 */
	private double convertViewToModelY(int y) {
		return ((double)y / getPaintDimension().getHeight()) * (main.getMeasurements().getMaxY() - main.getMeasurements().getMinY() + 2*main.getMargin());
	}

	/**
	 * Coordinate conversion model to UI (X)
	 * 
	 * @param x
	 * @return
	 */
	private int convertModelToViewX(double x) {
		return (int)((x / (main.getMeasurements().getMaxX() - main.getMeasurements().getMinX() + 2*main.getMargin())) * getPaintDimension().getWidth());
	}
	
	/**
	 * Coordinate conversion model to UI (Y)
	 * 
	 * @param y
	 * @return
	 */
	private int convertModelToViewY(double y) {
		return (int)((y / (main.getMeasurements().getMaxY() - main.getMeasurements().getMinY() + 2*main.getMargin())) * getPaintDimension().getHeight());
	}
}
