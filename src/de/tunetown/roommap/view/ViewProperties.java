package de.tunetown.roommap.view;

import java.awt.Color;

/**
 * Constants for UI
 * 
 * @author tweber
 *
 */
public class ViewProperties {
	public static final Color BGCOLOR = Color.WHITE;
	public static final Color FGCOLOR = Color.BLACK;
	public static final Color DATA_COLOR_NAN = Color.BLACK;
	public static final Color DATA_COLOR_POINTS = Color.DARK_GRAY;
	public static final Color GRIDCOLOR = new Color(Color.LIGHT_GRAY.getRed(), Color.LIGHT_GRAY.getGreen(), Color.LIGHT_GRAY.getBlue(), 100);
	
	public static final int POINTS_MIN_DIAMETER = 2;
	public static final int POINTS_MAX_DIAMETER = 8;
	
	public static final int INITIAL_WINDOW_SIZE = 800;             
	public static final double POINT_PROJECTION_DEPTH = 50;  
	public static final int MIN_ALPHA = 20;
	public static final int MAX_ALPHA = 255;
	public static final int FONT_SIZE = 10;
	public static final int LABEL_WIDTH = 20;
	
	public static final Color WAVELENGTH_CIRCLES_COLOR_HALF = Color.LIGHT_GRAY;
	public static final Color WAVELENGTH_CIRCLES_COLOR_QUARTER = Color.BLACK;
	public static final int WAVELENGTH_CIRCLES_POINT_DIAMETER = 4;

}
