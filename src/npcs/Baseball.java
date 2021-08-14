package npcs;

import engine.Sprite;

public class Baseball extends PushableNPC {
	
	public Baseball (double x, double y) {
		super (x, y);
		setHitboxAttributes (32, 32);
		setSprite (new Sprite ("resources/sprites/baseball.png"));
	}
	
	public void pop () {
		forget ();
	}
	
	@Override
	public void npcFrame () {
		super.npcFrame ();
	}

}
