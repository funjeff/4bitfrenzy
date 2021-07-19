package npcs;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.RenderLoop;
import engine.Sprite;
import gameObjects.DataSlot;
import gameObjects.Register;
import network.NetworkHandler;
import resources.Hud;

public class LoadedDice extends NPC {

	private Sprite diceSprite = new Sprite ("resources/sprites/config/dice.txt");
	
	private int die1 = 1;
	private int die2 = 1;
	
	private int timer = 0;
	
	private int lastUsed = 0;
	private int useDenyTimer = 0;
	
	public LoadedDice (double x, double y) {
		super (x, y);
		setHitboxAttributes (66, 32);
	}
	
	public boolean roll () {
		if (lastUsed == Hud.roundNum) {
			if (timer == 0) {
				useDenyTimer = 150;
			}
			return false;
		} else {
			timer = 150;
			lastUsed = Hud.roundNum;
			return true;
		}
	}
	
	@Override
	public void frameEvent () {
		if (timer != 0) {
			timer--;
			Random r = new Random ();
			boolean diceWereUpdated = false;
			if (timer % 5 == 0) {
				die1 = r.nextInt (6) + 1;
				die2 = r.nextInt (6) + 1;
				diceWereUpdated = true;
			}
			if (timer == 0) {
				if (r.nextInt (4) == 0 || (die1 == 1 && die2 == 1)) {
					//Snake eyes (spawning in new registers is yet to be implemented)
					die1 = 1;
					die2 = 1;
				} else {
					//Successful, remove a register
					ArrayList<GameObject> registers = ObjectHandler.getObjectsByName ("Register");
					ArrayList<GameObject> dataSlots = ObjectHandler.getObjectsByName ("DataSlot");
					if (registers != null && registers.size () != 0) {
						int toTurnIn = r.nextInt (registers.size ());
						Register regToTurnIn = (Register)registers.get (toTurnIn);
						for (int i = 0; i < dataSlots.size (); i++) {
							if (((DataSlot)dataSlots.get (i)).memAddress == regToTurnIn.memAddress) {
								regToTurnIn.setX (dataSlots.get (i).getX ());
								regToTurnIn.setY (dataSlots.get (i).getY ());
							}
						}
					}
				}
				diceWereUpdated = true;
			}
			if (diceWereUpdated) {
				if (NetworkHandler.isHost ()) {
					NetworkHandler.getServer ().sendMessage ("NPC UPDATE " + toString ());
				}
			}
		}
		if (useDenyTimer != 0) {
			useDenyTimer--;
		}
		
	}
	
	@Override
	public void draw () {
		
		int drawX = (int)(getX () - GameCode.getViewX ());
		int drawY = (int)(getY () - GameCode.getViewY ());
		diceSprite.draw (drawX, drawY, die1 - 1);
		diceSprite.draw (drawX + 34, drawY, die2 - 1);
		
		if (useDenyTimer != 0) {
			
			//Set up the drawing color
			Graphics g = RenderLoop.wind.getBufferGraphics ();
			float alpha = useDenyTimer < 50 ? (float)useDenyTimer / 50 : 1;
			g.setColor (new Color (1, 0, 0, alpha));
			
			
			Font f = new Font ("Arial", 16, 16);
			g.setFont (f);
			int messageX = (int)(getX () - GameCode.getViewX()) - 32;
			int messageY = (int)(getY () - GameCode.getViewY ()) - 24;
			g.drawString ("Dice may only be", messageX + 8, messageY);
			g.drawString ("used once per wave", messageX, messageY + 16);
		}
		
	}
	
	@Override
	public String toString () {
		die1 = die1 == 0 ? 1 : die1;
		die2 = die2 == 0 ? 1 : die2; //These are somehow 0
		return super.toString () + ":" + die1 + ":" + die2;
	}
	
	@Override
	public void updateNpc (String s) {
		System.out.println (s);
		super.updateNpc (s);
		String[] params = s.split (":");
		die1 = Integer.parseInt (params [4]);
		die2 = Integer.parseInt (params [5]);
	}
	
}
