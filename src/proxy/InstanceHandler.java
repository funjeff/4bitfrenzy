package proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Scanner;
import java.io.InputStream;
import java.io.File;

import network.NetworkHandler;
import network.Server;
import network.ServerConnection;

public class InstanceHandler {
	
	public static final int ACCEPT_PORT = 41881;
	
	public static String JAR_PATH = "game_jam.jar";
	
	private ServerSocket acceptSocket;
	
	private ServerInstance currInstance;
	
	private static Lock serverInstanceLock;
	
	public static void startInstance (int port) {
		new ServerInstance (port);
	}
	
	public InstanceHandler () {
		try {
			acceptSocket = new ServerSocket (ACCEPT_PORT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AcceptHandler ah = new AcceptHandler ();
		Thread ahThread = new Thread (ah);
		ahThread.run ();
		
	}
	
	public static Lock getServerInstanceLock () {
		return serverInstanceLock;
	}
	
	public class AcceptHandler extends Thread {
		
		public AcceptHandler () {
			
		}
		
		@Override
		public void run () {
			boolean accepted = false;
			Socket prevIncoming = null;
			while (true) {
				Socket incoming;
				try {
					
					while (accepted){
						try {
							//Connect to the server instance
							Socket server = new Socket ("127.0.0.1", currInstance.getPort ());
					
							System.out.println(prevIncoming);

							//Link the server and client connections
							currInstance.addConnection (prevIncoming, server);
					
							//Unlock
							serverInstanceLock.unlock ();
							accepted = false;
						} catch (IOException e){
						
						}
					}

					//Accept the incoming client
					incoming = acceptSocket.accept ();
					
					//Lock to prevent modifying the server instance
					if (serverInstanceLock == null) {
						serverInstanceLock = new ReentrantLock ();
					}
					
					serverInstanceLock.lock ();
					
					//System.out.println("debug");
					//Create a server instance if there isn't already one available
					if (currInstance == null || currInstance.gameStarted ()) {
						currInstance = new ServerInstance (0);
					}
					accepted = true;
					prevIncoming = incoming; //INCOMMING - quincy
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}

}
		
