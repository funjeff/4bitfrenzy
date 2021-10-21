package titleScreen;

import java.awt.event.KeyEvent;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import gameObjects.Compass;
import items.Bombs;
import items.ItemBox;
import menu.Menu;
import menu.MenuComponite;
import menu.TextComponite;

public class Tutorial extends GameObject{
	
	Menu consoleOut;
	
	boolean started = false;

	int task = -1;

	long endTime = 0;
	static long startTime = 0;
	Task currentTask = null;
	
	boolean pointingReg;
	
	public Tutorial (Menu console) {
		consoleOut = console;
	}
	
	public void start () {
		started = true;
	}
	
	public boolean hasStarted () {
		return started;
	}
	
	public Menu getConsoleOut () {
		return consoleOut;
	}
	
	@Override
	public void frameEvent () {
		
		if (started) {

			if (currentTask != null && !currentTask.prerequisite()) {
				endTime = System.currentTimeMillis() + currentTask.getTime(); //stops the tutorial from advancing if the player hasent done the prereq yet
			}
			
			if (null != ObjectHandler.getObjectsByName("Compass")){
				Compass c = (Compass) ObjectHandler.getObjectsByName("Compass").get(0);
				
				
				if (c != null && c.keyPressed(GameCode.getSettings().getControls()[6])) {
					if (!pointingReg) {
						c.setPointObject(ObjectHandler.getObjectsByName("TitleRegister").get(1));
					} else {
						c.setPointObject(ObjectHandler.getObjectsByName("TitleRegister").get(0));
					}
					pointingReg = !pointingReg;
				}
				}
			
			if (endTime < System.currentTimeMillis()) {
				
				if (currentTask != null) {
					currentTask.endTask();
				}
				
				task = task + 1;
				
				currentTask = pullTask(task);
				
				if (currentTask != null) {
					endTime = System.currentTimeMillis() + currentTask.getTime();
					startTime = System.currentTimeMillis();
				
					consoleOut.addComponite(new MenuComponite (10,10,consoleOut));
					consoleOut.addComponite(new TextComponite (consoleOut,currentTask.getString()));
				}
			}
			
			if (currentTask != null) {
				currentTask.taskEvent();
		
			}

		}		
		
		
	}
	
	public void fillProgressBar (String originalMessage, int barSpace) {
		int equalAmount = getProgessBarAmount((int)(System.currentTimeMillis() - startTime), (int)(endTime - startTime), barSpace );
		TextComponite taskDiscription = (TextComponite)consoleOut.getComponites().get(consoleOut.getComponites().size() -1);
		String newDiscription = originalMessage;
		for (int i = 0; i <= barSpace; i++) {
			if (i > equalAmount) {
				newDiscription = newDiscription + " ";
			} else {
				newDiscription = newDiscription + "=";
			}
		}
		int percentage = (int)(Math.floor( 100 * ((double)equalAmount/barSpace)));
		if (percentage > 100) {
			percentage = 100;
		}
		newDiscription = newDiscription + "] %" + percentage;
		taskDiscription.setText(newDiscription);
		
	}
	
	//returns the amount of = that should be in the progress bar
	public int getProgessBarAmount(int timePassed, int totalTime, int barSpace) {
		
		double IFuckinHateIntegerMathBecauseItMakesMeHaveToDoDumbShitLikeThis = (double) totalTime;
		
		return (int)((timePassed/IFuckinHateIntegerMathBecauseItMakesMeHaveToDoDumbShitLikeThis) * barSpace);
	}

