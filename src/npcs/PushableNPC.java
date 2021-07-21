package npcs;

import java.util.ArrayList;
import java.util.Iterator;

import engine.CollisionInfo;
import engine.GameObject;
import engine.Room;
import map.Roome;
import players.Bit;

public class PushableNPC extends NPC {
	
	private double vx;
	private double vy;
	
	private double friction = .1;
	
	private int stuckCount = 0;
	
	public PushableNPC (double x, double y) {
		super (x, y);
	}
	
	@Override
	public void npcFrame () {
		
		double pushMultiplier = 1;
				
		//Move according to vx, vy
		if (vx != 0 || vy != 0) {
			
			boolean noclip = false;
			double mul = 1;
			if ((this.isCollidingChildren ("NPC") && this.getCollisionInfo ().getCollidingObjects ().size () != 0)) {
				System.out.println ("HCK");
				noclip = true;
				pushMultiplier += Math.random () * .75 - .25;
			}
			double xprev = getX ();
			double yprev = getY ();
			setX (getX () + vx * mul);
			setY (getY () + vy * mul);
			if (Roome.getRoom(this.getX(), this.getY()).isColliding (this)
				|| (((this.isCollidingChildren ("NPC") && this.getCollisionInfo ().getCollidingObjects ().size () != 0)
				|| this.isColliding ("Register")) && !noclip)
			) {
				//Hit the wall
				this.setX (xprev);
				this.setY (yprev);
				vx *= -.5;
				vy *= -.5; //Bounce (NOTE: not a "proper" bounce for diagonal directions)
			}
			
			//Assert position if necessary
			assertPosition (getX (), getY ());
		}
		
		//Apply friction
		if (Math.abs (vx) > 0) {
			double vxprev = vx;
			vx -= vx < 0 ? -friction : friction;
			if (vxprev != 0 && Math.signum (vx) != Math.signum (vxprev)) {
				vx = 0;
			}
		}
		
		if (Math.abs (vy) > 0) {
			double vyprev = vy;
			vy -= vy < 0 ? -friction : friction;
			if (vyprev != 0 && Math.signum (vy) != Math.signum (vyprev)) {
				vy = 0;
			}
		}
		
		if (isColliding ("Bit")) {
			ArrayList<GameObject> objs = getCollisionInfo ().getCollidingObjects ();
			for (int i = 0; i < objs.size (); i++) {
				Bit curr = (Bit)objs.get (i);
				vx = Math.abs (curr.getPushVector ().x) > Math.abs (vx) ? curr.getPushVector ().x : vx;
				vy = Math.abs (curr.getPushVector ().y) > Math.abs (vy) ? curr.getPushVector ().y : vy;
			}
		}
		
	}
	
	public void setFriction (double friction) {
		this.friction = friction;
	}
	
	public double getFriction () {
		return friction;
	}
	
	@Override
	public CollisionInfo getCollisionInfo () {
		
		//WARNING: This does not affect isColliding methods, they may return true despite the new CollisionInfo containing no colliding objects.
		
		//Remove all exceptions from the colliding objects
		CollisionInfo info = super.getCollisionInfo ();
		ArrayList<GameObject> objs = info.getCollidingObjects ();
		Iterator<GameObject> iter = objs.iterator ();
		while (iter.hasNext ()) {
			GameObject obj = iter.next ();
			if (!canCollide (obj.getClass())) {
				iter.remove ();
			}
		}
		
		//Return the new CollisionInfo object
		return info;
		
	}
	
}
