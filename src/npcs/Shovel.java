package npcs;

import java.util.ArrayList;

import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;

public class Shovel extends PushableNPC {

	public static Sprite shovelSprite = new Sprite ("resources/sprites/shovel.png");
	
	public Shovel (double x, double y) {
		super (x, y);
		setSprite (shovelSprite);
		setHitboxAttributes (22, 48);
		this.setFriction (.25);
		this.addCollisionException(Dirt.class);
	}
	
	@Override
	public void forget () {
		ArrayList<GameObject> dirts = ObjectHandler.getObjectsByName ("Dirt");
		Dirt d = (Dirt)dirts.get (0);
		d.setSprite (Dirt.dirtTunnelSprite);
		d.getLinkedDirt ().setSprite (Dirt.dirtTunnelSprite);
		super.forget ();
	}
	
}
