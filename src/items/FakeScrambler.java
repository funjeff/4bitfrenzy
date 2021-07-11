package items;

import java.util.Random;

import engine.GameCode;
import engine.ObjectHandler;
import engine.Sprite;
import gameObjects.Register;
import network.NetworkHandler;
import players.Bit;
import resources.SoundPlayer;

public class FakeScrambler extends Item {
	
	public FakeScrambler () {
		this.setSprite(new Sprite ("resources/sprites/fake scrambler.png"));
		this.setHitboxAttributes(32, 32);
	}

	/**
	 * returns true if item use was succsefull false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
		if (user.regestersBeingCarried == null) {
			
			if (NetworkHandler.isHost()) {
				SoundPlayer play = new SoundPlayer ();
				play.playSoundEffect(GameCode.volume,"resources/sounds/effects/wrong.wav");
			} else {
				NetworkHandler.getServer().sendMessage("SOUND:"  + user.playerNum + ":resources/sounds/effects/wrong.wav");
			}
			
			
			return false;
		} else {
			
			
			Random rand = new Random ();
			
			Register reg1 = (Register) user.regestersBeingCarried.get(0);
			
			if (ObjectHandler.getObjectsByName("Register") != null && ObjectHandler.getObjectsByName("Register").size() > 1) {
				Register reg2 = (Register) ObjectHandler.getObjectsByName("Register").get(rand.nextInt(ObjectHandler.getObjectsByName("Register").size()));
				while (reg2.equals(reg1)) {
					reg2 = (Register) ObjectHandler.getObjectsByName("Register").get(rand.nextInt(ObjectHandler.getObjectsByName("Register").size()));
				}
				
				int x1 = (int) reg1.getX();
				int y1 = (int) reg1.getY();
				
				reg1.setX(reg2.getX());
				reg1.setY(reg2.getY());
				
				reg2.setX(x1);
				reg2.setY(y1);
				
				if (NetworkHandler.isHost()) {
					SoundPlayer play = new SoundPlayer ();
					play.playSoundEffect(GameCode.volume,"resources/sounds/effects/scrambler.wav");
				} else {
					NetworkHandler.getServer().sendMessage("SOUND:"  + user.playerNum + ":resources/sounds/effects/scrambler.wav");
				}
			
			} else {
				
				if (NetworkHandler.isHost()) {
					SoundPlayer play = new SoundPlayer ();
					play.playSoundEffect(GameCode.volume,"resources/sounds/effects/wrong.wav");
				} else {
					NetworkHandler.getServer().sendMessage("SOUND:"  + user.playerNum + ":resources/sounds/effects/wrong.wav");
				}
	
			}
			
			return true;
		}
	}
}
