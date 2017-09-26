package astar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.List;

import javax.swing.JPanel;

public class DrawingPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public DrawingPanel() {
		setBackground(Color.WHITE);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setStroke(new BasicStroke(2));
		for (int i=0; i < Test.cities.size(); i++){
			g2.fillOval((int) Test.cities.get(i).getX() -2, (int) Test.cities.get(i).getY() -2, 4, 4);
		}
		for (int i=0; i < Test.roads.size(); i++) {
			g2.setColor(Test.roads.get(i).getColor());
			g2.draw(Test.roads.get(i));
		}
	}
}