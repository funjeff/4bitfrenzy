package npcs;

import java.util.ArrayList;

import engine.GameObject;
import engine.Room;
import map.Roome;
import players.Bit;

public class PushableNPC extends NPC {
	
	private double vx;
	private double vy;
	
	private double friction = .1;
	
	public PushableNPC (double x, double y) {
		super (x, y);
	}
	
	@Override
	public void npcFrame () {
		
		//Move according to vx, vy
		double xprev = getX ();
		double yprev = getY ();
		setX (getX () + vx);
		setY (getY () + vy);
		if (Roome.getRoom(this.getX(), this.getY()).isColliding (this)) {
			//Hit the wall
			this.setX (xprev);
			this.setY (yprev);
			vx *= -.5;
			vy *= -.5; //Bounce (NOTE: not a "proper" bounce for diagonal directions)
		}
		assertPosition (getX (), getY ());
		
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
				int dir = curr.lastMove;
				switch (dir) {
					case 0:
						//Up
						vy = -curr.getCarrySpeed();
						break;
					case 1:
						//Down
						vy = curr.getCarrySpeed();
						break;
					case 2:
						//Right
						vx = curr.getCarrySpeed();
						break;
					case 3:
						//Left
						vx = -curr.getCarrySpeed();
						break;
					default:
						break;
				}
			}
		}
		
	}
	
	public void setFriction (double friction) {
		this.friction = friction;
	}
	
	public double getFriction () {
		return friction;
	}
	
}
