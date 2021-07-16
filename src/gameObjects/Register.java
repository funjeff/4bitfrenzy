package gameObjects;

import java.util.ArrayList;

import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import map.Roome;
import network.NetworkHandler;
import players.Bit;
import resources.Hud;
import resources.Textbox;
import util.Vector2D;

public class Register extends GameObject {
	
	int memAddress = 17;
	
	int secondAddress = -1;
	
	Textbox display;
	
	public boolean scrambled = false;
	
	public boolean isLargeRegister = false;
	
	public boolean isBlue = false;
	
	long spawnTime;
	
	int updateTime = 0;
	
	ArrayList<Bit> bitsPushing = new ArrayList<Bit> ();
	Vector2D trajectory;
	
	boolean modified;
	
	public Register () {
		this.setSprite(new Sprite ("resources/sprites/Regester.png"));
		display = new Textbox ("00");
		display.changeBoxVisability();
		display.setFont("text (lime green)");
		this.setRenderPriority(1);
		this.setHitboxAttributes(98, 42);
	}
	
	public Register (int memAdress) {
		this ();
		setMemAddress (memAdress);
	}
	
	public void refreshRegister (String info) {
		String [] infos = info.split(" ");
		memAddress = Integer.parseInt(infos[1]);
		int num = Integer.parseInt (infos[1]);
		if (display != null) {
			display.changeText(Integer.toHexString (num).toUpperCase ());
		}
		if (Integer.parseInt(infos[2]) != -1) {
			this.setSprite(new Sprite ("resources/sprites/Regester combined.png"));
			secondAddress = Integer.parseInt(infos[2]);
		}
		
		if (Boolean.parseBoolean(infos[3])) {
			this.setSprite(new Sprite ("resources/sprites/Regester scrambled.png"));
			display = null;
		
		}
		
		this.isLargeRegister = Boolean.parseBoolean (infos[4]);
		if (isLargeRegister) {
			this.setSprite (new Sprite ("resources/sprites/Register large.png"));
		}
		
		this.isBlue = Boolean.parseBoolean(infos[5]);
		if (isBlue) {
			this.setSprite (new Sprite ("resources/sprites/Regester blue.png"));
		}
		
		this.setX(Double.parseDouble(infos[7]));
		this.setY(Double.parseDouble(infos[8]));
		
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
		display.changeText (Integer.toHexString(memAddress).toUpperCase());
		this.memAddress = memAddress;
	}
	@Override
	public boolean goX (double val) {
		double x = this.getX();
		Roome currentRoom = Roome.getRoom(this.getX(), this.getY());
		if (currentRoom.r == null) {
			currentRoom.r = this;
		}
		//Check register collision
		this.setX(val);
		if (currentRoom.isColliding(this) || this.isColliding("Register")) {
			this.setX(x);
			return false;
		}
		//Check collision for all bits
		double[] xCoords = new double[bitsPushing.size ()];
		boolean canAllMove = true;
		for (int i = 0; i < bitsPushing.size (); i++) {
			xCoords [i] = bitsPushing.get (i).getX ();
			if (!bitsPushing.get (i).goX (bitsPushing.get (i).getX () + val - x)) {
				canAllMove = false;
			}
		}
		if (isColliding ("Bit") || !canAllMove) {
			this.setX (x);
			for (int i = 0; i < bitsPushing.size (); i++) {
				bitsPushing.get (i).setX (xCoords [i]);
			}
			return false;
		}
		if (!currentRoom.equals(Roome.getRoom(this.getX(), this.getY()))) {
			currentRoom.r = null;
		}
		return true;
	}
	@Override
	public boolean goY (double val) {
		double y = this.getY();
		Roome currentRoom = Roome.getRoom(this.getX(), this.getY());
		if (currentRoom.r == null) {
			currentRoom.r = this;
		}
		this.setY(val);
		
		//Check collision for register
		if (currentRoom.isColliding(this) || this.isColliding("Register")) {
			this.setY(y);
			return false;
		}
		
		//Check collision for all bits
		double[] yCoords = new double[bitsPushing.size ()];
		boolean canAllMove = true;
		for (int i = 0; i < bitsPushing.size (); i++) {
			yCoords [i] = bitsPushing.get (i).getY ();
			if (!bitsPushing.get (i).goY (bitsPushing.get (i).getY () + val - y)) {
				canAllMove = false;
			}
		}
		
		if (isColliding ("Bit") || !canAllMove) {
			this.setY (y);
			for (int i = 0; i < bitsPushing.size (); i++) {
				bitsPushing.get (i).setY (yCoords [i]);
			}
			return false;
		}
		
		if (!currentRoom.equals(Roome.getRoom(this.getX(), this.getY()))) {
			currentRoom.r = null;
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
	
	public void makeLarge () {
		this.isLargeRegister = true;
		this.setSprite(new Sprite ("resources/sprites/Register large.png"));
	}
	
	public void makeBlue () {
		this.isBlue = true;
		this.setSprite(new Sprite ("resources/sprites/Regester blue.png"));
	}
	
	public void push (Bit bit, double x, double y) {
		if (!bitsPushing.contains (bit)) {
			bitsPushing.add (bit);
		}
		if (trajectory == null) {
			trajectory = new Vector2D (x, y);
		} else {
			trajectory.add (new Vector2D (x, y));
		}
	}
	
	@Override
	public void frameEvent () {
		
		//Scale the trajectory by the number of bits
		if (trajectory != null) {
			trajectory.scale ((double)1 / bitsPushing.size ());
		}
		
		//Check if this register can be pushed
		boolean canPush = isLargeRegister ? false : true; //Defaults to false for large registers
		//Check for powerhouse perk
		for (int i = 0; i < bitsPushing.size (); i++) {
			if (bitsPushing.get (i).perk == 3 || bitsPushing.get(i).isPoweredUp()) {
				canPush = true; //Only one bit needs to have powerhouse
			}
		}
		
		
		
		//Check for 2 or more bits
		if (bitsPushing.size () > 1) {
			canPush = true;
			//Apply speed boost for multiple bits pulling
			if (!isLargeRegister && trajectory != null && trajectory.getLength () != 0 && trajectory.getLength () < 5 /*Default bit speed is 5*/) {
				trajectory.normalize ();
				trajectory.scale (5);
			}
		}
		
		//Move the register
		if (canPush) {
			if (trajectory != null) {
				goX (getX () + trajectory.x);
				goY (getY () + trajectory.y);
			}
		}
		
		//Reset variables for next frame
		bitsPushing = new ArrayList<Bit> ();
		trajectory = null;
		
	}
	
	@Override
	public void forget () {
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage ("FORGET REGISTER:" + this.id);
		}
		if (NetworkHandler.isHost () && ObjectHandler.getObjectsByName ("Register").size () == 1) {
			Hud.waveOver ();
		}
		super.forget();
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
		return getId () + " " + memAddress + " " + secondAddress + " " + scrambled + " " + isLargeRegister + " " + isBlue + " " + spawnTime + " " + this.getX() + " " + this.getY();
	}
}
