package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;

public class MenuBar extends GameObject {

	public static Sprite uiBarBg = new Sprite ("resources/sprites/ui_bar_bg.png");
	
	public MenuBar () {
		declare (0, 0);
		setSprite (uiBarBg);
		this.setRenderPriority (41999);
	}
	
	@Override
	public void draw () {
		
		//Get the graphcis
		Graphics g = RenderLoop.wind.getBufferGraphics ();
		
		//Draw the UI bar
		super.drawAbsolute ();
		if (GameCode.getSettings ().getResolutionX() > 1280) {
			g.setColor (Color.BLACK);
			g.fillRect (1280, 0, GameCode.getSettings ().getResolutionX () - 1280, 120);
		}
		
	}
	
}
