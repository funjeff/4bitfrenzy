package gameObjects;

import java.util.ArrayList;

import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import map.Roome;
import network.NetworkHandler;
import resources.Textbox;

public class Register extends GameObject {
	
	int memAddress = 17;
	
	int secondAddress = -1;
	
	Textbox display;
	
	public boolean scrambled = false;
	
	long spawnTime;
	
	int updateTime = 0;
	
	boolean modified;
	
	String prevEncoding = null;
	
	public Register (int memAdress) {
		this.memAddress = memAdress;
		this.setSprite(new Sprite ("resources/sprites/Regester.png"));
		display = new Textbox (Integer.toHexString(memAddress).toUpperCase());
		display.changeBoxVisability();
		display.setFont("text (lime green)");
		this.setRenderPriority(1);
		this.setHitboxAttributes(98, 42);
	}
	
	public void refreshRegister (String info) {
		String [] infos = info.split(" ");
		memAddress = Integer.parseInt(infos[1]);
		display.changeText(infos[1]);
		if (Integer.parseInt(infos[2]) != -1) {
			this.setSprite(new Sprite ("resources/sprites/Regester combined.png"));
			secondAddress = Integer.parseInt(infos[2]);
		}
		
		if (Boolean.parseBoolean(infos[3])) {
			this.setSprite(new Sprite ("resources/sprites/Regester scrambled.png"));
		
		}
		this.setX(Double.parseDouble(infos[5]));
		this.setY(Double.parseDouble(infos[6]));
		this.updateTime = 0;
		
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
	
	
	
	public void scramble () {
		scrambled = true;
		display = null;
		this.setSprite(new Sprite ("resources/sprites/Regester scrambled.png"));
		
	}
	
	public void combine (Register reg) {
		secondAddress = reg.memAddress;
		reg.forget();
		this.setSprite(new Sprite ("resources/sprites/Regester combined.png"));
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent ();
		
	}
	
	@Override
	public void draw () {
		super.draw();
		if (display != null) {
			if (secondAddress == -1) {
				display.setX(this.getX() + 35);
				display.setY(this.getY() + 5);
				display.draw();
			} else {
				display.setX(this.getX() + 8);
				display.setY(this.getY() + 5);
				display.changeText(Integer.toHexString(memAddress) + " " + Integer.toHexString(secondAddress));
				display.draw();
			}
		}
	}
	@Override
	public String toString () {
		prevEncoding = getId () + " " + memAddress + " " + secondAddress + " " + scrambled + " " + spawnTime + " " + this.getX() + " " + this.getY();
		return prevEncoding;
	}
	public boolean wasUpdated () {
		return prevEncoding == null || !prevEncoding.equals (toString ());
	}
}
