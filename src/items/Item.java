package items;

import engine.GameObject;
import map.Roome;
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
	
	public void pickUpItem (Bit pickUper) {
		
	}

	public void dropItem (Bit droper) {
		
		Roome romm = Roome.getRoom(droper.getX(), droper.getY());
		int [] spawnCoords = romm.biatch.getPosibleCoords(this.hitbox().width, this.hitbox().height);
		this.declare(spawnCoords[0], spawnCoords[1]);
	}
	
	public void refreshItem (String str) {
		String [] args = str.split(" ");
		this.setX(Integer.parseInt(args[2]));

		this.setY(Integer.parseInt(args[3]));
		
		uses = Integer.parseInt(args[4]);
	}
	
	@Override
	public String toString () {
		return getId () + " " + this.getClass().getName() + " " + (int)this.getX() + " " + (int)this.getY() + " " + uses;
	}
}