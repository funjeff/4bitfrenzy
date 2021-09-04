package titleScreen;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;
import menu.Menu;
import menu.MenuComponite;
import menu.TextComponite;
import resources.Textbox;

public class HintMessage extends GameObject {

	private Menu hintMenu;
	
	private TextComponite hintHeader;
	private TextComponite hintContent;
	
	private GameObject hintObject;
	
	public HintMessage () {
		
		hintMenu = new Menu ();
		MenuComponite topPadding = new MenuComponite (800, 16, hintMenu);
		hintHeader = new TextComponite (800, 32, hintMenu);
		hintContent = new TextComponite (800, 128, hintMenu);
		hintMenu.addComponite (topPadding);
		hintMenu.addComponite (hintHeader);
		hintMenu.addComponite (hintContent);
		hintContent.getTextbox ().setLineSpacing (1.5);
		hintHeader.setText ("HELPFUL TIP:");
		hintMenu.open ();
		hintMenu.setBackgroundColor (0);
		hintMenu.setX (100 - getDrawX ());
		hintMenu.setY (200 - getDrawY ());
		pickHint ();
		
		declare ();
		
	}
	
	public void pickHint () {
		
		//Forget the previous hint object if needed
		if (hintObject != null && hintObject.declared ()) {
			hintObject.forget ();
		}
		
		//Pick a new hint
		int NUM_HINTS = 4; //Number of different hints
		int hintId = (int)(Math.random () * NUM_HINTS);
		switch (hintId) {
			case 0:
				hintContent.setText ("PIANOS ARE HIGHLY FLAMMABLE");
				hintObject = new FlamingPiano ();
				hintObject.declare (600, 240);
				break;
			case 1:
				hintContent.setText ("ANYTHING IS \"EDIBLE\" WHEN DEEP FRIED;~N"
										+ "HOWEVER, DUCKS ARE THE TASTIEST.");
				break;
			case 2:
				hintContent.setText ("TRY BRINGING OBJECTS TO DESTINATIONS~N"
										+ "THAT ARE OUTLINED THE SAME COLOR WHEN~N"
										+ "YOU GET NEAR THEM!");
				break;
			case 3:
				hintContent.setText ("THE TRUCK NEEDS A KEY TO BE TURNED ON...~N"
										+ "BUT YOU DON'T NEED A KEY TO BURN IT!");
				break;
		}
		
	}
	
	@Override
	public void draw () {
		hintMenu.setX (100 - getDrawX () - GameCode.getViewX ());
		hintMenu.setY (200 - getDrawY () - GameCode.getViewY ());
	}
	
	private class FlamingPiano extends GameObject {
		
		public final Sprite pianoSprite = new Sprite ("resources/sprites/config/flaming_piano.txt");
		
		public FlamingPiano () {
			this.setSprite (pianoSprite);
			this.getAnimationHandler ().setFrameTime (198);
			this.setRenderPriority (420);
		}
		
	}
	
	@Override
	public void forget () {
		if (hintObject != null && hintObject.declared ()) {
			hintObject.forget ();
		}
		hintMenu.forget ();
		super.forget ();
	}
	
}
