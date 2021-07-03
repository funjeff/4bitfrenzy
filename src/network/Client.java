package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import engine.GameCode;
import engine.ObjectHandler;
import engine.RenderLoop;
import engine.Sprite;
import gameObjects.DataSlot;
import gameObjects.Register;
import gameObjects.TitleScreen;
import items.Item;
import engine.GameObject;
import map.Roome;
import players.Bit;
import resources.Hud;
import resources.SoundPlayer;

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
	
	private static HashMap<Integer, Register> registerMap = new HashMap<Integer, Register> ();;
	private static HashMap<Integer, DataSlot> slotMap = new HashMap<Integer, DataSlot> ();
	private static HashMap<Integer, Item> itemMap = new HashMap<Integer, Item> ();
	int datas = 0;
	
	private String uuid = UUID.randomUUID ().toString ();
	
	public SoundPlayer clientPlayer = new SoundPlayer ();
	
	public Client (String ip) {
		
		this.ip = ip.split (":")[0];
		this.port = Integer.parseInt (ip.split (":")[1]);
		
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
						
						if (str.substring (0, 5).equals ("PERKS")) {
							
							
							String [] perks = str.substring(6).split(":");
							
							for (int i = 0; i < perks.length;i++) {
								GameCode.setPerk(Integer.parseInt(perks[i]),i);
							}
							
						}
						
						
						if (str.substring (0, 5).equals ("POINT")) {
							
							String [] data = str.substring(6).split(":");

							if (NetworkHandler.getPlayerNum() == Integer.parseInt(data[0])) {
								if (DataSlot.getDataSlot(Integer.parseInt(data[1])) != null) { // I don't want to try to figure this out right now this will fix it in like 99% of cases
									GameCode.bits.get(NetworkHandler.getPlayerNum() - 1).compass.setPointObject(DataSlot.getDataSlot(Integer.parseInt(data[1])));
								}
							}
						}
						if (str.substring (0, 7).equals ("DESTROY")) {
							String [] toDestroy = str.substring(8).split(":");
							switch (toDestroy[0]) {
							case "top":
								Roome.map[Integer.parseInt(toDestroy[2])][Integer.parseInt(toDestroy[1])].destroyTopWall();
								break;
							case "bottom":
								Roome.map[Integer.parseInt(toDestroy[2])][Integer.parseInt(toDestroy[1])].destroyBottomWall();
								break;
							case "left":
								Roome.map[Integer.parseInt(toDestroy[2])][Integer.parseInt(toDestroy[1])].destroyLeftWall();
								break;
							case "right":
								Roome.map[Integer.parseInt(toDestroy[2])][Integer.parseInt(toDestroy[1])].destroyRightWall();
								break;
							}
						}
						
						if (str.length () >= 5 && str.substring (0, 5).equals ("SOUND")) {
							String targetedPlayer = str.split(":")[1];
							String filePath = str.split(":")[2];
							if (targetedPlayer.equals(Integer.toString(NetworkHandler.getPlayerNum())) || targetedPlayer.equals("ALL")) {
								clientPlayer.playSoundEffect(GameCode.volume, filePath);
							}
						}
						if (str.length () >= 5 && str.substring (0, 5).equals ("MUSIC")) {
							String filePath = str.split(":")[1];
							GameCode.changeMusic(filePath);
						}
						if (str.equals ("ROUND COMPLETE")) {
							Hud.waveOver ();
						}
						if (str.length () >= 15 && str.substring (0, 15).equals ("FORGET REGISTER")) {
							int forgetableReg = Integer.parseInt(str.split(":")[1]);
							if (ObjectHandler.getObjectsByName("Register") != null) { //obligatory null check
								for (int i = 0; i < ObjectHandler.getObjectsByName("Register").size(); i++) {
									Register reg = (Register)ObjectHandler.getObjectsByName("Register").get(i);
									if (reg.id == forgetableReg) {
										reg.forget();
									}
								}
							}
						}
						
						
						if (str.length () >= 9 && str.substring (0, 9).equals ("FORGET DS")) {
							int forgetableDS = Integer.parseInt(str.split(":")[1]);
							for (int i = 0; i < ObjectHandler.getObjectsByName("DataSlot").size(); i++) {
								DataSlot ds = (DataSlot)ObjectHandler.getObjectsByName("DataSlot").get(i);
								if (ds.id == forgetableDS) {
									ds.forget();
								}
							}
						}
						
						
						if (str.length () >= 5 && str.substring (0, 5).equals ("START")) {
							System.out.println(str);
							String[] data = str.split (":");
							String room_data = data[1];
							TitleScreen.titleClosed = true;
							
						
							
							GameCode.getTitleScreen().setSprite(new Sprite ("resources/sprites/now loading.png"));
							
							
							
							RenderLoop.pause();
							
							GameCode.getTitleScreen().draw();
							
							RenderLoop.wind.refresh();
							
							Roome.loadMap (room_data);
							
							RenderLoop.unPause();
							
							
							GameCode.initGameState ();
							
							
							GameCode.closeTitleScreen();
							
							
						}
						
						if (str.length () >= 4 && str.substring (0,4).equals ("DATA")) {
							if (datas==0) {
								System.out.println(str);
								datas++;
							}
							ArrayList<Integer> toKeep = new ArrayList <Integer> ();
							
							long dataParseTime = System.currentTimeMillis ();
							//Get the data
							String[] data = str.split (":");
							updateGameData (data);
							
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
		this.message = "PING " + TitleScreen.perkNum;
		this.readyToSend = true;
	}
	
	public void close () {
		close = true;
	}
	
	public void setThread (Thread thread) {
		this.thread = thread;
	}
	
	public static void updateGameData (String[] data) {
		//System.out.println(Arrays.toString(data));
		ArrayList<Integer> toKeep = new ArrayList <Integer> ();
		
		long dataParseTime = System.currentTimeMillis ();
		//Extract the timer
		String timer_data = data[1];
		int timer_amt = Integer.parseInt (timer_data);
		Hud.timeLeft = timer_amt;
		
		//Extract the score
		String score_data = data[2];
		int score_amt = Integer.parseInt (score_data);
		Hud.score = score_amt;
		
		int next = 3;
		int totalBits = 0;
		while (true) { 
			//Extract the bit positions
			String bit_data = data[next];
			String[] bit_coords = bit_data.split (",");
			if (bit_data.equals("") || bit_data.contains (" ")) {
				break;
			}
			totalBits++;
			try {
				int bit_x = Integer.parseInt (bit_coords[0]);
				int bit_y = Integer.parseInt (bit_coords[1]);

				GameCode.bits.get(next - 3).setX (bit_x);
				GameCode.bits.get(next - 3).setY (bit_y);

			} catch (IndexOutOfBoundsException e) {
				//Do nothing
			}
			next = next + 1;
		}
		//Extract the register data
		String reg_data = data[next];
		String[] registers = reg_data.split (",");
		for (int i = 0; i < registers.length; i++) {
			Scanner s = new Scanner (registers [i]);
			if (s.hasNext ()) {
				int r_id = s.nextInt ();
				if (registerMap.containsKey (r_id)) {
					registerMap.get (r_id).refreshRegister (registers [i]);
					toKeep.add(r_id);
				} else {
					Register reg = new Register (1);
					reg.declare ();
					reg.refreshRegister (registers [i]);
					reg.id = r_id;
					toKeep.add(r_id);
					registerMap.put (r_id, reg);
				}
				s.close ();
			}
		}
		
		//Extract the DataSlot data
		String slot_data = data[next + 1];
		String[] slots = slot_data.split (",");
		for (int i = 0; i < slots.length; i++) {
			Scanner s = new Scanner (slots [i]);
			if (s.hasNext ()) {
			int r_id = s.nextInt ();
				if (slotMap.containsKey (r_id)) {
					toKeep.add(r_id);
					slotMap.get (r_id).refreshDataSlot (slots [i]);
				} else {
					DataSlot ds = new DataSlot (1);
					ds.declare ();
					ds.refreshDataSlot (slots [i]);
					ds.id = r_id;
					toKeep.add(r_id);
					slotMap.put (r_id, ds);
				}
				s.close ();
			}
		}
		//Extract the DataSlot data
		String item_data = data[next + 2];
		if (!item_data.equals ("")) {
			String[] items = item_data.split (",");
			for (int i = 0; i < items.length; i++) {
				if (i < items.length - totalBits) {
					Scanner s = new Scanner (items [i]);
					if (s.hasNext ()) {
						int r_id = s.nextInt ();
						if (itemMap.containsKey (r_id)) {
							toKeep.add(r_id);
							itemMap.get (r_id).refreshItem (items [i]);
							} else {
								Class<?> itemToUse = null;
								try {
									itemToUse = Class.forName(s.next());
								} catch (ClassNotFoundException e) {
									e.printStackTrace();
								}
								
								Item it = null;
								try {
									it = (Item)itemToUse.getConstructor().newInstance();
								} catch (InstantiationException | IllegalAccessException
										| IllegalArgumentException | InvocationTargetException
										| NoSuchMethodException | SecurityException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								it.declare ();
								it.refreshItem (items [i]);
								it.id = r_id;
								toKeep.add(r_id);
								itemMap.put (r_id, it);
							}
							s.close ();
						}
					} else {
						try {
							Bit b = GameCode.bits.get(i - (items.length - totalBits));
							if (!items[i].equals("null")) {
								Scanner s2 = new Scanner (items [i]);
								s2.next();
								if (b.inventory.getItem() != null && b.inventory.getItem().getClass().toString().equals(s2.next())) {
									b.inventory.getItem().refreshItem(items[i]);
								} else {
									Class<?> itemToUse = null;
									try {
										Scanner s = new Scanner (items [i]);
										s.next();
										itemToUse = Class.forName(s.next());
										s.close();
									} catch (ClassNotFoundException e) {
										e.printStackTrace();
									}
								
									Item it = null;
									try {
										it = (Item)itemToUse.getConstructor().newInstance();
									} catch (InstantiationException | IllegalAccessException
											| IllegalArgumentException | InvocationTargetException
											| NoSuchMethodException | SecurityException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									b.inventory.setItem(it);
								}
								s2.close();
							} else {
								b.inventory.setItem(null);
							}
						} catch (IndexOutOfBoundsException e) {
							//Do nothing
						}
					}
				}
			}
			
			Set<Integer> itemSet = itemMap.keySet(); 
         
			ArrayList<Integer> itemIds = new ArrayList<Integer>(itemSet);
		
			for (int i = 0; i <itemIds.size();i++) {
				if (!toKeep.contains(itemIds.get(i))) {
					itemMap.get(itemIds.get(i)).forget();
					itemMap.remove(itemIds.get(i));
				}
			}
		
			Set<Integer> regSet = registerMap.keySet(); 
         
			ArrayList<Integer> regIds = new ArrayList<Integer>(regSet);
		
			for (int i = 0; i <regIds.size();i++) {
				if (!toKeep.contains(regIds.get(i))) {
					registerMap.get(regIds.get(i)).forget();
					registerMap.remove(regIds.get(i));
				}
			}
		
			Set<Integer> slotSet = slotMap.keySet(); 
         
			ArrayList<Integer> slotIds = new ArrayList<Integer>(slotSet);
		
			for (int i = 0; i <slotIds.size();i++) {
				if (!toKeep.contains(slotIds.get(i))) {
					slotMap.get(slotIds.get(i)).forget();
					slotMap.remove(slotIds.get(i));
			}
			
			
		}
	}
}
