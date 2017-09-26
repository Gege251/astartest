package astar;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AStar {
	public static List<LineX> getShortestPath(List<LineX> roads, List<PointX> cities, PointX start, PointX goal) {
		// The set of nodes already evaluated.
		Set<PointX> closedSet = new HashSet<>();
		// The set of currently discovered nodes that are not evaluated yet.
	    // Initially, only the start node is known.
		Set<PointX> openSet = new HashSet<>();
		openSet.add(start);
		// For each node, which node it can most efficiently be reached from.
	    // If a node can be reached from many nodes, cameFrom will eventually contain the
	    // most efficient previous step.
		Map<PointX, PointX> cameFrom = new HashMap<>();
		
		// For each node, the cost of getting from the start node to that node.
		Map<PointX, Double> gScores = new HashMap<>();
		Map<PointX, Double> fScores = new HashMap<>();
		
		for (int i=0; i < cities.size(); i++) {
			gScores.put(cities.get(i), new Double(Double.POSITIVE_INFINITY));
		}
		
		// The cost of going from start to start is zero.
		gScores.put(start, new Double(0.0));
		
		for (int i=0; i < cities.size(); i++) {
			fScores.put(cities.get(i), new Double(Double.POSITIVE_INFINITY));
		}
		
		fScores.put(start, start.distance(goal));
		
		while (!openSet.isEmpty()) {
			double min = Double.POSITIVE_INFINITY;
			PointX current = null;
			for (PointX point : openSet) {
				if (fScores.get(point).doubleValue() < min) {
					min = fScores.get(point).doubleValue();
					current = point;
				}
				
			}
			if (current.equals(goal)) {
				return reconstructPath(cameFrom, current);
			}
			openSet.remove(current);
			closedSet.add(current);
			
			for (PointX neighbour : current.getNeighbours()) {
				if (closedSet.contains(neighbour)) {
					continue;
				}

				double tentative_gScore = gScores.get(current) + current.distance(neighbour);
				if (!openSet.contains(neighbour)){
					openSet.add(neighbour);
				} else if (tentative_gScore >= gScores.get(neighbour).doubleValue()) {
					continue;
				}
				
				cameFrom.put(neighbour, current);
				gScores.put(neighbour, new Double(tentative_gScore));
				fScores.put(neighbour, new Double(tentative_gScore + goal.distance(neighbour)));
				
			}
			
		}
		
		
		return null;
	}
	
	private static List<LineX> reconstructPath(Map<PointX, PointX> cameFrom, PointX current) {
		List<LineX> totalPath = new ArrayList<>();
		while (cameFrom.containsKey(current)) {
			totalPath.add(new LineX(cameFrom.get(current), current, Color.RED));
			current = cameFrom.get(current);
		}
		return totalPath;
	}
	
}
