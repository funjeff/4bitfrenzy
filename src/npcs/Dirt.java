package npcs;

import java.util.ArrayList;

import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import gameObjects.Highlightable;
import players.Bit;

public class Dirt extends NPC implements Highlightable {

	public static Sprite dirtSprite = new Sprite ("resources/sprites/dirt.png");
	public static Sprite dirtTunnelSprite = new Sprite ("resources/sprites/dirt (tunnel).png");
	public static Sprite dirtHighlight = new Sprite ("resources/sprites/dirt_highlight.png");
	
	public Dirt linkedDirt;
	
	int cooldown = 0;
	
	public Dirt (double x, double y) {
		
		//Initialize the dirt
		super (x, y);
		setSprite (dirtSprite);
		setHitboxAttributes (31, 30);
		
		//Link the dirts
		ArrayList<GameObject> dirts = ObjectHandler.getObjectsByName ("Dirt");
		if (dirts != null && dirts.size () == 2) {
			((Dirt)dirts.get (0)).linkedDirt = (Dirt)dirts.get (1);
			((Dirt)dirts.get (1)).linkedDirt = (Dirt)dirts.get (0);
		}
		
	}
	
	public Dirt getLinkedDirt () {
		return linkedDirt;
	}
	
	@Override
	public void npcFrame () {
		if (this.isColliding ("Shovel")) {
			this.getCollisionInfo ().getCollidingObjects ().get (0).forget ();
		}
		if (cooldown == 0 && getSprite () == dirtTunnelSprite && this.isColliding ("Bit")) {
			Bit b = (Bit) this.getCollisionInfo ().getCollidingObjects ().get (0);
			b.setX (linkedDirt.getX ());
			b.setY (linkedDirt.getY ());
			linkedDirt.cooldown = 100;
		}
		if (cooldown > 0) cooldown--;
	}
	
	@Override
	public boolean spawnsQuestItem () {
		return true;
	}
	
	@Override
	public Class<?> getQuestItemType () {
		return Shovel.class;
	}
	
	@Override
	public double getQuestItemSpawnOdds () {
		return 0;
	}
	
	@Override
	public int getMinQuestItems () {
		return 1;
	}
	
	@Override
	public int getMaxQuestItems () {
		return 1;
	}
	
	@Override
	public int getMinQuestItemDist () {
		return 2;
	}
	
	@Override
	public int getMaxQuestItemDist () {
		return 3;
	}

	@Override
	public boolean usesDefaultHightlight() {
		return false;
	}

	@Override
	public void highlight() {
		dirtHighlight.draw (getDrawX () - 3, getDrawY () - 3);
	}
	
}
