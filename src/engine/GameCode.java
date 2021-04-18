package engine;

import java.util.ArrayList;
import java.util.Random;

import gameObjects.DataSlot;
import gameObjects.PixelBitch;
import gameObjects.Register;
import items.Bombs;
import items.DataScrambler;
import items.Glue;
import items.Speed;
import map.Roome;
import players.Bit;
import resources.Hud;
import resources.Textbox;

public class GameCode {
	
	public static int viewX;
	public static int viewY;
	
	static Textbox timer;
	
	
	
	
	
	public static void testBitch () {
		
		Roome.generateMap ();
		Bit bit = new Bit ();
		PixelBitch IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter = Roome.map[5][5].biatch;
		int [] spawnCoords = IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter.getPosibleCoords(bit.hitbox().width, bit.hitbox().height);
		bit.declare(spawnCoords[0],spawnCoords[1]);
		
		DataScrambler speed = new DataScrambler ();
		speed.declare(spawnCoords[0] + 10, spawnCoords[1] + 40);
		
		Register reg = new Register (33);
		Register reg2 = new Register (232);
		
		DataSlot d = new DataSlot (33);
		DataSlot d2 = new DataSlot (232);
		
		reg.declare(spawnCoords[0] - 10, spawnCoords[1] - 40);
		
		reg2.declare(spawnCoords[0] + 40, spawnCoords[1]);
		
		d.declare(spawnCoords[0] + 200, spawnCoords[1] + 200);
		
		d2.declare(spawnCoords[0] - 50, spawnCoords[1] - 60);
		
		
		Hud hud = new Hud ();
		hud.declare();
		
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
