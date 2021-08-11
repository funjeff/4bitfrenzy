package npcs;

import engine.Sprite;
import gameObjects.Highlightable;

public class Fish extends PushableNPC implements Highlightable {

	public static Sprite fishSprite = new Sprite ("resources/sprites/fish.png");
	public static Sprite fishHighlight = new Sprite ("resources/sprites/fish_highlight.png");
	
	public Fish (double x, double y) {
		super (x, y);
		setSprite (fishSprite);
		this.addCollisionException(Lake.class);
		this.setFriction (1);
		this.setHitboxAttributes (46, 22);
		this.addCollisionException (Fryer.class);
	}

	@Override
	public boolean usesDefaultHightlight() {
		return false;
	}

	@Override
	public void highlight() {
		fishHighlight.draw (getDrawX () - 3, getDrawY () - 3);
	}
	
}