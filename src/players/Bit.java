package players;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import gameObjects.Compass;
import gameObjects.Register;
import map.Roome;
import resources.Textbox;

public class Bit extends GameObject {
	
	int playerNum = 0;
	
	ArrayList<GameObject> regestersBeingCarried = null;
	
	Compass compass;
	
	
	public Bit () {
		this.setSprite(new Sprite ("resources/sprites/config/bits.txt"));
		this.getAnimationHandler().setAnimationFrame(playerNum);
		this.setHitboxAttributes(21, 16);
		this.setRenderPriority(1);
		compass = new Compass (this);
		Random rand = new Random ();
		compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(rand.nextInt(ObjectHandler.getObjectsByName("Register").size())));
		compass.declare(0, 0);
	}
	
	
	
	@Override
	public void onDeclare() {
		GameCode.setView((int)this.getX() - 540, (int)this.getY() - 360);
	}
	
	@Override
	public void frameEvent () {
		
		double resistance = 1;
		
		if (regestersBeingCarried != null) {
			if (!keyDown(16)) {
				regestersBeingCarried = null;
			} else {
				resistance = 0.5/regestersBeingCarried.size();
			}
			
		}
		
		double speed = 5 * resistance;
		
		if(keyPressed (' ')) {
			
			GameObject old = compass.getPointObject();
			
			
			while (old.equals(compass.getPointObject())){
				Random rand = new Random ();
				compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(rand.nextInt(ObjectHandler.getObjectsByName("Register").size())));
			}
		}
		
		
		if (keyDown('W')) {
			if (this.goY((int)(this.getY() - speed))) {
				this.carryRegestersY((((int)speed) * -1) - 1);
			}
		}
		if (keyDown ('D')) {
			if (this.goX((int)(this.getX() + speed))) {
				this.carryRegestersX((int)speed);
			}
		}
		if (keyDown ('A')) {
			if (this.goX((int)(this.getX() - speed))) {
				this.carryRegestersX((((int)speed) * -1) - 1);
			}
		}
		if (this.keyDown('S')) {
			if (this.goY((int)(this.getY() + speed))) {
				this.carryRegestersY((int)speed);
			}
		}
		this.setHitboxAttributes(this.hitbox().width + 4, this.hitbox().height + 4);
		this.setX(this.getX() - 2);
		this.setY(this.getY() - 2);
		
		if (this.isColliding("Register") && keyDown (16)) {
			
			regestersBeingCarried = this.getCollisionInfo().getCollidingObjects();
			
			Random rand = new Random ();
			Register reg = (Register) regestersBeingCarried.get(rand.nextInt(regestersBeingCarried.size()));
			
			compass.setPointObject(reg.getDataSlot() );
		}
		this.setHitboxAttributes(this.hitbox().width - 4, this.hitbox().height - 4);
		this.setX(this.getX() + 2);
		this.setY(this.getY() + 2);
		
	}
	private void carryRegestersY (double dist) {
		if (regestersBeingCarried != null) {
			for (int i = 0; i < regestersBeingCarried.size(); i++) {
				if (!regestersBeingCarried.get(i).goY(regestersBeingCarried.get(i).getY() + dist)) {
					regestersBeingCarried.remove(i);
					i = i -1;
				}
			}
			if (regestersBeingCarried.isEmpty()) {
				regestersBeingCarried = null;
			}
		}
	}
	private void carryRegestersX (double dist) {
		if (regestersBeingCarried != null) {
			for (int i = 0; i < regestersBeingCarried.size(); i++) {
				if (!regestersBeingCarried.get(i).goX(regestersBeingCarried.get(i).getX() + dist)) {
					regestersBeingCarried.remove(i);
					i = i -1;
				}
			}
			if (regestersBeingCarried.isEmpty()) {
				regestersBeingCarried = null;
			}
		}
	}
	@Override
	public boolean goX(double val) {
		double x = this.getX();
		Roome currentRoom = Roome.getRoom(this.getX(), this.getY());
		this.setX(val);
		if (currentRoom.isColliding(this)) {
			this.setX(x);
			return false;
		}
		
		if (this.isColliding("Register")) {
			ArrayList <GameObject> regesters= this.getCollisionInfo().getCollidingObjects();
			for (int i = 0; i < regesters.size(); i++) {
				if (val > x) {
				if (!regesters.get(i).goX(val + this.hitbox().width)) {
					for (int j = 0; j < i; j++) {
						regesters.get(i).setX(val - (val - x));
					}
					this.setX(x);
					return false;
				}
			} else {
				if (!regesters.get(i).goX(val - regesters.get(i).hitbox().width)) {
					for (int j = 0; j < i; j++) {
						regesters.get(i).setX(val - (val - x));
					}
					this.setX(x);
					return false;
				}
				}
			}
		}
		
		Rectangle hBox = new Rectangle (this.hitbox());
		

		Rectangle centerRect = new Rectangle (216 + GameCode.getViewX(),144 + GameCode.getViewY(),648,432);
		
		if (!centerRect.contains(hBox)) {
			GameCode.setView((int)(GameCode.getViewX() + (val - x)), GameCode.getViewY());
		}
		
		return true;
	}
	@Override
	public boolean goY(double val) {
		double y = this.getY();
		Roome currentRoom = Roome.getRoom(this.getX(), this.getY());
		this.setY(val);
		if (currentRoom.isColliding(this)) {
			this.setY(y);
			return false;
		}
		
		if (this.isColliding("Register")) {
			ArrayList <GameObject> regesters= this.getCollisionInfo().getCollidingObjects();
			for (int i = 0; i < regesters.size(); i++) {
				if (val > y) {
					if (!regesters.get(i).goY(val + this.hitbox().height)) {
						for (int j = 0; j < i; j++) {
							regesters.get(i).setY(val - (val - y));
						}
						this.setY(y);
						return false;
					}
				} else {
					if (!regesters.get(i).goY(val - regesters.get(i).hitbox().height)) {
						for (int j = 0; j < i; j++) {
							regesters.get(i).setY(val - (val - y));
						}
						this.setY(y);
						return false;
					}
				}
			}
		}
		
		Rectangle hBox = new Rectangle (this.hitbox());
	
		
		Rectangle centerRect = new Rectangle (216 + GameCode.getViewX(),144 + GameCode.getViewY(),648,432);
		
		if (!centerRect.contains(hBox)) {
			GameCode.setView(GameCode.getViewX(), (int)(GameCode.getViewY()  + (val - y)));
		}
		return true;
	}
	
}
