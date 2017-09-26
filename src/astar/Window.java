package astar;

import javax.swing.JFrame;

public class Window extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Window(String title, int x, int y) {
		super(title);
		setSize(x, y);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
