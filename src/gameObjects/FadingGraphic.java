package gameObjects;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;
import resources.Hud;
import resources.Textbox;

public class FadingGraphic extends GameObject {

	int time;
	
	public int endTime = 200;
	public int fadeOutTime = 150;
	public static final int FADE_IN_TIME = 50;
	
	
	
	Textbox graphic = new Textbox ("~Ctext (lime green)~ ~S48~WAVE COMPLETE!");

	boolean fadeIn = true;
	
	boolean fadeOut = false;
	
	boolean createRoundStartGraphic;
	
	public FadingGraphic () {
		declare (0, 0);
		time = 0;
		graphic.changeBoxVisability();
		createRoundStartGraphic = true;
	}
	
	@Override
	public void frameEvent () {
		
		if (time > endTime) {
			forget ();
		}
		
		
		if (time == 100 && createRoundStartGraphic) {
			FadingGraphic f = new FadingGraphic ();
			f.setText("~Ctext (lime green)~ ~S36~ROUND: " + Hud.roundNum + " START!");
			f.createRoundStartGraphic = false;
			f.setEndTime(100);
			if (graphic.getPureText().equals("WAVE COMPLETE!")) {
				f.setX(this.getX() + 40);
			} else {
				f.setX(this.getX() - 40);
			}
			f.setY(this.getY() + 60);
		}

		time++;
	}
	
	public void setText (String text) {
		graphic.changeText(text);
	}
	
	public void setEndTime (int endTime) {
		this.endTime =endTime;
		this.fadeOutTime = endTime -50;
	}
	
	@Override
	public void draw () {
	
		int fadeDuration = endTime - fadeOutTime;
		int fadeTime = time - fadeOutTime;

			if (fadeTime < 0 && time <FADE_IN_TIME) {
				fadeTime = FADE_IN_TIME -time;
			}
		
			if (fadeTime > 0) {
				try {
					float fadeAmt = (float)(fadeDuration - fadeTime) / fadeDuration;

					graphic.setTransparancy(fadeAmt);
				} catch (IllegalArgumentException e) {
					//Oops
				}
				
			}
			graphic.setX(GameCode.getViewX() + this.getX());
			graphic.setY(GameCode.getViewY() + this.getY());
			graphic.draw();
	}
	
}
