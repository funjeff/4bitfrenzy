package players;

import engine.GameObject;
import engine.Sprite;
import map.Roome;

public class Bit extends GameObject {
	
	int playerNum = 0;
	
	public Bit () {
		this.setSprite(new Sprite ("resources/sprites/config/bits.txt"));
		this.getAnimationHandler().setAnimationFrame(playerNum);
	}
	@Override
	public void frameEvent () {
		
		
		if (keyDown('W')) {
			this.setY(this.getY() -1);
		}
		if (keyDown ('D')) {
			this.setX(this.getX() + 1);
		}
		if (keyDown ('A')) {
			this.setX(this.getX() - 1);
		}
		if (this.keyDown('S')) {
			this.setY(this.getY() + 1);
		}
	}
	public Roome getSurroundingRoom ()
	{
		return null;
	}
}
