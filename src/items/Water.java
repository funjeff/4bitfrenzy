package items;

import engine.GameCode;
import engine.Sprite;
import gameObjects.Register;
import network.NetworkHandler;
import players.Bit;
import resources.SoundPlayer;

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
		if (user.regestersBeingCarried == null) {
			return false;
		} else {
			if (NetworkHandler.isHost()) {
				SoundPlayer play = new SoundPlayer ();
				play.playSoundEffect(GameCode.volume,"resources/sounds/effects/scrambler.wav");
			} else {
				NetworkHandler.getServer().sendMessage("SOUND:"  + user.playerNum + ":resources/sounds/effects/scrambler.wav");
			}
			Register reg = (Register)user.regestersBeingCarried.get(0);
			reg.scramble();
			return true;
		}
	}
	
	public String getName () {
		return "Water";
	}
	
	public String getDesc () {
		return "Use this item while grabbing\n"
				+ "a register to scramble it.";
	}
	
	public String getLongDescription () {
		return "default_long_desc";
	}
	
	public String getItemFlavor () {
		return "drinkable... probably";
	}
}
