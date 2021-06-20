package engine;

import java.awt.Point;
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
import map.Ribbon;
import map.RibbonPulse;
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
	
	public static ArrayList <Bit> bits = new ArrayList <Bit> ();
	
	public static int [] perks = {-1,-1,-1,-1};
	
	static int frame = 1;
	
<<<<<<< HEAD
	public static float volume = 6F;
=======
	private static boolean devMode = false;
>>>>>>> 92da650652a7779d2dcfb05d22ce8719adafda29
	
	public static void testBitch () {
		
		titleScreen = new TitleScreen ();
		
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
	
		if (titleScreen.keyPressed ('Q')) {
			devMode = true;
		}
		if (NetworkHandler.isHost() && !gameStarted) {
			NetworkHandler.getServer ().sendMessage ("PERKS " + perks[0] + ":" + perks[1] + ":" + perks[2] + ":" + perks[3]);
		}
		frame++;
		if (NetworkHandler.isHost () && gameStarted) {
		
			int mouseX = bits.get (0).getCursorX () + getViewX ();
			int mouseY = bits.get (0).getCursorY () + getViewY ();
			Point pt = Ribbon.getRibbonFromPoint (new Point (mouseX, mouseY));
			if (pt != null) {
				System.out.println (pt);
				new RibbonPulse (pt.x, pt.y).declare (0, 0);
			}
			
			//Send server stuff out
			String toSend = "DATA:";
			try {
				toSend += Hud.timeLeft;
				toSend += ":" + Hud.score;
			//	System.out.print("big");
				for (int i = 0; i < bits.size(); i++) {
					toSend += ":" + (int)bits.get(i).getX () + "," + (int)bits.get(i).getY ();
					//System.out.print("nbm");
				}
				//System.out.print("chungus");
				toSend = toSend + ":";
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
				toSend += ":";
				ArrayList<ArrayList<GameObject>> items = ObjectHandler.getChildrenByName("Item");
				for (int i = 0; i < items.size (); i++) {
					for (int j = 0; j < items.get(i).size();j++) {
						toSend += items.get (i).get(j).toString ();
						toSend += ",";
					}
				}
				for (int i = 0; i < bits.size(); i++) {
					if (bits.get(i).inventory.getItem() != null) {
						toSend += bits.get(i).inventory.getItem().toString ();
						if (i != bits.size() -1) {
							toSend += ",";
						}
					} else {
						toSend += "null";
						if (i != bits.size() -1) {
							toSend += ",";
						}
					}
				}
//			System.out.println(toSend);
			} catch (NullPointerException e) {
				return;
			}
			NetworkHandler.getServer ().sendMessage (toSend);
		} else {
			if (!NetworkHandler.isHost()) {
				String toSend = "KEYS:";
				try {
					if (bits.get(0).keyDown ('W')) {
						toSend += 'W';
					}
					if (bits.get(0).keyDown ('A')) {
						toSend += 'A';
					}
					if (bits.get(0).keyDown ('S')) {
						toSend += 'S';
					}
					if (bits.get(0).keyDown ('D')) {
						toSend += 'D';
					}
					if (bits.get(0).keyDown (KeyEvent.VK_UP)) {
						toSend += KeyEvent.VK_UP;
					}
					if (bits.get(0).keyDown (KeyEvent.VK_LEFT)) {
						toSend += KeyEvent.VK_LEFT;
					}
					if (bits.get(0).keyDown (KeyEvent.VK_DOWN)) {
						toSend += KeyEvent.VK_DOWN;
					}
					if (bits.get(0).keyDown (KeyEvent.VK_RIGHT)) {
						toSend += KeyEvent.VK_RIGHT;
					}
					if (bits.get(0).keyDown(KeyEvent.VK_CONTROL)) {
						toSend += KeyEvent.VK_CONTROL;
					}
					if (bits.get(0).keyDown (10)) {
						toSend += 10;
					}
					if (bits.get(0).keyDown (13)) {
						toSend += 13;
					}
					if (bits.get(0).keyDown('E')) {
						toSend += 'E';
					}
					if (bits.get(0).keyDown ('M')) {
						toSend += 'M';
					}
					if (bits.get(0).keyDown (KeyEvent.VK_SHIFT)) {
						toSend += 'v';
					}
				} catch (IndexOutOfBoundsException e) {
					return; //Stuff hasn't been initialized yet
				}
				NetworkHandler.getClient ().messageServer (toSend);
				
			} else {
				
			}
		}
	}

	
	public static void renderFunc () {
		
		if (titleScreen.titleClosed && NetworkHandler.isHost () && !gameStarted) {
			initGameState ();
			gameStarted = true;
			NetworkHandler.getServer ().sendMessage ("START:" + Roome.saveMap ());
		}
	}
	
	public static TitleScreen getTitleScreen () {
		return titleScreen;
	}
	public static void closeTitleScreen () {
		titleScreen.forget ();
	}
	
	public static void setPerk (int perk, int player) {
		perks[player] = perk;
		
		
	}

	
	public static void initGameState () {
		Hud hud = new Hud ();
		if (NetworkHandler.isHost()) {
			Hud.newWave ();
		}
		hud.declare();
		int i = 0;
		while (true) {
			try {
				if (perks[i] == -1) {
					break;
				}
			} catch (IndexOutOfBoundsException e) {
				break;
			}
			Bit bit = new Bit ();
			bit.playerNum = i + 1;
			PixelBitch IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter = Roome.map[5][5].biatch;
			int [] spawnCoords = IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter.getPosibleCoords(bit.hitbox().width, bit.hitbox().height);
			if (perks[i] == 5) {
				Bit bit1dot5 = new Bit();
				bit1dot5.playerNum = i + 1;
				bit1dot5.setPerk(69);
				bit1dot5.setActive(false);
				bit1dot5.makeSecondaryBit();
				bit1dot5.updateIcon();
				bit1dot5.declare(spawnCoords[0], spawnCoords[1]);
			}
			bit.declare(spawnCoords[0],spawnCoords[1]);
			bit.setPerk(perks[i]);
			bit.updateIcon ();
			bits.add(bit);
			i = i + 1;
		}
		
		Bombs b = new Bombs ();
		
		PixelBitch IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter = Roome.map[5][5].biatch;
		int [] spawnCoords = IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter.getPosibleCoords(b.hitbox().width, b.hitbox().height);

		b.declare(spawnCoords[0], spawnCoords[1]);
		
	}
	
	public static boolean devMode () {
		return devMode;
	}
}
