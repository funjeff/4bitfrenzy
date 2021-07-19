package npcs;

import java.util.Random;

import engine.Sprite;

public class Trash extends PushableNPC {

	public Trash (double x, double y) {
		super (x, y);
		
		Random rand = new Random ();
		int sprite = rand.nextInt(5) + 1;
		
		Sprite trashSprite = new Sprite ("resources/sprites/trash " + sprite +".png");
		
		setSprite (trashSprite);
		setHitboxAttributes (trashSprite.getWidth(), trashSprite.getHeight());
		this.setFriction (.25);
	}
	
	
}
