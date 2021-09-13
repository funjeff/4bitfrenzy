package npcs;

import engine.GameCode;

import engine.Sprite;
import menu.Menu;
import network.NetworkHandler;
import players.Bit;
import titleScreen.TitleBit;

public class TalkableNPC extends NPC {
	
	Menu diolog;
	
	TitleBit pausedBit;
	Bit alternativeBit;
	
	public TalkableNPC (double x, double y) {
		super(x,y);
	}
	
	@Override
	public void npcFrame () {
		
		
	}

	@Override
	public void frameEvent () {
		
		boolean closed = false;
		
		if (diolog.isClosed()) {
			closed = true;
			if (pausedBit != null) {
				pausedBit.unfreeze();
			}
			
			if (alternativeBit != null) {
				if (NetworkHandler.isHost()) {
					alternativeBit.unfreeze();
				} else {
					NetworkHandler.getClient().messageServer("UNFREEZE" + alternativeBit.playerNum);
				}
			}
			
			alternativeBit = null;
			pausedBit = null;
		}
		
		if ((this.isColliding("TitleBit") || this.isColliding("Bit"))) {
			if (this.keyReleased(GameCode.getSettings().getControls()[5]) && diolog.isClosed()) {
				if (this.isColliding("Bit")) {
					alternativeBit = (Bit) this.getCollisionInfo().getCollidingObjects().get(0);
					if (alternativeBit.playerNum == NetworkHandler.getPlayerNum()) {
						if (NetworkHandler.isHost()) {
							alternativeBit.freeze();
						} else {
							NetworkHandler.getClient ().messageServer ("FREEZE" + alternativeBit.playerNum);
						}
						diolog.open();
						diolog.setRenderPriority(this.getRenderPriority());
					} else {
						alternativeBit = null;
					}
				} else {
					this.isColliding("TitleBit");
					pausedBit = (TitleBit) this.getCollisionInfo().getCollidingObjects().get(0);
					pausedBit.freeze();
					diolog.open();
					diolog.setRenderPriority(this.getRenderPriority());
				}
			}
		}
		if (diolog.closeAttempt()) {
			this.onCloseAttempt();
		}
	}
	
	@Override
	public void draw () {
		super.draw();
		
		if (this.isColliding("TitleBit") || this.isColliding("Bit")) {		
			
			if (this.isColliding("TitleBit")) {
				Sprite tBubble = new Sprite ("resources/sprites/text buble.png");
				tBubble.draw((int)this.getCenterX() - GameCode.getViewX(), (int)(this.getDrawY() - 22));
			}
			
			if (this.isColliding("Bit")) {
				Bit b = (Bit)this.getCollisionInfo().getCollidingObjects().get(0);
				
				if (b.playerNum == NetworkHandler.getPlayerNum()) {
					Sprite tBubble = new Sprite ("resources/sprites/text buble.png");
					tBubble.draw((int)this.getCenterX() - GameCode.getViewX(), (int)(this.getDrawY() - 22));
				}
			}
			
		}
	}
	
	public void onCloseAttempt () {
		
	}
	
	public Menu getMenu() {
		return diolog;
	}

	public void setMenu(Menu diolog) {
		this.diolog = diolog;
	}
}
