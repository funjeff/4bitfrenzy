package items;

import java.util.ArrayList;

import engine.GameObject;
import engine.Sprite;
import gameObjects.Register;
import npcs.Truck;
import players.Bit;
import util.DummyCollider;

public class Lighter extends Item {
	
	public static Sprite lighterSprite = new Sprite ("resources/sprites/lighter.png");
	
	public Lighter () {
		setSprite (lighterSprite);
	}
	
	@Override
	public boolean useItem (Bit user) {
		DummyCollider dc = new DummyCollider ((int)user.getX () - Bit.HIGHLIGHT_RADIUS, (int)user.getY () - Bit.HIGHLIGHT_RADIUS, Bit.HIGHLIGHT_RADIUS * 2, Bit.HIGHLIGHT_RADIUS * 2);
		if (dc.isColliding ("Truck")) {
			Truck truck = (Truck)dc.getCollisionInfo ().getCollidingObjects ().get (0);
			truck.lightOnFire ();
			return true;
		}
		if (dc.isColliding ("Register")) {
			ArrayList<GameObject> regs = dc.getCollisionInfo ().getCollidingObjects ();
			for (int i = 0; i < regs.size (); i++) {
				Register r = (Register)regs.get (i);
				if (r.isFrozen) {
					r.isFrozen = false;
					r.freezeTimer = 0;
				}
			}
			return true;
		}
		return false;
	}
	
	public String getName () {
		return "Lighter";
	}
	
	public String getDesc () {
		return "(Quest Item)\nLights things on fire.";
	}
	
	public String getLongDescription () {
		return "default_long_desc";
	}
	
	public String getItemFlavor () {
		return "lights things on fire";
	}
	
}
