package map;

import java.awt.image.BufferedImage;

import engine.GameObject;
import engine.Sprite;

public class WallBox extends GameObject {

	static Sprite wallImg432x144;
	static Sprite wallImg216x108;
	static Sprite wallImg216x216;
	
	public WallBox (int x, int y, int width, int height) {
		
		//Load sprites if they aren't already loaded
		if (wallImg432x144 == null) {
			wallImg432x144 = new Sprite ("resources/sprites/code_snippet_1.png");
			wallImg216x108 = new Sprite ("resources/sprites/code_snippet_2.png");
			wallImg216x216 = new Sprite ("resources/sprites/code_snippet_3.png");
		}
		
		if (width == 432 && height == 144) {
			this.setSprite(wallImg432x144);
		}
		if (width == 216 && height == 108) {
			this.setSprite (wallImg216x108);
		}
		if (width == 216 && height == 216) {
			this.setSprite (wallImg216x216);
		}
		
		this.setHitboxAttributes (432, 144);
		this.declare (x, y);
		
	}
	
}
