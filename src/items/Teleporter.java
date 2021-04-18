package items;

import engine.Sprite;

public class Teleporter extends Item {

	public Teleporter () {
		this.setSprite(new Sprite ("resources/sprites/teleporter.png"));
		this.setHitboxAttributes(32, 32);
	}
	
	/**
	 * returns true if item use was succsefull false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
		
	}
	
}
