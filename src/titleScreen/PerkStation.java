package titleScreen;

import engine.GameObject;
import engine.Sprite;

public class PerkStation extends GameObject {

	public static Sprite perkStations = new Sprite ("resources/sprites/config/perk_stations.txt");
	
	public static final int[] perkMap = new int[] {15, 0, 1, 2, 4, 3, 5, 6};
	
	private int perk;
	
	public PerkStation (int perk) {
		super ();
		this.perk = perk;
		this.setSprite (perkStations);
		this.getAnimationHandler ().setFrameTime (0);
		this.getAnimationHandler ().setAnimationFrame (perk);
		this.setHitboxAttributes (64, 96);
		this.setRenderPriority (70);
	}
	
	@Override
	public void frameEvent () {
		if (isColliding ("TitleBit")) {
			TitleScreen.perkNum = perkMap [perk];
		}
	}
	
}
