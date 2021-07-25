package resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.awt.Point;

public class ParsedFrame {
	
	ArrayList<Pixel> pixels;
	
	public ParsedFrame (ArrayList<Pixel> pixels) {
		this.pixels = pixels;
	}
	
	public ArrayList<Pixel> getPixels () {
		return pixels;
	}
	
	public HashMap<Point, Pixel> getPixelMap () {
		Iterator<Pixel> iter = pixels.iterator ();
		HashMap<Point, Pixel> pixelMap = new HashMap<Point, Pixel> ();
		while (iter.hasNext ()) {
			Pixel px = iter.next ();
			pixelMap.put (new Point (px.getX (), px.getY ()), px);
		}
		return pixelMap;
	}
	
	public HashMap<Integer, Point> getColorMap () {
		Iterator<Pixel> iter = pixels.iterator ();
		HashMap<Integer, Point> colorMap = new HashMap<Integer, Point> ();
		while (iter.hasNext ()) {
			Pixel px = iter.next ();
			colorMap.put (px.getColor (), new Point (px.getX (), px.getY ()));
		}
		return colorMap;
	}
	
	@Override
	public String toString () {
		Iterator<Pixel> iter = pixels.iterator ();
		String pixelString = "[";
		while (iter.hasNext ()) {
			pixelString += iter.next ().toString ();
		}
		pixelString += "]";
		return pixelString;
	}
}
