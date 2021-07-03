package gameObjects;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;

public class WaveCompleteGraphic extends GameObject {

	public static Sprite icon = new Sprite ("resources/sprites/wave_complete.png");
	int time;
	
	public static final int WAVE_COMPLETE_TIME = 300;
	public static final int WAVE_COMPLETE_FADE_TIME = 250;
	
	public WaveCompleteGraphic () {
		setSprite (icon);
		declare (0, 0);
		time = 0;
	}
	
	@Override
	public void frameEvent () {
		if (time > 300) {
			forget ();
		}
		time++;
	}
	
	@Override
	public void draw () {
		setX (GameCode.getSettings ().getResolutionX () / 2 - icon.getWidth () / 2);
		setY (GameCode.getSettings ().getResolutionY () / 2 - icon.getHeight () / 2);
		int fadeDuration = WAVE_COMPLETE_TIME - WAVE_COMPLETE_FADE_TIME;
		int fadeTime = time - WAVE_COMPLETE_FADE_TIME;
		if (fadeTime > 0) {
			try {
				float fadeAmt = (float)(fadeDuration - fadeTime) / fadeDuration;
				Graphics2D g = (Graphics2D)RenderLoop.wind.getBufferGraphics ();
				AlphaComposite ac = AlphaComposite.getInstance (AlphaComposite.SRC_OVER, fadeAmt);
				g.setComposite (ac);
				g.drawImage (icon.getFrame (0), (int)getX (), (int)getY (), null);
			} catch (IllegalArgumentException e) {
				//Oops
			}
		} else {
			super.drawAbsolute();
		}
	}
	
}
