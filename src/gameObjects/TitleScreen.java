package gameObjects;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import engine.GameObject;
import engine.Sprite;
import network.Client;
import network.NetworkHandler;
import network.Server;
import resources.Textbox;

public class TitleScreen extends GameObject {
	
	public Sprite bg = new Sprite ("resources/sprites/title_screen.png");
	
	private String ip;
	
	private boolean typeIp = false;
	
	private static Textbox ipBox;
	private static volatile int numPlayers = 1;
	
	private Button hostButton;
	private Button joinButton;
	
	boolean ipMode = false;
	boolean isHost = false;
	boolean failedMode = false;
	boolean connectedMode = false;
	boolean waitMode = false;
	
	public boolean titleClosed = false;
	
	static Server server;
	static Client client;
	
	@Override
	public void onDeclare () {
		
		//Set stuff
		setSprite (bg);
		ip = "127.0.0.1:8080";
		
		//Make the buttons
		hostButton = new Button (new Sprite ("resources/sprites/host_game_button.png"));
		joinButton = new Button (new Sprite ("resources/sprites/join_game_button.png"));
		hostButton.declare (32, 32);
		joinButton.declare (128, 128);
		
		hostButton.setRenderPriority(69);
		joinButton.setRenderPriority(69);
		
		//Make the textbox
		ipBox = new Textbox ("HELLO");
		ipBox.declare ();
		ipBox.changeWidth (128);
		ipBox.changeHeight (128);
		ipBox.changeText ("HIA");
		ipBox.changeBoxVisability ();
		
		ipBox.setRenderPriority(69);
		
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
			isHost = true;
			NetworkHandler.setHost (true);
			enterIpMode ();
			
		}
		
		if (joinButton.isPressed ()) {
			joinButton.reset ();
			enterIpMode ();
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
	
	@Override
	public void draw () {
		super.draw ();
	}
	
	public void enterIpMode () {
		
		//Remove the buttons
		hostButton.forget ();
		joinButton.forget ();
		
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
		ipBox.changeText ("CONNECTION SUCCESSFUL. WAITING FOR HOST...");
	}
	
	public static void playerJoin () {
		numPlayers++;
	}
	
	public static class Button extends GameObject {
		
		private boolean pressed = false;
		
		public Button (Sprite sprite) {
			pressed = false;
			setSprite (sprite);
		}
		
		@Override
		public void frameEvent () {
			int mouseX = getCursorX ();
			int mouseY = getCursorY ();
			if (mouseX > getX () && mouseY > getY () && mouseX < getX () + getSprite ().getWidth () && mouseY < getY () + getSprite ().getHeight ()) {
				if (this.mouseButtonReleased (0)) {
					pressed = true;
				}
			}
		}
		
		public boolean isPressed () {
			return pressed;
		}
		
		public void reset () {
			pressed = false;
		}
		
	}
	
}
