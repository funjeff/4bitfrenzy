package items;

import engine.Sprite;

public class BasketBomb extends Bombs {
	
	public static Sprite basketBombSprite = new Sprite ("resources/sprites/basketbomb.png");
	
	public BasketBomb () {
		super ();
		setSprite (basketBombSprite);
	}

	public String getName () {
		return "Basket-bomb";
	}
	
	public String getDesc () {
		return "Approach a wall and\nthrow the bomb to destroy it.\nDoes not work on the\nedges of the map.";
	}
	
	public String getLongDescription () {
		return "default_long_desc";
	}
	
	public String getItemFlavor () {
		return "edible until proven otherwise";
	}
	
}
