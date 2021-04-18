package gameObjects;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import engine.GameObject;
import engine.Sprite;

public class PixelBitch extends GameObject {

	public PixelBitch (double x, double y, int width, int height, BufferedImage forSprite) {
		this.setHitboxAttributes(width, height);
		this.enablePixelCollisions();
		this.setX(x);
		this.setY(y);
		this.setSprite(new Sprite (forSprite));
	}
	public int [] getPosibleCoords (int width, int height) {
		Rectangle obj = new Rectangle (width,height);
		Random rand = new Random ();
		
		obj.x = (int) this.getX() + rand.nextInt(this.hitbox().width);
		obj.y = (int) this.getY() + rand.nextInt(this.hitbox().height);
		
		while (this.isColliding(obj) || !this.hitbox().contains(obj)) {
			obj.x = (int) this.getX() + rand.nextInt(this.hitbox().width);
			obj.y = (int) this.getY() + rand.nextInt(this.hitbox().height);
		}
		
		int [] working = {obj.x,obj.y};
		return working;
		
	}
}
