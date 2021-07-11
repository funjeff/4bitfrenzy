package npcs;

import engine.Sprite;
import items.BasketBomb;
import items.Bombs;

public class Hoop extends NPC {

	public static Sprite hoopSprite = new Sprite ("resources/sprites/hoop.png");
	
	public Hoop (double x, double y) {
		super (x, y);
		setSprite (hoopSprite);
		setHitboxAttributes (52, 75);
	}
	
	@Override
	public void npcFrame () {
		if (this.isColliding ("Basketball")) {
			((ItemGenerator)(this.getCollisionInfo ().getCollidingObjects ().get (0))).becomeItem (BasketBomb.class);
		}
	}
	
	@Override
	public void forget () {
		Thread.dumpStack ();
	}
	
}
