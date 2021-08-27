package resources;

import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import gameObjects.DataSlot;
import gameObjects.GameOverScreen;
import gameObjects.PixelBitch;
import gameObjects.Register;
import gameObjects.WaveCompleteGraphic;
import items.Bombs;
import items.DataScrambler;
import items.Glue;
import items.Lighter;
import items.Speed;
import items.Teleporter;
import map.Roome;
import network.NetworkHandler;
import npcs.NPC;
import titleScreen.TitleScreen;

public class Hud extends GameObject {
	
	public static long score = 0;
	static Textbox scoreDisplay;
	public static long timeLeft = 60000 * 5;
	public static int roundNum = 0;
	static Textbox timer;
	static Textbox waveNum;
	static Textbox registersRemaining;
	long prevTime;
	static int lives = 11;
	
	static boolean waitingForNewWave = false;
	
	//Register spawning parameters
	public static int minRegisterDistance = 1;
	public static int maxRegisterDistance = 4;
	public static int registerRadius = 3;
	
	public static int minBlueRegisterDistance = 6;
	public static int maxBlueRegisterDistance = 10;
	
	public static double blueRegisterOdds = .15;
	public static double largeRegisterOdds = .2;

	public static final engine.Sprite HEART = new engine.Sprite ("resources/sprites/heart.png");
	public static final Sprite LIFE_SEGMENT = new Sprite ("resources/sprites/life_segment.png");

	
	public Hud () {
		scoreDisplay = new Textbox ("00000000");
		scoreDisplay.changeHeight(2);
		scoreDisplay.changeWidth(17);
		scoreDisplay.setFont("text (lime green)");
		scoreDisplay.changeBoxVisability ();
		
		timer = new Textbox (" ");
		timer.changeHeight(2);
		timer.changeWidth(22);
		timer.setFont("text (lime green)");
		timer.setBox("Green");
		timer.changeBoxVisability ();
		
		registersRemaining = new Textbox (" ");
		registersRemaining.changeHeight(2);
		registersRemaining.changeWidth(22);
		registersRemaining.setFont("text (lime green)");
		registersRemaining.setBox("Green");
		registersRemaining.changeBoxVisability ();
		
		waveNum = new Textbox ("1");
		waveNum.changeHeight(2);
		waveNum.changeWidth(22);
		waveNum.setFont("text (lime green)");
		waveNum.setBox("Green");
		waveNum.changeBoxVisability ();
		
		this.setRenderPriority(42000);
	}

	public static void updateScore (long change) {
		score = score + change;
		
		String workin = Long.toString(score);
		int padNum = 8 - workin.length();
		
		String finalString = "";
		for (int i = 0; i < padNum; i++) {
			finalString = finalString + "0";
		}
		finalString = finalString + workin;
		
	
		
		scoreDisplay.changeText(finalString);
	}
	
	@Override
	public void frameEvent () {
		//Update registers and data slots
		for (int wy = 0; wy < Roome.getMapHeight (); wy++) {
			for (int wx = 0; wx < Roome.getMapWidth (); wx++) {
				Roome currRoome = Roome.map [wy][wx];
				currRoome.r = null;
				currRoome.ds = null;
			}
		}
		ArrayList<GameObject> regs = ObjectHandler.getObjectsByName ("Register");
		if (regs != null) {
			for (int i = 0; i < regs.size (); i++) {
				Register r = (Register)regs.get (i);
				Roome.getRoom (r.getX (), r.getY ()).r = r;
			}
		}
		ArrayList<GameObject> dSlots = ObjectHandler.getObjectsByName ("DataSlot");
		if (dSlots != null) {
			for (int i = 0; i < dSlots.size (); i++) {
				DataSlot ds = (DataSlot)dSlots.get (i);
				Roome.getRoom (ds.getX (), ds.getY ()).ds = ds;
			}
		}
	}
	
