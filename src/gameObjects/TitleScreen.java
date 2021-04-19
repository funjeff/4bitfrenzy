package gameObjects;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.RenderLoop;
import engine.Sprite;
import map.Roome;
import network.Client;
import network.NetworkHandler;
import network.Server;
import resources.Textbox;

public class TitleScreen extends GameObject {
	
	public Sprite bg = new Sprite ("resources/sprites/title.png");
	
	private String ip;
	
	public static int perkNum = 15;
	
	private boolean typeIp = false;
	
	private static Textbox ipBox;
	private static volatile int numPlayers = 1;
	
	private Button hostButton;
	private Button joinButton;
	private Button rulesButton;
	private Button perksButton;
	
	
	boolean ipMode = false;
	boolean isHost = false;
	boolean failedMode = false;
	boolean connectedMode = false;
	boolean waitMode = false;
	
	public static boolean titleClosed = false;
	
	static Server server;
	static Client client;
	
	@Override
	public void onDeclare () {
		
		//Set stuff
		setSprite (bg);
		ip = "";
		
		//Make the buttons
		hostButton = new Button (new Sprite ("resources/sprites/host red.png"));
		joinButton = new Button (new Sprite ("resources/sprites/join.png"));
		rulesButton = new Button (new Sprite ("resources/sprites/story red.png"));
		perksButton = new Button (new Sprite ("resources/sprites/perks red.png"));
		
		hostButton.setGreen(new Sprite ("resources/sprites/host.png"));
		joinButton.setGreen(new Sprite ("resources/sprites/join green.png"));
		rulesButton.setGreen(new Sprite ("resources/sprites/story green.png"));
		perksButton.setGreen(new Sprite ("resources/sprites/perks green.png"));
		
		hostButton.declare (700, 32);
		joinButton.declare (640, 207);
		rulesButton.declare(680, 382);
		perksButton.declare(720, 550);
		
		
		hostButton.setRenderPriority(69);
		joinButton.setRenderPriority(69);
		rulesButton.setRenderPriority(69);
		perksButton.setRenderPriority(69);
		
		//Make the textbox
		ipBox = new Textbox ("");
		ipBox.declare ();
		ipBox.changeWidth (128);
		ipBox.changeHeight (128);
		ipBox.changeBoxVisability ();
		
		ipBox.setRenderPriority(99);
		
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
						if (!failedMode) {
							try {
								client = new Client (ip);
								client.start ();
								NetworkHandler.setClient (client);
							} catch (Exception e) {
								failedMode = true;
								ipBox.changeText ("CONNECTION FAILED. TRY USING A DIFFERENT IP OR CHECK YOUR FIREWALL SETTINGS.");
								return;
							}
							//Connected, try to ping host
							waitMode = true;
							ipBox.changeText ("WAITING FOR HOST...");
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
		
		if (hostButton.isPressed ()) {
			hostButton.reset ();
			
			//Remove the buttons
			hostButton.forget ();
			joinButton.forget ();
			rulesButton.forget();
			perksButton.forget();
			
			isHost = true;
			this.setSprite(new Sprite ("resources/sprites/now loading.png"));
			this.draw();
			RenderLoop.pause();
			RenderLoop.wind.refresh();
			Roome.generateMap();
			RenderLoop.unPause();
			this.setSprite(new Sprite ("resources/sprites/lobby.png"));
			
			NetworkHandler.setHost (true);
			enterIpMode ();
			
			
		}
		
		if (joinButton.isPressed ()) {
			joinButton.reset ();
			
			//Remove the buttons
			hostButton.forget ();
			joinButton.forget ();
			rulesButton.forget();
			perksButton.forget();
			
			enterIpMode ();
		}
		if (rulesButton.isPressed()) {
			hostButton.forget();
			joinButton.forget();
			rulesButton.forget();
			perksButton.forget();
			this.setSprite(new Sprite ("resources/sprites/game infographic.png"));
			if (!getKeyEvents().isEmpty()) {
				
				setSprite (bg);
				ip = "";
				
				this.makeButtons();
				rulesButton.pressed = false;
			}
			
		}
		if (perksButton.isPressed()) {
			hostButton.forget();
			joinButton.forget();
			rulesButton.forget();
			perksButton.forget();
			perkMenu menu = new perkMenu (this);
			if (ObjectHandler.getObjectsByName("perkMenu") == null || ObjectHandler.getObjectsByName("perkMenu").size() == 0) {
				menu.declare();
			}
			
		}
		if (ipMode && !failedMode && !connectedMode && !waitMode) {
			
			//Change box contents for host
			if (isHost) {
				ipBox.changeText ("CONNECT USING IP " + server.getIp () + " (" + numPlayers + "/4 PLAYERS JOINED)");
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
			}
			
		}
		
	}
	
	private void makeButtons () {
		//Make the buttons
		hostButton = new Button (new Sprite ("resources/sprites/host red.png"));
		joinButton = new Button (new Sprite ("resources/sprites/join.png"));
		rulesButton = new Button (new Sprite ("resources/sprites/story red.png"));
		perksButton = new Button (new Sprite ("resources/sprites/perks red.png"));
		
		hostButton.setGreen(new Sprite ("resources/sprites/host.png"));
		joinButton.setGreen(new Sprite ("resources/sprites/join green.png"));
		rulesButton.setGreen(new Sprite ("resources/sprites/story green.png"));
		perksButton.setGreen(new Sprite ("resources/sprites/perks green.png"));
		
		hostButton.declare (700, 32);
		joinButton.declare (640, 207);
		rulesButton.declare(680, 382);
		perksButton.declare(720, 550);
		
		
		hostButton.setRenderPriority(69);
		joinButton.setRenderPriority(69);
		rulesButton.setRenderPriority(69);
		perksButton.setRenderPriority(69);
	}
	
	@Override
	public void draw () {
		super.draw ();
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
//		TitleScreen screen = (TitleScreen) ObjectHandler.getObjectsByName("TitleScreen").get(0);
//		screen.setSprite(new Sprite ("resources/sprites/lobby.png"));
//		
		
		
	}
	
	public static void playerJoin () {
		numPlayers++;
	}
	
	public static class Button extends GameObject {
		
		private boolean pressed = false;
		private Sprite green;
		private Sprite red;
		
		boolean mouseInside;
		
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
		
		public boolean isPressed () {
			return pressed;
		}
		
		public void reset () {
			pressed = false;
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
						screen.makeButtons();
						screen.perksButton.pressed = false;
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
	}
