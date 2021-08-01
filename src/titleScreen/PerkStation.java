package titleScreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;

public class PerkStation extends GameObject {

	public static Sprite perkStations = new Sprite ("resources/sprites/config/perk_stations.txt");
	
	public static final int[] perkMap = new int[] {15, 0, 1, 2, 4, 3, 5, 6};
	public static final Color[] perkColorMap = new Color[] {new Color (0x00FF00), new Color (0x0000FF), new Color (0x55FF0D), new Color (0xFFFF00), new Color (0xFF00FF), new Color (0xFF0000), new Color (0xFFFFFF), new Color (0x800080)};
	public static final String[] perkNameMap = new String[] {"Default Perk", "Blast Processing", "Grip Strength", "Navigation Bit", "Duplication Bit", "Powerhouse Bit", "Duel Core", "Professional Gambler"};
	
	private Scene scene;
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
			if (!TitleScreen.scenePlaying ()) {
				scene = TitleScreen.playScene ("resources/scenes/" + perkNameMap [perk] + ".txt", 900, 129);
			}
		} else {
			if (scene != null && scene.declared ()) {
				scene.forget ();
			}
		}
	}
	
	@Override
	public void draw () {
		super.draw ();
		if (isColliding ("TitleBit")) {
			Graphics g = RenderLoop.wind.getBufferGraphics ();
			Font f = new Font ("Arial", 96, 40);
			g.setFont (f);
			g.setColor (perkColorMap [perk]);
			g.drawString (perkNameMap [perk], 864, 90);
		}
	}
	
}
