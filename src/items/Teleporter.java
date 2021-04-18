package items;

import java.util.Random;

import engine.Sprite;
import map.Roome;
import players.Bit;

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
	 * returns true if item use was succsefully false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
		Random rand = new Random ();
		int [] telportCoords = Roome.map[rand.nextInt(10)][rand.nextInt(10)].biatch.getPosibleCoords(user.hitbox().width, user.hitbox().height);
		user.goX(telportCoords[0]);
		user.goY(telportCoords[1]);
		return true;
	}
	@Override
	public String toString () {
		return this.getX() + " " + this.getY();
	}	
}
