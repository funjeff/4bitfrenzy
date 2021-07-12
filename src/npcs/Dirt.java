package npcs;

import java.util.ArrayList;

import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;

public class Dirt extends NPC {

	public static Sprite dirtSprite = new Sprite ("resources/sprites/dirt.png");
	
	public Dirt linkedDirt;
	
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
	
}
