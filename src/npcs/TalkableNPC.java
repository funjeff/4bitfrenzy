package npcs;

import java.util.ArrayList;

import engine.GameCode;
import engine.Sprite;
import menu.Menu;
import network.NetworkHandler;
import players.Bit;

public class TalkableNPC extends NPC {
	
	Menu diolog;

	public TalkableNPC (double x, double y, Menu m) {
		super(x,y);
		diolog = m;
	}
	
	@Override
	public void npcFrame () {
		
		
	}
	@Override
	public void frameEvent () {
		
		boolean closed = false;
		
		if (diolog.isOpen() && this.keyReleased(GameCode.getSettings().getControls()[5])) {
			diolog.close();
			closed = true;
		}
		
		if ((this.isColliding("TitleBit") || this.isColliding("Bit"))) {
			if (this.keyReleased(GameCode.getSettings().getControls()[5]) && !closed) {
				diolog.open();
				diolog.setRenderPriority(this.getRenderPriority());
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
