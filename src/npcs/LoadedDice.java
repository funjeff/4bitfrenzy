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
import resources.SoundPlayer;

public class LoadedDice extends NPC {

	private Sprite diceSprite = new Sprite ("resources/sprites/config/dice.txt");
	
	private int die1 = 1;
	private int die2 = 1;
	
	private int timer = -150;
	
	private int lastUsed = -1;
	private int useDenyTimer = 0;
	
	private int updates = 0;
	
	public LoadedDice (double x, double y) {
		super (x, y);
		setHitboxAttributes (86, 120);
	}
	
	public boolean roll () {
		//TODO deny message can show for host when client tries to use dice (not that important)
		if (useable () && NetworkHandler.isHost ()) {
			timer = 150; //Only host can actually roll the dice
			lastUsed = Hud.roundNum;
			return true;
		}
		if (!useable () && timer <= 0) {
			useDenyTimer = 150; //Hint text is client side
		}
		return false;
	}
	
	public boolean useable () {
		return lastUsed != Hud.roundNum;
	}
	
	@Override
	public void frameEvent () {
		if (NetworkHandler.isHost ()) {
			if (timer > 0) {
				timer--;
				Random r = new Random ();
				boolean diceWereUpdated = false;
				if (timer % 5 == 0) {
					die1 = r.nextInt (6) + 1;
					die2 = r.nextInt (6) + 1;
					diceWereUpdated = true;
				}
				if (timer % 10 == 0) {
					SoundPlayer play = new SoundPlayer ();
					play.playSoundEffect(GameCode.volume,"resources/sounds/effects/pickup.wav");
					NetworkHandler.getServer().sendMessage("SOUND:"  + 2 + ":resources/sounds/effects/pickup.wav");
					NetworkHandler.getServer().sendMessage("SOUND:"  + 3 + ":resources/sounds/effects/pickup.wav");
					NetworkHandler.getServer().sendMessage("SOUND:"  + 4 + ":resources/sounds/effects/pickup.wav");
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
		} else {
			if (timer > 0) {
				timer--;
			}
		}
		
		if (useDenyTimer != 0) {
			useDenyTimer--;
		}
		if (timer <= 0 && timer > -150) {
			timer--;
		}
		
	}
	
	@Override
	public void draw () {
		
		int drawX = (int)(getX () - GameCode.getViewX ());
		int drawY = (int)(getY () - GameCode.getViewY ());
		if (timer != -150) {
			diceSprite.draw (drawX, drawY, die1 - 1);
			diceSprite.draw (drawX + 34, drawY, die2 - 1);
		}
		
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
		super.updateNpc (s);
		String[] params = s.split (":");
		die1 = Integer.parseInt (params [4]);
		die2 = Integer.parseInt (params [5]);
		if (timer == -150 && updates > 0) {
			timer = 150;
			lastUsed = Hud.roundNum;
		}
		updates++;
	}
	
}
