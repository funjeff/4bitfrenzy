package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;

public class Ribbon extends GameObject {

	Color defaultColor = new Color (0x157F41);
	
	int xFrom;
	int yFrom;
	int xTo;
	int yTo;
	
	private static HashMap<Point, Ribbon> startptMap = new HashMap<Point, Ribbon> ();
	private static HashMap<Integer, ArrayList<Point>> loops = new HashMap<Integer, ArrayList<Point>> (); //Id map
	private static HashMap<Point, Point> loopPts = new HashMap<Point, Point> (); //Map from x-y point to loopId-time pair
	
	public Ribbon (int xFrom, int yFrom, int xTo, int yTo) {
		this.xFrom = xFrom;
		this.yFrom = yFrom;
		this.xTo = xTo;
		this.yTo = yTo;
		declare (xFrom, yFrom);
		startptMap.put (new Point (xFrom, yFrom), this);
	}
	
	public int getLength () {
		if (xFrom == xTo) {
			return Math.abs (yFrom - yTo);
		} else {
			return Math.abs (xFrom - xTo);
		}
	}
	
	public Point startPt () {
		return new Point (xFrom, yFrom);
	}
	
	public Point endPt () {
		return new Point (xTo, yTo);
	}
	
	public Point getPt (int progress) {
		if (xFrom == xTo) {
			if (yFrom > yTo) {progress *= -1;}
			return new Point (xFrom, yFrom + progress);
		} else {
			if (xFrom > xTo) {progress *= -1;}
			return new Point (xFrom + progress, yFrom);
		}
	}
	
	public static void constructPath () {
		
		for (int loopId = 0; startptMap.size () > 0; loopId++) {
			
			//Make the point list
			ArrayList<Point> pts = new ArrayList<Point> ();
			loops.put (loopId, pts);
			
			//Get the elements from the HashMap
			Set<Entry<Point, Ribbon>> ribbons = startptMap.entrySet ();
			Iterator<Entry<Point, Ribbon>> iter = ribbons.iterator ();
			
			//Populate the list
			int i = 0;
			Ribbon curr = ribbons.iterator ().next ().getValue ();
			HashMap<Ribbon, Object> iterated = new HashMap<Ribbon, Object> ();
			while (curr != null) {
				for (int j = 0; j < curr.getLength (); j++) {
					pts.add (curr.getPt (j)); //Add point to map
					loopPts.put (curr.getPt (j), new Point (loopId, i + j)); //Add point to loopPts so it can be located by x,y value
				}
				i += curr.getLength();
				iterated.put (curr, curr);
				startptMap.remove (curr.startPt ());
				curr = startptMap.get (curr.endPt ());
			}
		}

		/* This tests the lengths/numbers of all the loops
		 * 
		 * Set<Entry<Integer, ArrayList<Point>>> ptsSet = loops.entrySet ();
		Iterator<Entry<Integer, ArrayList<Point>>> iter = ptsSet.iterator ();
		while (iter.hasNext ()) {
			System.out.println (iter.next ().getValue ().size ());
		}*/
		
	}
	
	/**
	 * Gets the point on the ribbon for the given time value t
	 * @param loopId the ribbon loop to query
	 * @param t the time value
	 * @return the point the given time value on the given ribbon loop corresponds to
	 */
	public static Point getRibbonPoint (int loopId, int t) {
		ArrayList<Point> pts = loops.get (loopId);
		return pts.get (t % pts.size ());
	}
	
	/**
	 * Gets the loopId-time pair for the given x-y point, if it exists
	 * @param pt the x-y point to check
	 * @return a point containing the pair (loopId,time) corresponding to the given x-y
	 */
	public static Point getRibbonFromPoint (Point pt) {
		return loopPts.get (pt);
	}
	
	@Override
	public void draw () {
		Graphics g = RenderLoop.wind.getBufferGraphics ();
		g.setColor (defaultColor);
		if (xFrom == xTo) {
			int y1 = yFrom < yTo ? yFrom : yTo; //Swap if needed
			int y2 = yFrom < yTo ? yTo : yFrom;
			g.fillRect (xFrom - 2 - GameCode.getViewX (), y1 - GameCode.getViewY (), 4, y2 - y1);
		} else if (yFrom == yTo) {
			int x1 = xFrom < xTo ? xFrom : xTo; //Swap if needed
			int x2 = xFrom < xTo ? xTo : xFrom;
			g.fillRect (x1 - GameCode.getViewX (), yFrom - 2 - GameCode.getViewY (), x2 - x1, 4);
		}
	}
	
}
