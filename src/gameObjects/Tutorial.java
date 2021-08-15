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
	
	public static final Rectangle startingRect = new Rectangle (TUTORIAL_MENU_X - PADDING, TUTORIAL_MENU_Y - PADDING, TUTORIAL_MENU_WIDTH * 8 + PADDING * 2, TUTORIAL_MENU_HEIGHT * 8 + 8 + PADDING * 2);
	public static final Rectangle scoreRect = new Rectangle (320, 89, 272, 33);
	public static final Rectangle compassRect = new Rectangle (45, 27, 150, 156);
	public static final Rectangle itemRect = new Rectangle (240, 99, 32, 32);
	public static final Rectangle waveRect = new Rectangle (640, 9, 352, 113);
	public static final Rectangle livesRect = new Rectangle (20, 4, 512, 27);
	public static final Rectangle screenRect = new Rectangle (4, 4, 1272, 712);
	
	public Tutorial () {
		
		//Render stuff
		setRenderPriority (42000);
		
		//Create the menu
		menu = new Menu ();
		menu.declare (TUTORIAL_MENU_X, TUTORIAL_MENU_Y);
		menu.setRenderPriority (42001);
		
		//Add components and set menu dimension
		menuText = new TextComponite (25, 14, menu);
		continueComponent = new TextComponite (25, 1, menu);
		continueComponent.setText ("PRESS ENTER TO CONTINUE...");
		menu.addComponite (menuText);
		menu.addComponite (continueComponent);
		menu.setBackgroundColor (0);
		menu.setWidth (TUTORIAL_MENU_WIDTH);
		menu.setHeight (TUTORIAL_MENU_HEIGHT);
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
		
		if (this.keyPressed ('W') || this.keyPressed ('A') || this.keyPressed ('S') || this.keyPressed ('D')) {
			forget (); //TODO do we keep this?
		}
		
		//Handle tutorial progression
		if ((this.keyPressed (KeyEvent.VK_ENTER) || stage == -1) && (!highlight.isMoving () || stage == -1)) {
			stage++;
			switch (stage) {
				case 0:
					menuText.setText (
					"YOUR JOB IS TO DELIVER    " +
					"REGISTERS TO DATA SLOTS.  " +
					"THESE WILL BE HIGHLIGHTED " +
					"WHITE WHEN YOU GET NEAR   " +
					"THEM. YOU MAY PUSH THE    " +
					"REGISTERS, OR YOU MAY HOLD" +
					"SHIFT TO PULL THEM.  ");
					break;
				case 1:
					menuText.setText (
					"THIS IS THE COMPASS. IT   " +
					"POINTS TO THE NEAREST     " +
					"REGISTER BY DEFAULT. YOU  " +
					"CAN TOGGLE THE REGISTER IT" +
					"POINTS TO BY PRESSING     " +
					"SPACE."
					);
					moveHighlight (compassRect, 20);
					break;
				case 2:
					menuText.setText (
					"THE ITEM BOX HOLDS THE    " +
					"ITEM YOU CURRENTLY HAVE.  " +
					"TO USE YOUR ITEM, PRESS   " +
					"THE ENTER KEY."
					);
					moveHighlight (itemRect, 20);
					break;
				case 3:
					menuText.setText (
					"THIS BOX DISPLAYS YOUR    " +
					"SCORE. YOU CAN ONLY SCORE " +
					"POINTS BY TURNING IN      " +
					"REGISTERS."
					);
					moveHighlight (scoreRect, 20);
					break;
				case 4:
					menuText.setText (
					"THESE BOXES DISPLAY INFO  " +
					"ABOUT THE CURRENT WAVE.   " +
					"WHEN THE TIME HITS ZERO,  " +
					"MORE REGISTERS WILL SPAWN " +
					"AND THE NEXT WAVE WILL    " +
					"START."
					);
					moveHighlight (waveRect, 20);
					break;
				case 5:
					menuText.setText (
					"AT THEN END OF A WAVE, YOU" +
					"WILL LOSE ONE LIFE FOR    " +
					"EACH REMAINING REGISTER.  " +
					"WHEN YOUR LIVES HIT ZERO, " +
					"YOU LOSE THE GAME.        "
					);
					moveHighlight (livesRect, 20);
					break;
				case 6:
					menuText.setText (
					"NOW, GO FORTH AND DELIVER " +
					"REGISTERS AS QUICKLY AS   " +
					"YOU CAN!"
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
