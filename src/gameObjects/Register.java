package gameObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.RenderLoop;
import engine.Sprite;
import map.Roome;
import network.NetworkHandler;
import players.Bit;
import resources.Hud;
import resources.Textbox;
import util.Vector2D;

public class Register extends GameObject implements Highlightable {
	
	public static final int LARGE_HINT_DURATION = 300;
	public static final int LARGE_HINT_FADE = 100;
	
	public static Sprite navArrow = new Sprite ("resources/sprites/register_nav_arrow.png");
	public static Sprite regOutline = new Sprite ("resources/sprites/register_hint.png");
	
	public int memAddress = 17;
	
	public int secondAddress = -1;
	
	Textbox display;
	
	public boolean scrambled = false;
	
	public boolean isLargeRegister = false;
	
	public boolean isBlue = false;
	
	public boolean showNavArrow = true;
	
	long spawnTime;
	
	int updateTime = 0;
	
	ArrayList<Bit> bitsPushing = new ArrayList<Bit> ();
	Vector2D trajectory;
	
	boolean modified;
	
	private int largeHintTime;
	
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
		return true;
	}
	@Override
	public boolean goY (double val) {
		double y = this.getY();
		Roome currentRoom = Roome.getRoom(this.getX(), this.getY());
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
				canPush = true; //Only one bit needs to have powerhouse or be powered up
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
		
		if (!canPush && trajectory != null && largeHintTime != 1) {
			largeHintTime = LARGE_HINT_DURATION;
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
		super.forget();
	}
	
	@Override
	public void draw () {
		
		//Draw the register
		super.draw();
		
		//Draw what this is
		if (display != null) {
			if (secondAddress == -1) {
				display.setX(this.getX() + 35);
				display.setY(this.getY() + 12);
				display.draw();
			} else {
				display.setX(this.getX() + 8);
				display.setY(this.getY() + 12);
				display.changeText(Integer.toHexString(memAddress) + " " + Integer.toHexString(secondAddress));
				display.draw();
			}
		}
		
		//Draw the nav arrow
		if (showNavArrow) {
			try {
			DataSlot slot = null;
			ArrayList<GameObject> slots = ObjectHandler.getObjectsByName ("DataSlot");
			for (int i = 0; i < slots.size (); i++) {
				if (((DataSlot)slots.get (i)).getMemAddress() == this.getMemAddress ()) {
					slot = (DataSlot)slots.get (i);
				}
			}
			double dir = -Math.atan2 (slot.getCenterX () - getCenterX (), slot.getCenterY () - getCenterY ()) + Math.PI / 2;
			dir = Compass.getNotchedDirection (dir, Compass.ARROW_NOTCHES);
			int drawX = (int)(getX () - GameCode.getViewX ()) + 36;
			int drawY = (int)(getY () - GameCode.getViewY ()) - 28;
			navArrow.drawRotated (drawX, drawY, 0, 15, 9, dir);
			} catch (NullPointerException e) {
				//Do nothing, data slot has not been created yet
			}
		}
		
		//Draw the hint if needed
		if (largeHintTime > 1) { //Incredibly hacky way to get the message to show only once
			
			//Calculate transparency
			float alpha;
			if (largeHintTime < LARGE_HINT_FADE) {
				alpha = ((float)largeHintTime / LARGE_HINT_FADE);
			} else {
				alpha = 1;
			}
			
			Graphics g = RenderLoop.wind.getBufferGraphics ();
			g.setColor (new Color (0f, 0f, 0f, alpha));
			g.fillRect (getDrawX () - 36, getDrawY () - 84, 178, 48);
			g.setFont (new Font ("Arial", 16, 16));
			g.setColor (new Color (1f, 0f, 0f, alpha));
			g.drawString ("Two players are needed", getDrawX () - 32, getDrawY () - 64);
			g.drawString ("to move red registers.", getDrawX () - 26, getDrawY () - 44);
			largeHintTime--;
		}
		
	}
	@Override
	public String toString () {
		return getId () + " " + memAddress + " " + secondAddress + " " + scrambled + " " + isLargeRegister + " " + isBlue + " " + spawnTime + " " + this.getX() + " " + this.getY();
	}

	@Override
	public boolean usesDefaultHightlight() {
		return false;
	}

	@Override
	public void highlight() {
		regOutline.draw ((int)(getX () - GameCode.getViewX () - 3), (int)(getY () - GameCode.getViewY () - 3));
	}
}
