package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;

import engine.GameCode;
import engine.ObjectHandler;
import engine.RenderLoop;
import gameObjects.DataSlot;
import gameObjects.Register;
import gameObjects.TitleScreen;
import engine.GameObject;
import map.Roome;
import resources.Hud;

public class Client extends Thread {

	private Thread thread;
	
	private Socket socket;
	private ArrayList<ServerConnection> connections;
	
	private static volatile String ip;
	private static volatile int port;
	
	private static volatile String message;
	private static volatile boolean readyToSend;
	private static volatile boolean connected;
	
	private static volatile boolean close;
	
	private static HashMap<Integer, Register> registerMap;
	private static HashMap<Integer, DataSlot> slotMap;
	
	private String uuid = UUID.randomUUID ().toString ();
	
	public Client (String ip) {
		
		this.ip = ip.split (":")[0];
		this.port = Integer.parseInt (ip.split (":")[1]);
		registerMap = new HashMap<Integer, Register> ();
		slotMap = new HashMap<Integer, DataSlot> ();
		
	}
	
	@Override
	public void run () {
		try {
			//Establish connection
			socket = new Socket (ip, port);
			InputStream inStream = socket.getInputStream ();
			OutputStream outStream = socket.getOutputStream ();
			DataInputStream dataIn = new DataInputStream (inStream);
			DataOutputStream dataOut = new DataOutputStream (outStream);
			System.out.println ("Client connected to " + ip + " on port " + port);
			connected = true;
			
			//Wait to send data/send data
			while (true) {
				//Wait until there's data to send
				while (!readyToSend && !close) {
					
					if (dataIn.available () != 0) {
						String str = dataIn.readUTF ();
						//System.out.println ("Message recieved: " + str);
						
						//Parse message, etc.
						if (str.length () >= 6 && str.substring (0, 6).equals("PLAYER")) {
							TitleScreen.connectSuccess ();
							NetworkHandler.setPlayerNum (str.charAt (7) - '0');
							RenderLoop.wind.setTitle (str);
						}
						
						if (str.length () >= 5 && str.substring (0, 5).equals ("START")) {
							String[] data = str.split (":");
							String room_data = data[1];
							TitleScreen.titleClosed = true;
							Roome.loadMap (room_data);
							GameCode.initGameState ();
						}
						
						if (str.length () >= 4 && str.substring (0,4).equals ("DATA")) {
							long dataParseTime = System.currentTimeMillis ();
							//Get the data
							String[] data = str.split (":");
							
							//Extract the timer
							String timer_data = data[1];
							int timer_amt = Integer.parseInt (timer_data);
							Hud.timeLeft = timer_amt;
							
							//Extract the score
							String score_data = data[2];
							int score_amt = Integer.parseInt (score_data);
							Hud.score = score_amt;
							
							//Extract the bit positions
							String bit_data = data[3];
							String[] bit_coords = bit_data.split (",");
							int bit_x = Integer.parseInt (bit_coords[0]);
							int bit_y = Integer.parseInt (bit_coords[1]);
							GameCode.bit.goX (bit_x);
							GameCode.bit.goY (bit_y);
							//Bit 2
							bit_data = data[4];
							bit_coords = bit_data.split (",");
							bit_x = Integer.parseInt (bit_coords[0]);
							bit_y = Integer.parseInt (bit_coords[1]);
							GameCode.bit2.goX (bit_x);
							GameCode.bit2.goY (bit_y);
							//Bit 3
							bit_data = data[5];
							bit_coords = bit_data.split (",");
							bit_x = Integer.parseInt (bit_coords[0]);
							bit_y = Integer.parseInt (bit_coords[1]);
							GameCode.bit3.goX (bit_x);
							GameCode.bit3.goY (bit_y);
							//Bit 4
							bit_data = data[6];
							bit_coords = bit_data.split (",");
							bit_x = Integer.parseInt (bit_coords[0]);
							bit_y = Integer.parseInt (bit_coords[1]);
							GameCode.bit4.goX (bit_x);
							GameCode.bit4.goY (bit_y);
							
							//Extract the register data
							ArrayList<GameObject> regObjs = ObjectHandler.getObjectsByName ("Register");
							String reg_data = data[7];
							String[] registers = reg_data.split (",");
							for (int i = 0; i < registers.length; i++) {
								Scanner s = new Scanner (registers [i]);
								if (s.hasNext ()) {
									int r_id = s.nextInt ();
									if (registerMap.containsKey (r_id)) {
										registerMap.get (r_id).refreshRegister (registers [i]);
									} else {
										Register reg = new Register (1);
										reg.declare ();
										reg.refreshRegister (registers [i]);
										reg.id = r_id;
										registerMap.put (r_id, reg);
									}
									s.close ();
								}
							}
							
							//Extract the DataSlot data
							ArrayList<GameObject> slotObjs = ObjectHandler.getObjectsByName ("DataSlot");
							String slot_data = data[8];
							String[] slots = slot_data.split (",");
							for (int i = 0; i < slots.length; i++) {
								Scanner s = new Scanner (slots [i]);
								if (s.hasNext ()) {
								int r_id = s.nextInt ();
									if (slotMap.containsKey (r_id)) {
										slotMap.get (r_id).refreshDataSlot (slots [i]);
									} else {
										DataSlot ds = new DataSlot (1);
										ds.declare ();
										ds.refreshDataSlot (slots [i]);
										ds.id = r_id;
										slotMap.put (r_id, ds);
									}
									s.close ();
								}
							}
						}
						
						//System.out.println ("Message recieved: " + str);

					}
					
					try {
						thread.sleep (1);
					} catch (InterruptedException e) {
						//Do nothing, timing is not critical
					}
				}
				
				if (close) {
					break;
				}
				
				//Send message
				dataOut.writeUTF (message);
				readyToSend = false;
				//System.out.println ("Message sent: " + message);
				
			}
			
			//Close connection
			socket.close ();
			System.out.println ("Client closed");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isConnected () {
		return connected;
	}
	
	public void setConnectionInfo (String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public void messageServer (String message) {
		this.message = message;
		this.readyToSend = true;
	}
	
	public void joinServer () {
		this.message = "PING";
		this.readyToSend = true;
	}
	
	public void close () {
		close = true;
	}
	
	public void setThread (Thread thread) {
		this.thread = thread;
	}
}
