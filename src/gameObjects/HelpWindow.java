package gameObjects;

import java.awt.event.KeyEvent;

import engine.GameCode;
import engine.GameObject;
import menu.Menu;
import menu.MenuComponite;
import menu.TextComponite;
import players.Bit;

public class HelpWindow extends GameObject {

	public Menu menu;
	
	public HelpWindow () {
		
		//Render stuff
		declare ();
		setRenderPriority (42000);
		
		//Make the menu
		menu = new Menu ();
		menu.declare (OldTutorial.TUTORIAL_MENU_X, OldTutorial.TUTORIAL_MENU_Y);
		menu.setRenderPriority (42001);
		
		//Add components and set menu dimension
		MenuComponite m = new MenuComponite (420, 20, menu);
		TextComponite menuText = new TextComponite (400, 200, menu);
		if (Bit.getCurrentPlayer ().perk == 2) {
			menuText.setText ("          CONTROLS ~N"
					  + "MOVE: WASD~N"
					  + "GRAB/PULL REGISTER: SHIFT~N"
					  + "USE ITEM: ENTER~N"
					  + "SHOW MAP: M~N"
					  + "TOGGLE COMPASS: SPACE" );
		} else if (Bit.getCurrentPlayer ().perk == 5) {
			menuText.setText ("          CONTROLS ~N"
					  + "MOVE (1): WASD~N"
					  + "MOVE (2): ARROW KEYS~N"
					  + "CHANGE CAMERA: LEFT CTRL~N"
					  + "GRAB/PULL REGISTER: SHIFT~N"
					  + "USE ITEM: ENTER~N"
					  + "TOGGLE COMPASS: SPACE" );
		} else {
			menuText.setText ("          CONTROLS ~N"
					  + "MOVE: WASD~N"
					  + "GRAB/PULL REGISTER: SHIFT~N"
					  + "USE ITEM: ENTER~N"
					  + "TOGGLE COMPASS: SPACE" );
		}
		menuText.getTextbox().setLineSpacing(1.5);
		TextComponite continueComponent = new TextComponite (400, 16, menu);
		continueComponent.setText ("PRESS H TO CLOSE");
		menu.addComponite(m);
		menu.addComponite (menuText);
		menu.addComponite (continueComponent);
		menu.addComponite(m);
		menu.setBackgroundColor (0);
		
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent ();
		menu.setX(OldTutorial.TUTORIAL_MENU_X + GameCode.getViewX());
		menu.setY(OldTutorial.TUTORIAL_MENU_Y + GameCode.getViewY());
		if (keyPressed ('H')) {
			if (menu.isOpen ()) {
				menu.close ();
			} else {
				menu.open ();
			}
		}
		if (keyPressed (KeyEvent.VK_ESCAPE)) {
			menu.close ();
		}
	}
	
}
