package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
