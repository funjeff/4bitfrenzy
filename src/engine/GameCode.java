package engine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import gameObjects.DataSlot;
import gameObjects.PixelBitch;
import gameObjects.Register;
import items.Bombs;
import items.BrokenTeleporter;
import items.Cash;
import items.DataScrambler;
import items.Egg;
import items.FakeScrambler;
import items.FriedFood;
import items.Glue;
import items.Item;
import items.Speed;
import items.Teleporter;
import items.Water;
import gameObjects.TitleScreen;
import map.Ribbon;
import map.RibbonPulse;
import map.Roome;
import network.Client;
import network.NetworkHandler;
import npcs.Baseball;
import npcs.Basketball;
import npcs.NPC;
import players.Bit;
import resources.Hud;
import resources.SoundPlayer;
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
	

	public static float volume = 5;
	
	public static SoundPlayer musicHandler = new SoundPlayer ();

	private static boolean devMode = false;
	
	private static GameSettings settings;
	
	private File loadFile = null;
	
	private static double scoreMultiplier = 1;
	private static boolean hasPerk15 = false;
	
	public static final String[] packageList = new String[] {"items", "npcs"};
	
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
	
	private static void initSoundEffects() {
		File folder = new File("resources/sounds/effects");
		File[] listOfFiles = folder.listFiles();
		
		for (int i = 0; i < listOfFiles.length; i++) {
			SoundPlayer.casheSoundEffect(listOfFiles[i]);
		}
	}
	
	public static void initControls () {
			File file = new File ("resources/saves/controls.txt");
				if (!file.exists()) {
					try {
						file.createNewFile();
						
						FileWriter fw = new FileWriter (file);
				
						BufferedWriter bw = new BufferedWriter(fw);
						
						bw.write(Integer.toString(KeyEvent.VK_W));
						bw.newLine();
						bw.write(Integer.toString(KeyEvent.VK_S));
						bw.newLine();
						bw.write(Integer.toString(KeyEvent.VK_A));
						bw.newLine();
						bw.write(Integer.toString(KeyEvent.VK_D));
						bw.newLine();
						
						
						bw.write(Integer.toString(KeyEvent.VK_SHIFT));
						bw.newLine();
						bw.write(Integer.toString(KeyEvent.VK_ENTER));
						bw.newLine();
						bw.write(Integer.toString(KeyEvent.VK_SPACE));
						bw.newLine();
						
						
						bw.write(Integer.toString(KeyEvent.VK_M));
						bw.newLine();
						
						
						
						bw.write(Integer.toString(KeyEvent.VK_UP));
						bw.newLine();
						bw.write(Integer.toString(KeyEvent.VK_DOWN));
						bw.newLine();
						bw.write(Integer.toString(KeyEvent.VK_LEFT));
						bw.newLine();
						bw.write(Integer.toString(KeyEvent.VK_RIGHT));
						bw.newLine();
						bw.write(Integer.toString(KeyEvent.VK_CONTROL));
						bw.newLine();
						
						bw.close();
						
					} catch (IOException e) {
			
						e.printStackTrace();
					}
					
				}

				loadControls();
				
	}
	
	public static void loadControls () {
		
		File file = new File ("resources/saves/controls.txt");
		
		int [] controls = new int [13];
		
		try {
			Scanner scan = new Scanner (file);
			
			int index = 0;
			
			while (scan.hasNextLine()) {
				controls[index] = Integer.parseInt(scan.nextLine());
				index = index + 1;
			}
			
			scan.close();
			
			getSettings().setControls(controls);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void init () {
		
		getSettings (); //Initializes the settings
		
		
		initControls();
		RenderLoop.wind.setResolution (getSettings ().getResolutionX(), getSettings ().getResolutionY ());
		RenderLoop.wind.setSize (getSettings ().getResolutionX(), getSettings ().getResolutionY ());
		
		initSoundEffects();
	}
	
	public static void gameLoopFunc () {
	
		if (!NetworkHandler.isHost ()) {
			Client.processMessages ();
		}
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
					if (bits.get(0).keyDown (getSettings().getControls()[0])) {
						toSend += 'W';
					}
					if (bits.get(0).keyDown (getSettings().getControls()[2])) {
						toSend += 'A';
					}
					if (bits.get(0).keyDown (getSettings().getControls()[1])) {
						toSend += 'S';
					}
					if (bits.get(0).keyDown (getSettings().getControls()[3])) {
						toSend += 'D';
					}
					if (bits.get(0).keyDown (getSettings().getControls()[8])) {
						toSend += 'U';
					}
					if (bits.get(0).keyDown (getSettings().getControls()[10])) {
						toSend += 'L';
					}
					if (bits.get(0).keyDown (getSettings().getControls()[9])) {
						toSend += 'G';
					}
					if (bits.get(0).keyDown (getSettings().getControls()[11])) {
						toSend += 'R';
					}
					if (bits.get(0).keyDown(getSettings().getControls()[12])) {
						toSend += 'C';
					}
					if (bits.get(0).keyDown (getSettings().getControls()[5])) {
						toSend += 10;
					}
					if (bits.get(0).keyDown (getSettings().getControls()[6])) {
						toSend += 13;
					}
//					if (bits.get(0).keyDown('E')) {
//						toSend += 'E';
//					}
					if (bits.get(0).keyDown (getSettings().getControls()[7])) {
						toSend += 'M';
					}
					if (bits.get(0).keyDown (getSettings().getControls()[4])) {
						toSend += 'v';
					}
				} catch (IndexOutOfBoundsException e) {
					return; //Stuff hasn't been initialized yet
				}
				NetworkHandler.getClient ().messageServer (toSend);
				
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
		
		//Check for perk 15 (score booster, no longer default)
		int perk15Count = 0;
		for (int i = 0; i < 4; i++) {
			System.out.println(perks[i]);
			if (perks [i] == 15) {
				perk15Count++;
			}
		}
		scoreMultiplier = 1 + (perk15Count * .05);
		if (perk15Count > 0) hasPerk15 = true;
		
		//If generating a map,
		if (TitleScreen.initialData == null) {
			//Populate the map
			Roome.populateRoomes ();
			//Make a new wave
			if (NetworkHandler.isHost()) {
				Hud.newWave ();
			}
		}
		
		//Declare the hud and make the bits
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
			PixelBitch IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter = Roome.map[Roome.getMapHeight() / 2][Roome.getMapWidth () / 2].getSpawningMask ();
			int [] spawnCoords = IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter.getPosibleCoords(bit.hitbox().width, bit.hitbox().height);
			if (perks[i] == 5) {
				Bit bit1dot5 = new Bit();
				bit1dot5.playerNum = i + 1;
				bit1dot5.setPerk(69);
				bit1dot5.setActive(false);
				bit1dot5.makeSecondaryBit();
				bit1dot5.updateIcon();
				bit1dot5.declare(spawnCoords[0], spawnCoords[1]);
				bits.add(bit1dot5);
			}
			bit.declare(spawnCoords[0],spawnCoords[1]);
			bit.setPerk(perks[i]);
			bit.updateIcon ();
			bits.add(bit);
			i = i + 1;
		}
		
		Roome.map[Roome.getMapHeight() / 2][Roome.getMapWidth () / 2].spawnObject (FriedFood.class);
//		
//		Register regBig = new Register (45);
//		
//		Roome.map[Roome.getMapHeight() / 2][Roome.getMapWidth () / 2].r = regBig;
//		
//		regBig.makeLarge();
//		
//		regBig.declare(spawnCoords[0] - 40, spawnCoords[1] - 40);
		
		if (TitleScreen.initialData != null) {
			Client.updateGameData (TitleScreen.initialData);
		}
		
	}
	
	public static void changeMusic (String songPath) {
		musicHandler.play(songPath, volume);
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("MUSIC:" + songPath);
		}
	}
	
	public static boolean devMode () {
		return devMode;
	}
	
	public static void setScoreMultiplier (double amt) {
		scoreMultiplier = amt;
	}
	
	public static double getScoreMultipler () {
		return scoreMultiplier;
	}
	
	public static boolean hasPerk15 () {
		return hasPerk15;
	}
	
	public static GameSettings getSettings () {
		if (settings == null) {
			settings = new GameSettings ();
		}
		return settings;
	}
	
	public static GameObject makeInstanceOfGameObject (String type, double x, double y) {
		
		//Check all the packages
		for (int i = 0; i < packageList.length; i++) {
			try {
				String prefix = packageList [i];
				return makeInstanceOfGameObject (Class.forName (prefix + '.' + type), x, y);
			} catch (ClassNotFoundException e) {
				//Do nothing
			}
		}
		
		//Return null if class is not found
		return null;
		
	}
	
	public static GameObject makeInstanceOfGameObject (Class<?> type, double x, double y) {
		
		try {
			
			if (NPC.class.isAssignableFrom (type)) {
				NPC npc = (NPC) type.getConstructor (Double.TYPE, Double.TYPE).newInstance (x, y);
				
				return npc;
			} else {
				GameObject obj = (GameObject) type.getConstructor ().newInstance ();
				obj.declare ((int) x, (int) y);
				return obj;
			}
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			return null;
		}
		
	}
	
	public static boolean isCollidingWithInteractable (GameObject obj) {
		return obj.isCollidingChildren ("NPC") ||
			   obj.isCollidingChildren ("Item") ||
			   obj.isColliding ("Register") ||
			   obj.isColliding ("DataSlot");
	}
	
	public static class GameSettings {
		
		public static final int SCALE_MODE_HORIZONTAL_BORDER = 0;
		public static final int SCALE_MODE_FULL_BORDER = 1;
		public static final int SCALE_MODE_STRETCH = 2;
		public static final int SCALE_MODE_FULL = 3;
		
		private int resolutionX;
		private int resolutionY;
		
		private int defaultresX;
		private int defaultresY;
		
		private int scaleMode = 0;
		
		
		//0 is for up
		//1 is for down
		//2 is for left
		//3 is for right
		//4 is for grab
		//5 is for items
		//6 is for compass
		//7 is for map
		//8 is for up (duel core)
		//9 is for down (duel core)
		//10 is for left (duel core)
		//11 is for right (duel core)
		//12 is for camera (duel core)
		private int [] controls = new int [13];
		
		public GameSettings () {
			resolutionX = 1280;
			resolutionY = 720;
			
			defaultresX = 1280;
			defaultresY = 720;
			
			scaleMode = SCALE_MODE_HORIZONTAL_BORDER;
		}
		
		public int getResolutionX () {
			return resolutionX;
		}
		
		public int getResolutionY () {
			return resolutionY;
		}
		
		public int getScalingMode () {
			return scaleMode;
		}
		
		public void setResolution (int width, int height) {
			resolutionX = width;
			resolutionY = height;
			defaultresX = width;
			defaultresY = height;

			RenderLoop.wind.setResolution (width, height);
		}
		
		public void resetRes () { 
			resolutionX = defaultresX;
			resolutionY = defaultresY;
			RenderLoop.wind.setResolution (resolutionX, resolutionY);
		}
		
		public void changeResolution (int width, int height) {
			resolutionX = width;
			resolutionY = height;
			RenderLoop.wind.setResolution (width, height);
		}
		
		public int getScaleMode() {
			return scaleMode;
		}

		public void setScaleMode(int scaleMode) {
			this.scaleMode = scaleMode;
		}

		public int [] getControls() {
			return controls;
		}

		public void setControls(int [] controls) {
			this.controls = controls;
		}
		
		public void updateControlFile () {
			File file = new File ("resources/saves/controls.txt");
				try {
					
					FileWriter fw = new FileWriter (file);
			
					BufferedWriter bw = new BufferedWriter(fw);
					
					
					for (int i = 0; i < controls.length; i++) {
						bw.write(Integer.toString(controls[i]));
						bw.newLine();
					}
					
					bw.close();
					
				} catch (IOException e) {
		
					e.printStackTrace();
				}
		}
		
	}
	
	
	
}
