package network;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.LinkedList;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;

import engine.GameCode;
import engine.RenderLoop;
import titleScreen.TitleScreen;

public class Server extends Thread {

	private Thread thread;
	
	private ServerSocket socket;
	private volatile ArrayList<ServerConnection> connections;
	
	private String serverIp;
	
	private static volatile boolean open = false;
	private static volatile LinkedBlockingDeque<String> writeMessage = new LinkedBlockingDeque<String> ();
	private static ArrayList<String> allMessages = new ArrayList<String> ();
	
	private FileWriter saveWriter;
	
	private boolean doneLogging = false;
	
	public Server () {
		connections = new ArrayList<ServerConnection> ();
		if (TitleScreen.doMapSave) {
			File f = new File ("resources/maps/saved_map.txt");
			try {
				saveWriter = new FileWriter (f);
				saveWriter.write ("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run () {
		try {
			socket = new ServerSocket (NetworkHandler.getPort ());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			//Print server ip
			System.out.println ("Server opened at " + socket.getInetAddress ().getLocalHost ().getHostAddress () + " on port " + socket.getLocalPort ());
			serverIp = socket.getInetAddress ().getLocalHost ().getHostAddress () + ":" + socket.getLocalPort ();
			open = true;
			
			//Get connection and get io streams
			AcceptHandler h = new AcceptHandler (this);
			h.start ();
			while (true) {
				
				if (!writeMessage.isEmpty ()) {
					
					//Attempt to pop the message from writeMessage
					String s = null;
					s = writeMessage.removeFirst ();
					
					//Send the message
					for (int i = 0; i < connections.size (); i++) {
						connections.get (i).message (s);
					}
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage (String message) {
		
		//Save the message if applicable
		/*if (saveWriter != null) {
			try {
				if (!message.substring (0, 5).equals ("PERKS")) {
					saveWriter.append (message);
					saveWriter.append ('\n');
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (message.substring (0, 4).equals("DATA")) {
				try {
					saveWriter.close ();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				saveWriter = null;
			}
		}*/
		
		/*allMessages.add (message);
		if (!doneLogging && message.substring (0, 4).equals("DATA")) {
			File f = new File ("log.txt");
			FileWriter fw;
			try {
				fw = new FileWriter (f);
				fw.write ("");
				for (int i = 0; i < allMessages.size (); i++) {
					fw.append (allMessages.get (i) + "\n");
				}
				fw.close ();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			doneLogging = true;
		}*/
		writeMessage.add (message);
	}
	
	public boolean isOpen () {
		return open;
	}
	
	public void close () {
		open = false;
	}
	
	public void setThread (Thread thread) {
		this.thread = thread;
	}
	
	public String getIp () {
		return serverIp;
	}
	
	public int getNumPlayers () {
		return NetworkHandler.isHostAPlayer () ? connections.size () + 1 : connections.size ();
	}
	
	public String getPlayerInputs (int playerNum) {
		if (NetworkHandler.isHostAPlayer() && playerNum == 1) {
			//Do inputs normally
			String toSend = "";
			try {
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[0])) {
					toSend += 'W';
				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[2])) {
					toSend += 'A';
				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[1])) {
					toSend += 'S';
				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[3])) {
					toSend += 'D';
				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[8])) {
					toSend += 'U';
				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[10])) {
					toSend +='L';
				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[9])) {
					toSend += 'G';
				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[11])) {
					toSend += 'R';
				}
				if (GameCode.bits.get(0).keyDown(GameCode.getSettings().getControls()[12])) {
					toSend += 'C';
				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[5])) {
					toSend += 10;
				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[6])) {
					toSend += 13;
				}
//				if (bits.get(0).keyDown('E')) {
//					toSend += 'E';
//				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[7])) {
					toSend += 'M';
				}
				if (GameCode.bits.get(0).keyDown (GameCode.getSettings().getControls()[4])) {
					toSend += 'v';
				}
				//System.out.println (toSend);
				return toSend;
			} catch (IndexOutOfBoundsException e) {
				return ""; //Stuff hasn't been initialized yet
			}
		} else {
			//Do inputs specially
			int startNum = NetworkHandler.isHostAPlayer () ? playerNum - 2 : playerNum - 1;
			if (startNum <= connections.size ()) {
				return connections.get (startNum).getInputs ();
			} else {
				return "";
			}
		}
	}
	
	public class AcceptHandler extends Thread {
		
		private Server server;
		
		public AcceptHandler (Server s) {
			this.server = s;
		}
		
		@Override
		public void run () {
			while (true) {
				Socket incoming;
				try {
					incoming = socket.accept ();
					ServerConnection curr = new ServerConnection (this.server, incoming, NetworkHandler.isHostAPlayer() ? 2 + connections.size () : 1 + connections.size ());
					connections.add (curr);
					curr.start ();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	public boolean areAllConnectionsInitialized () {
		for (int i = 0; i < connections.size (); i++) {
			if (!connections.get (i).setupFinished) {
				return false;
			}
		}
		return true;
	}
	
}
