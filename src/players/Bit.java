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
import items.Item;
import items.ItemBox;
import map.Map;
import map.Roome;
import network.NetworkHandler;
import resources.Textbox;

public class Bit extends GameObject {
	
	public int playerNum = 0;
	
	public ArrayList<GameObject> regestersBeingCarried = null;
	
	Compass compass;
	
	ItemBox inventory = new ItemBox ();
	
	public int lastMove = 0; // 0 for up 1 for down 2 for right 3 for left
	
	int speed = 5;
	
	long speedUpTimer = 0;
	
	Map map = new Map ();
	
	public int perk = 5; 
	// 0 = blast processing
	// 1 = register hauler
	// 2 = navigation bit
	// 3 = powerhouse
	// 4 = duplication bit
	// 5 = dual processing
	
	public Bit () {
		this.setSprite(new Sprite ("resources/sprites/config/bits.txt"));
		this.getAnimationHandler().setAnimationFrame(playerNum);
		this.setHitboxAttributes(21, 16);
		this.setRenderPriority(1);
		inventory.declare();
		compass = new Compass (this);
		Random rand = new Random ();
		compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(rand.nextInt(ObjectHandler.getObjectsByName("Register").size())));
		compass.declare(0, 0);
	}
	
	public void updateIcon () {
		this.getAnimationHandler ().setAnimationFrame (playerNum - 1);
	}
	
	
	
	@Override
	public void onDeclare() {
		GameCode.setView((int)this.getX() - 540, (int)this.getY() - 360);
	}
	
	@Override
	public void frameEvent () {
		String keys = NetworkHandler.getServer ().getPlayerInputs (playerNum);
		if (NetworkHandler.isHost ()) {

			if (keyPressed (10)) {
				if (inventory.getItem() != null) {
					inventory.useItem(this);
				}
			}
			
			if (speedUpTimer < System.currentTimeMillis()&& speedUpTimer != 0) {
				speedUpTimer = 0;
				speed = speed - 2;
			}
			
			if (this.isCollidingChildren("Item")) {
				Item toUse = (Item)this.getCollisionInfo().getCollidingObjects().get(0); 
				if (toUse.pickupablity) {
					if (inventory.getItem() != null) {
						Item it = inventory.getItem();
						Roome romm = Roome.getRoom(this.getX(), this.getY());
						int [] spawnCoords = romm.biatch.getPosibleCoords(it.hitbox().width, it.hitbox().height);
						it.declare(spawnCoords[0], spawnCoords[1]);
					}
					inventory.setItem(toUse);
					toUse.forget();
				}
			}
			
			if (!ObjectHandler.getObjectsByName("Register").isEmpty()){
				GameObject old = compass.getPointObject();
				while (old.equals(compass.getPointObject())){
					Random rand = new Random ();
					compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(rand.nextInt(ObjectHandler.getObjectsByName("Register").size())));
				}
			}
		}
		
		
			double resistance = 1;
			if (perk != 1) {
				if (regestersBeingCarried != null) {
					if (keys != null && !keys.contains ("v")) {
						regestersBeingCarried = null;
					} else {
						resistance = 0.5/regestersBeingCarried.size();
					}
					
				}
			}
			
			double speed = this.speed * resistance;
			
			if (perk == 0) {
				speed = speed + 2;
			}
			
			if(keyPressed (' ')) {
				
				GameObject old = compass.getPointObject();
				
				
				while (old.equals(compass.getPointObject())){
					Random rand = new Random ();
					compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(rand.nextInt(ObjectHandler.getObjectsByName("Register").size())));
				}
			}
			

			if (keys != null && keys.contains ("W")) {
				if (this.goY((int)(this.getY() - speed))) {
					this.carryRegestersY((((int)speed) * -1) - 1);
				}
				lastMove = 0;
			}
			if (keys != null && keys.contains ("D")) {
				if (this.goX((int)(this.getX() + speed))) {
					this.carryRegestersX((int)speed);
				}
				lastMove = 2;
			}
			if (keys != null && keys.contains ("A")) {
				if (this.goX((int)(this.getX() - speed))) {
					this.carryRegestersX((((int)speed) * -1) - 1);
				}
				lastMove = 3;
			}
			if (keys != null && keys.contains ("S")) {
				if (this.goY((int)(this.getY() + speed))) {
					this.carryRegestersY((int)speed);
				}
				lastMove = 1;
			}
			this.setHitboxAttributes(this.hitbox().width + 6, this.hitbox().height + 6);
			this.setX(this.getX() - 3);
			this.setY(this.getY() - 3);
			
			if (this.isColliding("Register") && (keys != null && keys.contains ("v"))) {
				
				regestersBeingCarried = this.getCollisionInfo().getCollidingObjects();
				
				Register reg = (Register) regestersBeingCarried.get(0);
				
				compass.setPointObject(reg.getDataSlot() );
			}
			this.setHitboxAttributes(this.hitbox().width - 6, this.hitbox().height - 6);
			this.setX(this.getX() + 3);
			this.setY(this.getY() + 3);
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
		
		if (NetworkHandler.isHost ()) {
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
		}
		
		if (playerNum == NetworkHandler.getPlayerNum ()) {
			Rectangle hBox = new Rectangle (this.hitbox());
			
	
			Rectangle centerRect = new Rectangle (216 + GameCode.getViewX(),200 + GameCode.getViewY(),648,350);
			
			if (!centerRect.contains(hBox)) {
				GameCode.setView((int)(GameCode.getViewX() + (val - x)), GameCode.getViewY());
			}
		}
		
		return true;
	}
	public void speedUpTemporarly() {
		speed = speed + 2;
		speedUpTimer = System.currentTimeMillis() +  30 * 1000;
	}
	@Override
	public boolean goY(double val) {
		
		double y = this.getY();
		Roome currentRoom = Roome.getRoom(this.getX(), this.getY());
		this.setY(val);
		
		if (NetworkHandler.isHost ()) {
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
		}
		
		if (playerNum == NetworkHandler.getPlayerNum ()) {
		
			Rectangle hBox = new Rectangle (this.hitbox());
		
	
			Rectangle centerRect = new Rectangle (216 + GameCode.getViewX(),200 + GameCode.getViewY(),648,350);
			
		
			if (!centerRect.contains(hBox)) {
				GameCode.setView(GameCode.getViewX(), (int)(GameCode.getViewY()  + (val - y)));
			}
		}
		return true;
	}
	@Override
	public void draw () {
		super.draw();
		if (perk == 2 && keyDown ('M')) {
			map.draw();
		}
	}
}
