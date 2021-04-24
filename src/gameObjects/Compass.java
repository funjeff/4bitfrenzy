package gameObjects;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import network.NetworkHandler;
import players.Bit;
import resources.Textbox;

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
		if (pointObject.getX() - owner.getX() < -1080 || pointObject.getX() - owner.getX() > 1080) {
			if (pointObject.getX() > owner.getX()) {
				if (pointObject.getY() - owner.getY() <-720 || pointObject.getY() - owner.getY() > 720) {
					if (pointObject.getY() > owner.getY()) {
						if (pointObject instanceof DataSlot) {
							this.setSprite(UP_RIGHTR);
						} else {
							this.setSprite(UP_RIGHT);
						}
						this.getAnimationHandler().setFlipVertical(true);
						this.getAnimationHandler().setFlipHorizontal(false);
					} else {
						if (pointObject instanceof DataSlot) {
							this.setSprite(UP_RIGHTR);
						} else {
							this.setSprite(UP_RIGHT);
								
						}
						this.getAnimationHandler().setFlipVertical(false);
						this.getAnimationHandler().setFlipHorizontal(false);
					}
				} else {
					if (pointObject instanceof DataSlot) {
						this.setSprite(RIGHTR);
					} else {
						this.setSprite(RIGHT);
					}
					this.getAnimationHandler().setFlipHorizontal(false);
					this.getAnimationHandler().setFlipVertical(false);
				}
			} else {
				if (pointObject.getY() - owner.getY() <-720 || pointObject.getY() - owner.getY() > 720) {
					if (pointObject.getY() > owner.getY()) {
						if (pointObject instanceof DataSlot) {
							this.setSprite(UP_RIGHTR);
						} else {
							this.setSprite(UP_RIGHT);
						}
						this.getAnimationHandler().setFlipVertical(true);
						this.getAnimationHandler().setFlipHorizontal(true);
					} else {
						if (pointObject instanceof DataSlot) {
							this.setSprite(UP_RIGHTR);
						} else {
							this.setSprite(UP_RIGHT);
						}
						this.getAnimationHandler().setFlipVertical(false);
						this.getAnimationHandler().setFlipHorizontal(true);
					}
				} else {
					if (pointObject instanceof DataSlot) {
						this.setSprite(RIGHTR);
					} else {
						this.setSprite(RIGHT);
					}
					this.getAnimationHandler().setFlipHorizontal(true);
					this.getAnimationHandler().setFlipVertical(false);
				}
			}
		} else {
			if (pointObject.getY() < owner.getY()) {
				if (pointObject instanceof DataSlot) {
					this.setSprite(UPR);
				} else {
					this.setSprite(UP);
				}
				this.getAnimationHandler().setFlipVertical(false);
				this.getAnimationHandler().setFlipHorizontal(false);
			} else {
				if (pointObject instanceof DataSlot) {
					this.setSprite(UPR);
				} else {
					this.setSprite(UP);
				}
				this.getAnimationHandler().setFlipVertical(true);
				this.getAnimationHandler().setFlipHorizontal(false);
			}
		}
		
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
