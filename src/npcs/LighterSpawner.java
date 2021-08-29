package npcs;

import engine.Sprite;
import gameObjects.Highlightable;
import items.BasketBomb;
import items.Bombs;
import items.Lighter;

public class LighterSpawner extends NPC {
	
	public LighterSpawner (double x, double y) {
		super (x, y);
	}
	
	@Override
	public void npcFrame () {
		//Do nothing
	}
	
	@Override
	public boolean spawnsQuestItem () {
		return true;
	}
	
	@Override
	public int getMinQuestItemDist () {
		return 1;
	}
	
	@Override
	public int getMaxQuestItemDist () {
		return 3;
	}
	
	@Override
	public Class<?> getQuestItemType () {
		return Lighter.class;
	}
	
	@Override
	public double getQuestItemSpawnOdds () {
		return 0.7;
	}
	
	@Override
	public int getMinQuestItems () {
		return 1;
	}
	
	@Override
	public int getMaxQuestItems () {
		return 8;
	}
	
}

