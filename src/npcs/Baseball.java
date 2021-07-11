package npcs;

import engine.Sprite;

public class Baseball extends PushableNPC {
	
	public Baseball (double x, double y) {
		super (x, y);
		setHitboxAttributes (32, 32);
		setSprite (new Sprite ("resources/sprites/baseball.png"));
	}
	
	@Override
	public void npcFrame () {
		super.npcFrame ();
	}

}
