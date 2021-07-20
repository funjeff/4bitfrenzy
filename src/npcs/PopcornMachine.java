package npcs;

import java.util.Random;

import engine.Sprite;

public class PopcornMachine extends NPC {
	
	long spawnTime;

	public PopcornMachine (double x, double y) {

		super(x,y);
		setSprite (new Sprite ("resources/sprites/popcorn.png"));
		
		setHitboxAttributes (this.getSprite().getWidth(), this.getSprite().getHeight());

		Random rand = new Random ();
		
		spawnTime = System.currentTimeMillis() + rand.nextInt(2000);
		
	}

	@Override
	public void npcFrame () {
		
		if (spawnTime < System.currentTimeMillis()) {
			
			Random rand = new Random ();
			
			spawnTime = System.currentTimeMillis() + rand.nextInt(2000);
			
			int negX = 1;
			int negY = 1;
			
			if (rand.nextBoolean()) {
				negX = negX * -1;
			}
			
			if (rand.nextBoolean()) {
				negY = negY * -1;
			}
			
			Popcorn corn = new Popcorn (this.getX() + (rand.nextInt(200) * negX),this.getY() + (rand.nextInt(200) * negY));
			
		}
	}
	
}
