package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Roome {
	public static Roome [][] map = new Roome [10][10];
	public static HashMap<Point, Integer> distMap;
	
	
	//true = open false = closed
	boolean topJunction;
	boolean bottomJunction;
	boolean leftJunction;
	boolean rightJunction;
	
	public Roome ()
	{
		
	}
	
	public static void generateMap () {
		do {
			for (int wy = 0; wy < map.length; wy++) {
				for (int wx = 0; wx < map[0].length; wx++) {
					Roome working = new Roome ();
					
					Random rand = new Random ();
					
					working.bottomJunction = rand.nextDouble () > .45;
					working.rightJunction = rand.nextDouble () > .45;
					
			
					if (wy != 0) {
						working.topJunction = map[wy-1][wx].bottomJunction;
					}
					if (wx != 0) {
						working.leftJunction = map[wy][wx -1].rightJunction;
					}
					
					if (wx == 0) {
						working.leftJunction = false;
					}
					if (wx == map.length - 1) {
						working.rightJunction = false;
					}
					if (wy == 0) {
						working.topJunction = false;
					}
					if (wy == map[0].length - 1) {
						working.bottomJunction = false;
					}
					
					map[wy][wx] = working;
					
				}
			}
		} while (!areAllAccessable ());
	}
	
	public static boolean areAllAccessable () {
		
		HashMap<Point, Integer> accessables = new HashMap<Point, Integer> ();
		fillDistMap (5, 5, accessables);
		if (accessables.size () == 100) {
			distMap = accessables;
			return true;
		}
		return false;
		
	}
	
	private static void fillDistMap (int x, int y, HashMap<Point,Integer> visitMap) {
		
		int[] xoffs = {0, 0, -1, 1};
		int[] yoffs = {-1, 1, 0, 0}; //Top, Bottom, Left, Right
		
		LinkedList<Point> toCheck = new LinkedList<Point> ();
		toCheck.add (new Point (x, y));
		visitMap.put (new Point (x, y), 0);
		
		while (toCheck.size () != 0) {
			int dist = visitMap.get (toCheck.getLast ());
			int cx = toCheck.getLast ().x;
			int cy = toCheck.getLast ().y;
			for (int i = 0; i < 4; i++) {
				int px = cx + xoffs [i];
				int py = cy + yoffs [i];
				Roome r = map [cy][cx];
				if (px < 0 || py < 0 || px >= 10 || py >= 10) {
					//
				} else {
					boolean canVisit = false;
					if (i == 0 && r.topJunction) {canVisit = true;}
					if (i == 1 && r.bottomJunction) {canVisit = true;}
					if (i == 2 && r.leftJunction) {canVisit = true;}
					if (i == 3 && r.rightJunction) {canVisit = true;}
					if (canVisit) {
						Point pt = new Point (px, py);
						if (!visitMap.containsKey (pt)) {
							visitMap.put (pt, dist + 1);
							toCheck.addFirst (pt);
						}
					}
				}
			}
			toCheck.removeLast ();
		}
		
	}
	
	public static void draw (Graphics g) {
		Color green = new Color (0x00FF00);
		Color white = new Color (0xFFFFFFF);
		for (int wy = 0; wy < map.length; wy++) {
			for (int wx = 0; wx < map[0].length; wx++) {
				int sx = wx * 16 + 256;
				int sy = wy * 16 + 256;
				Roome r = map [wy][wx];
				g.setColor (white);
				if (!r.topJunction) g.drawLine (sx + 2, sy + 2, sx + 12, sy + 2);
				if (!r.leftJunction) g.drawLine (sx + 2, sy + 2, sx + 2, sy + 12);
				if (!r.bottomJunction) g.drawLine (sx + 12, sy + 12, sx + 2, sy + 12);
				if (!r.rightJunction) g.drawLine (sx + 12, sy + 12, sx + 12, sy + 2);
				g.setColor (green);
				//g.drawBytes (new byte[]{(byte) ('A' + distMap.get (new Point (wx, wy)))}, 0, 1, sx + 4, sy + 12);
			}
		}
		
	}
	
}
