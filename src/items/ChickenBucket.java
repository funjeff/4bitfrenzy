package items;

import java.util.ArrayList;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import network.NetworkHandler;
import players.Bit;
import resources.Hud;
import resources.SoundPlayer;

public class ChickenBucket extends Item {
	
	public ChickenBucket () {
		this.setSprite(new Sprite ("resources/sprites/kfc bucket.png"));	
		this.setHitboxAttributes(32, 32);
	}
	/**
	 * returns true if item use was succsefull false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
		if (NetworkHandler.isHost()) {
			SoundPlayer play = new SoundPlayer ();
			play.playSoundEffect(GameCode.volume,"resources/sounds/effects/speed.wav");
		} else {
			NetworkHandler.getServer().sendMessage("SOUND:"  + user.playerNum + ":resources/sounds/effects/speed.wav");
		}
		
			Hud.setLives(Hud.getLives() + 3);
			user.powerUpTemporarly();
			user.speedUpTemporarly();
		
		return true;
	}
	
	public String getName () {
		return "KFC Bucket";
	}
	
	public String getDesc () {
		return "Eating fried chicken heals\n"
				+ "three lives, and also grants\n"
				+ "temporary boosts to speed\n"
				+ "and strength (ability to carry\n"
				+ "red registers).";
	}
	
	public String getLongDescription () {
		return "default_long_desc";
	}
	
	public String getItemFlavor () {
		return "less than 101% rat meat or your money back";
	}
}
