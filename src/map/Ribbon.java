package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
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
	private static Point[] ribbonMap;
	
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
		return new Point (xFrom, xFrom);
	}
	
	public Point endPt () {
		return new Point (xTo, yTo);
	}
	
	public Point getPt (int progress) {
		if (xFrom == xTo) {
			return new Point (xFrom, yFrom + progress);
		} else {
			return new Point (xFrom + progress, yFrom);
		}
	}
	
	public static void constructPath () {
		
		//Find the length of the ribbon map
		Set<Entry<Point, Ribbon>> ribbons = startptMap.entrySet ();
		Iterator<Entry<Point, Ribbon>> iter = ribbons.iterator ();
		int len = 0;
		int numLines1 = 0;
		while (iter.hasNext ()) {
			len += iter.next ().getValue ().getLength ();
			numLines1++;
		}
		ribbonMap = new Point[len];
		
		//Populate the ribbon map
		int i = 0;
		int numLines2 = 0;
		Ribbon curr = ribbons.iterator ().next ().getValue ();
		HashMap<Ribbon, Object> iterated = new HashMap<Ribbon, Object> ();
		while (!iterated.containsKey (curr)) {
			for (int j = 0; j < curr.getLength (); j++) {
				try{ribbonMap[i + j] = curr.getPt (j);}catch(Exception e) {/*System.out.println (i + j);*/}
			}
			numLines2++;
			i += curr.getLength();
			iterated.put (curr, curr);
			curr = startptMap.get (curr.endPt ());
		}
		System.out.println (numLines1 + ", " + numLines2);
		
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
