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
	
	
	public SettingsTxt(double x, double y) {
		super(x, y);
		resolutionComponite = new CompositeComponite (new TextComponite (this.getMenu(), "~S8~  ~Cnormal~RESOLUTION:       "), new ObjectComponite (resolutions, this.getMenu()), this.getMenu());

		
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
		
		TextComponite t = new TextComponite(menu,"~S8~  ~Cnormal~GAME VOLUME:     ");
		
		MenuComponite m = new MenuComponite (400, 10,menu);
		TextComponite t2 = new TextComponite(menu,"~S8~  ~Cnormal~MAP WIDTH:       ");
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
		this.setSprite(new Sprite ("resources/sprites/notepad.png"));
		this.setHitboxAttributes(this.getSprite().getWidth(), this.getSprite().getHeight());
	}
	
	@Override
	public void frameEvent () {
		super.frameEvent();
		ArrayList <GameObject> buttons = this.getMenu().getAllObjs();
		
		
		ArrowButtons volume = (ArrowButtons) buttons.get(0);
		ArrowButtons width = (ArrowButtons) buttons.get(1);
		ArrowButtons height = (ArrowButtons) buttons.get(2);
		ArrowButtons displayMode = (ArrowButtons) buttons.get(3);
		
		
		if (width.wasToggled()) {
			Roome.setMapWidth(Integer.parseInt(width.getSelectedString()));
		}
		if (height.wasToggled()) {
			Roome.setMapHeight(Integer.parseInt(height.getSelectedString()));
		}
		if (volume.wasToggled()) {
			if (Integer.parseInt(volume.getSelectedString()) == 0) {
				GameCode.musicHandler.muted = true;
			} else {
				GameCode.musicHandler.muted = false;
				GameCode.volume =  -45 + (5 * Integer.parseInt(volume.getSelectedString()));
				GameCode.musicHandler.playSoundEffect(GameCode.volume, "resources/sounds/effects/test sound.wav");
			}
		}
		
		if (displayMode.wasToggled()) {
			GameCode.getSettings().setScaleMode(displayMode.getIndex());
			
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
			
			GameCode.getSettings().setResolution(Integer.parseInt(num1), Integer.parseInt(num2));
		}
		
	}

}
