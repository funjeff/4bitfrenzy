package items;

import java.util.ArrayList;

import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import players.Bit;

public class Speed extends Item {
	
	public Speed () {
		this.setSprite(new Sprite ("resources/sprites/speed.png"));	
		this.setHitboxAttributes(32, 32);
	}
	/**
	 * returns true if item use was succsefull false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
		ArrayList <GameObject> bits = ObjectHandler.getObjectsByName("Bit");
		for (int i = 0; i < bits.size(); i++) {
			Bit bit = (Bit) bits.get(i);
			bit.speedUpTemporarly();
		}
		return true;
	}
}
