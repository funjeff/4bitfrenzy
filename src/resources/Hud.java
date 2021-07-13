package resources;

import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import gameObjects.DataSlot;
import gameObjects.GameOverScreen;
import gameObjects.PixelBitch;
import gameObjects.Register;
import gameObjects.TitleScreen;
import gameObjects.WaveCompleteGraphic;
import items.Bombs;
import items.DataScrambler;
import items.Glue;
import items.Speed;
import items.Teleporter;
import map.Roome;
import network.NetworkHandler;
import npcs.NPC;

public class Hud extends GameObject {
	
	public static long score = 0;
	static Textbox scoreDisplay;
	public static long timeLeft = 60000 * 5;
	public static int roundNum = 0;
	static Textbox timer;
	static Textbox waveNum;
	static Textbox registersRemaining;
	long prevTime;
	static int lives = 10;
	
	//Register spawning parameters
	public static int minRegisterDistance = 1;
	public static int maxRegisterDistance = 4;
	public static int registerRadius = 3;
	
	public static int minBlueRegisterDistance = 6;
	public static int maxBlueRegisterDistance = 10;
	
	public static double blueRegisterOdds = .15;
	public static double largeRegisterOdds = 1090;

	public static final engine.Sprite HEART = new engine.Sprite ("resources/sprites/heart.png");

	
	public Hud () {
		scoreDisplay = new Textbox ("SCORE: 00000000");
		scoreDisplay.changeHeight(2 * 16);
		scoreDisplay.changeWidth(17 * 16);
		scoreDisplay.setFont("text (lime green)");
		scoreDisplay.setBox("Green");
		
		timer = new Textbox (" ");
		timer.changeHeight(2 * 16);
		timer.changeWidth(22 * 16);
		timer.setFont("text (lime green)");
		timer.setBox("Green");
		
		registersRemaining = new Textbox (" ");
		registersRemaining.changeHeight(2 * 16);
		registersRemaining.changeWidth(22 * 16);
		registersRemaining.setFont("text (lime green)");
		registersRemaining.setBox("Green");
		
		waveNum = new Textbox ("WAVE NUMBER 1");
		waveNum.changeHeight(2 * 16);
		waveNum.changeWidth(22 * 16);
		waveNum.setFont("text (lime green)");
		waveNum.setBox("Green");
		
		this.setRenderPriority(5);
	}

	public static void updateScore (long change) {
		score = score + change;
		
		String workin = Long.toString(score);
		int padNum = 8 - workin.length();
		
		String finalString = "SCORE: ";
		for (int i = 0; i < padNum; i++) {
			finalString = finalString + "0";
		}
		finalString = finalString + workin;
		
	
		
		scoreDisplay.changeText(finalString);
	}
	
