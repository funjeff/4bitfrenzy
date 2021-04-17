package engine;

import map.Roome;
import players.Bit;

public class GameCode {
	
	
	public static void testBitch () {
		
		Roome.generateMap ();
		
		Bit bit = new Bit ();
		bit.declare();
	}
	
	public static void gameLoopFunc () {
		
	}

	public static void renderFunc () {
		Roome.draw(RenderLoop.wind.getBufferGraphics ());
	}
	
}
