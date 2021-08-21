package titleScreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;

public class MoveHint extends GameObject {
	
	public static Sprite keysSprite = new Sprite ("resources/sprites/keys.png");
	
	public MoveHint () {
		setSprite (keysSprite);
		setRenderPriority (100);
	}

	@Override
	public void draw () {
		
		//Draw the key sprite
		keysSprite.draw (getDrawX (), getDrawY ());
		
		//Draw text
		Graphics g = RenderLoop.wind.getBufferGraphics ();
		g.setColor (new Color (0x00FF00));
		Font f = new Font ("Arial", 0, 16);
		g.setFont (f);
		g.drawString ("W", (int)getDrawX () + 46, (int)getDrawY () + 22);
		g.drawString ("A", (int)getDrawX () + 10, (int)getDrawY () + 60);
		g.drawString ("S", (int)getDrawX () + 47, (int)getDrawY () + 60);
		g.drawString ("D", (int)getDrawX () + 86, (int)getDrawY () + 60);
		
	}
	
}