	//returns the amount of time required to "run this task"
	public Task pullTask (int taskNum) {
		switch (taskNum) {
		case 0:
			Task start = new Task ("~CWhite~ STARTING TUTORIAL [", true);
			start.setTime(500);
			return start;
		case 1:
			Task createBit = new Task ("~CWhite~ CREATING BIT 01[", true);
			createBit.setTime(1000);
			return createBit;
		case 2:
			SpawnBitTask bitCreated = new SpawnBitTask ("~CWhite~ BIT CREATED ;)", false);
			bitCreated.setTime(1);
			return bitCreated;
		case 3:
			MoveTask moveTutorial = new MoveTask ("~CWhite~ USE MOVEMENT INSTRUCTIONS WITH " + KeyEvent.getKeyText(GameCode.getSettings().getControls()[0]) + " " + KeyEvent.getKeyText(GameCode.getSettings().getControls()[2]) + " " + KeyEvent.getKeyText(GameCode.getSettings().getControls()[1]) + " OR " + KeyEvent.getKeyText(GameCode.getSettings().getControls()[3]), false);
			moveTutorial.setTime(2500);
			return moveTutorial;
		case 4:
			Task meme = new Task ("~CWhite~ MOVEMENT INSTRUCTIONS ACCEPTED", false);
			meme.setTime(300);
			return meme;
		case 5:
			Task example = new Task ("~CWhite~ CREATING REGISTER 69 [", true);
			example.setTime(1000);
			return example;
		case 6:
			SpawnRegTask success = new SpawnRegTask ("~CWhite~ REGISTER CREATED SUCCESSFULLY", false);
			success.setTime(1);
			return success;
		case 7:
			RegTask carry = new RegTask ("~CWhite~ SET THE CARRY FLAG BY HOLDING " + KeyEvent.getKeyText(GameCode.getSettings().getControls()[4]).toUpperCase() + " WHILE NEAR THE REGISTER", false);
			carry.setTime(2500);
			return carry;
		case 8:
			Task ImOutOfNames = new RegTask ("~CWhite~ CARRY INSTRUCTIONS ACCEPTED ", false);
			ImOutOfNames.setTime(400);
			return ImOutOfNames;
		case 9:
			Task ImInOfNames = new Task ("~CWhite~ CREATING DATA SLOT      [", true);
			ImInOfNames.setTime(1000);
			return ImInOfNames;
		case 10:
			SpawnSlotTask dataCreation = new SpawnSlotTask ("~CWhite~ DATA SLOT CREATED", false);
			dataCreation.setTime(1);
			return dataCreation;
		case 11:
			DeliverRegisterTask recovery = new DeliverRegisterTask ("~CWhite~ ~N ~N BRINGING REGISTERS TO SLOTS WILL RESTORE THE COMPUTER AND GIVE YOU POINTS", false);
			recovery.setTime(1);
			return recovery;
		case 12:
			Task cleared = new Task ("~CWhite~ ~N ~N SLOT CLEARED! :>", false);
			cleared.setTime(500);
			return cleared;
		case 13:
			ClearTask clearing = new ClearTask ("~CWhite~ ~N ~N CLEARING CASHE [", true);
			clearing.setTime(2500);
			return clearing;
		case 14:
			Task compass = new Task ("~CWhite~ BOOTING POINTER.EXE [", true);
			compass.setTime(1500);
			return compass;
		case 15:
			SpawnCompassTask online = new SpawnCompassTask ("~CWhite~ POINTER ONLINE", false);
			online.setTime(300);
			return online;
		case 16:
			Task info = new Task ("~CWhite~ THE POINTER POINTS TOWARDS A REGISTER", false);
			info.setTime(600);
			return info;
		case 17:
			Task info2 = new Task ("~CWhite~ PRESSING " + KeyEvent.getKeyText(GameCode.getSettings().getControls()[6]).toUpperCase() + " INCREMENTS THE POINTER", false);
			info2.setTime(6000);
			return info2;
		case 18:
			Task itembox = new Task ("~CWhite~ INZIALIZING ITEM BOX.EXE [", true);
			itembox.setTime(1000);
			return itembox;
		case 19:
			SpawnItemBoxTask setupbox = new SpawnItemBoxTask ("~CWhite~ ITEM BOX.EXE HAS STARTED", false);
			setupbox.setTime(100);
			return setupbox;	
		case 20:
			Task infoAboutItems = new Task ("~CWhite~ COLLECING AN ITEM PASSES ITS VALUE INTO THE ITEM BOX", false);
			infoAboutItems.setTime(1);
			return infoAboutItems;
		case 21:
			Task infoPage2 = new Task ("~CWhite~ PRESSING " + KeyEvent.getKeyText(GameCode.getSettings().getControls()[5]).toUpperCase() + " EXECUTES THE ITEM BOXES DATA",false);
			infoPage2.setTime(300);
			return infoPage2;
		case 22:
			Task itemCreation = new Task ("~CWhite~ CREATING ITEM \"BOMB\" [", true);
			itemCreation.setTime(700);
			return itemCreation;
		case 23:
			SpawnBombTask itemCreationSuccess = new SpawnBombTask ("~CWhite~ \"BOMB\" CREATED SUCESSFULLY ", false);
			itemCreationSuccess.setTime(700);
			return itemCreationSuccess;
		case 24:
			Task tutorial = new Task ("~CWhite~ THAT CONDLUDES MY 4 BIT FRENZY CONTROLLS TUTORIAL", false);
			tutorial.setTime(700);
			return tutorial;
		case 25:
			EndTutorialTask ending = new EndTutorialTask ("~CWhite~ BE SURE TO RATE 5 STARS AND SUBSCRIBE FOR MORE EPIC TUTORIALS", false);
			ending.setTime(2500);
			return ending;
		
			
		}
		
		
		return null;
	}
	
