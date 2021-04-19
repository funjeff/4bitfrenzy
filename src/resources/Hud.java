package resources;

import java.util.ArrayList;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import gameObjects.DataSlot;
import gameObjects.GameOverScreen;
import gameObjects.Register;
import items.Bombs;
import items.DataScrambler;
import items.Glue;
import items.Speed;
import items.Teleporter;
import map.Roome;

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
		waveNum.changeText("WAVE NUMBER " + Integer.toString(roundNum));
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
		for (int i = 0; i < Roome.map.length; i++) {
			for (int j = 0; j < Roome.map[i].length; j++) {
				if (rand.nextInt(10) < roundNum) {
					
					int [] spawnCoords = Roome.map[i][j].biatch.getPosibleCoords(32, 32);
					
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
				
				if (rand.nextInt(20) < roundNum) {
					int memNum = rand.nextInt(256);
					
					Register r = new Register(memNum);
					
					int [] spawnPoint = Roome.map[i][j].biatch.getPosibleCoords(r.hitbox().width, r.hitbox().height);
					
					r.declare((int)spawnPoint[0], (int) spawnPoint[1]);
					
					Roome.map[i][j].r = r;
					
					int xCoord = rand.nextInt(3);
					
					if (rand.nextBoolean()) {
						xCoord = xCoord * -1;
					}
					
					xCoord = i + xCoord;
					
					
					int yCoord = rand.nextInt(3);
					
					if (rand.nextBoolean()) {
						yCoord = yCoord * -1;
					}
					
					yCoord = j + yCoord;
					
					if (xCoord > 9) {
						xCoord = 9;
					}
					
					if (xCoord < 0) {
						xCoord = 0;
					}
					if (yCoord > 9) {
						yCoord = 9;
					}
					
					if (yCoord < 0) {
						yCoord = 0;
					}
					
					Roome dataRoom = Roome.map [xCoord][yCoord];
					DataSlot ds = new DataSlot (memNum);
					
					int [] otherPoint = dataRoom.biatch.getPosibleCoords(ds.hitbox().width, ds.hitbox().height);
					
					
					ds.declare((int)otherPoint[0],(int) otherPoint[1]);
					
					dataRoom.ds = ds;
				}
			}
		}
		timeLeft = 60000 * 5 + 60000 * roundNum;
	}
	
}
