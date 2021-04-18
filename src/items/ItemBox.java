package items;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;
import players.Bit;

public class ItemBox extends GameObject {
	
	Item item = null;
	
	public ItemBox () {
		this.setSprite(new Sprite ("resources/sprites/itemBox.png"));
		this.setRenderPriority(3);
	}
	
	
	public Item getItem() {
		return item;
	}


	public void setItem(Item item) {
		this.item = item;
	}


	public void useItem (Bit user) {
		if (item != null) {
			if (item.useItem(user)) {
				if (user.perk != 4) {
					item = null;
				} else {
					item.uses = item.uses - 1;
					if (item.uses == 0) {
						item = null;
					}
				}
			}
		}
	}
	
	@Override
	public void draw () {
		this.setX(GameCode.getViewX() + 240);
		this.setY(GameCode.getViewY() + 100);
		
		super.draw();
		if (item != null) {

			item.setX(this.getX());
			item.setY(this.getY());
			item.draw();
		}
	}
	@Override
	public String toString () {
		if (item == null) {
			return "null";
		} else {
			return item.getClass().getSimpleName();
		}
	}
}
