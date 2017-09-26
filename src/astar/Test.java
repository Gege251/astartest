package astar;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class Test {
	public static List<PointX> cities = new ArrayList<>();
	public static List<LineX> roads = new ArrayList<>();

	public static void main(String[] args) {
		final int windowWidth	= 1200;
		final int windowHeight	= 700;
		final int citiesCount	= 1500;
		final int plusRoads		= 400;
		
//		final int timer		   = 1000;

		DrawingPanel panel = new DrawingPanel();
		JFrame drawFrame = new JFrame("Map");
		drawFrame.setSize(windowWidth, windowHeight);
		drawFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		drawFrame.add(panel);
		drawFrame.setVisible(true);
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				drawFrame.repaint();
			}
		}, 10, 10);
		
		panel.addMouseListener(new MouseAdapter() {
			PointX start = null;
			PointX goal = null;
			List<LineX> aStarResult = new ArrayList<>();
			
			@Override
			public void mouseClicked(MouseEvent e) {
				int mouseX = e.getX();
				int mouseY = e.getY();
				Point2D mousePointer = new Point2D.Double(mouseX, mouseY);
					
				PointX closest = MapCreator.searchClosestCity(mousePointer, cities);
				
				if (start == null) {
					this.start = closest;
				} else if (goal == null) {
					Test.roads.removeAll(this.aStarResult);
					goal = closest;
					this.aStarResult = AStar.getShortestPath(roads, cities, this.start, this.goal);
					Test.roads.addAll(this.aStarResult);
					this.start = null;
					this.goal = null;
				}
				
			}
			
		});

		MapCreator.createCities(cities, citiesCount, windowWidth, windowHeight);
		MapCreator.createRoads(roads, cities, plusRoads);
//		Test.roads.addAll(AStar.getShortestPath(roads, cities, cities.get(start), cities.get(goal)));
	
	}

}


