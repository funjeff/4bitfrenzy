package gameObjects;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import network.NetworkHandler;
import players.Bit;

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
		super.draw();
	}
}
