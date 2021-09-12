package npcs;

import engine.Sprite;

public class USB extends NPC {
	public USB (double x, double y) {
		super(x,y);
		this.setSprite(new Sprite ("resources/sprites/USB.png"));
	}
}
