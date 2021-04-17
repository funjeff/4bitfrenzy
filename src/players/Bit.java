package players;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;
import map.Roome;

public class Bit extends GameObject {
	
	int playerNum = 0;

	public Bit () {
		this.setSprite(new Sprite ("resources/sprites/config/bits.txt"));
		this.getAnimationHandler().setAnimationFrame(playerNum);
		this.setHitboxAttributes(21, 16);
		this.setRenderPriority(1);
		
	}
	
	@Override
	public void onDeclare() {
		GameCode.setView((int)this.getX() - 540, (int)this.getY() - 360);
	}
	
	@Override
	public void frameEvent () {
		if (keyDown('W')) {
			this.goYAndScroll(this.getY() -2);
		}
		if (keyDown ('D')) {
			this.goXAndScroll(this.getX() + 2);
		}
		if (keyDown ('A')) {
			this.goXAndScroll(this.getX() - 2);
		}
		if (this.keyDown('S')) {
			this.goYAndScroll(this.getY() + 2);
		}
		
		
		
	}
}