	@Override
	public void draw () {
		// once we do multiplayer put something here that make this happen only if its the right player
		try {
		scoreDisplay.setX(441 + GameCode.getViewX());
		scoreDisplay.setY(68 + GameCode.getViewY());
		scoreDisplay.draw();
		
		timer.setX(957 + GameCode.getViewX());
		timer.setY(87 + GameCode.getViewY());
		
		waveNum.setX(957 + GameCode.getViewX());
		waveNum.setY(21 + GameCode.getViewY());
		waveNum.draw();
		
		registersRemaining.setX(957 + GameCode.getViewX());
		registersRemaining.setY(54 + GameCode.getViewY());
		ArrayList<GameObject> dataSlots = ObjectHandler.getObjectsByName ("DataSlot");
		int numSlots = 0;
		for (int i = 0; i < dataSlots.size (); i++) {
			if (!((DataSlot)dataSlots.get(i)).isCleared () && !((DataSlot)dataSlots.get(i)).isScrambled ()) {
				numSlots++;
			}
		}
		if (NetworkHandler.isHost () && numSlots == 0 && !waitingForNewWave) {
			waitingForNewWave = true;
			waveOver ();
		}
		registersRemaining.changeText("" + numSlots);
		registersRemaining.draw();
		
		if (prevTime != 0 && NetworkHandler.isHost ()) {
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
		
		timer.changeText(Integer.toString(numMinutes) + ":"+ secondsString);
		timer.draw();
		
		for (int i = 0; i < lives; i++) {
				LIFE_SEGMENT.draw((i * 14) + 447, 32);
		}
		} catch (NullPointerException e) {
			//Things aren't set up yet
		}
	
	}
	public static void newWave() {	
		
		waitingForNewWave = false;
		roundNum = roundNum + 1;
		waveNum.changeText(Integer.toString(roundNum));

		ArrayList<GameObject> slots = ObjectHandler.getObjectsByName("DataSlot");

		if (roundNum != 1) {
			for (int i = 0; i < slots.size(); i++) {
				DataSlot currentSlot = (DataSlot) slots.get(i);
				if (currentSlot.isCleared()) {
					currentSlot.forget();
				} else if (!currentSlot.isScrambled ()) {
					setLives(getLives() - 1);
					if (lives <= 0) {
						gameOver();
						
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
		int numItems = rand.nextInt (TitleScreen.getNumberOfPlayers () + 4);
			
		for (int i = 0; i < numItems; i++) {
			int wx = rand.nextInt (Roome.getMapWidth ());
			int wy = rand.nextInt (Roome.getMapHeight ());
			Roome r = Roome.map[wy][wx];
		
			//IMPORTANT client is unresponsive if there are no items
			switch (rand.nextInt(6)) {
				case 0:
					r.spawnObject (Glue.class);
					break;
				case 1:
					r.spawnObject (Bombs.class);
					break;
				case 2:
					r.spawnObject (Speed.class);
					break;
				case 3:
					r.spawnObject (Teleporter.class);
					break;
				case 4:
					r.spawnObject (DataScrambler.class);
					break;
				case 5:
					r.spawnObject (Lighter.class);
			}
		}
		
		//Spawn in first register (always in starting room)
		if (roundNum == 1) {
			Point pt1 = new Point (Roome.getMapWidth () / 2, Roome.getMapHeight () / 2);
			Point pt2 = getNearbyRoome (pt1, 1, 1);
			Roome registerRoome = Roome.map [pt1.y][pt1.x];
			Roome slotRoome = Roome.map [pt2.y][pt2.x];
			int memNum = rand.nextInt (256);
			//Register
			Register r = (Register)registerRoome.spawnObject (Register.class);
			r.setMemAddress (memNum);
			//Data slot
			DataSlot ds = (DataSlot)slotRoome.spawnObject (DataSlot.class);
			ds.setMemAddress (memNum);
		}
		
		//Spawn in registers
		int newRegisters = 1 + (roundNum) * TitleScreen.getNumberOfPlayers() + rand.nextInt (TitleScreen.getNumberOfPlayers() + 1);
		for (int i = 0; i < newRegisters; i++) {
				
			try {
				if (ObjectHandler.getObjectsByName ("Register").size () >= 50) {
					return;
				}
			} catch (NullPointerException e) {
				//Do nothing
			}
			
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
			
			Register r = (Register)Roome.map[wy][wx].spawnObject (Register.class);
			r.setMemAddress (memNum);
			
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
			DataSlot slot = (DataSlot)dataRoom.spawnObject (DataSlot.class);
			slot.setMemAddress (memNum);
			
		}
		
		//Spawn in quest NPCs/Quest items
		ArrayList<ArrayList<GameObject>> npcs = ObjectHandler.getChildrenByName ("NPC");
		if (npcs != null) {
			for (int i = 0; i < npcs.size (); i++) {
				ArrayList<GameObject> currNpcs = npcs.get (i);
				if (currNpcs.size () > 0) {
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
								Point p1 = new Point (roomX, roomY);
								Point p2 = getNearbyRoome (p1, currNpc.getMinQuestItemDist (), currNpc.getMaxQuestItemDist ());
							
								//Spawn the quest item
								GameObject newObj = Roome.map [p2.y][p2.x].spawnObject (spawnClass);
								if (newObj != null) currNpc.linkQuestObject (newObj);
							
							}
						}
					}
				}
			}
		}
		
		//Update the round timer
		if (roundNum == 1) {
			timeLeft = 360000; // 6 minutes
		} else {
			timeLeft = 300000 - 30000 * roundNum;
			if (timeLeft <= 120000) {
				timeLeft = 120000; //2 minutes
			}
		}
	}
	public static void waveOver () {
		if (NetworkHandler.isHost ()) {
			NetworkHandler.getServer ().sendMessage ("ROUND COMPLETE");
		}
		setRoundTime (10000);
		new WaveCompleteGraphic ();
	}
	public static void setRoundTime (long roundTime) {
		timeLeft = roundTime;
	}
	
	public static void gameOver () {
		GameOverScreen screen = new GameOverScreen();
		screen.declare(0,0);
		GameCode.setView(0, 0);
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("GAME OVER");
		}
	}
	
	public static int getLives() {
		return lives;
	}

	public static void setLives(int lives) {
		Hud.lives = lives;
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("LIVES" + lives);
		}
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
