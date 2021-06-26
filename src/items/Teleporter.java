package items;

import java.util.Random;

import engine.GameCode;
import engine.Sprite;
import map.Roome;
import network.NetworkHandler;
import players.Bit;
import resources.SoundPlayer;

public class Teleporter extends Item {

	public Teleporter () {
		this.setSprite(new Sprite ("resources/sprites/teleporter.png"));
		this.setHitboxAttributes(32, 32);
	}
	
	/**
	 * returns true if item use was succsefully false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
		Random rand = new Random ();
		if (NetworkHandler.isHost()) {
			SoundPlayer play = new SoundPlayer ();
			play.playSoundEffect(GameCode.volume,"resources/sounds/effects/teleporter.wav");
		} else {
			NetworkHandler.getServer().sendMessage("SOUND:"  + user.playerNum + ":resources/sounds/effects/teleporter.wav");
		}
		int [] telportCoords = Roome.map[rand.nextInt(Roome.getMapHeight ())][rand.nextInt(Roome.getMapWidth ())].biatch.getPosibleCoords(user.hitbox().width, user.hitbox().height);
		user.goX(telportCoords[0]);
		user.goY(telportCoords[1]);
		return true;
	}
}
