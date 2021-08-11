package npcs;

import engine.GameObject;
import engine.Sprite;
import gameObjects.Highlightable;
import items.ChickenBucket;
import items.FriedFood;

public class Fryer extends NPC implements Highlightable {

	private Sprite fryerSprite = new Sprite ("resources/sprites/fryer.png");
	private Sprite fryerHighlight = new Sprite ("resources/sprites/fryer_highlight.png");
	
	public Fryer (double x, double y) {
		super (x, y);
		setSprite (fryerSprite);
		setHitboxAttributes (114, 63);
	}

	@Override
	public void frameEvent () {
		super.frameEvent ();
		if (isColliding ("Duck")) {
			GameObject obj = this.getCollisionInfo ().getCollidingObjects ().get (0);
			new ChickenBucket ().declare ((int)obj.getX (), (int)obj.getY ());
			obj.forget ();
		}
		if (isCollidingChildren ("PushableNPC")) {
			GameObject obj = this.getCollisionInfo ().getCollidingObjects ().get (0);
			new FriedFood ().declare ((int)obj.getX (), (int)obj.getY ());
			obj.forget ();
		}
	}
	
	@Override
	public boolean usesDefaultHightlight() {
		return false;
	}

	@Override
	public void highlight() {
		fryerHighlight.draw (getDrawX () - 2, getDrawY () - 3);
	}
	
}
