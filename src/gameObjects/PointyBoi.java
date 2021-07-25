package gameObjects;

import java.awt.image.BufferedImage;

import engine.GameObject;
import engine.Sprite;

public class PointyBoi extends GameObject {

	
	
	public PointyBoi () {
		//setSprite (spr);
		setRenderPriority (101);
	}
	
	@Override
	public void draw () {
		try {
		double ang = -Math.atan2 (getCursorX () - 128, getCursorY () - 128) + Math.PI / 2;
		//spr.drawRotated (128, 128, 0, 60, 36, ang);
		} catch (Exception e) {
			
		}
	}
	
}
