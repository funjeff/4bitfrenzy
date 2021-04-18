package engine;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import gameObjects.DataSlot;
import gameObjects.PixelBitch;
import gameObjects.Register;
import items.Bombs;
import items.DataScrambler;
import items.Glue;
import items.Speed;
import gameObjects.TitleScreen;
import map.Roome;
import network.NetworkHandler;
import players.Bit;
import resources.Hud;
import resources.Textbox;

public class GameCode {
	
	public static int viewX;
	public static int viewY;
	
	static Textbox timer;
	static boolean gameStarted = false;
	
	private static TitleScreen titleScreen;
	
	public static Bit bit;
	public static Bit bit2;
	public static Bit bit3;
	public static Bit bit4;
	
	static int frame = 1;
	
	
	public static void testBitch () {
		
		titleScreen = new TitleScreen ();
		
		Roome.generateMap();
		titleScreen.declare (0, 0);
		titleScreen.setRenderPriority(69);
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
		
		frame++;
		if (NetworkHandler.isHost () && titleScreen.titleClosed) {
			//Send server stuff out
			String toSend = "DATA:";
			try {
				toSend += Hud.timeLeft;
				toSend += ":" + Hud.score;
				toSend += ":" + (int)bit.getX () + "," + (int)bit.getY ();
				toSend += ":" + (int)bit2.getX () + "," + (int)bit2.getY ();
				toSend += ":" + (int)bit3.getX () + "," + (int)bit3.getY ();
				toSend += ":" + (int)bit4.getX () + "," + (int)bit4.getY () + ":";
				
				ArrayList<GameObject> regObjs = ObjectHandler.getObjectsByName ("Register");
				for (int i = 0; i < regObjs.size (); i++) {
					toSend += regObjs.get (i).toString ();
					if (i != regObjs.size () - 1) {
						toSend += ",";
					}
				}
				
				toSend += ":";
				ArrayList<GameObject> slotObjs = ObjectHandler.getObjectsByName ("DataSlot");
				for (int i = 0; i < slotObjs.size (); i++) {
					toSend += slotObjs.get (i).toString ();
					if (i != slotObjs.size () - 1) {
						toSend += ",";
					}
				}
			} catch (NullPointerException e) {
				return;
			}
			NetworkHandler.getServer ().sendMessage (toSend);
		} else {
			
			String toSend = "KEYS:";
			try {
				if (bit.keyDown ('W')) {
					toSend += 'W';
				}
				if (bit.keyDown ('A')) {
					toSend += 'A';
				}
				if (bit.keyDown ('S')) {
					toSend += 'S';
				}
				if (bit.keyDown ('D')) {
					toSend += 'D';
				}
				if (bit.keyDown (KeyEvent.VK_SHIFT)) {
					toSend += 'v';
				}
			} catch (NullPointerException e) {
				return; //Stuff hasn't been initialized yet
			}
			NetworkHandler.getClient ().messageServer (toSend);
			
		}
		
	}

	
	public static void renderFunc () {
		
		if (titleScreen.titleClosed && NetworkHandler.isHost () && !gameStarted) {
			gameStarted = true;
			NetworkHandler.getServer ().sendMessage ("START:" + Roome.saveMap ());
			initGameState ();
		}
	}
	
	public static void closeTitleScreen () {
		titleScreen.forget ();
	}
	
	public static void initGameState () {
		bit = new Bit ();
		bit2 = new Bit ();
		bit3 = new Bit ();
		bit4 = new Bit ();
		bit.playerNum = 1;
		bit2.playerNum = 2;
		bit3.playerNum = 3;
		bit4.playerNum = 4;
		PixelBitch IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter = Roome.map[5][5].biatch;
		int [] spawnCoords = IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter.getPosibleCoords(bit.hitbox().width, bit.hitbox().height);
		int [] spawnCoords2 = IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter.getPosibleCoords(bit.hitbox().width, bit.hitbox().height);
		int [] spawnCoords3 = IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter.getPosibleCoords(bit.hitbox().width, bit.hitbox().height);
		int [] spawnCoords4 = IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter.getPosibleCoords(bit.hitbox().width, bit.hitbox().height);
 		bit.declare(spawnCoords[0],spawnCoords[1]);
		bit2.declare(spawnCoords2[0] + 16,spawnCoords2[1] + 16);
		bit3.declare(spawnCoords3[0] + 32,spawnCoords3[1] + 32);
		bit4.declare(spawnCoords4[0] + 48,spawnCoords4[1] + 48);
		bit.updateIcon ();
		bit2.updateIcon ();
		bit3.updateIcon ();
		bit4.updateIcon ();
		Hud hud = new Hud ();
		hud.newWave ();
		hud.declare();
	}
	
}
