package npcs;

import java.util.ArrayList;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;
import map.Roome;
import menu.CompositeComponite;
import menu.Menu;
import menu.MenuComponite;
import menu.ObjectComponite;
import menu.TextComponite;
import titleScreen.TitleScreen.ArrowButtons;
import titleScreen.TitleScreen.Button;

public class SettingsTxt extends TalkableNPC {

	String [] resoultionArray = {"1280 x 720","1366 x 768","1600 x 900","1920 x 1080","1920 x 1200"};
	ArrowButtons resolutions = new ArrowButtons (resoultionArray);
	CompositeComponite resolutionComponite;
	Menu confirmMenu;
	
	Button confirmButton;
	Button declineButton;
	
	int volumeValue;
	int resoultionNum1;
	int resoultionNum2;
	int displayModeIndex;
	int widthValue;
	int heightValue;
	
	
	public SettingsTxt(double x, double y) {
		super(x, y);
		resolutionComponite = new CompositeComponite (new TextComponite (this.getMenu(), "~S8~  ~Cnormal~RESOLUTION:"), new ObjectComponite (resolutions, this.getMenu()), this.getMenu());

	
		
		String [] volumeArray = {"0","1","2","3","4","5","6","7","8","9","10"};
		ArrowButtons volume = new ArrowButtons (volumeArray);
		
		String [] widthArray = {"2","3","4","5","6","7","8","9","10"};
		ArrowButtons width = new ArrowButtons (widthArray);
		
		String [] heightArray = {"2","3","4","5","6","7","8","9","10"};
		ArrowButtons height = new ArrowButtons (heightArray);
		
		String [] displayModeArray = {"Horizontal Border","Full Border","Strech","Full"};
		ArrowButtons displayMode = new ArrowButtons (displayModeArray);
		
		width.setIndex(8);
		height.setIndex(8);
		volume.setIndex(10);
		
		
		Menu menu = new Menu ();
		
		MenuComponite paddingTop = new MenuComponite (400, 10,menu);
		
		TextComponite t = new TextComponite(menu,"~S8~  ~Cnormal~GAME VOLUME:     ");
		
		MenuComponite m = new MenuComponite (400, 10,menu);
		TextComponite t2 = new TextComponite(menu,"~S8~  ~Cnormal~MAP WIDTH:     ");
		TextComponite t3 = new TextComponite (menu,"~S8~  ~Cnormal~MAP HEIGHT:     " );
		TextComponite t4 = new TextComponite (menu,"~S8~  ~Cnormal~DISPLAY MODE:     " );
		
		ObjectComponite b1 = new ObjectComponite (volume,menu);
		ObjectComponite b2 = new ObjectComponite (width,menu);
		ObjectComponite b3 = new ObjectComponite (height,menu);
		ObjectComponite b4 = new ObjectComponite (displayMode,menu);
		
		CompositeComponite comp1 = new CompositeComponite (t,b1,menu);
		CompositeComponite comp2 = new CompositeComponite (t2,b2,menu);
		CompositeComponite comp3 = new CompositeComponite (t3,b3,menu);
		CompositeComponite comp4 = new CompositeComponite (t4,b4,menu);
		
		
		menu.setColor("Notepad");
		
		menu.setTop(new Sprite ("resources/sprites/Text/notepadTop.png"));
		
		menu.setName("SETTINGS.CONFIG - NOTEPAD");
		
		menu.setX(this.getX());
		menu.setY(this.getY());
		
		menu.addComponite(m);
		menu.addComponite(comp1);
		menu.addComponite(comp2);
		menu.addComponite(comp3);
		menu.addComponite(comp4);
		menu.addComponite(resolutionComponite);
		
		
		menu.setBackgroundColor(0xFFFFFF);
		
		this.setMenu(menu);
		this.setSprite(new Sprite ("resources/sprites/Notpad.png"));
		this.setHitboxAttributes(this.getSprite().getWidth(), this.getSprite().getHeight());
		
		menu.setCloseButton(new Button (new Sprite ("resources/sprites/x button.png")));
		menu.disableCloseButton();
		
		
		confirmMenu = new Menu();
		
		confirmButton = new Button (new Sprite ("resources/sprites/yes.png"));
		declineButton = new Button (new Sprite ("resources/sprites/no.png"));
		
		TextComponite confirmText = new TextComponite (menu,"~S8~  ~Cnormal~ DO YOU WANT TO SAVE YOUR CHANGES TO SETTINGS.CONFIG?");

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
		
		volumeValue = ((int)GameCode.volume + 45)/5;
		resoultionNum1 = GameCode.getSettings().getResolutionX();
		resoultionNum2 = GameCode.getSettings().getResolutionY();
		displayModeIndex = GameCode.getSettings().getScaleMode();
		widthValue = Roome.getMapWidth();
		heightValue = Roome.getMapHeight();
	
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent();
		if (this.getMenu().isOpen()) {
			ArrayList <GameObject> buttons = this.getMenu().getAllObjs();
			
			
			ArrowButtons volume = (ArrowButtons) buttons.get(0);
			ArrowButtons width = (ArrowButtons) buttons.get(1);
			ArrowButtons height = (ArrowButtons) buttons.get(2);
			ArrowButtons displayMode = (ArrowButtons) buttons.get(3);
			
			
			if (width.wasToggled()) {
				widthValue = Integer.parseInt(width.getSelectedString());
			}
			if (height.wasToggled()) {
				heightValue = Integer.parseInt(height.getSelectedString());
			}
			if (volume.wasToggled()) {
				volumeValue = Integer.parseInt(volume.getSelectedString());
				GameCode.musicHandler.playSoundEffect( -45 + (5 * volumeValue), "resources/sounds/effects/test sound.wav");
			}
			
			if (displayMode.wasToggled()) {
				displayModeIndex = displayMode.getIndex();
				
				if (displayMode.getIndex() == 3) {
					this.getMenu().removeComponite(resolutionComponite);
				} else {
					if (!this.getMenu().containtsComponite(resolutionComponite)) {
						this.getMenu().addComponite(resolutionComponite);
					}
				}
			}
			
			if (resolutions.wasToggled()) {
				String num1 = "";
				String num2 = "";
				
				boolean writeToNum1Or2 = false;
				
				for (int i = 0; i < resolutions.getSelectedString().length(); i++) {
					if (resolutions.getSelectedString().charAt(i) == ' ') {
						writeToNum1Or2 = true;
						i = i + 2;
					} else {
						
						if (!writeToNum1Or2) {
							num1 = num1 + resolutions.getSelectedString().charAt(i);
						} else {
							num2 = num2 + resolutions.getSelectedString().charAt(i);
						}
					}
				}
				resoultionNum1 = Integer.parseInt(num1);
				resoultionNum2 = Integer.parseInt(num2);
			}
			
			if (confirmButton.isPressed()) {
				this.saveSettings();
				this.getMenu().close();
				confirmMenu.close();
				confirmButton.reset();
			}
			
			if (declineButton.isPressed()) {
				discardSettings();
				this.getMenu().close();
				confirmMenu.close();
				declineButton.reset();
			}
			
		}
	}
	@Override
	public void onCloseAttempt () {
		if (!confirmMenu.isOpen()) {
			confirmMenu.open();
		}
	}
	
