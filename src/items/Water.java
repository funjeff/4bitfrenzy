package items;

import engine.GameCode;
import engine.Sprite;
import gameObjects.DataSlot;
import gameObjects.Register;
import network.NetworkHandler;
import players.Bit;
import resources.SoundPlayer;
import util.DummyCollider;

public class Water extends Item {
	
	public Water () {
		this.setSprite(new Sprite ("resources/sprites/waterr.png"));
		this.setHitboxAttributes(32, 32);
	}

	/**
	 * returns true if item use was succsefull false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
		DummyCollider dc = new DummyCollider ((int)user.getX () - Bit.HIGHLIGHT_RADIUS, (int)user.getY () - Bit.HIGHLIGHT_RADIUS, Bit.HIGHLIGHT_RADIUS * 2, Bit.HIGHLIGHT_RADIUS * 2);
		if (dc.isColliding ("DataSlot")) {
			DataSlot ds = (DataSlot)dc.getCollisionInfo ().getCollidingObjects ().get (0);
			ds.scramble ();
			return true;
		}
		return false;
	}
	
	public String getName () {
		return "Water";
	}
	
	public String getDesc () {
		return "Use this item on a data slot\n"
				+ "to scramble it. Scrambled\n"
				+ "data slots will not deduct\n"
				+ "lives at the end of a wave.";
	}
	
	public String getLongDescription () {
		return "default_long_desc";
	}
	
	public String getItemFlavor () {
		return "drinkable... probably";
	}
}
