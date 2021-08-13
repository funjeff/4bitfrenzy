package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.TextComponent;
import java.awt.event.KeyEvent;

import engine.GameObject;
import engine.RenderLoop;
import menu.Menu;
import menu.TextComponite;

public class Tutorial extends GameObject {

	public Menu menu;
	public MovableRectHighlight highlight;
	public TextComponite menuText;
	public TextComponite continueComponent;
	
	public int stage = -1;
	
	public static final int TUTORIAL_MENU_X = 300;
	public static final int TUTORIAL_MENU_Y = 160;
	public static final int TUTORIAL_MENU_WIDTH = 53;
	public static final int TUTORIAL_MENU_HEIGHT = 31;
	public static final int PADDING = 4;
	
	public static final Rectangle startingRect = new Rectangle (TUTORIAL_MENU_X - PADDING, TUTORIAL_MENU_Y - PADDING, TUTORIAL_MENU_WIDTH * 8 + PADDING * 2, TUTORIAL_MENU_HEIGHT * 8 + PADDING * 2);
	public static final Rectangle scoreRect = new Rectangle (320, 89, 272, 33);
	public static final Rectangle compassRect = new Rectangle (45, 27, 150, 156);
	public static final Rectangle itemRect = new Rectangle (240, 99, 32, 32);
	public static final Rectangle waveRect = new Rectangle (640, 9, 352, 113);
	public static final Rectangle livesRect = new Rectangle (20, 4, 512, 27);
	
	public Tutorial () {
		
		//Render stuff
		setRenderPriority (42000);
		
		//Create the menu
		menu = new Menu ();
		menu.declare (TUTORIAL_MENU_X, TUTORIAL_MENU_Y);
		menu.setRenderPriority (42001);
		
		//Add components and set menu dimension
		menuText = new TextComponite (25, 26, menu);
		menuText.setText ("AAAAAAAAAAAAAAAAAAAAAAAAA");
		continueComponent = new TextComponite (25, 1, menu);
		continueComponent.setText ("PRESS ENTER TO CONTINUE...");
		menu.setWidth (TUTORIAL_MENU_WIDTH);
		menu.setHeight (TUTORIAL_MENU_HEIGHT);
		
		//Make the highlight
		highlight = new MovableRectHighlight (startingRect);
		highlight.setRenderPriority (42003);
		
		//Declare
		declare ();
		
	}
	
	public void moveHighlight (Rectangle objBounds, int time) {
		Rectangle toRect = new Rectangle (objBounds.x - PADDING, objBounds.y - PADDING, objBounds.width + PADDING * 2, objBounds.height + PADDING * 2);
		highlight.moveTo (toRect, time);
	}
	
	@Override
	public void frameEvent () {
		if (this.keyPressed (KeyEvent.VK_ENTER) || stage == -1) {
			stage++;
			switch (stage) {
				case 0:
					break;
				case 1:
					menuText.setText ("COMPASS");
					moveHighlight (compassRect, 30);
					break;
				case 2:
					menuText.setText ("ITEM BOX");
					moveHighlight (itemRect, 30);
					break;
				case 3:
					menuText.setText ("SCORE");
					moveHighlight (scoreRect, 30);
					break;
				case 4:
					menuText.setText ("GAME INFO");
					moveHighlight (waveRect, 30);
					break;
				case 5:
					menuText.setText ("LIVES");
					moveHighlight (livesRect, 30);
					break;
				case 6:
					forget ();
					break;
			}
		}
	}
	
	@Override
	public void draw () {
		if (this.keyPressed ('W') || this.keyPressed ('A') || this.keyPressed ('S') || this.keyPressed ('D')) {
			forget (); //TODO do we keep this?
		}
		Graphics g = RenderLoop.wind.getBufferGraphics ();
		g.setColor (Color.BLACK);
		g.fillRect (TUTORIAL_MENU_X + 1, TUTORIAL_MENU_Y + 8, TUTORIAL_MENU_WIDTH * 8 - 1, TUTORIAL_MENU_HEIGHT * 8 - 8);
	}
	
	@Override
	public void forget () {
		menu.forget ();
		highlight.forget ();
		super.forget ();
	}
	
}
