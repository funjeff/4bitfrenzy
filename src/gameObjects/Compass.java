package gameObjects;

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
	
	
	
	GameObject pointObject; // the object the compass is pointing too
	
	Bit owner;
	
	public Compass (Bit owner) {
		this.owner = owner;
		this.setRenderPriority(3);
	}
	
	@Override
	public void frameEvent () {
		
		//Set this arrow's sprite
		double ang = Math.atan2 (owner.getCenterX () - pointObject.getCenterX (), owner.getCenterY () - pointObject.getCenterY ());
		ang += Math.PI / 2; //Start point correction
		setArrowParams (ang);
		
		//Not really sure what this does, Jeffrey explain pls
		try {
			DataSlot ds = (DataSlot) pointObject;
			if (ds.cleared) {
				if (ObjectHandler.getObjectsByName("Register") != null && ObjectHandler.getObjectsByName("Register").size() != 0) {
					this.pointObject = ObjectHandler.getObjectsByName("Register").get(0);
					owner.regNum = 0;
				}
			}
		} catch (ClassCastException e) {
			
		}
	}

	public GameObject getPointObject() {
		return pointObject;
	}

	public void setPointObject(GameObject pointObject) {
		this.pointObject = pointObject;
	}
	
	public void setArrowParams (double direction) {
		
		//Convert the angle to the domain 0-7
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
		}
		
	}
	
	
	@Override
	public void draw () {
		this.setX(GameCode.viewX + 50);
		this.setY(GameCode.viewY + 50);
		
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
		t.changeWidth(144);
		t.changeHeight(32);
		
		t.setX(this.getX());
		try {
			t.setY(this.getY() + this.getSprite().getHeight());
		} catch (NullPointerException e) {
			
		}
		t.draw();
		
		super.draw();
	}
}
