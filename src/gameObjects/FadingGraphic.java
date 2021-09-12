package gameObjects;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;
import resources.Hud;

public class FadingGraphic extends GameObject {

	int time;
	
	public static final int WAVE_COMPLETE_TIME = 300;
	public static final int WAVE_COMPLETE_FADE_TIME = 250;
	
	public FadingGraphic () {
		declare (0, 0);
		time = 0;
	}
	
	@Override
	public void frameEvent () {
		if (time > 300) {
			if (this.getSprite().equals(Hud.TIME_UP) || this.getSprite().equals(Hud.WAVE_COMPLEATE)){
				FadingGraphic g = new FadingGraphic ();
				g.setSprite(new Sprite ("resources/sprites/start.png"));
			}
			forget ();
		}
		time++;
	}
	
	@Override
	public void draw () {
		setX (GameCode.getSettings ().getResolutionX () / 2 - this.getSprite().getWidth () / 2);
		setY (GameCode.getSettings ().getResolutionY () / 2 - this.getSprite().getHeight () / 2);
		int fadeDuration = WAVE_COMPLETE_TIME - WAVE_COMPLETE_FADE_TIME;
		int fadeTime = time - WAVE_COMPLETE_FADE_TIME;
		if (fadeTime > 0) {
			try {
				float fadeAmt = (float)(fadeDuration - fadeTime) / fadeDuration;
				Graphics2D g = (Graphics2D)RenderLoop.wind.getBufferGraphics ();
				AlphaComposite ac = AlphaComposite.getInstance (AlphaComposite.SRC_OVER, fadeAmt);
				g.setComposite (ac);
				g.drawImage (this.getSprite().getFrame (0), (int)getX (), (int)getY (), null);
			} catch (IllegalArgumentException e) {
				//Oops
			}
		} else {
			super.drawAbsolute();
		}
	}
	
}