	private class Task {
		
		public String taskString;

		public boolean progressBar = false;

		int barLength = 34;
		
		int time;
		
		public int getTime() {
			return time;
		}

		public void setTime(int time) {
			this.time = time;
		}

		public Task (String task, boolean progressBar) {
			this.taskString = task;
			this.progressBar = progressBar;
			
		}

		public void setString (String newString) {
			taskString = newString;
		}
		
		public String getString () {
			return taskString;
		}
		
		public boolean progressBar() {
			return progressBar;	
		}
		
		public void setBarLength(int barLength) {
			this.barLength = barLength;
		}
		//sets a condition that has to be true before it will clear
		public boolean prerequisite () {
			return true;
		}
		
		public void taskEvent () {
			if (progressBar) {
				fillProgressBar(taskString, barLength);
			}
		}
		

		public void endTask () {
			if (progressBar) {
				fillProgressBar(taskString, barLength);
			}
		}
		
		public int getPercentDone () {
			double IFuckinHateIntegerMathBecauseItMakesMeHaveToDoDumbShitLikeThis = (double) time;
			
			return (int)((System.currentTimeMillis() - startTime/IFuckinHateIntegerMathBecauseItMakesMeHaveToDoDumbShitLikeThis) * 100);

		}
	}
	private class SpawnBitTask extends Task {
		
		TitleBit b;
		
		public SpawnBitTask (String task, boolean progressBar) {
			super(task,progressBar);
		}
		
		@Override
		public void taskEvent () {
			super.taskEvent();
			if (b == null) {
				TextComponite prev = (TextComponite)consoleOut.getComponites().get(consoleOut.getComponites().size() -3);
				prev.setText("~CWhite~ CREATING BIT   [==================================] %100");
				b = new TitleBit ();
				b.declare(220, 58);
			}
		}
	}
	
	private class MoveTask extends Task {
		
		
		public MoveTask (String task, boolean progressBar) {
			super(task,progressBar);
		}
		
		@Override
		public boolean prerequisite () {
			
			TitleBit spawned = (TitleBit) ObjectHandler.getObjectsByName("TitleBit").get(0);
			
			if (spawned.getX() != 220 || spawned.getY() != 58) {
				return true;
			} else {
				return false;
			}
		
		}
	}
	
	private class DeliverRegisterTask extends Task {
		
		
		public DeliverRegisterTask (String task, boolean progressBar) {
			super(task,progressBar);
		}
		
		@Override
		public boolean prerequisite () {
			
			TitleSlot spawned = (TitleSlot) ObjectHandler.getObjectsByName("TitleSlot").get(0);
			
			spawned.frameEvent();
					
			if (spawned.isSelected()) {
				spawned.getAnimationHandler().setAnimationFrame(1);
				TitleRegister oldReg = (TitleRegister) ObjectHandler.getObjectsByName("TitleRegister").get(0);
				if (oldReg != null) {
					oldReg.forget();
				}
				return true;
			} else {
				return false;
			}
		
		}
	}
	
private class RegTask extends Task {
		
		boolean carried;
		
		public RegTask (String task, boolean progressBar) {
			super(task,progressBar);
		}
		
		@Override
		public boolean prerequisite () {
			
			TitleBit spawned = (TitleBit) ObjectHandler.getObjectsByName("TitleBit").get(0);
			
			if (spawned.isCarrying()) {
				carried = true;
			}
			
			return carried;
		}
	}
	private class SpawnRegTask extends Task {
		
		TitleRegister r;
		
		public SpawnRegTask (String task, boolean progressBar) {
			super(task,progressBar);
		}
		
		@Override
		public void taskEvent () {
			super.taskEvent();
			if (r == null) {
				TextComponite prev = (TextComponite)consoleOut.getComponites().get(consoleOut.getComponites().size() -3);
				prev.setText("~CWhite~ CREATING REGISTER   [==================================] %100");
				r = new TitleRegister ();
				r.declare(280, 150);
				
				if (r.isColliding("TitleBit")) {
					r.getCollisionInfo().getCollidingObjects().get(0).setY(r.getCollisionInfo().getCollidingObjects().get(0).getY() + 50);
				}
			}
		}
	}
	
	private class SpawnSlotTask extends Task {
		
		TitleSlot s;
		
		public SpawnSlotTask (String task, boolean progressBar) {
			super(task,progressBar);
		}
		
		@Override
		public void taskEvent () {
			super.taskEvent();
			if (s == null) {
				s = new TitleSlot (new Sprite ("resources/sprites/config/drive.txt"));
				s.declare(300, 250);
			}
		}
	}
		
	private class ClearTask extends Task {
		
