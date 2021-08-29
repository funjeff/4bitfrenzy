package npcs;

import engine.Sprite;
import gameObjects.Highlightable;

public class Piano extends NPC implements Highlightable {
	
	public static Sprite pianoSprite = new Sprite ("resources/sprites/piano.png");
	public static Sprite flamingPiano = new Sprite ("resources/sprites/config/flaming_piano.txt");
	public static Sprite pianoHighlight = new Sprite ("resources/sprites/piano_highlight.png");
	
	private int fireTimer = -1;
	
	public Piano (double x, double y) {
		super (x, y);
		setSprite (pianoSprite);
		setHitboxAttributes (147, 120);
	}
	
	public void lightOnFire () {
		fireTimer = 500;
		setSprite (flamingPiano);
		getAnimationHandler ().setFrameTime (198);
	}
	
	@Override
	public void npcFrame () {
		if (fireTimer != -1) {
			fireTimer--;
			if (fireTimer == 0) {
				forget ();
			}
		}
	}

	@Override
	public boolean usesDefaultHightlight() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void highlight() {
		if (fireTimer == -1) {
			pianoHighlight.draw (getDrawX () - 3, getDrawY () - 3);
		}
	}

}
