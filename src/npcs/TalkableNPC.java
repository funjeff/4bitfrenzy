package npcs;

import java.util.ArrayList;

import engine.GameCode;
import engine.Sprite;
import network.NetworkHandler;
import players.Bit;

public class TalkableNPC extends NPC {

	public TalkableNPC (double x, double y) {
		super(x,y);
	}
	
	@Override
	public void npcFrame () {
		
		
	}
	@Override
	public void frameEvent () {
		
		if ((this.isColliding("TitleBit") || this.isColliding("Bit"))) {
			
			if (!this.isColliding("TitleBit")) {
				
				this.isColliding("Bit");
				
				ArrayList <Bit> collidingBits = new ArrayList <Bit>();
				
				for (int i = 0; i < this.getCollisionInfo().getCollidingObjects().size(); i++) {
				
					collidingBits.add((Bit) this.getCollisionInfo().getCollidingObjects().get(i));
				}
				
				try {
					for (int i = 0; i < collidingBits.size(); i++) {
						if (NetworkHandler.getServer ().getPlayerInputs (collidingBits.get(i).playerNum).contains("10")) {
							
						}
					}
				} catch (NullPointerException e) {
					
				}
			} else {
				if (this.keyDown(GameCode.getSettings().getControls()[5])) {
					
				}
			}
			
		}
		
	}
	
	@Override
	public void draw () {
		super.draw();

		if (this.isColliding("TitleBit") || this.isColliding("Bit")) {		
			
			Sprite tBubble = new Sprite ("resources/sprites/text buble.png"); 
			tBubble.draw((int)this.getX() + 7, (int)(this.getY() - 22));
		}
	}
}
