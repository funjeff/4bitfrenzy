package npcs;

import engine.Sprite;
import gameObjects.Highlightable;
import items.Bombs;

public class Basketball extends PushableNPC implements Highlightable {

	public static Sprite basketballSprite = new Sprite ("resources/sprites/basketball.png");
	public static Sprite basketballHighlight = new Sprite ("resources/sprites/basketball_highlight.png");
	
	public Basketball (double x, double y) {
		super (x, y);
		setSprite (basketballSprite);
		this.addCollisionException(Hoop.class);
	}

	public void pop () {
		forget ();
	}
	
	@Override
	public boolean usesDefaultHightlight() {
		return false;
	}

	@Override
	public void highlight() {
		basketballHighlight.draw (getDrawX () - 1, getDrawY () - 1);
	}
	
}
