package resources;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import engine.Sprite;



public class HitboxFilter {

	public HitboxFilter () {
		
	}
	/**
	 * 
	 * @return hashmap of arrayLists of ArrayLists the hashmap splits between pixel colors
	 * the lists of the pixels are sorted into seprate lists that each have the same y coordinate
	 */
	public static HashMap <Integer, ArrayList <ArrayList <Point>>> filter (ArrayList<ParsedFrame> frames) {

		HashMap<Integer, ArrayList<ArrayList <Point>>> result = new HashMap<Integer, ArrayList<ArrayList <Point>>>();
		
		Iterator<ParsedFrame> frameIter = frames.iterator ();
		Iterator<Pixel> pixelIter;
		
		while (frameIter.hasNext ()) {
			
			ArrayList<Pixel> hitboxPixels = new ArrayList<Pixel> ();
			pixelIter = frameIter.next ().getPixels ().iterator ();
			while (pixelIter.hasNext ()) {
				Pixel working = pixelIter.next ();
				ArrayList<ArrayList<Point>> pixels = result.get(working.getRgb());
				if (pixels == null) {
					pixels = new ArrayList<ArrayList<Point>>();
				}
				
				boolean newY = true;
				for (int i = 0; i < pixels.size(); i++) {
					if (pixels.get(i).get(0).y == working.getY()) {
						newY = false;
						pixels.get(i).add(new Point (working.getX(),working.getY()));
					}
				}
				
				if (newY) {
					ArrayList <Point> newPixel = new ArrayList <Point>();
					newPixel.add(new Point (working.getX(),working.getY()));
					
					pixels.add(newPixel);
				}
				
				result.put(working.getRgb(), pixels);
				
			}
			
		}
		return result;
	}
	
	public static HashMap <Integer, ArrayList <ArrayList <Point>>> filter (Sprite sprite) {
		return filter (PixelParser.parse (sprite));
	}
}