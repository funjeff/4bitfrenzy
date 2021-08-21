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

public class FriedFood extends Item {
	
	public FriedFood () {
		this.setSprite(new Sprite ("resources/sprites/drumstick.png"));	
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
		
			Hud.setLives(Hud.getLives() - 1);
			user.speedUpTemporarly();
		
		return true;
	}
	
	public String getName () {
		return "Fried Food";
	}
	
	public String getDesc () {
		return "Gives you a speed boost,\n"
				+ "but it's bad for your health...";
	}
	
	public String getLongDescription () {
		return "default_long_desc";
	}
	
	public String getItemFlavor () {
		return "Made with no preservatives or artificial ingredients";
	}
}
