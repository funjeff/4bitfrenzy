package items;

import java.util.Random;

import engine.GameCode;
import engine.ObjectHandler;
import engine.Sprite;
import gameObjects.Register;
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
		if (ObjectHandler.getObjectsByName("Register") != null && ObjectHandler.getObjectsByName("Register").size() != 0) {
			Random rand = new Random ();
			if (NetworkHandler.isHost()) {
				SoundPlayer play = new SoundPlayer ();
				play.playSoundEffect(GameCode.volume,"resources/sounds/effects/teleporter.wav");
			} else {
				NetworkHandler.getServer().sendMessage("SOUND:"  + user.playerNum + ":resources/sounds/effects/teleporter.wav");
			}
			int [] telportCoords;
			while (true) {
				telportCoords = Roome.map[rand.nextInt(Roome.getMapHeight ())][rand.nextInt(Roome.getMapWidth ())].getSpawningMask ().getPosibleCoords(user.hitbox().width, user.hitbox().height);
				if (Roome.getRoom(telportCoords[0], telportCoords[1]).r != null ) {
					break;
				}
			}
			user.goX(telportCoords[0]);
			user.goY(telportCoords[1]);
			return true;
		} else {
			return false;
		}
	}
	
	public String getName () {
		return "Teleporter";
	}
	
	public String getDesc () {
		return "Teleports you to a room\n"
				+ "with a register in it.";
	}
	
	public String getLongDescription () {
		return "default_long_desc";
	}
	
	public String getItemFlavor () {
		return "zoom";
	}
}
