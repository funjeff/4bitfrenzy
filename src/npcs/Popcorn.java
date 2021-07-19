package npcs;

import java.util.Random;

import engine.Sprite;

public class Popcorn extends PushableNPC {

	public Popcorn (double x, double y) {
		super (x, y);
		
		Random rand = new Random ();
		int sprite = rand.nextInt(5) + 1;
		
		Sprite popcornSprite = new Sprite ("resources/sprites/Popcorn kernel " + sprite +".png");
		
		setSprite (popcornSprite);
		setHitboxAttributes (16, 16);
		this.setFriction (.25);
	}
	
	
}
