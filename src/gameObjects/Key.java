package gameObjects;

import engine.GameObject;
import npcs.Keyboard;
import players.Bit;

public class Key extends GameObject {
	
	String key;
	
	Keyboard owner;
	
	boolean steping = false;
	
	public Key (String key, Keyboard owner) {
		this.key = key;
		
		this.owner = owner;
		
	}
	
	@Override
	public void frameEvent () {
	
		if (this.isColliding("Bit")) {
			
			if (!steping) {
				steping = true;
				if (this.key.equals("Backspace")) {
					owner.delete();
				} else {
					if (!this.key.equals("del")) {
						owner.type(key);
					} else {
						owner.clear();
					}
				}
			}
		} else {
			steping = false;
		}
		
	}
	
}
