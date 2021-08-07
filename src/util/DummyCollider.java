package util;

import engine.GameObject;

public class DummyCollider extends GameObject {
	
	public DummyCollider (int x, int y, int width, int height) {
		setX (x);
		setY (y);
		setHitboxAttributes (width, height);
	}
	
}