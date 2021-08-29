package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.TextComponent;
import java.awt.event.KeyEvent;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import menu.Menu;
import menu.MenuComponite;
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
	
	public static final Rectangle startingRect = new Rectangle (TUTORIAL_MENU_X - PADDING, TUTORIAL_MENU_Y - PADDING, TUTORIAL_MENU_WIDTH * 8 + PADDING * 2, TUTORIAL_MENU_HEIGHT * 8 + 8 + PADDING * 2);
	public static final Rectangle scoreRect = new Rectangle (314, 68, 266, 17);
	public static final Rectangle compassRect = new Rectangle (17, 6, 232, 107);
	public static final Rectangle itemRect = new Rectangle (1141, 28, 58, 70);
	public static final Rectangle waveRect = new Rectangle (706, 19, 324, 86);
	public static final Rectangle livesRect = new Rectangle (318, 25, 289, 32);
	public static final Rectangle screenRect = new Rectangle (4, 4, 1272, 712);
	
	public Tutorial () {
		//Render stuff
		setRenderPriority (42000);
		
		//Create the menu
		menu = new Menu ();
		menu.declare (TUTORIAL_MENU_X, TUTORIAL_MENU_Y);
		menu.setRenderPriority (42001);
		
	
		//Add components and set menu dimension
		MenuComponite m = new MenuComponite (420, 20, menu);
		menuText = new TextComponite (400, 200, menu);
		menuText.getTextbox().setLineSpacing(1.5);
		continueComponent = new TextComponite (400, 16, menu);
		continueComponent.setText ("PRESS ENTER TO CONTINUE...");
		menu.addComponite(m);
		menu.addComponite (menuText);
		menu.addComponite (continueComponent);
		menu.addComponite(m);
		menu.setBackgroundColor (0);
		//menu.setWidth (TUTORIAL_MENU_WIDTH);
		//menu.setHeight (TUTORIAL_MENU_HEIGHT);
		menu.open ();
		
		//Make the highlight
		highlight = new MovableRectHighlight (screenRect);
		moveHighlight (startingRect, 20);
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
		menu.setX(TUTORIAL_MENU_X + GameCode.getViewX());
		menu.setY(TUTORIAL_MENU_Y + GameCode.getViewY());
		if (this.keyPressed (KeyEvent.VK_ESCAPE)) {
			forget (); //TODO do we keep this?
		}
		
		//Handle tutorial progression
		if ((this.keyPressed (KeyEvent.VK_ENTER) || stage == -1) && (!highlight.isMoving () || stage == -1)) {
			stage++;
			switch (stage) {
				case 0:
					menuText.setText (
					"YOUR JOB IS TO DELIVER   ~N" +
					"REGISTERS TO DATA SLOTS. ~N" +
					"THESE WILL BE HIGHLIGHTED~N" +
					"WHITE WHEN YOU GET NEAR  ~N" +
					"THEM. YOU MAY PUSH THE   ~N" +
					"REGISTERS, OR YOU MAY HOLD~N" +
					"SHIFT TO PULL THEM.  ");
					break;
				case 1:
					menuText.setText (
					"THIS IS THE COMPASS. IT   ~N" +
					"POINTS TO THE NEAREST     ~N" +
					"REGISTER BY DEFAULT. YOU  ~N" +
					"CAN TOGGLE THE REGISTER IT~N" +
					"POINTS TO BY PRESSING     ~N" +
					"SPACE."
					);
					moveHighlight (compassRect, 20);
					break;
				case 2:
					menuText.setText (
					"THE ITEM BOX HOLDS THE    ~N" +
					"ITEM YOU CURRENTLY HAVE.  ~N" +
					"TO USE YOUR ITEM, PRESS   ~N" +
					"THE ENTER KEY."
					);
					moveHighlight (itemRect, 20);
					break;
				case 3:
					menuText.setText (
					"THIS BOX DISPLAYS YOUR    ~N" +
					"SCORE. YOU CAN ONLY SCORE ~N" +
					"POINTS BY TURNING IN      ~N" +
					"REGISTERS."
					);
					moveHighlight (scoreRect, 20);
					break;
				case 4:
					menuText.setText (
					"THESE BOXES DISPLAY INFO  ~N" +
					"ABOUT THE CURRENT WAVE.   ~N" +
					"WHEN THE TIME HITS ZERO,  ~N" +
					"MORE REGISTERS WILL SPAWN ~N" +
					"AND THE NEXT WAVE WILL    ~N" +
					"START."
					);
					moveHighlight (waveRect, 20);
					break;
				case 5:
					menuText.setText (
					"AT THEN END OF A WAVE, YOU~N" +
					"WILL LOSE ONE LIFE FOR    ~N" +
					"EACH REMAINING REGISTER.  ~N" +
					"WHEN YOUR LIVES HIT ZERO, ~N" +
					"YOU LOSE THE GAME.        "
					);
					moveHighlight (livesRect, 20);
					break;
				case 6:
					menuText.setText (
					"NOW, GO FORTH AND DELIVER ~N" +
					"REGISTERS AS QUICKLY AS   ~N" +
					"YOU CAN!                  ~N" +
					"(YOU CAN PRESS H TO SEE   ~N" +
					"THE CONTROLS AGAIN AT     ~N" +
					"ANY TIME)"
					);
					moveHighlight (screenRect, 20);
					break;
				case 7:
					forget ();
					break;
			}
		}
		
	}
	
	@Override
	public void forget () {
		menu.forget ();
		highlight.forget ();
		super.forget ();
	}
	
}
