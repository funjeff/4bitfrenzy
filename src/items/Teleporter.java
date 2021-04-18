package items;

import engine.Sprite;

public class Teleporter extends Item {

	public Teleporter () {
		this.setSprite(new Sprite ("resources/sprites/teleporter.png"));
		this.setHitboxAttributes(32, 32);
	}
	public void refresh(String info) {
		String [] infos = info.split(" ");
		this.setX(Double.parseDouble(infos[0]));
		this.setY(Double.parseDouble(infos[1]));
	}
	/**
	 * returns true if item use was succsefull false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
		
	}
	@Override
	public String toString () {
		return this.getX() + " " + this.getY();
	}	
}
