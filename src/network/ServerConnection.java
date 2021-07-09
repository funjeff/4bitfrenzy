package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;

import engine.GameCode;
import engine.RenderLoop;
import gameObjects.TitleScreen;

public class ServerConnection extends Thread {
	
	Server server;
	Socket incoming;
	boolean open = true;
	
	private volatile LinkedList<String> message = new LinkedList<String> ();
	
	private String inputs;
	
	public ServerConnection (Server server, Socket incoming) {
		this.server = server;
		this.incoming = incoming;
	}
	
	@Override
	public void run () {
		try {
			//Get connection and get io streams
			InputStream inStream = incoming.getInputStream ();
			DataInputStream dataIn = new DataInputStream (inStream);
			OutputStream outStream = incoming.getOutputStream ();
			DataOutputStream dataOut = new DataOutputStream (outStream);
			
			//Recieve data (blocking)
			while (open) {
				try {
					this.sleep (1);
				} catch (InterruptedException e) {
					// Do nothing, timing is not critical
				}
				if (dataIn.available () != 0) {
					String str = dataIn.readUTF ();
					if (str.substring (0, 4).equals ("PING")) {
						GameCode.setPerk(Integer.parseInt(str.substring(5)), server.getNumPlayers());
						dataOut.writeUTF ("PLAYER " + (server.getNumPlayers () + 1));
						TitleScreen.playerJoin ();
						dataOut.flush ();
					}
					if (str.length () >= 4 && str.substring (0, 4).equals ("KEYS")) {
						String[] keyData = str.split (":");
						if (keyData.length > 1) {
							inputs = keyData[1];
						} else {
							inputs = "";
						}
					}
				}
				while (!message.isEmpty ()) {
					
					//Attempt to pop the message from writeMessage
					String s = null;
					while (s == null) {
						try {
							s = message.removeLast ();
						} catch (ConcurrentModificationException e) {
							try {
								Thread.sleep (1); //Wait and try again
							} catch (InterruptedException e1) {
								//Timing is not important
							}
						}
					}
					
					//Send the message
					dataOut.writeUTF (s);
					dataOut.flush ();
					
				}
			}
			
			//Close
			/*incoming.close ();
			socket.close ();
			System.out.println ("Server closed");*/
		} catch (IOException e) {
			e.printStackTrace ();
		}
	}

	public void message (String message) {
		this.message.add (message);
	}
	
	public String getInputs () {
		return inputs;
	}
	
}
