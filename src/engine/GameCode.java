package engine;

import map.Roome;
import players.Bit;

public class GameCode {
	
	public static int viewX;
	public static int viewY;
	
	public static void testBitch () {
		
		Roome.generateMap ();
		
		Bit bit = new Bit ();
		bit.declare((1080 * 5) + 540, (720 * 5) + 360);
	}
	
	public static void setView (int x, int y) {
		//Sets the top-right coordinate of the viewport of the room to (x, y)
		viewX = x;
		viewY = y;
	}
	public static int getViewX () {
		//Returns the x-coordinate of the viewport of the room
		return viewX;
	}
	public static int getViewY () {
		//Returns the y-coordinate of the viewport of the room
		return viewY;
	}
	
	public static void gameLoopFunc () {
		
	}

	public static void renderFunc () {
		Roome.draw(RenderLoop.wind.getBufferGraphics ());
	}
	
}
