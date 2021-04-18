package gameObjects;

import java.util.ArrayList;

import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import map.Roome;
import resources.Textbox;

public class Register extends GameObject {
	
	int memAddress = 17;
	
	Textbox display;
	
	long spawnTime;
	public Register (int memAdress) {
		this.memAddress = memAdress;
		this.setSprite(new Sprite ("resources/sprites/Regester.png"));
		display = new Textbox (Integer.toHexString(memAddress).toUpperCase());
		display.changeBoxVisability();
		this.setRenderPriority(1);
		display.setFont("text (lime green)");
		this.setHitboxAttributes(98, 42);
	}
	@Override 
	public void onDeclare () {
		spawnTime = System.currentTimeMillis();
	}
	public int getMemAddress() {
		return memAddress;
	}
	public void setMemAddress(int memAddress) {
		this.memAddress = memAddress;
	}
	@Override
	public boolean goX (double val) {
		double x = this.getX();
		Roome currentRoom = Roome.getRoom(this.getX(), this.getY());
		this.setX(val);
		if (currentRoom.isColliding(this) || this.isColliding("Register")) {
			this.setX(x);
			return false;
		}
		return true;
	}
	@Override
	public boolean goY (double val) {
		double y = this.getY();
		this.setY(val);
		Roome currentRoom = Roome.getRoom(this.getX(), this.getY());
		if (currentRoom.isColliding(this) || this.isColliding("Register")) {
			this.setY(y);
			return false;
		}
		return true;
	}
	
	public DataSlot getDataSlot() {
		ArrayList <GameObject> slots = ObjectHandler.getObjectsByName("DataSlot");
		for (int i = 0; i < slots.size(); i++) {
			DataSlot working = (DataSlot)slots.get(i);
			
			if (working.memAddress == this.memAddress) {
				return working;
			}
			
		}
		return null;
	}
	
	@Override
	public void draw () {
		super.draw();
		display.setX(this.getX() + 35);
		display.setY(this.getY() + 5);
		display.draw();
	}
	
	
}
