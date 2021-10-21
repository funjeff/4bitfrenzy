package npcs;

import java.io.File;
import java.util.ArrayList;

import engine.GameCode;
import engine.ObjectHandler;
import engine.Sprite;
import menu.CompositeComponite;
import menu.Menu;
import menu.MenuComponite;
import menu.ObjectComponite;
import menu.TextComponite;
import titleScreen.TitleScreen;
import titleScreen.TitleScreen.Button;
import titleScreen.Tutorial;

public class TutorialNPC extends TalkableNPC {
	
	Button confirmButton = new Button (new Sprite ("resources/sprites/yes.png"));
	Button cancelButton = new Button (new Sprite ("resources/sprites/no.png"));
	
	public TutorialNPC (int x, int y) {
		super (x,y);
		this.setSprite(new Sprite ("resources/sprites/Notpad.png"));
		Menu menu = new Menu ();
		
		menu.addComponite(new MenuComponite (20,20,menu));
		
		menu.addComponite(new TextComponite (menu," ~S8~  ~Cnormal~    DO YOU WANT TO PLAY THE TUTORIAL AGAIN?   "));
		menu.addComponite(new MenuComponite (20,20,menu));
		
		ArrayList <MenuComponite> buttonComponites = new ArrayList <MenuComponite>();
		
		buttonComponites.add(new MenuComponite (150,20,menu));
		
		buttonComponites.add(new ObjectComponite (confirmButton, menu));
		
		buttonComponites.add(new MenuComponite (40,20,menu));
		
		buttonComponites.add(new ObjectComponite (cancelButton, menu));
		
		
		menu.addComponite(new CompositeComponite (buttonComponites, menu));
		
		menu.setColor("Notepad");
		
		menu.setTop(new Sprite ("resources/sprites/Text/notepadTop.png"));
		
		menu.setName("CMD.EXE");
		
		menu.setBackgroundColor(0xFFFFFF);
		
		menu.setX(this.getX() + 100);
		menu.setY(this.getY() + 100);
		
		
		this.setMenu(menu);
		
		this.setHitboxAttributes(this.getSprite().getWidth(), this.getSprite().getHeight());
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent();
		if (cancelButton.isPressed()) {
			cancelButton.reset();
			this.getMenu().close();
		}
		if (confirmButton.isPressed()) {
			TitleScreen.clearTitleScreen();
			GameCode.getTitleScreen().forget();
			
			Menu m = new Menu ();
			
			
			m.setBackgroundColor(0xFFFFFF);
			m.addComponite(new TextComponite (m," ~CWhite~YO WELCOME BACK TO MY 4 BIT FRENZY CONTROLS TUTORIAL!"));
			m.setWidth(400);
			m.setHeight(-1);
			m.open();
			m.setRenderPriority(69);
			
			Tutorial t = new Tutorial (m);
			
			t.start();
			
			t.declare(0, 0);
			
			confirmButton.reset();
			this.getMenu().close();
		}
	}
}
