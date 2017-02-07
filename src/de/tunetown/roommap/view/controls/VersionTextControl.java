package de.tunetown.roommap.view.controls;

import de.tunetown.roommap.main.Main;

/**
 * Shows the program version
 * 
 * @author tweber
 *
 */
public class VersionTextControl extends TextControl {
	private static final long serialVersionUID = 1L;
	
	public VersionTextControl(Main main, Controls parent) {
		super(parent, main);
	}

	@Override
	protected int getLabelWidth() {
		return 200;
	}

	@Override
	protected String getLabelText() {
		return "RoomMap (C) Tom Weber 2016/17, Version: " + getMain().getVersion();
	}

}
