package npcs;

import java.awt.Rectangle;

import engine.GameCode;
import engine.Sprite;
import gameObjects.Highlightable;
import map.Roome;
import network.NetworkHandler;
import resources.SoundPlayer;

public class Truck extends NPC implements Highlightable {
	
	public static Sprite truckSprite = new Sprite ("resources/sprites/truck.png");
	public static Sprite truckHighlight = new Sprite ("resources/sprites/truck_highlight.png");
	
	private boolean isRunning = false;
	private boolean isDriving = false;
	private int driveCountdown = 120;
	private int smokeTimer = 1;
	
	public Truck (double x, double y) {
		super (x, y);
		setSprite (truckSprite);
		this.setHitboxAttributes (106, 49);
	}
	
	@Override
	public void frameEvent () {
		if (this.isColliding("CarKey")) {
			isRunning = true;
			getCollisionInfo ().getCollidingObjects ().get (0).forget ();
		}
		if (isRunning) {
			driveCountdown--;
			if (driveCountdown == 0) {
				isDriving = true;
			}
			smokeTimer--;
			if (smokeTimer == 0) {
				new SmokePuff (getX () - 18, getY () + 20);
				smokeTimer = 30;
			}
		}
		if (isDriving) {
			if (!this.goX(this.getX() + 6)) {
				this.setX(this.getX() + 6);
				Rectangle rect9 = new Rectangle ((int)(864 + Roome.getRoom(this.getX(),this.getY()).getX()), (int)(252 + Roome.getRoom(this.getX(), this.getY()).getY()),216,144);
				if (this.hitbox().intersects(rect9)) {
					Roome currRoome = Roome.getRoom(this.getX(), this.getY());
					currRoome.destroyRightWall();
					currRoome.update (0); //Replace the room's background with nothing
					NetworkHandler.getServer ().sendMessage ("RUPDATE:" + (int)(getX () / 1080) + "," + (int)(getY () / 720) + "," + 0);
					if (NetworkHandler.isHost()) {
						SoundPlayer play = new SoundPlayer ();
						play.playSoundEffect(GameCode.volume,"resources/sounds/effects/bomb.wav");
					} else {
						NetworkHandler.getServer().sendMessage("SOUND:"  + NetworkHandler.getPlayerNum() + ":resources/sounds/effects/bomb.wav");
					}
					if (Roome.getRoom(getX (), getY ()).getX () >= 9720) {
						forget ();
					}
				}
			}
		}
	}
	
	public void lightOnFire () {
		this.forget ();
		//Destroy all walls in the current room
		Roome.getRoom (getX (), getY ()).destroyTopWall ();
		Roome.getRoom (getX (), getY ()).destroyBottomWall ();
		Roome.getRoom (getX (), getY ()).destroyLeftWall ();
		Roome.getRoom (getX (), getY ()).destroyRightWall ();
		//Play sound effects
		if (NetworkHandler.isHost()) {
			SoundPlayer play = new SoundPlayer ();
			play.playSoundEffect(GameCode.volume,"resources/sounds/effects/bomb.wav");
		} else {
			NetworkHandler.getServer().sendMessage("SOUND:"  + NetworkHandler.getPlayerNum() + ":resources/sounds/effects/bomb.wav");
		}
	}

	@Override
	public boolean usesDefaultHightlight() {
		return false;
	}

	@Override
	public void highlight() {
		truckHighlight.draw (getDrawX () - 3, getDrawY () - 3);
	}

}