	@Override
	public void draw () {
		// once we do multiplayer put something here that make this happen only if its the right player
		try {
		scoreDisplay.setX(320 + GameCode.getViewX());
		scoreDisplay.setY(100 + GameCode.getViewY());
		scoreDisplay.draw();
		
		timer.setX(640 + GameCode.getViewX());
		timer.setY(60 + GameCode.getViewY());
		
		waveNum.setX(640 + GameCode.getViewX());
		waveNum.setY(20 + GameCode.getViewY());
		waveNum.draw();
		
		registersRemaining.setX(640 + GameCode.getViewX());
		registersRemaining.setY(100 + GameCode.getViewY());
		registersRemaining.changeText(Integer.toString(ObjectHandler.getObjectsByName("Register").size()) +" REGISTERS REMAIN");
		registersRemaining.draw();
		
		if (prevTime != 0) {
			timeLeft = timeLeft - (System.currentTimeMillis() - prevTime);
			if (timeLeft <= 0) {
				newWave();
			}
		}
		prevTime = System.currentTimeMillis();
		
		int numMinutes = (int) (timeLeft/60000);
		int numSeconds = (int) ((timeLeft - numMinutes * 60000)/1000);
		
		String secondsString = Integer.toString(numSeconds);
		
		if (secondsString.length() == 1) {
			secondsString = "0" + secondsString;
		}
		
		timer.changeText(Integer.toString(numMinutes) + ":"+ secondsString + " REMAINING");
		timer.draw();
		
		for (int i = 0; i < lives; i++) {
				HEART.draw((i * 54) + 20, 0);
		}
		} catch (NullPointerException e) {
			//Things aren't set up yet
		}
	
	}
	public static void newWave() {	
		
		roundNum = roundNum + 1;
		waveNum.changeText("CURRENT WAVE: " + Integer.toString(roundNum));
		ArrayList<GameObject> slots = ObjectHandler.getObjectsByName("DataSlot");

		if (roundNum != 1) {
			for (int i = 0; i < slots.size(); i++) {
				DataSlot currentSlot = (DataSlot) slots.get(i);
				if (currentSlot.isCleared()) {
					currentSlot.forget();
				} else {
					lives = lives - 1;
					if (lives <= 0) {
						GameOverScreen screen = new GameOverScreen();
						screen.declare(0,0);
						GameCode.setView(0, 0);
					}
				}
			}
			if (slots != null) {
				for (int i = 0; i < slots.size(); i++) {
					DataSlot currentSlot = (DataSlot) slots.get(i);
					if (currentSlot.isCleared()) {
						currentSlot.forget();
	
					}
				}
			}
		}
		Random rand = new Random ();
		
		//Spawn in items
		int numItems = rand.nextInt (TitleScreen.getNumberOfPlayers () + 1);
			
		for (int i = 0; i < numItems; i++) {
			int wx = rand.nextInt (Roome.getMapWidth ());
			int wy = rand.nextInt (Roome.getMapHeight ());
			int [] spawnCoords = Roome.map[wy][wx].biatch.getPosibleCoords(32, 32);
		
			//IMPORTANT client is unresponsive if there are no items
			switch (rand.nextInt(5)) {
				case 0:
					Glue glue = new Glue ();
					glue.declare(spawnCoords[0], spawnCoords[1]);
					break;
				case 1:
					Bombs bombs = new Bombs ();
					bombs.declare(spawnCoords[0], spawnCoords[1]);
					break;
				case 2:
					Speed speed = new Speed ();
					speed.declare(spawnCoords[0], spawnCoords[1]);
					break;
				case 3:
					Teleporter tele = new Teleporter ();
					tele.declare(spawnCoords[0], spawnCoords[1]);
					break;
				case 4:
					DataScrambler scrambler = new DataScrambler ();
					scrambler.declare(spawnCoords[0], spawnCoords[1]);
					break;
			}
		}
		
		//Spawn in perk 15 bonus register (if applicable)
		if (roundNum == 1 && GameCode.hasPerk15()) {
			Point pt1 = new Point (Roome.getMapWidth () / 2, Roome.getMapHeight () / 2);
			Point pt2 = getNearbyRoome (pt1, 1, 1);
			Roome registerRoome = Roome.getRoom (pt1.x, pt1.y);
			Roome slotRoome = Roome.getRoom (pt2.x, pt2.y);
			int memNum = rand.nextInt (256);
			//Register
			Register r = new Register (memNum);
			int[] regSpawn = registerRoome.biatch.getPosibleCoords ((int)r.hitbox ().getWidth (), (int)r.hitbox ().getHeight ());
			r.declare (regSpawn [0] + pt1.x * 1080, regSpawn [1] + pt1.y * 720);
			registerRoome.r = r;
			System.out.println ("COORDS: " + r.getX () + ", " + r.getY ());
			//Data slot
			DataSlot ds = new DataSlot (memNum);
			int[] dsSpawn = slotRoome.biatch.getPosibleCoords ((int)ds.hitbox ().getWidth (), (int)ds.hitbox ().getHeight ());
			ds.declare (dsSpawn [0] + pt2.x * 1080, dsSpawn [1] + pt2.y * 720);
			slotRoome.ds = ds;
		}
		
		//Spawn in registers
		int newRegisters = (roundNum) * TitleScreen.getNumberOfPlayers() + rand.nextInt (TitleScreen.getNumberOfPlayers() + 1);
		for (int i = 0; i < newRegisters; i++) {
				
			int memNum = rand.nextInt(256);
			int wx = rand.nextInt (Roome.getMapWidth ());
			int wy = rand.nextInt (Roome.getMapHeight ());
			
			if (ObjectHandler.getObjectsByName("Register") != null) {
				while (true) {
					
					boolean broken = false;
					
					for (int b = 0; b < ObjectHandler.getObjectsByName("Register").size(); b++) {
						Register reg = (Register)ObjectHandler.getObjectsByName("Register").get(b);
						if (reg.getMemAddress() == memNum) {
							broken = true;
							break;
						}
					}
						if (!broken) {
							break;
						}
						memNum = rand.nextInt(256);
				}
			}
			Register r = new Register(memNum);
			
			int [] spawnPoint = Roome.map[wy][wx].biatch.getPosibleCoords(r.hitbox().width, r.hitbox().height);
			
			r.declare((int)spawnPoint[0], (int) spawnPoint[1]);
			
			Roome.map[wy][wx].r = r;
			
			int xCoord, yCoord;
			Point p1 = new Point (wx, wy);
			Point p2 = new Point (wx, wy);
			
			boolean isBlue = rand.nextDouble () < blueRegisterOdds;
			boolean isLarge = isBlue ? false : rand.nextDouble () < largeRegisterOdds; //Blue registers can't be large
			if (TitleScreen.getNumberOfPlayers() == 1) {
				isLarge = false;
			}
			
			//Change the register as needed
			if (isBlue) {
				r.makeBlue ();
			}
			if (isLarge) {
				r.makeLarge ();
			}
			
			if (isBlue) {
				p2 = getNearbyRoome (p1, minBlueRegisterDistance, maxBlueRegisterDistance);
			} else {
				p2 = getNearbyRoome (p1, minRegisterDistance, maxRegisterDistance);
			}
			xCoord = p2.x;
			yCoord = p2.y;
			
			Roome dataRoom = Roome.map [yCoord][xCoord];
			DataSlot ds = new DataSlot (memNum);
			
			int [] otherPoint = dataRoom.biatch.getPosibleCoords(ds.hitbox().width, ds.hitbox().height);
			
			
			ds.declare((int)otherPoint[0],(int) otherPoint[1]);
			
			dataRoom.ds = ds;
		}
		
		//Spawn in quest NPCs/Quest items
		ArrayList<ArrayList<GameObject>> npcs = ObjectHandler.getChildrenByName ("NPC");
		for (int i = 0; i < npcs.size (); i++) {
			ArrayList<GameObject> currNpcs = npcs.get (i);
			NPC firstNpc = (NPC)currNpcs.get (0);
			if (firstNpc.spawnsQuestItem()) {
				
				//Uhhh
				
				for (int j = 0; j < currNpcs.size (); j++) {
					
					//Get the curr npc
					NPC currNpc = (NPC)currNpcs.get (j);
					
					//Establish quest item parameters
					boolean canSpawn = true;
					double spawnOdds = currNpc.getQuestItemSpawnOdds ();
					Class<?> spawnClass = currNpc.getQuestItemType ();
					ArrayList<GameObject> alreadySpawned = ObjectHandler.getObjectsByName (spawnClass.getSimpleName ());
					int spawnedSize = alreadySpawned == null ? 0 : alreadySpawned.size ();
					if (spawnedSize >= currNpc.getMaxQuestItems ()) {
						canSpawn = false; //Ensure no more than the maximum # of items are present
					}
					if (spawnedSize < currNpc.getMinQuestItems () && roundNum == 1) {
						spawnOdds = 1.0;
					}
					
					//Prevent spawning if this already has a spawn
					if (currNpc.getLinkedQuestsItem() != null && currNpc.getLinkedQuestsItem ().declared ()) {
						canSpawn = false;
					}
					
					if (canSpawn && Math.random () < spawnOdds) {
						
						//Find the spawn location
						int roomX = (int)(currNpc.getX () / 1080);
						int roomY = (int)(currNpc.getY () / 720);
						System.out.println (roomX + ", " + roomY);
						Point p1 = new Point (roomX, roomY);
						Point p2 = getNearbyRoome (p1, currNpc.getMinQuestItemDist (), currNpc.getMaxQuestItemDist ());
						PixelBitch biatch = Roome.getRoom (p2.x, p2.y).biatch;
						Dimension d = NPC.getHitboxDimensions (spawnClass);
						int[] spawnCoords = biatch.getPosibleCoords (d.width, d.height);
						
						//Spawn the quest item
						try {
							GameObject newObj = (GameObject)spawnClass.getConstructor (Double.TYPE, Double.TYPE).newInstance (spawnCoords [0] + p2.x * 1080, spawnCoords [1] + p2.y * 720);
							currNpc.linkQuestObject (newObj);
						} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | SecurityException e) {
							e.printStackTrace();
							// TODO Auto-generated catch block
						} catch (NoSuchMethodException e) {
							//Not an NPC
							GameObject newObj;
							try {
								newObj = (GameObject)spawnClass.getConstructor ().newInstance ();
								newObj.setX (spawnCoords [0]);
								newObj.setY (spawnCoords [1]);
								currNpc.linkQuestObject (newObj);
							} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
									| InvocationTargetException | NoSuchMethodException | SecurityException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
					}
					
				}
			}
		}
		
		timeLeft = 60000 * 5 + 60000 * roundNum;
	}
	public static void waveOver () {
		if (NetworkHandler.isHost ()) {
			NetworkHandler.getServer ().sendMessage ("ROUND COMPLETE");
		}
		setRoundTime (15000);
		new WaveCompleteGraphic ();
	}
	public static void setRoundTime (long roundTime) {
		timeLeft = roundTime;
	}
	public static Point getNearbyRoome (Point start, int minDist, int maxDist) {
		
		Random rand = new Random ();
		
		int xCoord, yCoord;
		Point to = new Point (start);
		
		int attempts = 0;
		
		while (true) {
			
			//Select a random room (distance lookups are cheap because they are lookup tables)
			xCoord = rand.nextInt (Roome.getMapWidth ());
			yCoord = rand.nextInt (Roome.getMapHeight ());
			
			//Test for the required proximity
			to.x = xCoord;
			to.y = yCoord;
			int dist = Roome.distBetween (start, to);
			if (dist >= minDist && dist <= maxDist) {
				break;
			}
			
			if (attempts > 10000) {
				return null;
			}
			
		}
		
		return to;
				
	}
}
