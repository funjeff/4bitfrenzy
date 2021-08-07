package titleScreen;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.RenderLoop;
import engine.Sprite;
import engine.SpriteParser;
import map.Roome;
import menu.Menu;
import menu.MenuComponite;
import menu.ObjectComponite;
import menu.TextComponite;
import network.Client;
import network.NetworkHandler;
import network.Server;
import npcs.PopcornMachine;
import npcs.TalkableNPC;
import resources.SoundPlayer;
import resources.Textbox;

public class TitleScreen extends GameObject {
	
	public static Sprite bg = new Sprite ("resources/sprites/title.png");
	public static Sprite infographic = new Sprite ("resources/sprites/game infographic.png");
	
	private String ip;
	
	public static int perkNum = 15;
	
	private boolean typeIp = false;
	
	private static Textbox ipBox;
	private static Textbox infoBox;
	private static volatile int numPlayers = 1;
	
	private Button hostButton;
	private Button joinButton;
	private Button rulesButton;
	private Button perksButton;
	private Button settingsButton;
	
	private static boolean connected = false;
	
	private static Sprite lobbySprite = new Sprite ("resources/sprites/lobby.png");
	
	
	boolean ipMode = false;
	boolean isHost = false;
	boolean failedMode = false;
	boolean connectedMode = false;
	boolean waitMode = false;
	
	public static boolean titleClosed = false;
	
	static Server server;
	static Client client;
	
	public String mapLoadPath = null; //Set this to a filepath before closing the title screen to load a map
	public static String[] initialData = null;
	
	private TitleBit titleBit;
	private TitleRegister titleReg;
	private TitleSlot startGameSlot;
	private TitleSlot joinSlot;
	private TitleSlot perksSlot;
	private TitleSlot settingsSlot;
	private TitleSlot helpSlot;
	
	private PerkStation defaultStation;
	private PerkStation blastStation;
	private PerkStation gripStation;
	private PerkStation navStation;
	private PerkStation dupeStation;
	private PerkStation powerStation;
	private PerkStation dualStation;
	private PerkStation gamblerStation;
	
	private TalkableNPC settingsBot;
	
	private static Scene perkScene;
	
	@Override
	public void onDeclare () {
		
		//Set stuff
		setSprite (bg);
		ip = "";
		
		//Make the buttons
		initMainMenu();
		
		//Make the textboxes
		ipBox = new Textbox ("");
		ipBox.declare ();
		ipBox.changeWidth (128);
		ipBox.changeHeight (128);
		ipBox.setFont ("text (red)");
		ipBox.changeBoxVisability ();
		
		ipBox.setRenderPriority(99);
		
		infoBox = new Textbox ("");
		infoBox.declare (0, 32);
		infoBox.changeWidth (128);
		infoBox.changeHeight (128);
		infoBox.setFont ("text (red)");
		infoBox.changeBoxVisability ();
		
		infoBox.setRenderPriority(99);
		
	}
	
