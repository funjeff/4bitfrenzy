package npcs;

import java.util.Random;

import engine.Sprite;
import util.DummyCollider;

public class PopcornMachine extends NPC {
	
	long spawnTime;
	
	public static final int CORN_SPREAD = 24;

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
			
			int xOffs = rand.nextInt (200) * negX;
			int yOffs = rand.nextInt (200) * negY;
			DummyCollider collider = new DummyCollider ((int)getX () + xOffs - CORN_SPREAD, (int)getY () + yOffs - CORN_SPREAD, CORN_SPREAD * 2, CORN_SPREAD * 2);
			if (!collider.isColliding ("Popcorn")) {
				Popcorn corn = new Popcorn (this.getX() + xOffs, this.getY() + yOffs);
			}
			
		}
	}
	
}
