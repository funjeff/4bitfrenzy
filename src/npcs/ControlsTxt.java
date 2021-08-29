package npcs;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.GameCode;
import engine.Sprite;
import menu.CompositeComponite;
import menu.Menu;
import menu.MenuComponite;
import menu.ObjectComponite;
import menu.TextComponite;
import titleScreen.TitleScreen.Button;
import titleScreen.TitleScreen.TextButton;

public class ControlsTxt extends TalkableNPC{
	TextButton [] buttons;
	int selectedButton = -1;
	int [] controls;
	Button defaultButton;
	
	Menu confirmMenu;
	
	Button confirmButton;
	Button declineButton;
	
	public ControlsTxt(double x, double y) {
		super(x, y);
		
		this.getAnimationHandler().setFlipHorizontal(true);
		buttons = new TextButton[13];
		controls = new int [13];
		
		ObjectComponite [] componites = new ObjectComponite [13];
		
		Menu menu = new Menu ();
		
		MenuComponite paddingTop = new MenuComponite (400, 10,menu);
		MenuComponite paddingSide = new MenuComponite (30, 8,menu);
		
		MenuComponite paddingDefaultButton = new MenuComponite (175,8, menu);
		
		MenuComponite paddingBottom = new MenuComponite (10,10, menu);
		
		defaultButton = new Button (new Sprite ("resources/sprites/default button.png"));
		
		CompositeComponite defaultButtonCompointe = new CompositeComponite (paddingDefaultButton,new ObjectComponite (defaultButton,menu),menu);
		
		TextComponite t = new TextComponite(menu,"~S8~  ~Cnormal~ UP:");
		TextComponite t2 = new TextComponite(menu,"~S8~  ~Cnormal~DOWN:");
		TextComponite t3 = new TextComponite (menu,"~S8~  ~Cnormal~LEFT:" );
		TextComponite t4 = new TextComponite (menu,"~S8~  ~Cnormal~RIGHT:" );
		TextComponite t5 = new TextComponite(menu,"~S8~  ~Cnormal~GRAB:");
		TextComponite t6 = new TextComponite (menu,"~S8~  ~Cnormal~USE ITEM:" );
		TextComponite t7 = new TextComponite (menu,"~S8~  ~Cnormal~ADJUST COMPASS:" );
		TextComponite t8 = new TextComponite(menu,"~S8~  ~Cnormal~MAP (NAVIGATION BIT):");
		TextComponite t9 = new TextComponite (menu,"~S8~  ~Cnormal~UP (DUAL CORES):" );
		TextComponite t10 = new TextComponite (menu,"~S8~  ~Cnormal~DOWN (DUAL CORES):" );
		TextComponite t11 = new TextComponite(menu,"~S8~  ~Cnormal~LEFT (DUAL CORES):");
		TextComponite t12 = new TextComponite (menu,"~S8~  ~Cnormal~RIGHT (DUAL CORES):" );
		TextComponite t13 = new TextComponite (menu,"~S8~  ~Cnormal~CHANGE CAMERA (DUAL CORES):" );
		
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new TextButton ();
			buttons[i].setText(KeyEvent.getKeyText(GameCode.getSettings().getControls()[i]));
			controls[i] = GameCode.getSettings().getControls()[i];
			buttons[i].setRenderPriority(73);
			componites[i] = new ObjectComponite(buttons[i],menu);
		}
		
		ArrayList <MenuComponite> line1 = new ArrayList <MenuComponite> ();
			line1.add(t);
			line1.add(paddingSide);
			line1.add(componites[0]);
			line1.add(paddingSide);
			line1.add(t9);
			line1.add(paddingSide);
			line1.add(componites[8]);
		
		ArrayList <MenuComponite> line2 = new ArrayList <MenuComponite> ();
			line2.add(t2);
			line2.add(paddingSide);
			line2.add(componites[1]);
			line2.add(paddingSide);
			line2.add(t10);
			line2.add(paddingSide);
			line2.add(componites[9]);
		
		ArrayList <MenuComponite> line3 = new ArrayList <MenuComponite> ();
			line3.add(t3);
			line3.add(paddingSide);
			line3.add(componites[2]);
			line3.add(paddingSide);
			line3.add(t11);
			line3.add(paddingSide);
			line3.add(componites[10]);
			
		ArrayList <MenuComponite> line4 = new ArrayList <MenuComponite> ();
			line4.add(t4);
			line4.add(paddingSide);
			line4.add(componites[3]);
			line4.add(paddingSide);
			line4.add(t12);
			line4.add(paddingSide);
			line4.add(componites[11]);
			
		ArrayList <MenuComponite> line5 = new ArrayList <MenuComponite> ();
			line5.add(t5);
			line5.add(paddingSide);
			line5.add(componites[4]);	
		
		ArrayList <MenuComponite> line6 = new ArrayList <MenuComponite> ();
			line6.add(t6);
			line6.add(paddingSide);
			line6.add(componites[5]);	
		
		ArrayList <MenuComponite> line7 = new ArrayList <MenuComponite> ();
			line7.add(t7);
			line7.add(paddingSide);
			line7.add(componites[6]);	
		
		ArrayList <MenuComponite> line8 = new ArrayList <MenuComponite> ();
			line8.add(t8);
			line8.add(paddingSide);
			line8.add(componites[7]);	
		