	@Override
	public void frameEvent () {
		
		//Handle typing with the IP
		if (ipMode && !isHost) {
			ArrayList<KeyEvent> events = getKeyEvents ();
			for (int i = 0; i < events.size (); i++) {
				KeyEvent currEvent = events.get (i);
				if (currEvent.getID () == KeyEvent.KEY_TYPED) {
					char c = currEvent.getKeyChar ();
					if ((c >= '0' && c <= '9') || (c == '.') || (c == ':')) {
						ip += c;
					}
				}
				if (currEvent.getID () == KeyEvent.KEY_PRESSED) {
					if (currEvent.getKeyCode () == KeyEvent.VK_BACK_SPACE || currEvent.getKeyCode () == KeyEvent.VK_DELETE) {
						if (ip.length () > 0) {
							ip = ip.substring (0, ip.length () - 1);
						}
					} else if (currEvent.getKeyCode () == KeyEvent.VK_ENTER) {
						if (!failedMode && !connected) {
							try {
								client = new Client (ip);
								client.start ();
								NetworkHandler.setClient (client);
							} catch (Exception e) {
								failedMode = true;
								ipBox.changeText ("CONNECTION FAILED. TRY USING A DIFFERENT IP OR CHECK YOUR FIREWALL SETTINGS.");
								infoBox.changeText ("");
								return;
							}
							//Connected, try to ping host
							waitMode = true;
							ipBox.changeText ("WAITING FOR HOST...");
							infoBox.changeText ("(IF THIS TAKES A LONG TIME, THERE MAY BE A CONNECTION ERROR)");
							client.joinServer ();
							
						} else {
							failedMode = false;
						}
					} else if (currEvent.getKeyCode () == KeyEvent.VK_ESCAPE) {
						System.exit (0);
					}
				}
			}
		}
		
		if (startGameSlot.isSelected ()) {
			
			exitMainMenu ();
			new GameMenu ().declare (0, 0);
			
		}
		
		if (/*joinSlot.isSelected ()*/false) {
			//joinButton.reset ();
			
			//Remove the buttons

		}
		if (/*helpSlot.isSelected ()*/false) {
			exitMainMenu ();
			
			this.setSprite(infographic);
		
		}
		if (getSprite () == infographic && keyPressed (KeyEvent.VK_ESCAPE)) {
			
			setSprite (bg);
			ip = "";
			
			this.initMainMenu();
			//rulesButton.pressed = false;
		}
		if (/*perksSlot.isSelected ()*/false) {
			exitMainMenu ();
			perkMenu menu = new perkMenu (this);
			if (ObjectHandler.getObjectsByName("perkMenu") == null || ObjectHandler.getObjectsByName("perkMenu").size() == 0) {
				menu.declare();
			}
		}
		if (/*settingsSlot.isSelected ()*/false) {
			exitMainMenu ();
			//settingsButton.reset();
			SettingMenu menu = new SettingMenu (this);
			if (ObjectHandler.getObjectsByName("SettingMenu") == null || ObjectHandler.getObjectsByName("SettingMenu").size() == 0) {
				menu.declare();
			}
		}
		if (ipMode && !failedMode && !connectedMode && !waitMode) {
			
			//Change box contents for host
			if (isHost) {
				ipBox.changeText ("CONNECT USING IP " + server.getIp () + " (" + numPlayers + "/4 PLAYERS JOINED)");
				infoBox.changeText ("PRESS ENTER ONCE ALL PLAYERS HAVE JOINED TO START THE GAME");
				if (keyPressed (KeyEvent.VK_ENTER)) {
					System.out.println ("STARTING");
					titleClosed = true;
					ipBox.forget ();
					forget ();
				}
			}
			
			//Change box contents for joining
			if (!isHost) {
				ipBox.changeText ("ENTER THE CONNECTION IP: " + ip);
				infoBox.changeText ("(PRESS ENTER AFTER TYPING THE IP TO JOIN)");
			}
			
		}
		
	}
	
	private void enterMainMenu () {
		
		
		titleBit = new TitleBit ();
		titleReg = new TitleRegister ();
		startGameSlot = new TitleSlot (TitleSlot.titleStartGame);
//		joinSlot = new TitleSlot (TitleSlot.titleJoin);
//		perksSlot = new TitleSlot (TitleSlot.titlePerks);
//		settingsSlot = new TitleSlot (TitleSlot.titleSettings);
//		helpSlot = new TitleSlot (TitleSlot.titleHelp);
		settingsBot = new TalkableNPC (150,200);
		settingsBot.setSprite(new Sprite ("resources/sprites/robot.png"));
		
		settingsBot.setRenderPriority(101);
		
		defaultStation = new PerkStation (0);
		blastStation = new PerkStation (1);
		gripStation = new PerkStation (2);
		navStation = new PerkStation (3);
		dupeStation = new PerkStation (4);
		powerStation = new PerkStation (5);
		dualStation = new PerkStation (6);
		gamblerStation = new PerkStation (7);
		
//		Menu menu = new Menu ();
//		
//		TextComponite t = new TextComponite(30,5,menu);
//		
//		MenuComponite m = new MenuComponite (30, 80,menu);
//		
//		TextComponite t2 = new TextComponite(30,5,menu);
//		
//		PopcornMachine cornUnspread = new PopcornMachine (10,10);
//		
//		cornUnspread.declare();
//		
//		ObjectComponite obj = new ObjectComponite (cornUnspread,menu);
//		
//		t2.setText("LOLZ");
//		
//		t.setText("MY PENUS IS VERY LARGE AND YOURS IS ~Ctext (lime green)~VERY ~HSMALL!");
//		
//		menu.declare(900,250);
//		
//		menu.setRenderPriority(72);
//		
		titleBit.declare (920, 360);
		titleReg.declare (487, 161);
		startGameSlot.declare (331, 139);
//		joinSlot.declare (1150, 180);
//		helpSlot.declare (1150, 322);
//		perksSlot.declare (1150, 460);
//		settingsSlot.declare (1150, 600);
		settingsBot.declare(150,200);
		
		defaultStation.declare (676, 168);
		blastStation.declare (676, 316);
		gripStation.declare (676, 464);
		navStation.declare (676, 612);
		dupeStation.declare (783, 168);
		powerStation.declare (783, 316);
		dualStation.declare (783, 464);
		gamblerStation.declare (783, 612);
		
	}
	
