package npcs;

import engine.Sprite;
import gameObjects.Highlightable;
import items.Egg;
import items.Pin;
import items.Water;

public class Dartboard extends NPC implements Highlightable {
	
	public static Sprite dartboardSprite = new Sprite ("resources/sprites/dartboard.png");
	public static Sprite dartboardHighlight = new Sprite ("resources/sprites/dartboard_highlight.png");
	
	public Dartboard (double x, double y) {
		super (x, y);
		setSprite (dartboardSprite);
		setHitboxAttributes (2, 2);
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent ();
	}
	
	@Override
	public boolean spawnsQuestItem () {
		return true;
	}
	
	@Override
	public int getMinQuestItemDist () {
		return 0;
	}
	
	@Override
	public int getMaxQuestItemDist () {
		return 1;
	}
	
	@Override
	public Class<?> getQuestItemType () {
		return Pin.class;
	}
	
	@Override
	public double getQuestItemSpawnOdds () {
		return .7;
	}
	
	@Override
	public int getMinQuestItems () {
		return 1;
	}
	
	@Override
	public int getMaxQuestItems () {
		return 4;
	}
	
	@Override
	public boolean usesDefaultHightlight() {
		return false;
	}
	
	@Override
	public void highlight() {
		dartboardHighlight.draw (getDrawX () - 4, getDrawY () - 3);
	}

}
