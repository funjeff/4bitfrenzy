package titleScreen;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;
import gameObjects.Register;
import resources.SoundPlayer;

public class TitleBit extends GameObject {

	Sprite sprite = new Sprite ("resources/sprites/config/bits.txt");
	
	private TitleRegister carried = null;

	public TitleBit () {
		setSprite (sprite);
		getAnimationHandler ().setAnimationFrame ((int)(Math.random () * 4));
		setRenderPriority (100);
		setHitboxAttributes (21, 16);
	}
	
	@Override
	public void frameEvent () {
		
		//Assert the default speed
		double speed = 5;
		
		//Check for the start carrying condition
		this.setX (getX () - speed);
		this.setY (getY () - speed);
		setHitboxAttributes (21 + speed * 2, 16 + speed * 2);
		if (carried == null && this.isColliding ("TitleRegister")) {
			if (keyDown (KeyEvent.VK_SHIFT)) {
				carried = (TitleRegister)this.getCollisionInfo ().getCollidingObjects ().get (0);
				SoundPlayer play = new SoundPlayer ();
				play.playSoundEffect(GameCode.volume,"resources/sounds/effects/pickup.wav");
			}
		}
		this.setX (getX () + speed);
		this.setY (getY () + speed);
		setHitboxAttributes (21, 16);
		
		//Stop carrying if shift is not held
		if (!keyDown (KeyEvent.VK_SHIFT)) {
			carried = null;
		}
		
		//Slow down the player if they're carrying the TitleRegister
		if (carried != null) {
			speed /= 2;
		}
		
		//Handle movement
		double startX = getX ();
		double startY = getY ();
		if (keyDown ('W')) {
			setY (getY () - speed);
		}
		if (keyDown ('A')) {
			setX (getX () - speed);
		}
		if (keyDown ('S')) {
			setY (getY () + speed);
		}
		if (keyDown ('D')) {
			setX (getX () + speed);
		}
		
		//Handle the pushing and pulling of the TitleRegister
		double diffX = getX () - startX;
		double diffY = getY () - startY;
		if (carried != null) {
			if (!carried.goX (carried.getX () + diffX)) {
				setX (startX);
			}
			if (!carried.goY (carried.getY () + diffY)) {
				setY (startY);
			}
		} else {
			double finalX = startX + diffX;
			double finalY = startY + diffY;
			setX (startX + diffX);
			setY (startY);
			if (this.isColliding ("TitleRegister")) {
				TitleRegister r = (TitleRegister)this.getCollisionInfo ().getCollidingObjects ().get (0);
				if (!r.goX (r.getX () + diffX)) {
					finalX = startX;
				}
			}
			setX (startX);
			setY (startY + diffY);
			if (this.isColliding ("TitleRegister")) {
				TitleRegister r = (TitleRegister)this.getCollisionInfo ().getCollidingObjects ().get (0);
				if (!r.goY (r.getY () + diffY)) {
					finalY = startY;
				}
			}
			setX (finalX);
			setY (finalY);
		}
		
		//Check for OOB
		Rectangle r = new Rectangle (0, 0, GameCode.getSettings ().getResolutionX (), GameCode.getSettings ().getResolutionY ());
		if (!r.contains (hitbox ())) {
			setX (startX);
			setY (startY);
		}
		
	}
	
}
