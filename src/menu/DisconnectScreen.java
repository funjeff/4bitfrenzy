package menu;

import java.awt.Color;
import java.awt.Graphics;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;

public class DisconnectScreen extends GameObject {
	
	public static Sprite disconnectBg = new Sprite ("resources/sprites/disconnect_screen.png");
	
	public DisconnectScreen () {
		
		setSprite (disconnectBg);
		this.setRenderPriority (69999);
		declare (0, 0);
		
	}
	
	@Override
	public void draw () {
		Graphics g = RenderLoop.wind.getBufferGraphics ();
		g.setColor (Color.BLACK);
		int screenWidth = GameCode.getSettings ().getResolutionX ();
		int screenHeight = GameCode.getSettings ().getResolutionY ();
		g.fillRect (0, 0, screenWidth, screenHeight);
		int drawX = (screenWidth - 1280) / 2;
		int drawY = (screenHeight - 720) / 2;
		getSprite ().draw (drawX, drawY);
	}

}
