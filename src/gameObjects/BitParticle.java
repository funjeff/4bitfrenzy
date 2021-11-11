package gameObjects;

import engine.GameObject;
import engine.Sprite;

public class BitParticle extends GameObject {

	public static Sprite bitSprite = new Sprite ("resources/sprites/config/bit_particle.txt");
	private int speed;
	private int lifespan;
	
	public BitParticle () {
		
		setSprite (bitSprite);
		getAnimationHandler ().setFrameTime (0);
		getAnimationHandler ().setAnimationFrame ((int)(Math.random () * 2)); //Random frame from 0-1 inclusive
		speed = (int)(Math.random () * 3) + 1;
		lifespan = 20 + (int)(Math.random () * 20);
		
	}
	
	@Override
	public void frameEvent () {
		setY (getY () - speed);
		lifespan--;
		if (lifespan <= 0) {
			forget ();
		}
	}
	
}
