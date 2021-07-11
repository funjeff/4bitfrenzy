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

public class BrokenTeleporter extends Item {

	public BrokenTeleporter () {
		this.setSprite(new Sprite ("resources/sprites/broken teleporter.png"));
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
				telportCoords = Roome.map[rand.nextInt(Roome.getMapHeight ())][rand.nextInt(Roome.getMapWidth ())].biatch.getPosibleCoords(user.hitbox().width, user.hitbox().height);
				if (Roome.getRoom(telportCoords[0], telportCoords[1]).r != null ) {
					break;
				}
			}
			
			Bit b = (Bit) ObjectHandler.getObjectsByName("Bit").get(rand.nextInt(ObjectHandler.getObjectsByName("Bit").size()));
			
			b.goX(telportCoords[0]);
			b.goY(telportCoords[1]);
			return true;
		} else {
			return false;
		}
	}
}
