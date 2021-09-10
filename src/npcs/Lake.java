package npcs;

import engine.Sprite;
import gameObjects.Highlightable;
import items.Egg;
import items.Water;

public class Lake extends NPC implements Highlightable {
	
	public static Sprite lakeSprite = new Sprite ("resources/sprites/pond.png");
	public static Sprite lakeHighlight = new Sprite ("resources/sprites/pond_highlight.png");
	
	public Lake (double x, double y) {
		super (x, y);
		setSprite (lakeSprite);
		setHitboxAttributes (144, 138);
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent ();
		if (this.isColliding ("Fish")) {
			Fish f = (Fish)this.getCollisionInfo ().getCollidingObjects().get (0);
			f.becomeItem (Water.class);
		}
		if (this.isColliding ("Duck")) {
			Duck d = (Duck)this.getCollisionInfo ().getCollidingObjects ().get (0);
			d.becomeItem (Egg.class);
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
		return Math.random () < .5 ? Fish.class : Duck.class;
	}
	
	@Override
	public double getQuestItemSpawnOdds () {
		return 1;
	}
	
	@Override
	public int getMinQuestItems () {
		return 2;
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
		lakeHighlight.draw (getDrawX () - 3, getDrawY () - 3);
	}

}
