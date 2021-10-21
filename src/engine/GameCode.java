package engine;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

import gameObjects.DataSlot;
import gameObjects.HelpWindow;
import gameObjects.MovableRectHighlight;
import gameObjects.PixelBitch;
import gameObjects.Register;
import gui.MenuBar;
import items.BasketBomb;
import items.Bombs;
import items.BrokenTeleporter;
import items.Cash;
import items.ChickenBucket;
import items.DataScrambler;
import items.Egg;
import items.FakeScrambler;
import items.FriedFood;
import items.Glue;
import items.Item;
import items.Lighter;
import items.Pin;
import items.Speed;
import items.Teleporter;
import items.Water;
import map.Ribbon;
import map.RibbonPulse;
import map.Roome;
import menu.CompositeComponite;
import menu.Menu;
import menu.MenuComponite;
import menu.ObjectComponite;
import menu.TextComponite;
import network.Client;
import network.NetworkHandler;
import npcs.Baseball;
import npcs.Basketball;
import npcs.NPC;
import npcs.SettingsTxt;
import npcs.USB;
import players.Bit;
import resources.Hud;
import resources.SoundPlayer;
import resources.Textbox;
import titleScreen.Scene;
import titleScreen.TitleScreen;
import titleScreen.TitleScreen.ArrowButtons;
import titleScreen.Tutorial;

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
	
	private static Scene introScene;
	
	public static final String[] packageList = new String[] {"items", "npcs"};
	
	static Tutorial tutorial;
	
	public static void testBitch () {
		
		titleScreen = new TitleScreen ();
		
		File playedTutorial = new File ("resources/saves/tutorial");
		if (!playedTutorial.exists()) {
	
			Menu m = new Menu ();
			m.setBackgroundColor(0xFFFFFF);
			m.addComponite(new TextComponite (m," ~CWhite~DO YOU WANT TO PLAY THE TUTORIAL?(Y/N)"));
			m.setWidth(400);
			m.setHeight(-1);
			m.open();
			m.setRenderPriority(69);
			tutorial = new Tutorial (m);
		} else {
			titleScreen.declare (0, 0);
			titleScreen.setRenderPriority(69);
			}
		}
	
	public static void endTutorial () {
		titleScreen.declare (0, 0);
		titleScreen.setRenderPriority(69);
		tutorial.forget();
		tutorial = null;
		File playedTutorial = new File ("resources/saves/tutorial");
		try {
			playedTutorial.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	public static int [] getDefaultControls () {
		int [] defaultControls = new int [13];
		defaultControls[0] = KeyEvent.VK_W;
		defaultControls[1] = KeyEvent.VK_S;
		defaultControls[2] = KeyEvent.VK_A;
		defaultControls[3] = KeyEvent.VK_D;
		defaultControls[4] = KeyEvent.VK_SHIFT;
		defaultControls[5] = KeyEvent.VK_ENTER;
		defaultControls[6] = KeyEvent.VK_SPACE;
		defaultControls[7] = KeyEvent.VK_M;
		defaultControls[8] = KeyEvent.VK_UP;
		defaultControls[9] = KeyEvent.VK_DOWN;
		defaultControls[10] = KeyEvent.VK_LEFT;
		defaultControls[11] = KeyEvent.VK_RIGHT;
		defaultControls[12] = KeyEvent.VK_CONTROL;
		
		return defaultControls;
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
		
		if (RenderLoop.wind != null) {
			RenderLoop.wind.setResolution (getSettings ().getResolutionX(), getSettings ().getResolutionY ());
			RenderLoop.wind.resizeWindow (getSettings ().getResolutionX(), getSettings ().getResolutionY ());
		}
		
		initSoundEffects();
	}
	
	public static void gameLoopFunc () {
	
		if (!NetworkHandler.isHost ()) {
			Client.processMessages ();
		}
		
		if (titleScreen.keyPressed ('Q')) {
			devMode = true;
		}
		if (titleScreen.keyDown('Y') && tutorial != null && !tutorial.hasStarted()) {
			tutorial.start();
			tutorial.declare();
		}
		
		if (titleScreen.keyDown('N') && tutorial != null) {
			
			tutorial.getConsoleOut().forget();
			tutorial = null;
			
			File playedTutorial = new File ("resources/saves/tutorial");
			
			try {
				playedTutorial.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			titleScreen.declare (0, 0);
			titleScreen.setRenderPriority(69);
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
				//System.out.println (pt);
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
		
		if (TitleScreen.titleClosed && NetworkHandler.isHost () && !gameStarted && introScene == null) {
			startGame();
		}
		if (introScene != null) {
			introScene.frameEvent();
			introScene.draw();
			
			if (!introScene.isPlaying()) {
				initGameState ();
				gameStarted = true;
				if (NetworkHandler.isHost()) {
					NetworkHandler.getServer ().sendMessage ("START:" + Roome.saveMap ()); //TODO intro scene for clients
				}
			}
		}
		
		for (int i = 0; i < bits.size (); i++) {
			Bit b = bits.get (i);
			if (b.playerNum == NetworkHandler.getPlayerNum() && b.isActive()) {
				b.updateScroll ();
			}
		}
		
	}
	public static void startGame () {
		introScene = new Scene ("resources/scenes/intro.txt");
		viewX = (Roome.getMapWidth () / 2) * 1080;
		viewY = (Roome.getMapHeight() / 2) * 720;
		introScene.setX(viewX);
		introScene.setY(viewY);
	
		introScene.play();
		introScene.frameEvent();
		
		for (int i = 0; i < 4; i++) {
			if (perks[i] != -1) {
				Bit bit = new Bit ();
				bit.setPerk(perks[i]);
				introScene.objs.get(i).sprite = bit.getSprite();
				introScene.objs.get(i).frame = i;
			} else {
				introScene.objs.get(i).sprite = new Sprite ("resources/sprites/blank.png");
			}
		}
		
		if (!NetworkHandler.isHost()) {
			ObjectHandler.getObjectsByName("USB").get(0).setVisability(false);
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
		
		//DEBUG SPAWNING STARTS HERE
		if (NetworkHandler.isHost ()) {
			//Roome.map [5][5].spawnObject (Bombs.class);
		}
		//Roome.map [5][5].spawnObject (Bombs.class);
		//DEBUG SPAWNING ENDS HERE
		//Declare the hud and make the bits
		hud.declare();
		new MenuBar ();
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
			if (perks[i] == 5) {
				Bit bit1dot5 = new Bit();
				bit1dot5.playerNum = i + 1;
				bit1dot5.setPerk(69);
				bit1dot5.setActive(false);
				bit1dot5.makeSecondaryBit();
				bit1dot5.updateIcon();
				bit1dot5.declare((int)(introScene.objs.get(i).x + + introScene.getX()), (int)(introScene.objs.get(i).y + + introScene.getY()));
				bits.add(bit1dot5);
			}
			bit.declare((int)(introScene.objs.get(i).x + introScene.getX()), (int)(introScene.objs.get(i).y + introScene.getY()));
//			while (bit.isColliding ("Register")) {
//				spawnCoords = IReallyDidentThinkIWouldHaveToUseThisTypeEnoghToHaveThisMatter.getPosibleCoords(bit.hitbox().width, bit.hitbox().height);
//				bit.setX (spawnCoords [0]);
//				bit.setY (spawnCoords [1]); //Fix for bit spawning inside register
//			} //TODO fix bit spawning inside register again
			bit.setPerk(perks[i]);
			bit.updateIcon ();
			bits.add(bit);
			i = i + 1;
		}
		
		//new OldTutorial ();
		if (Bit.getCurrentPlayer () != null) {
			new HelpWindow ();
		}
		
	
//	Roome.map[Roome.getMapHeight() / 2][Roome.getMapWidth () / 2].spawnObject (Bombs.class);
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
		if (NetworkHandler.isHost()) {
			USB USB = new USB((int)(introScene.objs.get(4).x + introScene.getX()),(int) (introScene.objs.get(4).y + introScene.getY()));
			USB.declare((int)(introScene.objs.get(4).x + introScene.getX()),(int) (introScene.objs.get(4).y + introScene.getY()));
		} else {
			ObjectHandler.getObjectsByName("USB").get(0).setVisability(true);
		}
		introScene = null;
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
			e.printStackTrace();
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
			if (controls[0] == 0) {
				initControls();
			}
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
