package gameObjects;

import java.util.ArrayList;

import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import players.Bit;
import resources.Hud;
import resources.Textbox;

public class DataSlot extends GameObject {
	
	int memAddress = 0;
	
	boolean cleared = false;
	
	Textbox display;
	
	Textbox reward;

	public int getMemAddress() {
		return memAddress;
	}

	public void setMemAddress(int memAddress) {
		this.memAddress = memAddress;
	}

	public DataSlot (int memAdress) {
		this.memAddress = memAdress;
		this.setSprite(new Sprite("resources/sprites/config/drive.txt"));
		this.getAnimationHandler().setAnimationFrame(0);
		display = new Textbox (Integer.toHexString(memAddress));
		display.changeBoxVisability();
		this.setRenderPriority(2);
		display.setFont("text (red)");
		this.setHitboxAttributes(84, 90);
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
	
	public DataSlot getDataSlot(int memAddress) {
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
		
		if (this.isColliding("Register") && !this.cleared) {
			ArrayList <GameObject> collidingRegesters = this.getCollisionInfo().getCollidingObjects();
			for (int i = 0; i < collidingRegesters.size(); i++) {
				
				Register working = (Register)collidingRegesters.get(i);
				if (working.getMemAddress() == memAddress || working.scrambled || working.secondAddress == memAddress) {
					this.awardPoints(working);
					working.forget();
					if (working.scrambled) {
						Register OG = this.getRegester();
						if (OG != null) {
							OG.memAddress = working.memAddress;
							OG.display.changeText(Integer.toHexString(working.memAddress));
						}
					}
					if (working.secondAddress != -1) {
						if (working.memAddress == memAddress) {
							this.getDataSlot(working.secondAddress).forget();
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
}
