package items;

import engine.Sprite;
import gameObjects.Register;
import players.Bit;

public class DataScrambler extends Item {
	
	public DataScrambler () {
		this.setSprite(new Sprite ("resources/sprites/Data scrambler.png"));
		this.setHitboxAttributes(32, 32);
	}
	
	/**
	 * returns true if item use was succsefull false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
		if (user.regestersBeingCarried == null) {
			return false;
		} else {
			Register reg = (Register)user.regestersBeingCarried.get(0);
			reg.scramble();
			return true;
		}
	}
	
}