		ArrayList <MenuComponite> line9 = new ArrayList <MenuComponite> ();
			line9.add(t13);
			line9.add(paddingSide);
			line9.add(componites[12]);	
			
			
		ArrayList <CompositeComponite> menuComponites = new ArrayList <CompositeComponite>();
		menuComponites.add(new CompositeComponite (line1,menu));
		menuComponites.add(new CompositeComponite (line2,menu));
		menuComponites.add(new CompositeComponite (line3,menu));
		menuComponites.add(new CompositeComponite (line4,menu));
		menuComponites.add(new CompositeComponite (line5,menu));
		menuComponites.add(new CompositeComponite (line6,menu));
		menuComponites.add(new CompositeComponite (line7,menu));
		menuComponites.add(new CompositeComponite (line8,menu));
		menuComponites.add(new CompositeComponite (line9,menu));
		
		menu.addComponite(paddingTop);
		
		for (int i = 0; i < menuComponites.size(); i++) {
			menu.addComponite(menuComponites.get(i));
		}
		menu.addComponite(defaultButtonCompointe);
		menu.addComponite(paddingBottom);
		
		
		menu.setColor("Notepad");
		
		menu.setTop(new Sprite ("resources/sprites/Text/notepadTop.png"));
		
		menu.setName("CONTROLS.CONFIG - NOTEPAD");
		
		menu.setX(this.getX());
		menu.setY(this.getY());
		
		
		
		menu.setBackgroundColor(0xFFFFFF);
		
		this.setMenu(menu);
		this.setSprite(new Sprite ("resources/sprites/Notpad.png"));
		this.setHitboxAttributes(this.getSprite().getWidth(), this.getSprite().getHeight());
		
		menu.setCloseButton(new Button (new Sprite ("resources/sprites/x button.png")));
		menu.disableCloseButton();
		
		//configure the confrim/deny saving changes menu
		
		confirmMenu = new Menu();
		
		confirmButton = new Button (new Sprite ("resources/sprites/yes.png"));
		declineButton = new Button (new Sprite ("resources/sprites/no.png"));
		
		TextComponite confirmText = new TextComponite (menu,"~S8~  ~Cnormal~ DO YOU WANT TO SAVE YOUR CHANGES TO CONTROLS.CONFIG?");

		ObjectComponite yesButton = new ObjectComponite (confirmButton,menu);
		ObjectComponite noButton = new ObjectComponite (declineButton,menu);
		MenuComponite spaceBetween = new MenuComponite (200,20,menu);
		ArrayList <MenuComponite> buttons = new ArrayList <MenuComponite>();
		
		buttons.add(new MenuComponite (80,10,menu));
		buttons.add(yesButton);
		buttons.add(spaceBetween);
		buttons.add(noButton);
		
		CompositeComponite buttonComponite = new CompositeComponite (buttons,menu);
		
		confirmMenu.setColor("NotepadAlt");
		
		confirmMenu.setTop(new Sprite ("resources/sprites/Text/notepadTop.png"));
		
		confirmMenu.setName("CONTROLS.CONFIG - NOTEPAD");
		
		confirmMenu.setBackgroundColor(0xFFFFFF);
		
		confirmMenu.setX(this.getX() + 80);
		confirmMenu.setY(this.getY() + 80);
		
		confirmMenu.setRenderPriority(157);
		
		confirmMenu.addComponite(paddingTop);
		confirmMenu.addComponite(confirmText);
		confirmMenu.addComponite(new MenuComponite (10,20,menu));
		confirmMenu.addComponite(buttonComponite);
		confirmMenu.addComponite(new MenuComponite (10,30,menu));
		
		
		
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent();
		if (!this.getMenu().isClosed() && !confirmMenu.isOpen()) {
			for (int i = 0; i < buttons.length; i++) {
				if (buttons[i].isPressed()) {
					if (i != selectedButton) {
						if (selectedButton != -1) {
							buttons[selectedButton].reset();
						}
						selectedButton = i;
					} else {
						if (getKeysDown().length != 0) {
							controls[i] = getKeysDown()[0];
							buttons[i].setText(KeyEvent.getKeyText(controls[i]));
							selectedButton = -1;
						}
					}
				}
			}
			if (defaultButton.isPressed()) {
				
				controls = GameCode.getDefaultControls();
				
				for (int i = 0; i < buttons.length; i++) {
					buttons[i].setText(KeyEvent.getKeyText(controls[i]));
				}
			}
		}
		
		if (confirmButton.isPressed()) {
			this.saveControls();
			this.getMenu().close();
			confirmMenu.close();
			confirmButton.reset();
		}
		
		if (declineButton.isPressed()) {
			discardControls();
			this.getMenu().close();
			confirmMenu.close();
			declineButton.reset();
		}
		
	}
	
	@Override
	public void onCloseAttempt () {
		if (!confirmMenu.isOpen()) {
			confirmMenu.open();
		
		}
	}
	
	public void saveControls () {
		GameCode.getSettings().setControls(controls);
		GameCode.getSettings().updateControlFile();
	}
	public void discardControls () {
		for (int i = 0; i < GameCode.getSettings().getControls().length; i++) {
			controls[i] = GameCode.getSettings().getControls()[i];
			buttons[i].setText(KeyEvent.getKeyText(controls[i]));
		}
	}
}
