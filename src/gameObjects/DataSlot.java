package gameObjects;

import java.util.ArrayList;
import java.util.Arrays;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import map.Roome;
import network.NetworkHandler;
import players.Bit;
import resources.Hud;
import resources.SoundPlayer;
import resources.Textbox;

public class DataSlot extends GameObject {
	
	public int memAddress = 0;
	
	boolean cleared = false;
	
	Textbox display;
	
	Textbox reward;
	
	int updateTime = 0;
	
	String prevEncoding = null;
	
	Sprite driveSprite = new Sprite("resources/sprites/config/drive.txt");

	public int getMemAddress() {
		return memAddress;
	}

	public void setMemAddress(int memAddress) {
		display.changeText (Integer.toHexString(memAddress).toUpperCase());
		this.memAddress = memAddress;
	}

	public DataSlot () {
		this.setSprite (driveSprite);
		this.getAnimationHandler().setAnimationFrame(0);
		display = new Textbox ("00");
		display.changeBoxVisability();
		display.setRenderPriority(3);
		this.setRenderPriority(2);
		display.setFont("text (red)");
		this.setHitboxAttributes(84, 90);
	}
	
	public DataSlot (int memAdress) {
		this ();
		setMemAddress (memAddress);
	}
	
	public Register getRegester() {
		ArrayList <GameObject> slots = ObjectHandler.getObjectsByName("Register");
		for (int i = 0; i < slots.size(); i++) {
			Register working = (Register)slots.get(i);
			
			if (working.memAddress == this.memAddress) {
				return working;
			}
			
		}
		return null;
	}
	
	public static DataSlot getDataSlot(int memAddress) {
		
		ArrayList <GameObject> slots = ObjectHandler.getObjectsByName("DataSlot");
		for (int i = 0; i < slots.size(); i++) {
			DataSlot working = (DataSlot)slots.get(i);
			
			if (working.memAddress == memAddress) {
			
				return working;
			}
			
		}
		return null;
	}
	
	@Override
	public void frameEvent () {
		
		if (this.isColliding("Register") && !this.cleared && NetworkHandler.isHost()) {
			ArrayList <GameObject> collidingRegesters = this.getCollisionInfo().getCollidingObjects();
			for (int i = 0; i < collidingRegesters.size(); i++) {
				
				Register working = (Register)collidingRegesters.get(i);
				if (working.getMemAddress() == memAddress || working.scrambled || working.secondAddress == memAddress) {
					this.awardPoints(working);
					working.forget();
					Roome.getRoom(this.getX(), this.getY()).ds = null;
					
					//play a sound effect TODO make it only play for the player who put it in

						SoundPlayer play = new SoundPlayer ();
						play.playSoundEffect(GameCode.volume,"resources/sounds/effects/regester in.wav");
						NetworkHandler.getServer().sendMessage("SOUND:"  + "ALL" + ":resources/sounds/effects/regester in.wav");
					
					
					if (working.scrambled) {
						Register OG = this.getRegester();
						if (OG != null) {
							OG.memAddress = working.memAddress;
							OG.display.changeText(Integer.toHexString(working.memAddress));
						}
					}
					if (working.secondAddress != -1) {
						if (working.memAddress == memAddress) {
							DataSlot.getDataSlot(working.secondAddress).forget();
						} else {
							working.getDataSlot().forget();
						}
					}
					this.cleared = true;
					this.getAnimationHandler().setAnimationFrame(1);
					display = null;
				}
			}
		}
	}
	public void draw () {
		super.draw();
		if (display != null) {
			display.setX(this.getX() + 10);
			display.setY(this.getY() + 5);
			display.draw();
		}
		if (reward != null) {
			reward.setX(this.getX() - 10);
			reward.setY(this.getY() - 25);
			reward.draw();
		}
	}
	public void awardPoints (Register reg) {
		long pointsToAward = 10000;
		long deliveryTime = System.currentTimeMillis() - reg.spawnTime;
		pointsToAward = pointsToAward + ((180000 - deliveryTime)/2);
		if (pointsToAward < 10000) {
			pointsToAward = 10000;
		}
		reward = new Textbox ("+ " + Long.toString(pointsToAward));
		reward.changeBoxVisability();
		reward.setFont("text (lime green)");
		
		Hud.updateScore(pointsToAward);
	}

	public boolean isCleared() {
		return cleared;
	}

	public void setCleared(boolean cleared) {
		this.cleared = cleared;
	}
	
	@Override
	public void forget () {
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("FORGET DS:" + this.id);
		}
		super.forget();
	}
	
	@Override
	public String toString () {
		if (reward == null) {
			return getId () + " " + memAddress  + " " + cleared + " " + "null" + " " + this.getX() + " " + this.getY();
		} else {
			return getId () + " " + memAddress  + " " + cleared + " " + reward.getText().substring(2) + " " + this.getX() + " " + this.getY();
		}
	}
	
	public void refreshDataSlot (String info) {
		
		String [] infos = info.split(" ");
		if (display != null) {
			
			memAddress = Integer.parseInt (infos[1]);
			display.changeText(Integer.toHexString (memAddress).toUpperCase ());
		}
		if (Boolean.parseBoolean(infos[2])) {
			this.getAnimationHandler().setAnimationFrame(1);
			cleared = true;
		} else {
			cleared = false;
		}
		if (!infos[3].equals("null") && reward == null) {
			reward = new Textbox ("+ " + infos[3]);
			reward.changeBoxVisability();
			reward.setFont("text (lime green)");
			display = null;
		}
		this.setX(Double.parseDouble(infos[4]));
		this.setY(Double.parseDouble(infos[5]));
		updateTime = 0;
		Roome.getRoom(this.getX(), this.getY()).ds = this;
	}
	
}
