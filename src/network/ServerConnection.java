package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

import engine.GameCode;
import engine.RenderLoop;
import titleScreen.TitleScreen;

public class ServerConnection extends Thread {
	
	Server server;
	Socket incoming;
	boolean open = true;
	boolean setupFinished = false;
	
	private volatile LinkedBlockingDeque<String> message = new LinkedBlockingDeque<String> ();
	
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
			
			//Prevent a race condition
			setupFinished = true;
			
			//Recieve data (blocking)
			while (open) {
				//System.out.println ("RECIEVING DATA!");
				try {
					this.sleep (1);
				} catch (InterruptedException e) {
					// Do nothing, timing is not critical
				}
				if (dataIn.available () != 0) {
					String str = dataIn.readUTF ();
					if (str.substring (0, 4).equals ("PING")) {
						GameCode.setPerk(Integer.parseInt(str.substring(5)), server.getNumPlayers() - 1);
						dataOut.writeUTF ("PLAYER " + (server.getNumPlayers ()));
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
					if (str.length () >= 6 && str.substring (0, 6).equals("FREEZE")) {
						
						GameCode.bits.get(Integer.parseInt(str.substring(6)) -1).freeze();
					
					}
					
					if (str.length () >= 8 && str.substring (0, 8).equals("UNFREEZE")) {
						GameCode.bits.get(Integer.parseInt(str.substring(8)) -1).unfreeze();
						
					}
				}
				
				while (!message.isEmpty ()) {
					//Attempt to pop the message from writeMessage
					String s = null;

					s = message.removeFirst ();
					
					//Send the message
					if (s != null) {
						dataOut.writeUTF (s);
						dataOut.flush ();
					}
					
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
		if (message != null) {
			this.message.add (message);
		}
	}
	
	public String getInputs () {
		return inputs;
	}
	
}
