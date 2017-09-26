package astar;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

class MapCreator {
	
	public static void createCities(List<PointX> cities, int numOfCities, int xMax, int yMax) {
		Random rnd = new Random();
		
		for (int i=0; i < numOfCities; i++) {
			int x = rnd.nextInt(xMax-40)+20;
			int y = rnd.nextInt(yMax-60)+20;
			
			cities.add(new PointX(x,y));
		}
		
		Collections.sort(cities, (e1, e2) -> { 
		        if(e1.getX()+e1.getY() > e2.getX()+e2.getY()) {
		            return 1;
		        } else {
		            return -1;
		        }
		});
		
	}
	
	
	public static void createRoads(List<LineX> roads, List<PointX> cities, int plusRoads) {
		Random rnd = new Random();
		List<Integer> p1list = new LinkedList<>();
		List<List<Integer>> p2list = new LinkedList<>();
		
		
		// p1listとp2listを初期化する（すべてのポイントからすべてのポイントへの繋がり）
		for (int i=0; i < cities.size(); i++) {
			p1list.add(i);
			p2list.add(new LinkedList<Integer>());
			for (int j=0; j < cities.size(); j++){
				p2list.get(i).add(j);
			}
		}
		
		int p1;
		int p2;
		double distance;
		double distMin;
		int minDistCity = 1;
		
		for (int i=0; i < cities.size()-1; i++) {
			distMin = Double.POSITIVE_INFINITY;
			for (int j=i+1; j < cities.size(); j++) {
				distance = cities.get(i).distance(cities.get(j));
				if (distance < distMin) {
					distMin = distance;
					minDistCity = j;
				}
			}
			PointX cityA = cities.get(i);
			PointX cityB = cities.get(minDistCity);
			
			roads.add(new LineX(cityA, cityB));
			cityA.addNeighbour(cityB);
			cityB.addNeighbour(cityA);
			
			p2list.get(i).remove(new Integer(minDistCity));
			p2list.get(minDistCity).remove(new Integer(i));
			
		}
		
		
		// random routes
		for (int i=0; i < plusRoads; i++) {
			boolean canCreate = false;
			LineX newRoad = null;	
			
			while (!p1list.isEmpty() && !canCreate) {
				
				// set a p1 point
				int rand1 = rnd.nextInt(p1list.size());
				
				// Find a random valid path from p1
				while (!p2list.get(rand1).isEmpty() && !canCreate) {
					// set a p2 point
					int rand2 = rnd.nextInt(p2list.get(rand1).size());

					p1 = p1list.get(rand1);
					p2 = p2list.get(rand1).get(rand2);
					
					newRoad = new LineX(cities.get(p1), cities.get(p2));
					
					p2list.get(rand1).remove(rand2);
					p2list.get(rand1).remove(new Integer(p1));
					
					// create new road if doesn't intersect another road
					if (!newRoad.intersectsLineX(roads)) {
						roads.add(newRoad);
						cities.get(p1).addNeighbour(cities.get(p2));
						cities.get(p2).addNeighbour(cities.get(p1));
						canCreate = true;
					}
					
				}
				
				// remove unnecessary elements
				for (int j=0; j < p2list.size(); j++) {	
					if (p2list.get(j).isEmpty()) {
						p1list.remove(j);
						p2list.remove(j);
					}
				}
				
			}
		}
		
	}
	
	public static PointX searchClosestCity(Point2D location, List<PointX> cities) {
		double minDistance	= Double.POSITIVE_INFINITY;
		double distance		= 0;
		PointX minDistCity	= null;
		
		for (PointX city : cities) {
			distance = city.distance(location);
			if (distance < minDistance) {
				minDistance = distance;
				minDistCity = city;
			}
		}
		
		return minDistCity;
		
	}
	
	
}

class PointX extends Point2D.Double {
	private static final long serialVersionUID = 1L;
	
	private Set<PointX> neighbours = new HashSet<>(5);
	
	public PointX(int x, int y) {
		super(x, y);
	}
	
	public void addNeighbour(PointX neighbour) {
		this.neighbours.add(neighbour);
	}
	
	public Set<PointX> getNeighbours() {
		return this.neighbours;
	}

}

class LineX extends Line2D.Double {

	private Color color;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LineX(PointX point1, PointX point2) {
		super(point1, point2);
		this.color = Color.LIGHT_GRAY;
	}
	
	public LineX(PointX point1, PointX point2, Color color) {
		this(point1, point2);
		this.color = color;
	}
	
	public double getDistance() {
		double a	= Math.abs(this.getX1()-this.getX2());
		double b	= Math.abs(this.getY1()-this.getY2());
		return Math.sqrt(a*a + b*b);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
	
//	public double getAngle() {
//		double x	 = Math.abs(this.getX1()-this.getX2());
//		double y	 = Math.abs(this.getY1()-this.getY2());
//		double angle = Math.toDegrees(Math.atan2(x,y));
//		if (this.getX2() < this.getX1() ||
//			this.getY2() < this.getY1()) {
//			angle += 90;
//		}
//		return angle;
//	}
	
	public boolean intersectsLineX(List<LineX> otherLines) {
		
		for (LineX otherLine : otherLines) {
		
			if (this.getP1().equals(otherLine.getP1()) || this.getP2().equals(otherLine.getP2()) ||
				this.getP1().equals(otherLine.getP2()) || this.getP2().equals(otherLine.getP1()) )
			{
				continue;
			}
			if (this.intersectsLine(otherLine)) {
				return true;
			}
			
		}
		
		return false;
	}
}