package de.tunetown.roommap.view.data;

import java.awt.Color;

/**
 * Buffer class to store results of multithreaded interpolation
 *  
 * @author tweber
 *
 */
public class PaintBuffer {
	
	private Color color;
	private int x;
	private int y;
	private int w;
	private int h;
	
	public PaintBuffer(Color color, int x, int y, int w, int h) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Color getColor() {
		return color;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getW() {
		return w;
	}

	public int getH() {
		return h;
	}
}
