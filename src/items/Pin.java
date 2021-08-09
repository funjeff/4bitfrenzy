package items;

import engine.Sprite;
import map.Roome;
import npcs.Basketball;
import npcs.Dartboard;
import players.Bit;
import util.DummyCollider;

public class Pin extends Item {
	
	public static Sprite pinSprite = new Sprite ("resources/sprites/pin.png");
	
	public Pin () {
		setSprite (pinSprite);
	}

	@Override
	public boolean useItem (Bit user) {
		DummyCollider dc = new DummyCollider ((int)user.getX () - Bit.HIGHLIGHT_RADIUS, (int)user.getY () - Bit.HIGHLIGHT_RADIUS, Bit.HIGHLIGHT_RADIUS * 2, Bit.HIGHLIGHT_RADIUS * 2);
		if (dc.isColliding ("Basketball")) {
			Basketball basketball = (Basketball)dc.getCollisionInfo ().getCollidingObjects ().get (0);
			basketball.pop ();
			return true;
		}
		if (dc.isColliding ("Dartboard")) {
			Roome.getRoom (user.getX (), user.getY ()).spawnObject (Cash.class); //TODO this is very hacky, find a better way pls
			return true;
		}
		return false;
	}
	
}
