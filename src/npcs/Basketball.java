package npcs;

import engine.Sprite;
import items.Bombs;

public class Basketball extends PushableNPC {

	public static Sprite basketballSprite = new Sprite ("resources/sprites/basketball.png");
	
	public Basketball (double x, double y) {
		super (x, y);
		setSprite (basketballSprite);
		this.addCollisionException(Hoop.class);
	}
	
}
