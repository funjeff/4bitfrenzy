package titleScreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

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
		keysSprite.draw ((int)getX (), (int)getY ());
		
		//Draw text
		Graphics g = RenderLoop.wind.getBufferGraphics ();
		g.setColor (new Color (0x00FF00));
		Font f = new Font ("Arial", 0, 16);
		g.setFont (f);
		g.drawString ("W", (int)getX () + 46, (int)getY () + 22);
		g.drawString ("A", (int)getX () + 10, (int)getY () + 60);
		g.drawString ("S", (int)getX () + 47, (int)getY () + 60);
		g.drawString ("D", (int)getX () + 86, (int)getY () + 60);
		
	}
	
}
