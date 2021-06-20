package players;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import engine.SpriteParser;
import gameObjects.Compass;
import gameObjects.DataSlot;
import gameObjects.Register;
import items.Item;
import items.ItemBox;
import map.Map;
import map.Roome;
import network.NetworkHandler;
import resources.SoundPlayer;
import resources.Textbox;

public class Bit extends GameObject {
	
	public int playerNum = 0;
	
	public ArrayList<GameObject> regestersBeingCarried = null;
	
	public Compass compass;
	
	public ItemBox inventory = new ItemBox ();
	
	public int lastMove = 0; // 0 for up 1 for down 2 for right 3 for left
	
	int speed = 5;
	
	long speedUpTimer = 0;
	
	boolean active = true;
	
	boolean switching = false;
	
	boolean secondaryBit = false;
	
	public int regNum = 0;
	
	public boolean isSecondaryBit() {
		return secondaryBit;
	}

	public void makeSecondaryBit() {
		this.secondaryBit = true;
	}
	Map map = new Map ();
	
	public int perk = 6; 
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
		
	
	}
	
	public void updateIcon () {
		this.getAnimationHandler ().setAnimationFrame (playerNum - 1);
	}
	
	
	
	@Override
	public void onDeclare() {
		
		if (NetworkHandler.getPlayerNum() == this.playerNum) {
			GameCode.setView((int)this.getX() - 540, (int)this.getY() - 360);
			compass = new Compass (this);
			try {
				compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(0));
			} catch (NullPointerException e) {
				compass.setPointObject(this);
			}
				compass.declare(0, 0);
			} else {
				inventory.setVisability(false);
			}
		
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public void frameEvent () {	
			String keys;
			try {
		
				keys = NetworkHandler.getServer ().getPlayerInputs (playerNum);
			} catch (NullPointerException e) {
				keys = null;
			}
		
			if (speedUpTimer < System.currentTimeMillis()&& speedUpTimer != 0) {
				speedUpTimer = 0;
				speed = speed - 2;
			}
			if (compass != null && compass.getPointObject().equals(this)) {
				try {
					compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(0));
				} catch (NullPointerException e) {
					compass.setPointObject(this);
				}
			}
			if (this.isCollidingChildren("Item") && NetworkHandler.isHost()) {
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
			if (keys != null && keys.contains("10") && this.isActive() && NetworkHandler.isHost()) {
				if (inventory.getItem() != null) {
					inventory.useItem(this);
				}
			}
			if (!this.isActive()) {
				if (compass != null) {
					compass.setVisability(false);
				}
				inventory.setVisability(false);
			} else {
				if (perk == 5) {
					if (compass != null) {
						compass.setVisability(true);
					}
					inventory.setVisability(true);
				}
			}
			if (!this.isActive() && keys != null && keys.contains(Integer.toString(KeyEvent.VK_CONTROL)) && !switching) {
				active = true;
				switching = true;
				GameCode.setView((int)this.getX() - 540, (int)this.getY() - 360);
				ArrayList <GameObject> bits = ObjectHandler.getObjectsByName("Bit");
				for (int i = 0; i < bits.size(); i++) {
					Bit working = (Bit)bits.get(i);
					if (!working.equals(this) && working.playerNum == this.playerNum) {
						working.setActive(false);
						working.switching = true;
					}
				}
			}
			if (keys != null && !keys.contains(Integer.toString(KeyEvent.VK_CONTROL))) {
				this.switching = false;
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
				
				if (NetworkHandler.getPlayerNum() == this.playerNum) {
					if(compass != null && ObjectHandler.getObjectsByName("Register") != null &&ObjectHandler.getObjectsByName("Register").size() != 0) {
						try {
						DataSlot ds = (DataSlot) compass.getPointObject();
						if (ds.isCleared() || keyPressed (' ')) {
							System.out.println("debg");
							try {
								regNum = regNum + 1;
								compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(regNum));
							} catch (IndexOutOfBoundsException e) {
								compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(0));
								regNum = 0;
							}
						}
						} catch (ClassCastException e) {
							if (keyPressed (' ')) {
								try {
									regNum = regNum + 1;
									compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(regNum));
								} catch (IndexOutOfBoundsException g) {
									compass.setPointObject(ObjectHandler.getObjectsByName("Register").get(0));
									regNum = 0;
								}
							}
						}
					}
				}
				if (keys != null && ((keys.contains ("W") && !this.isSecondaryBit()) || (keys.contains (Integer.toString(KeyEvent.VK_UP)) && this.isSecondaryBit()))) {
					if (this.goY((int)(this.getY() - speed))) {
						this.carryRegestersY((((int)speed) * -1) - 1);
					}
					lastMove = 0;
				}
				if (keys != null && ((keys.contains ("D") && !this.isSecondaryBit()) || (keys.contains (Integer.toString(KeyEvent.VK_RIGHT)) && this.isSecondaryBit()))) {
					if (this.goX((int)(this.getX() + speed))) {
						this.carryRegestersX((int)speed);
					}
					lastMove = 2;
				}
				if (keys != null && ((keys.contains ("A") && !this.isSecondaryBit()) || (keys.contains (Integer.toString(KeyEvent.VK_LEFT)) && this.isSecondaryBit()))) {
					if (this.goX((int)(this.getX() - speed))) {
						this.carryRegestersX((((int)speed) * -1) - 1);
					}
					lastMove = 3;
				}
				if (keys != null && ((keys.contains ("S") && !this.isSecondaryBit()) || (keys.contains (Integer.toString(KeyEvent.VK_DOWN)) && this.isSecondaryBit()))) {
					if (this.goY((int)(this.getY() + speed))) {
						this.carryRegestersY((int)speed);
					}
					lastMove = 1;
				}
				this.setHitboxAttributes(this.hitbox().width + 6, this.hitbox().height + 6);
				this.setX(this.getX() - 3);
				this.setY(this.getY() - 3);
					if (keys != null && keys.contains ("v")) {
						
						if (this.isColliding ("Register")) {
							boolean startedCarring = false;
							if (regestersBeingCarried == null) {
								regestersBeingCarried = new ArrayList<GameObject> ();
								startedCarring = true;
								if (NetworkHandler.isHost()) {
									SoundPlayer play = new SoundPlayer ();
									play.playSoundEffect(GameCode.volume,"resources/sounds/effects/pickup.wav");
								} else {
									NetworkHandler.getServer().sendMessage("SOUND:"  + this.playerNum + ":resources/sounds/effects/pickup.wav");
								}
							}
							
							ArrayList<GameObject> workingRegisters = this.getCollisionInfo().getCollidingObjects();
							for (int i = 0; i < workingRegisters.size (); i++) {
								if (!regestersBeingCarried.contains (workingRegisters.get (i))) {
									regestersBeingCarried.add (workingRegisters.get (i));
								}
							}
							
							Register reg = (Register) regestersBeingCarried.get(0);
							
							//compass = null for client
							if (this.playerNum == 1) {
								
								compass.setPointObject(reg.getDataSlot() );
							} else {
								if(startedCarring) {
									NetworkHandler.getServer().sendMessage("POINT:" + this.playerNum + ":" + reg.getMemAddress());
								}
							}
						}
					} else {
						if (regestersBeingCarried != null) {
							regestersBeingCarried.clear ();
						}
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
		
		if (playerNum == NetworkHandler.getPlayerNum () && isActive()) {
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
		
		if (playerNum == NetworkHandler.getPlayerNum () && isActive()) {
		
			Rectangle hBox = new Rectangle (this.hitbox());
		
	
			Rectangle centerRect = new Rectangle (216 + GameCode.getViewX(),200 + GameCode.getViewY(),648,350);
			
		
			if (!centerRect.contains(hBox)) {
				GameCode.setView(GameCode.getViewX(), (int)(GameCode.getViewY()  + (val - y)));
			}
		}
		return true;
	}
	public void setPerk (int perkNum) {
		perk = perkNum;
		if (perk == 69) {
			perk = 5;
		}
		ArrayList <String> parserQuantitiys = new ArrayList<String>();
		parserQuantitiys.add("grid 21 16");
		switch (perkNum) {
		
		case 0:
			this.setSprite(new Sprite ("resources/sprites/bits blue.png", new SpriteParser (parserQuantitiys)));
			break;
		case 1:
			this.setSprite(new Sprite ("resources/sprites/bits green.png", new SpriteParser (parserQuantitiys)));
			break;
		case 2:
			this.setSprite(new Sprite ("resources/sprites/bits yellow.png", new SpriteParser (parserQuantitiys)));
			break;
		case 3:
			this.setSprite(new Sprite ("resources/sprites/bits red.png", new SpriteParser (parserQuantitiys)));
			break;
		case 4:
			this.setSprite(new Sprite ("resources/sprites/bits purple.png", new SpriteParser (parserQuantitiys)));
			break;
		case 5:
			this.setSprite(new Sprite ("resources/sprites/bits duel 1.png", new SpriteParser (parserQuantitiys)));
			break;
		case 69:
			this.setSprite(new Sprite ("resources/sprites/bits duel 2.png", new SpriteParser (parserQuantitiys)));
			break;
		}
		
	}
	@Override
	public void draw () {
		super.draw();
		if (perk == 2 && keyDown ('M')) {
			map.draw();
		}
	}
}
