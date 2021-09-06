package npcs;

import engine.Sprite;
import gameObjects.Highlightable;
import players.Bit;

public class CarKey extends PushableNPC implements Highlightable {

	public static Sprite keySprite = new Sprite ("resources/sprites/key.png");
	public static Sprite keyHighlight = new Sprite ("resources/sprites/key_highlight.png");
	
	public CarKey(double x, double y) {
		super(x, y);
		setSprite (keySprite);
		this.setFriction (.25);
		this.setHitboxAttributes (16, 36);
		this.addCollisionException (Truck.class);
	}

	@Override
	public boolean usesDefaultHightlight() {
		return false;
	}

	@Override
	public void highlight() {
		keyHighlight.draw (getDrawX () - 2, getDrawY () - 2);
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent ();
		if (keyPressed ('K')) {
			Bit b = Bit.getCurrentPlayer ();
			setX (b.getX ());
			setY (b.getY ());
		}
	}

}
