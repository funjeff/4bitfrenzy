package npcs;

import engine.Sprite;
import items.BasketBomb;
import items.Bombs;

public class Hoop extends NPC {

	public static Sprite hoopSprite = new Sprite ("resources/sprites/hoop.png");
	
	public Hoop (double x, double y) {
		super (x, y);
		setSprite (hoopSprite);
		setHitboxAttributes (52, 75);
	}
	
	@Override
	public void npcFrame () {
		if (this.isColliding ("Basketball")) {
			((ItemGenerator)(this.getCollisionInfo ().getCollidingObjects ().get (0))).becomeItem (BasketBomb.class);
		}
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
		return 2;
	}
	
	@Override
	public Class<?> getQuestItemType () {
		return Basketball.class;
	}
	
	@Override
	public double getQuestItemSpawnOdds () {
		return 0.2;
	}
	
	@Override
	public int getMinQuestItems () {
		return 1;
	}
	
	@Override
	public int getMaxQuestItems () {
		return 3;
	}
	
}
