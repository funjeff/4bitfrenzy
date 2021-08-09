package items;

import engine.Sprite;
import npcs.Truck;
import players.Bit;
import util.DummyCollider;

public class Lighter extends Item {
	
	public static Sprite lighterSprite = new Sprite ("resources/sprites/lighter.png");
	
	public Lighter () {
		setSprite (lighterSprite);
	}
	
	@Override
	public boolean useItem (Bit user) {
		DummyCollider dc = new DummyCollider ((int)user.getX () - Bit.HIGHLIGHT_RADIUS, (int)user.getY () - Bit.HIGHLIGHT_RADIUS, Bit.HIGHLIGHT_RADIUS * 2, Bit.HIGHLIGHT_RADIUS * 2);
		if (dc.isColliding ("Truck")) {
			Truck truck = (Truck)dc.getCollisionInfo ().getCollidingObjects ().get (0);
			truck.lightOnFire ();
			return true;
		}
		return false;
	}
	
}
