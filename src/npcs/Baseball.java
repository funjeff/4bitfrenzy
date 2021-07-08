package npcs;

import engine.Sprite;

public class Baseball extends NPC {
	
	public Baseball () {
		super ();
		setHitboxAttributes (32, 32);
		setSprite (new Sprite ("resources/sprites/baseball.png"));
	}
	
	@Override
	public void npcFrame () {
		if (isColliding ("Bit")) {
			assertPosition (getX () + 1, getY ());
		}
	}

}
