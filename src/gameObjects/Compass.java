package gameObjects;

import java.util.ArrayList;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import network.NetworkHandler;
import players.Bit;
import resources.Textbox;
import util.Vector2D;

public class Compass extends GameObject {
	
	public static final Sprite UP = new Sprite ("resources/sprites/arrow up.png");
	public static final Sprite UP_RIGHT = new Sprite ("resources/sprites/arrow upRight.png");
	public static final Sprite RIGHT = new Sprite ("resources/sprites/arrow right.png");
	
	public static final Sprite UPR = new Sprite ("resources/sprites/arrow up red.png");
	public static final Sprite UP_RIGHTR = new Sprite ("resources/sprites/arrow upRight red.png");
	public static final Sprite RIGHTR = new Sprite ("resources/sprites/arrow right red.png");
	
	public static final int ARROW_NOTCHES = 60;
	
	private double pointDir = 0;
	
	GameObject pointObject; // the object the compass is pointing too
	
	Bit owner;
	
	public Compass (Bit owner) {
		this.owner = owner;
		this.setRenderPriority(3);
		setSprite (RIGHT);
	}
	
	@Override
	public void frameEvent () {
		
		//Set this arrow's sprite
		double ang = -Math.atan2 (owner.getCenterX () - pointObject.getCenterX (), owner.getCenterY () - pointObject.getCenterY ());
		ang -= Math.PI / 2; //Start point correction
		setArrowParams (ang);
		
		//Points to the nearest register when you turn in the current one
		if (!pointObject.declared ()) {
			ArrayList<GameObject> registers = ObjectHandler.getObjectsByName ("Register");
			double minDist = Double.POSITIVE_INFINITY;
			Register minReg = null;
			for (int i = 0; i < registers.size (); i++) {
				if (registers.get (i).getDistance (this) < minDist) {
					minReg = (Register)registers.get (i);
					minDist = minReg.getDistance (this);
				}
			}
			if (minReg != null) {
				pointObject = minReg;
			}
		}
		
	}

	public GameObject getPointObject() {
		return pointObject;
	}

	public void setPointObject(GameObject pointObject) {
		this.pointObject = pointObject;
	}
	
	public void setArrowParams (double direction) {
		
		pointDir = getNotchedDirection (direction, ARROW_NOTCHES);
		
		/*//Convert the angle to the domain 0-7
		direction += direction < 0 ? Math.PI * 2 : 0; //Convert to 0-2pi range
		direction *= (4 / Math.PI); //Map to [0,8)
		direction = Math.round (direction); //Snap to one of (9) angles
		int intDir = direction == 8 ? 0 : (int)direction;
		
		//Set the arrow's direction (sprite, flip) accordingly
		switch (intDir) {
			case 0:
				setSprite (RIGHT);
				getAnimationHandler ().setFlipHorizontal (false);
				getAnimationHandler ().setFlipVertical (false);
				break;
			case 1:
				setSprite (UP_RIGHT);
				getAnimationHandler ().setFlipHorizontal (false);
				getAnimationHandler ().setFlipVertical (false);
				break;
			case 2:
				setSprite (UP);
				getAnimationHandler ().setFlipHorizontal (false);
				getAnimationHandler ().setFlipVertical (false);
				break;
			case 3:
				setSprite (UP_RIGHT);
				getAnimationHandler ().setFlipHorizontal (true);
				getAnimationHandler ().setFlipVertical (false);
				break;
			case 4:
				setSprite (RIGHT);
				getAnimationHandler ().setFlipHorizontal (true);
				getAnimationHandler ().setFlipVertical (false);
				break;
			case 5:
				setSprite (UP_RIGHT);
				getAnimationHandler ().setFlipHorizontal (true);
				getAnimationHandler ().setFlipVertical (true);
				break;
			case 6:
				setSprite (UP);
				getAnimationHandler ().setFlipHorizontal (false);
				getAnimationHandler ().setFlipVertical (true);
				break;
			case 7:
				setSprite (UP_RIGHT);
				getAnimationHandler ().setFlipHorizontal (false);
				getAnimationHandler ().setFlipVertical (true);
				break;
			default:
				break;
		}
		
		//Change to the red arrow if pointing to a data slot
		if (pointObject instanceof DataSlot) {
			if (getSprite () == UP) {
				setSprite (UPR);
			}
			if (getSprite () == UP_RIGHT) {
				setSprite (UP_RIGHTR);
			}
			if (getSprite () == RIGHT) {
				setSprite (RIGHTR);
			}
		}*/
		
	}
	
	
	@Override
	public void draw () {
		this.setX(50);
		this.setY(50);
		
		String memAdress = "";
		boolean regOrDs = false; //true for reg false for ds
		try {
			Register reg = (Register) pointObject;
			memAdress = Integer.toHexString(reg.memAddress).toUpperCase();
			regOrDs = true;
		} catch (ClassCastException e) {
			
		}
		try {
			DataSlot ds = (DataSlot) pointObject;
			memAdress = Integer.toHexString(ds.memAddress).toUpperCase();
			regOrDs = false;
		} catch (ClassCastException e) {
			
		}
		
		Textbox t = new Textbox ("   " + memAdress);
		
		if (regOrDs) {
			t.setFont("text (lime green)");
		} else {
			t.setFont("text (red)");
		}
		t.changeWidth(144/16);
		t.changeHeight(32/16);
		
		t.setX(this.getX() + GameCode.getViewX ());
		try {
			t.setY(this.getY() + GameCode.getViewY () + 110);
		} catch (NullPointerException e) {
			
		}
		t.draw();
		
		this.getSprite ().drawRotated ((int)getX (), (int)getY (), 0, 60, 36, pointDir);
	}
	
	public static double getNotchedDirection (double dir, int notches) {
		int intDir = ((int)(dir / (2 * Math.PI) * notches));
		return ((2 * Math.PI) * intDir) / notches;
	}
}