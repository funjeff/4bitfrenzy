package items;

import engine.GameObject;
import players.Bit;

public class Item extends GameObject {
	
	public boolean pickupablity = true;
	
	public int uses = 2;
	
	public Item () {
		
	}

	/**
	 * returns true if item use was succsefull false otherwise
	 */
	public boolean useItem (Bit user) {
		return true;
	
	}
}