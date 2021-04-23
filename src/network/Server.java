package network;

import java.awt.event.KeyEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import engine.GameCode;
import engine.RenderLoop;

public class Server extends Thread {

	private Thread thread;
	
	private ServerSocket socket;
	private volatile ArrayList<ServerConnection> connections;
	
	private String serverIp;
	
	private static volatile boolean open = false;
	private static volatile boolean write = false;
	private static volatile String writeMessage;
	
	public Server () {
		connections = new ArrayList<ServerConnection> ();
	}
	
	@Override
	public void run () {
		try {
			socket = new ServerSocket (8080);
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

				if (write) {
					for (int i = 0; i < connections.size (); i++) {
						connections.get (i).message (writeMessage);
					}
					write = false;
				}
				
			}
			
			
			//Close
			/*incoming.close ();
			socket.close ();
			System.out.println ("Server closed");*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage (String message) {
		write = true;
		writeMessage = message;
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
		return connections.size ();
	}
	
	public String getPlayerInputs (int playerNum) {
		if (playerNum == 1) {
			//Do inputs normally
			String toSend = "";
			try {
				if (GameCode.bits.get(0).keyDown ('W')) {
					toSend += 'W';
				}
				if (GameCode.bits.get(0).keyDown ('A')) {
					toSend += 'A';
				}
				if (GameCode.bits.get(0).keyDown ('S')) {
					toSend += 'S';
				}
				if (GameCode.bits.get(0).keyDown ('D')) {
					toSend += 'D';
				}
				if (GameCode.bits.get(0).keyDown (KeyEvent.VK_UP)) {
					toSend += KeyEvent.VK_UP;
				}
				if (GameCode.bits.get(0).keyDown (KeyEvent.VK_LEFT)) {
					toSend += KeyEvent.VK_LEFT;
				}
				if (GameCode.bits.get(0).keyDown (KeyEvent.VK_DOWN)) {
					toSend += KeyEvent.VK_DOWN;
				}
				if (GameCode.bits.get(0).keyDown (KeyEvent.VK_RIGHT)) {
					toSend += KeyEvent.VK_RIGHT;
				}
				if (GameCode.bits.get(0).keyDown(KeyEvent.VK_CONTROL)) {
					toSend += KeyEvent.VK_CONTROL;
				}
				if (GameCode.bits.get(0).keyDown (10)) {
					toSend += 10;
				}
				if (GameCode.bits.get(0).keyDown (13)) {
					toSend += 13;
				}
				if (GameCode.bits.get(0).keyDown('E')) {
					toSend += 'E';
				}
				if (GameCode.bits.get(0).keyDown ('M')) {
					toSend += 'M';
				}
				if (GameCode.bits.get(0).keyDown (KeyEvent.VK_SHIFT)) {
					toSend += 'v';
				}
				//System.out.println (toSend);
				return toSend;
			} catch (IndexOutOfBoundsException e) {
				return ""; //Stuff hasn't been initialized yet
			}
		} else {
			//Do inputs specially
			
			if (playerNum - 1 <= connections.size ()) {
				return connections.get (playerNum - 2).getInputs ();
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
					ServerConnection curr = new ServerConnection (this.server, incoming);
					curr.start ();
					connections.add (curr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
	}
	
}