		public ClearTask (String task, boolean progressBar) {
			super(task,progressBar);
		}
		
		@Override
		public void endTask () {
			super.endTask();
			consoleOut.clear();
			ObjectHandler.getObjectsByName("TitleSlot").get(0).forget();
			TitleRegister r = new TitleRegister ();
			r.declare(300, 300);
			
			if (r.isColliding("TitleBit")) {
				r.getCollisionInfo().getCollidingObjects().get(0).setY(r.getCollisionInfo().getCollidingObjects().get(0).getY() + 50);
			}
			
			TitleRegister r2 = new TitleRegister ();
			r2.declare(450, 100);
			
			if (r2.isColliding("TitleBit")) {
				r2.getCollisionInfo().getCollidingObjects().get(0).setY(r2.getCollisionInfo().getCollidingObjects().get(0).getY() + 50);
			}
			
		}
	}
	
	private class SpawnCompassTask extends Task {
		
		Compass s; //s stands for "sorry I coulden't come up with a better name"
		
		public SpawnCompassTask (String task, boolean progressBar) {
			super(task,progressBar);
		}
		
		@Override
		public void taskEvent () {
			super.taskEvent();
			if (s == null) {
				s = new Compass (ObjectHandler.getObjectsByName("TitleBit").get(0));
				s.setPointObject(ObjectHandler.getObjectsByName("TitleRegister").get(0));
				s.declare(00, 250);
				s.setX(750);
				
			}
		}
	} 
private class SpawnItemBoxTask extends Task {
		
		ItemBox s; //s stands for "sorry I coulden't come up with a better name again"?
		
		public SpawnItemBoxTask (String task, boolean progressBar) {
			super(task,progressBar);
		}
		
		@Override
		public void taskEvent () {
			super.taskEvent();
			if (s == null) {
				s = new ItemBox ();
				
				s.declare(00, 250);
				s.setX(500);
				s.setY(350);
				
			}
		}
	}

private class SpawnBombTask extends Task {
	
	Bombs b;
	int direction = 0;
	boolean didTheSidequest = false;
	
	public SpawnBombTask (String task, boolean progressBar) {
		super(task,progressBar);
	}
	
	@Override
	public boolean prerequisite () {
		return didTheSidequest;
	}
	
	
	@Override
	public void taskEvent () {
		super.taskEvent();
		ItemBox itemB = (ItemBox)ObjectHandler.getObjectsByName("ItemBox").get(0);
		
		if (itemB.keyDown(GameCode.getSettings().getControls()[0])) {
			direction = 0;
		}
		
		if (itemB.keyDown(GameCode.getSettings().getControls()[1])) {
			direction = 1;
		}
		
		if (itemB.keyDown(GameCode.getSettings().getControls()[3])) {
			direction = 2;
		}
		
		if (itemB.keyDown(GameCode.getSettings().getControls()[2])) {
			direction = 3;
		}
		
		if (b == null) {
			TextComponite prev = (TextComponite)consoleOut.getComponites().get(consoleOut.getComponites().size() -3);
			prev.setText("~CWhite~ CREATING ITEM        [==================================] %100");
			b = new Bombs ();
			b.declare(275, 205);
		} else {
			if (b.isColliding(ObjectHandler.getObjectsByName("TitleBit").get(0))) {
				if (b.pickupability) {
					itemB.putItemQuietly(b);
					b.forget();
				}
			}
		}
		if(itemB.getItem() != null && itemB.keyPressed(GameCode.getSettings().getControls()[5])) {
			Bombs obviouslyBombs = (Bombs)itemB.getItem();
			TitleBit currentCharictar = (TitleBit)ObjectHandler.getObjectsByName("TitleBit").get(0);
			obviouslyBombs.useItemAlternate(direction, (int)currentCharictar.getX(), (int)currentCharictar.getY());
			itemB.putItem(null);
			didTheSidequest = true;
		}
		
	}
}
public class EndTutorialTask extends Task {
	
	public EndTutorialTask (String task, boolean progressBar) {
		super(task,progressBar);
	}
	
	@Override
	public void endTask () {
		super.endTask();
		ObjectHandler.getObjectsByName("TitleBit").get(0).forget();
		ObjectHandler.getObjectsByName("TitleRegister").get(0).forget();
		ObjectHandler.getObjectsByName("TitleRegister").get(0).forget();
		ObjectHandler.getObjectsByName("ItemBox").get(0).forget();
		ObjectHandler.getObjectsByName("Compass").get(0).forget();
		ObjectHandler.getObjectsByName("Bombs").get(0).forget();

		
		consoleOut.close();
		consoleOut.forget();
		
		GameCode.endTutorial();
		
	}
}
	
	
}