	private void exitMainMenu () {
		
		titleBit.forget ();
		titleReg.forget ();
		startGameSlot.forget ();
//		joinSlot.forget ();
//		perksSlot.forget ();
//		settingsSlot.forget ();
//		helpSlot.forget ();
		settingsBot.forget();
		
		/*hostButton.forget();
		joinButton.forget();
		rulesButton.forget();
		perksButton.forget();
		settingsButton.forget();*/
		
		defaultStation.forget ();
		blastStation.forget ();
		gripStation.forget ();
		navStation.forget ();
		dupeStation.forget ();
		powerStation.forget ();
		dualStation.forget ();
		gamblerStation.forget ();
		
	}
	
	private void initMainMenu () {
		
		enterMainMenu ();
		
		/*hostButton = new Button (new Sprite ("resources/sprites/host red.png"));
		joinButton = new Button (new Sprite ("resources/sprites/join.png"));
		rulesButton = new Button (new Sprite ("resources/sprites/story red.png"));
		perksButton = new Button (new Sprite ("resources/sprites/perks red.png"));
		settingsButton = new Button (new Sprite ("resources/sprites/setup red.png"));
		
		
		hostButton.setGreen(new Sprite ("resources/sprites/host.png"));
		joinButton.setGreen(new Sprite ("resources/sprites/join green.png"));
		rulesButton.setGreen(new Sprite ("resources/sprites/story green.png"));
		perksButton.setGreen(new Sprite ("resources/sprites/perks green.png"));
		settingsButton.setGreen(new Sprite ("resources/sprites/setup green.png"));
		
		
		hostButton.declare (700, 32);
		joinButton.declare (680, 167);
		rulesButton.declare(680, 322);
		perksButton.declare(720, 480);
		settingsButton.declare(720, 600);
		
		hostButton.setRenderPriority(69);
		joinButton.setRenderPriority(69);
		rulesButton.setRenderPriority(69);
		perksButton.setRenderPriority(69);
		settingsButton.setRenderPriority(69);*/
	
	}
	
	public static Scene playScene (String path, int x, int y) {
		perkScene = new Scene (path);
		perkScene.declare (x, y);
		return perkScene;
	}
	
	public static boolean scenePlaying () {
		return perkScene != null && perkScene.declared ();
	}
	