	public void saveSettings () {
		if (volumeValue == 0) {
			GameCode.musicHandler.muted = true;
		} else {
			GameCode.musicHandler.muted = false;
			GameCode.volume =  -45 + (5 * volumeValue);
		}
		GameCode.getSettings().changeResolution(resoultionNum1, resoultionNum2);
		GameCode.getSettings().setScaleMode(displayModeIndex);
		Roome.setMapHeight(heightValue);
		Roome.setMapWidth(widthValue);
		
	}
	
	public void discardSettings () {
		
		if (displayModeIndex == 3 && GameCode.getSettings().getScaleMode() != 3) {
			this.getMenu().addComponite(resolutionComponite);
		}
		
		if (displayModeIndex != 3 && GameCode.getSettings().getScaleMode() == 3) {
			this.getMenu().removeComponite(resolutionComponite);
		}
		
		
		volumeValue = ((int)GameCode.volume + 45)/5;
		resoultionNum1 = GameCode.getSettings().getResolutionX();
		resoultionNum2 = GameCode.getSettings().getResolutionY();
		displayModeIndex = GameCode.getSettings().getScaleMode();
		widthValue = Roome.getMapWidth();
		heightValue = Roome.getMapHeight();
		
		ArrayList <GameObject> buttons = this.getMenu().getAllObjs();
		
		ArrowButtons volume = (ArrowButtons) buttons.get(0);
		ArrowButtons width = (ArrowButtons) buttons.get(1);
		ArrowButtons height = (ArrowButtons) buttons.get(2);
		ArrowButtons displayMode = (ArrowButtons) buttons.get(3);
		
		volume.setTo(Integer.toString(volumeValue));
		width.setTo(Integer.toString(widthValue));
		height.setTo(Integer.toString(heightValue));
		resolutions.setTo(Integer.toString(resoultionNum1) + " X " + Integer.toString(resoultionNum2));
		displayMode.setIndex(displayModeIndex);
	
	}
}
