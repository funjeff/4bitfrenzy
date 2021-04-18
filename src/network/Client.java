package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.UUID;

import engine.GameCode;
import engine.RenderLoop;
import gameObjects.TitleScreen;
import map.Roome;

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
	
	private String uuid = UUID.randomUUID ().toString ();
	
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
						System.out.println ("Message recieved: " + str);
						
						//Parse message, etc.
						if (str.equals ("PONG")) {
							TitleScreen.connectSuccess ();
						}
						
						if (str.length () >= 5 && str.substring (0, 5).equals ("START")) {
							String[] data = str.split (":");
							String room_data = data[1];
							Roome.loadMap (room_data);
							GameCode.initGameState ();
						}
						
						System.out.println ("Message recieved: " + str);
						RenderLoop.wind.setTitle (str);

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
				System.out.println ("Message sent: " + message);
				
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
