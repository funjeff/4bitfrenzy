package items;

import engine.Sprite;
import map.Roome;
import players.Bit;

public class Cash extends Item {
	
	public Cash () {
		this.setSprite(new Sprite ("resources/sprites/dollar bill.png"));
		this.setHitboxAttributes(32, 32);
	}

	/**
	 * returns true if item use was succsefull false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
			
			this.dropItem(user);
			
			System.out.println(user.getSpeed());
			
			return true;
	}
	@Override
	public void pickUpItem (Bit pickuper) {

		pickuper.setSpeed(pickuper.getSpeed() + 2);
		
	}
	@Override
	public void dropItem (Bit dropper) {
		super.dropItem(dropper);
		
		dropper.setSpeed(dropper.getSpeed() - 2);
	}
}
