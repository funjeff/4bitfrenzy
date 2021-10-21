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
	
	private MapDisplay mapDisplay;
	
	public static int MAP_MENU_X = 100;
	public static int MAP_MENU_Y = 200;
	
	public MenuBar () {
		declare (0, 0);
		setSprite (uiBarBg);
		mapDisplay = new MapDisplay ();
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
		
		//Draw the map display
		mapDisplay.getMenu ().setX (GameCode.getViewX () + MAP_MENU_X);
		mapDisplay.getMenu ().setY (GameCode.getViewY () + MAP_MENU_Y);
		mapDisplay.draw ();
		
	}
	
}
