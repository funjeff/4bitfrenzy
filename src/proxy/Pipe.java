package proxy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

import engine.GameCode;
import network.ServerConnection;
import titleScreen.TitleScreen;

public class Pipe extends Thread {

	private Socket clientPipe;
	private Socket serverPipe;
	
	private ServerInstance server;

	private volatile LinkedBlockingDeque<String> message = new LinkedBlockingDeque<String> ();
	
	public Pipe (Socket clientPipe, Socket serverPipe) {
		this.clientPipe = clientPipe;
		this.serverPipe = serverPipe;
	}
	
	public void setServerInstance (ServerInstance inst) {
		server = inst;
	}
	
	@Override
	public void run() {

		try {
			
			//Get client connection and IO streams
			InputStream inStream;
			inStream = clientPipe.getInputStream ();
			DataInputStream clientIn = new DataInputStream (inStream);
			OutputStream outStream = clientPipe.getOutputStream ();
			DataOutputStream clientOut = new DataOutputStream (outStream);
			
			//Get server connection and IO streams
			inStream = serverPipe.getInputStream ();
			DataInputStream serverIn = new DataInputStream (inStream);
			outStream = serverPipe.getOutputStream ();
			DataOutputStream serverOut = new DataOutputStream (outStream);
			
			//Recieve data (blocking)
			while (true) {
				//System.out.println ("RECIEVING DATA!");
				try {
					this.sleep (1);
				} catch (InterruptedException e) {
					// Do nothing, timing is not critical
				}
				
				if (serverIn.available () != 0) {
					String msg = serverIn.readUTF ();
					if (!server.gameStarted () && msg.length () >= 5 && msg.substring (0, 5).equals ("START")) {
						InstanceHandler.getServerInstanceLock ().lock ();
						server.setGameStarted ();
						InstanceHandler.getServerInstanceLock ().unlock ();
					}
					clientOut.writeUTF (msg);
				}
				if (clientIn.available () != 0) {
					String msg = clientIn.readUTF ();
					serverOut.writeUTF (msg);
				}
				
				//Check to see if the server instance is done
				if (server.proc.isAlive ()) {
					clientIn.close ();
					clientOut.close ();
					serverIn.close ();
					serverOut.close ();
					clientPipe.close ();
					serverPipe.close ();
					return;
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
}
