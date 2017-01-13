package de.tunetown.roommap.view;

import javax.swing.JFrame;
import de.tunetown.roommap.main.Main;

/**
 * Main frame for the application
 * 
 * @author xwebert
 *
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private Main main;
	private MainPanel mainPanel;
	
	public MainFrame(Main main) {
		super("RoomMap");
		this.main = main;
	}
	
	public void init() {
		mainPanel = new MainPanel(main, this);
		
		// Add this main GUI instance to the main frame (this contains all elements)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(mainPanel);

		// Do some size and location stuff
		pack();
		setLocationByPlatform(true);
		setVisible(true);
		/* TODO
		JFrame wrapper = this;
		addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizePreview(mainPanel, wrapper);
            }
        });
        */
	}
	
	public MainPanel getMainPanel() {
		return mainPanel;
	}
	
	/*
	private static void resizePreview(JPanel innerPanel, JFrame container) {
        int w = container.getWidth();
        int h = container.getHeight();
        System.out.println(2);
        int size =  Math.min(w, h);
        innerPanel.setPreferredSize(new Dimension(size, size));
        container.revalidate();
    }*/
}
