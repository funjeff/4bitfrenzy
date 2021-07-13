package items;

import engine.Sprite;

public class BasketBomb extends Bombs {
	
	public static Sprite basketBombSprite = new Sprite ("resources/sprites/basketbomb.png");
	
	public BasketBomb () {
		super ();
		setSprite (basketBombSprite);
	}

}
