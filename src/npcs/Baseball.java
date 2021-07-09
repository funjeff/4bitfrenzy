package npcs;

import engine.Sprite;

public class Baseball extends PushableNPC {
	
	public Baseball () {
		super ();
		setHitboxAttributes (32, 32);
		setSprite (new Sprite ("resources/sprites/baseball.png"));
	}
	
	@Override
	public void npcFrame () {
		super.npcFrame ();
	}

}
