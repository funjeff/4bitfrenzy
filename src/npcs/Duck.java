package npcs;

import engine.Sprite;
import gameObjects.Highlightable;

public class Duck extends PushableNPC implements Highlightable {

	public static Sprite duckSprite = new Sprite ("resources/sprites/duck.png");
	public static Sprite duckHighlight = new Sprite ("resources/sprites/duck_highlight.png");
	
	public Duck (double x, double y) {
		super (x, y);
		setSprite (duckSprite);
		this.addCollisionException(Lake.class);
		this.setFriction (1);
		this.setHitboxAttributes (25, 54);
		this.addCollisionException (Fryer.class);
	}

	@Override
	public boolean usesDefaultHightlight() {
		return false;
	}

	@Override
	public void highlight() {
		duckHighlight.draw (getDrawX () - 3, getDrawY () - 3);
	}
	
}