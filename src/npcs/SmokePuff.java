package npcs;

import engine.Sprite;

public class SmokePuff extends NPC {
	
	public Sprite puffSprite = new Sprite ("resources/sprites/smoke_puff.png");
	
	private int puffTime = 0;
	
	public SmokePuff (double x, double y) {
		super (x, y);
		setSprite (puffSprite);
	}
	
	@Override
	public void frameEvent () {
		setY (getY () - 1);
		puffTime++;
		if (puffTime < 5) {
			setX (getX () - 1);
		}
		if (puffTime > 300) {
			forget ();
		}
	}

}
