package gameObjects;

import java.util.ArrayList;

import engine.GameObject;
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
	
	@Override
	public void frameEvent () {
		
		if (this.isColliding("Register") && !this.cleared) {
			ArrayList <GameObject> collidingRegesters = this.getCollisionInfo().getCollidingObjects();
			for (int i = 0; i < collidingRegesters.size(); i++) {
				
				Register working = (Register)collidingRegesters.get(i);
				if (working.getMemAddress() == memAddress) {
					this.awardPoints(working);
					working.forget();
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
