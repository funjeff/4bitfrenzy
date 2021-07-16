package items;

import engine.GameObject;
import map.Roome;
import players.Bit;

public class Item extends GameObject {
	
	public boolean pickupablity = true;
	
	public int uses = 2;
	
	private Bit dropper;
	
	public Item () {
		
	}

	/**
	 * returns true if item use was succsefull false otherwise
	 */
	public boolean useItem (Bit user) {
		return true;
	
	}
	
	public void pickUpItem (Bit pickUper) {
		Thread.dumpStack ();
	}

	public void dropItem (Bit droper) {
		
		Roome romm = Roome.getRoom(droper.getX(), droper.getY());
		this.dropper = droper;
		this.declare ((int)droper.getX (), (int)droper.getY ());
		
	}
	
	public void refreshItem (String str) {
		String [] args = str.split(" ");
		this.setX(Integer.parseInt(args[2]));

		this.setY(Integer.parseInt(args[3]));
		
		uses = Integer.parseInt(args[4]);
	}
	
	public Bit getDropper () {
		return dropper;
	}
	
	@Override
	public void frameEvent () {
		if (dropper != null && !this.isColliding (dropper)) {
			dropper = null;
		}
	}
	
	@Override
	public String toString () {
		return getId () + " " + this.getClass().getName() + " " + (int)this.getX() + " " + (int)this.getY() + " " + uses;
	}
}