	@Override
	public void draw () {
		super.draw ();
		
		if (this.getSprite().equals(lobbySprite)) {
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < 2; j++) {
					Textbox perk = null;
					
					ArrayList <String> parserQuantitys = new ArrayList<String> ();
					parserQuantitys.add("grid 168 128");
					
					Sprite bits = null;
			
					switch (GameCode.perks[(i*2) + j]) {
					case 0:
						perk = new Textbox ("BLAST PROCESSING");
						perk.setFont("text (blue)");
						bits = new Sprite ("resources/sprites/bits blue big.png", new SpriteParser (parserQuantitys));
						break;
					case 1:
						perk = new Textbox ("GRIP STRENTH");
						perk.setFont("text (lime green)");
						bits = new Sprite ("resources/sprites/bits green big.png", new SpriteParser (parserQuantitys));
						break;
					case 2:
						perk = new Textbox ("NAVIGATION BIT");
						perk.setFont("text (yellow)");
						bits = new Sprite ("resources/sprites/bits yellow big.png", new SpriteParser (parserQuantitys));
						break;
					case 3:
						perk = new Textbox ("POWERHOUSE");
						perk.setFont("text (red)");
						bits = new Sprite ("resources/sprites/bits red big.png", new SpriteParser (parserQuantitys));
						break;
					case 4:
						perk = new Textbox ("DUPLICATION BIT");
						perk.setFont("text (purple)");
						bits = new Sprite ("resources/sprites/bits purple big.png", new SpriteParser (parserQuantitys));
						break;
					case 5:
						perk = new Textbox ("DUEL CORE");
						perk.setFont("text (red)");
						bits = new Sprite ("resources/sprites/bits duel 1 big.png", new SpriteParser (parserQuantitys));
						break;
					case 15:
						perk = new Textbox ("NO PERK");
						bits = new Sprite ("resources/sprites/bits big.png", new SpriteParser (parserQuantitys));
						switch ((i*2) + j) {
						case 0:
							perk.setFont("text (lime green)");
							break;
						case 1:
							perk.setFont("text (red)");
							break;
						case 2: 
							perk.setFont("text (blue)");
							break;
						case 3: 
							perk.setFont("text (purple)");
							break;
						}
						break;
					}
					if (perk != null) {
						
						String str = perk.getText();
						
						int middleNum = str.length()/2;
						
						int middlePos = (middleNum * 16) + 8;
						
						int middlePosTarget = bits.getWidth()/2;
						
						int displace = middlePosTarget - middlePos;
						
						
						perk.changeBoxVisability();
						perk.setRenderPriority(72);
						perk.setX((i * 500) + 165 + displace);
						perk.setY((j * 300) + 75);
						perk.draw();
						bits.draw((i * 500) + 165, (j * 300) + 130, (i*2) + j);
					}
				}
			}
		}
	}
	
	public void enterHostMode () {
		
		isHost = true;
		
		GameCode.setPerk(perkNum, 0);
		NetworkHandler.setHost (true);
		
		this.setSprite(new Sprite ("resources/sprites/now loading.png"));
		
		RenderLoop.pause();
		
		this.draw();
		RenderLoop.wind.refresh();
		
		//If there is a map to load
		if (mapLoadPath != null) {
			//Load the map
			File f = new File (mapLoadPath);
			try {
				Scanner s = new Scanner (f);
				//Load the room data
				String mapStr = s.nextLine ();
				String roomsStr = mapStr.split (":")[1];
				Roome.loadMap (roomsStr);
				//Load the object data
				String dataStr = s.nextLine ();
				initialData = dataStr.split (":");
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			//Arcade mode, generate a map
			Roome.generateMap();
		}
		
		RenderLoop.unPause();
		this.setSprite(lobbySprite);
		
		enterIpMode ();

	}
	
	public void enterJoinMode () {

		enterIpMode ();
		
	}
	
	public void enterIpMode () {
		
		
		//Setup the server if hosting
		if (isHost) {
			server = new Server ();
			server.start ();
			NetworkHandler.setServer (server);
		}
		
		ipMode = true;
		System.out.println (isHost);
	}
	
	public static void connectSuccess () {
		ipBox.changeText("CONNECTED: WAITING FOR HOST TO START GAME");
		TitleScreen screen = GameCode.getTitleScreen();
		screen.setSprite(lobbySprite);
		connected = true;
		
	}
	
	public static void playerJoin () {
		numPlayers++;
	}
	
	public static class Button extends GameObject {
		
		private boolean pressed = false;
		private Sprite green;
		private Sprite red;
		
	
		boolean mouseInside;
		
		public boolean hidden = false;
		
		public boolean isMouseInside() {
			return mouseInside;
		}
		public void setMouseInside(boolean mouseInside) {
			this.mouseInside = mouseInside;
		}
		public Button (Sprite sprite) {
			pressed = false;
			red = sprite;
			setSprite (sprite);
		}
		public void setGreen (Sprite green) {
			this.green = green;
		}
		@Override
		public void frameEvent () {
			if (this.isVisable()) {
				int mouseX = getCursorX ();
				int mouseY = getCursorY ();
				if (mouseX > getX () && mouseY > getY () && mouseX < getX () + getSprite ().getWidth () && mouseY < getY () + getSprite ().getHeight ()) {
					if (green != null) {
						this.setSprite(green);
					}
					mouseInside = true;
					if (this.mouseButtonReleased (0)) {
						pressed = true;
					}
				} else {
					mouseInside = false;
					this.setSprite(red);
				}
			}
		}
		public boolean isPressed () {
			return pressed;
		}
		
		public void reset () {
			pressed = false;
		}
		public boolean isHidden() {
			return hidden;
		}
		public void setHidden(boolean hidden) {
			this.hidden = hidden;
		}
		@Override
		public void setSprite (Sprite src) {
			super.setSprite(src);
			if (green == null) {
				red = src;
			} else if (!green.equals(src)) {
				red = src;
			}
		}
		
	}
	
	public static class TextButton extends Button {

		String text;
		
		public TextButton() {
			super(new Sprite ("resources/sprites/text button border.png"));
			
		}
		
		public void setText (String text) {
			this.text = text;
		}
		
		@Override
		public void draw () {
			if (this.isVisable()) {
				super.draw();
				Graphics g = RenderLoop.wind.getBufferGraphics();
				g.drawString(text, (int)this.getX() + 1, (int)this.getY() + 14);
			}
		}
		
		
	}
	
	public static class ArrowButtons extends GameObject {
		Button leftButton;
		Button rightButton;
		
		boolean leftSelectable = false;
		boolean rightSelectable = false;
		
		boolean toggled = false;
		
		String [] stringList;
		int selectedIndex = 0;
		
		public ArrowButtons (String [] strings) {
			stringList = strings;
			leftButton = new Button (new Sprite ("resources/sprites/left arrow green.png"));
			rightButton = new Button (new Sprite ("resources/sprites/right arrow green.png"));
			
		}
		
		public String getSelectedString () {
			return stringList[selectedIndex];
		}
		
		public boolean wasToggled () {
			boolean wasToggled = toggled;
			toggled = false;
			return wasToggled;
		}
		
		public void setIndex (int index) {
			selectedIndex = index;
		}
		public int getIndex () {
			return selectedIndex;
		}
		@Override
		public void frameEvent () {
			if (selectedIndex == stringList.length -1) {
				rightButton.setSprite(new Sprite ("resources/sprites/right arrow red.png"));
				rightSelectable = false;
			} else {
				rightButton.setSprite(new Sprite ("resources/sprites/right arrow green.png"));
				rightSelectable = true;
			}
			if (selectedIndex == 0) {
				leftButton.setSprite(new Sprite ("resources/sprites/left arrow red.png"));
				leftSelectable = false;
			} else {
				leftButton.setSprite(new Sprite ("resources/sprites/left arrow green.png"));
				leftSelectable = true;
			}
			
			leftButton.frameEvent();
			rightButton.frameEvent();
			
			if (leftButton.isPressed() && leftSelectable) {
				selectedIndex = selectedIndex - 1;
				leftButton.reset();
				toggled = true;
			}
			if (rightButton.isPressed() && rightSelectable) {
				selectedIndex = selectedIndex + 1;
				rightButton.reset();
				toggled = true;
			}
		}
		@Override
		public void onDeclare () {
			leftButton.setX(this.getX());
			leftButton.setY(this.getY());
			rightButton.setY(this.getY());
		
			
			Graphics g = RenderLoop.wind.getBufferGraphics();
			FontMetrics fm = g.getFontMetrics();
			
			g.setColor(new Color (0x32a852));
			g.drawString(getSelectedString(), (int) (this.getX() + leftButton.getSprite().getWidth()) + 10, (int)this.getY() + 20);
			
			int width = leftButton.getSprite().getWidth() + 20;
			
			int cpy = width;
			
			for (int j = 0; j < stringList.length; j++) {
				int tempWidth = cpy;
				for (int i = 0; i < stringList[j].length(); i++) {
					tempWidth = tempWidth + fm.charWidth(stringList[j].charAt(i));
				}
				if (tempWidth > width) {
					width = tempWidth;
				}
			}
			rightButton.setX(this.getX() + width);
			
		
		}
		@Override
		public void draw () {
			
			if (this.isVisable()) {
				leftButton.draw();
				rightButton.draw();
				Graphics g = RenderLoop.wind.getBufferGraphics();
	
				g.setColor(new Color (0x32a852));
				g.drawString(getSelectedString(), (int) (this.getX() + leftButton.getSprite().getWidth()) + 10, (int)this.getY() + 20);
				
			}
		}
		
	}
	public class SettingMenu extends GameObject {
		
		String [] volumeArray = {"0","1","2","3","4","5","6","7","8","9","10"};
		ArrowButtons volume = new ArrowButtons (volumeArray);
		
		String [] widthArray = {"2","3","4","5","6","7","8","9","10"};
		ArrowButtons width = new ArrowButtons (widthArray);
		
		String [] heightArray = {"2","3","4","5","6","7","8","9","10"};
		ArrowButtons height = new ArrowButtons (heightArray);
		
		String [] resoultionArray = {"1280 x 720","1366 x 768","1600 x 900","1920 x 1080","1920 x 1200"};
		ArrowButtons resolutions = new ArrowButtons (resoultionArray);
		
		String [] displayModeArray = {"Horizontal Border","Full Border","Strech","Full"};
		ArrowButtons displayMode = new ArrowButtons (displayModeArray);
		
		
		Button controllsButton = new Button (new Sprite ("resources/sprites/config button.png"));
		
		TitleScreen screen;
		
		Sprite resolutionsText = new Sprite ("resources/sprites/resolutions.png");
		
		boolean showResText = true;
		
		Button backButton;
		
		public SettingMenu (TitleScreen screen) {
			this.screen = screen;
			
			this.setSprite(new Sprite ("resources/sprites/settings menu.png"));
			
			this.setRenderPriority(70);
			
			volume.setRenderPriority(71);
			resolutions.setRenderPriority(71);
			width.setRenderPriority(71);
			height.setRenderPriority(71);
			controllsButton.setRenderPriority(71);
			displayMode.setRenderPriority(71);
			
			backButton = new Button (new Sprite ("resources/sprites/back.png"));
			
			width.setIndex(8);
			height.setIndex(8);
			volume.setIndex(10);
			
			volume.declare(140,260);
			resolutions.declare(180,175);
			width.declare(140,460);
			height.declare(140,520);
			controllsButton.declare(150, 345);
			backButton.declare(300, 512);
			displayMode.declare(550, 165);
			
			
			backButton.setRenderPriority(71);
			
			
		}
		
		@Override
		public void frameEvent () {
			if (width.wasToggled()) {
				Roome.setMapWidth(Integer.parseInt(width.getSelectedString()));
			}
			if (width.wasToggled()) {
				Roome.setMapWidth(Integer.parseInt(width.getSelectedString()));
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
					resolutions.setVisability(false);
					showResText = false;
				} else {
					showResText = true;
					resolutions.setVisability(true);
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
			
			if (controllsButton.isPressed()) {
				ControlMenu menu = new ControlMenu (screen);
				
				menu.declare();
				
				controllsButton.reset();
				this.forgetStuff();
				
				
			}
		
			if (backButton.isPressed()) {
				screen.initMainMenu();
				forgetStuff();
			}
			
		}
		private void forgetStuff () {

			volume.forget();
			resolutions.forget();
			controllsButton.forget();
			width.forget();
			height.forget();
			displayMode.forget();
			
			
			backButton.forget();
			
			this.forget();
		}
		
		@Override
		public void draw () {			
			super.draw();
			if (showResText) {
				resolutionsText.draw(15,160);
			}
		}
		
	}
	public class ControlMenu extends GameObject {
		
		
		TextButton [] buttons = new TextButton[13];
		
		Button backButton;
		
		TitleScreen screen;
		
		int selectedButton = -1;
		
		Button defaultButton;
		
		public ControlMenu (TitleScreen screen) {
		
			this.screen = screen;
			this.setSprite(new Sprite ("resources/sprites/controls menu.png"));
			this.setRenderPriority(72);
			
	
			
		}
		
		@Override
		public void onDeclare () {
			
			for (int i = 0; i < buttons.length; i++) {
				buttons[i] = new TextButton ();
				buttons[i].setText(KeyEvent.getKeyText(GameCode.getSettings().getControls()[i]));
				buttons[i].setRenderPriority(73);
			}
			
			buttons[0].declare(120, 125);
			buttons[1].declare(180, 170);
			buttons[2].declare(140, 210);
			buttons[3].declare(170, 260);
			buttons[4].declare(160, 340);
			buttons[5].declare(240, 385);
			buttons[6].declare(400, 430);
			buttons[7].declare(480, 520);
			buttons[8].declare(690, 135);
			buttons[9].declare(750, 180);
			buttons[10].declare(710, 220);
			buttons[11].declare(730, 265);
			buttons[12].declare(940, 350);
			
			backButton = new Button (new Sprite ("resources/sprites/back.png"));
			
			backButton.declare(550, 452);
			
			backButton.setRenderPriority(73);
			
			
			defaultButton = new Button (new Sprite ("resources/sprites/default button.png"));
			
			defaultButton.declare(550, 402);
			
			defaultButton.setRenderPriority(73);
			
		}
		
		
		@Override
		public void frameEvent () {
			if (backButton.isPressed()) {
				this.forgetStuff();
				this.forget();
				
				SettingMenu menu = new SettingMenu (screen);
				menu.declare();
			}
			
			for (int i = 0; i < buttons.length; i++) {
				if (buttons[i].isPressed()) {
					if (i != selectedButton) {
						if (selectedButton != -1) {
							buttons[selectedButton].reset();
						}
						selectedButton = i;
					} else {
						if (getKeysDown().length != 0) {
							int [] oldControls = GameCode.getSettings().getControls();
							oldControls[i] = getKeysDown()[0];
							
							
							
							GameCode.getSettings().setControls(oldControls);
							GameCode.getSettings().updateControlFile();
							
							buttons[i].setText(KeyEvent.getKeyText(GameCode.getSettings().getControls()[i]));
							selectedButton = -1;
						}
					}
				}
			}
			if (defaultButton.isPressed()) {
				File oldControls = new File ("resources/saves/controls.txt");
				oldControls.delete();
				GameCode.initControls();
				
				
				for (int i = 0; i < buttons.length; i++) {
					buttons[i].setText(KeyEvent.getKeyText(GameCode.getSettings().getControls()[i]));
				}
			}
		}
		
		
		private void forgetStuff () {

			for (int i = 0; i < buttons.length; i++) {
				buttons[i].forget();
			}
			
			backButton.forget();
			
			defaultButton.forget();
			
			this.forget();
		}
	}
	public class perkMenu extends GameObject {
		//Make the buttons
			
			Button blastButton;
			Button haulerButton;
			Button naviationButton;
			Button duplicatieButton;
			Button dualButton;
			Button powerButton;
			
			Button backButton;
			
			TitleScreen screen;
			
			Sprite sideImage;
			
			Sprite check;
			
			@Override
			public void onDeclare () {
				blastButton = new Button (new Sprite ("resources/sprites/blast processsing  red.png"));
				haulerButton = new Button (new Sprite ("resources/sprites/grip strength.png"));
				naviationButton = new Button (new Sprite ("resources/sprites/navigation bit green.png"));
				duplicatieButton = new Button (new Sprite ("resources/sprites/duplication red.png"));
				powerButton = new Button (new Sprite ("resources/sprites/power house red.png"));
				dualButton = new Button (new Sprite ("resources/sprites/duel core red.png"));
				
				backButton = new Button (new Sprite ("resources/sprites/back.png"));
				
				sideImage = new Sprite ("resources/sprites/blast processsing explanation.png");
				
				blastButton.declare (100, 32);
				haulerButton.declare (470, 32);
				naviationButton.declare(100, 212);
				duplicatieButton.declare(470, 212);
				powerButton.declare(100, 412);
				dualButton.declare(470, 412);
				
				backButton.declare(300, 512);
				
				check = new Sprite ("resources/sprites/check.png");
				
				blastButton.setRenderPriority(71);
				haulerButton.setRenderPriority(71);
				naviationButton.setRenderPriority(71);
				duplicatieButton.setRenderPriority(71);
				powerButton.setRenderPriority(71);
				dualButton.setRenderPriority(71);
				
				backButton.setRenderPriority(71);
	}
			
				@Override
				public void draw () {
					super.draw();
					if (sideImage != null) {
						sideImage.draw(790,0);
					}
						switch (perkNum) {
							case 0:
								check.draw(170, 62);
								break;
							
							case 1:
								check.draw(540, 62);
								break;
							
							case 2:
								check.draw(190, 242);
								break;
								
							case 3:
								check.draw(190, 442);
								break;
								
							case 4:
								check.draw(540, 242);
								break;
							
							case 5:
								check.draw(540, 442);
								break;
	
						}
				}
				public perkMenu (TitleScreen screen) {
					this.screen = screen;
					this.setSprite (new Sprite ("resources/sprites/perk Menu.png"));
					this.setRenderPriority(70);
				}
				
				@Override
				public void frameEvent () {
					if (blastButton.isPressed ()) {	
						blastButton.pressed = false;
						perkNum = 0;
					}
					
					if (haulerButton.isPressed ()) {
						haulerButton.pressed = false;
						perkNum = 1;
					}
					if (naviationButton.isPressed ()) {
						naviationButton.pressed = false;
						perkNum = 2;
					}
					
					if (duplicatieButton.isPressed ()) {
						duplicatieButton.pressed = false;
						perkNum = 4;
					}
					if (powerButton.isPressed ()) {
						powerButton.pressed = false;
						perkNum = 3;
						
					}
					if (dualButton.isPressed ()) {
						dualButton.pressed = false;
						perkNum = 5;
					}
					
					if (backButton.isPressed()) {
						screen.initMainMenu();
						//screen.perksButton.pressed = false;
						forgetStuff();
					}
					
					if (blastButton.mouseInside) {
						sideImage = new Sprite ("resources/sprites/blast processsing explanation.png");
					}
					if (haulerButton.mouseInside) {
						sideImage = new Sprite ("resources/sprites/grip strength explination.png");
					}
					if (naviationButton.mouseInside) {
						sideImage = new Sprite ("resources/sprites/navigation bit explanation.png");
					}
					if (duplicatieButton.mouseInside) {
						sideImage = new Sprite ("resources/sprites/duplication explination.png");
					}
					if (powerButton.mouseInside) {
						sideImage = new Sprite ("resources/sprites/powerhouse explination.png");
					}
					if (dualButton.mouseInside) {
						sideImage = new Sprite ("resources/sprites/duel core explination.png");
					}
					
				}	
				private void forgetStuff () {

					blastButton.forget();
					haulerButton.forget();
					naviationButton.forget();
					duplicatieButton.forget();
					powerButton.forget();
					dualButton.forget();
					
					backButton.forget();
					
					this.forget();
				}
		}
	
	public class GameMenu extends GameObject {
		
		private Button gmHostButton;
		private Button gmJoinButton;
		
		public Sprite joinDescSprite = new Sprite ("resources/sprites/join_description.png");
		public Sprite hostDescSprite = new Sprite ("resources/sprites/host_description.png");
		
		@Override
		public void onDeclare () {
			
			//Host button coords: 146 257
			//Join button coords: 697 252
			//Host desc coords: 58 355
			//Join desc corods: 586 354
			
			//Render this above the title screen
			this.setSprite (new Sprite ("resources/sprites/game_menu.png"));
			this.setRenderPriority (70);
			
			//Initialize the buttons
			gmHostButton = new Button (new Sprite ("resources/sprites/host red.png"));
			gmJoinButton = new Button (new Sprite ("resources/sprites/join.png"));
			gmHostButton.declare (146, 257);
			gmJoinButton.declare (697, 252);
			gmHostButton.setGreen (new Sprite ("resources/sprites/host.png"));
			gmJoinButton.setGreen (new Sprite ("resources/sprites/join green.png"));
			gmHostButton.setRenderPriority (71);
			gmJoinButton.setRenderPriority (71);
			
		}
		
		@Override
		public void frameEvent () {
			if (gmHostButton.isPressed ()) {
				forgetStuff ();
				enterHostMode ();
				gmHostButton.reset ();
			}
			if (this.gmJoinButton.isPressed ()) {
				forgetStuff ();
				enterJoinMode ();
				gmJoinButton.reset ();
			}
		}
		
		@Override
		public void draw () {
			
			super.drawAbsolute ();
			
			if (gmHostButton != null && gmHostButton.isMouseInside ()) {
				hostDescSprite.draw (58, 355);
			}
			if (gmJoinButton != null && gmJoinButton.isMouseInside ()) {
				joinDescSprite.draw (586, 354);
			}
			
		}
		
		public void forgetStuff () { 
			gmHostButton.forget ();
			gmJoinButton.forget ();
			forget ();
		}
		
	}
	
		public static int getNumberOfPlayers () {
			return numPlayers;
		}
}
