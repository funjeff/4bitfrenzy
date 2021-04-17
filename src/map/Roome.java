package map;

import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Random;

public class Roome {
	public static Roome [][] map = new Roome [10][10];
	
	
	//true = open false = closed
	boolean topJunction;
	boolean bottomJunction;
	boolean leftJunction;
	boolean rightJunction;
	
	public Roome ()
	{
		
	}
	
	public static void generateMap () {
		for (int wy = 0; wy < map.length; wy++) {
			for (int wx = 0; wx < map[0].length; wx++) {
				Roome working = new Roome ();
				
				Random rand = new Random ();
				
				working.bottomJunction = rand.nextDouble () > .4;
				working.rightJunction = rand.nextDouble () > .4;
				
		
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
		areAllAccessable ();
	}
	
	public static boolean areAllAccessable () {
		
		HashMap<Point, Boolean> accessables = new HashMap<Point, Boolean> ();
		checkAccessable (0, 0, accessables);
		System.out.println (accessables.size ());
		return true;
		
	}
	
	private static boolean checkAccessable (int x, int y, HashMap<Point,Boolean> map) {
		
		int[] xoffs = {0, 0, -1, 1};
		int[] yoffs = {-1, 1, 0, 0};
		
		for (int i = 0; i < 4; i++) {
			int px = x + xoffs [i];
			int py = y + yoffs [i];
			if (px < 0 || py < 0 || px >= 10 || py >= 10) {
				//
			} else {
				if (map.containsKey (new Point (px, py))) {
					//
				} else {
					map.put (new Point (px, py), null);
					checkAccessable (px, py, map);
					//
				}
			}
		}
		return false;
		
	}
	
	public static void draw (Graphics g) {
		
		for (int wy = 0; wy < map.length; wy++) {
			for (int wx = 0; wx < map[0].length; wx++) {
				int sx = wx * 16 + 256;
				int sy = wy * 16 + 256;
				Roome r = map [wy][wx];
				if (!r.topJunction) g.drawLine (sx + 2, sy + 2, sx + 12, sy + 2);
				if (!r.leftJunction) g.drawLine (sx + 2, sy + 2, sx + 2, sy + 12);
				if (!r.bottomJunction) g.drawLine (sx + 12, sy + 12, sx + 2, sy + 12);
				if (!r.rightJunction) g.drawLine (sx + 12, sy + 12, sx + 12, sy + 2);
			}
		}
		
	}
	
}
