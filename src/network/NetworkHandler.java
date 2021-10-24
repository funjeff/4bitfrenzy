package network;

import java.util.Scanner;

public class NetworkHandler {

	private static int playerNum = 1;
	private static boolean isHost;
	private static boolean hostIsPlayer = true;
	private static Server server;
	private static Client client;
	private static int port = 52754; //Arbitrary random port above 30,000
	
	public static void setServerMode () {
		hostIsPlayer = false;
		playerNum = -1;
	}
	
	public static boolean isServer () {
		return !hostIsPlayer;
	}
	
	public static void setHost (boolean host) {
		isHost = host;
	}
	
	public static boolean isHost () {
		return isHost;
	}
	
	public static boolean isHostAPlayer () {
		if (hostIsPlayer) {
			return true;
		}
		return false;
 	}
	
	public static boolean isPlayer () {
		if (isHost && !hostIsPlayer) {
			return true;
		}
		return false;
	}
	
	public static int getPlayerNum () {
		return playerNum;
	}
	
	public static Client getClient () {
		return client;
	}
	
	public static Server getServer () {
		return server;
	}
	
	public static int getPort () {
		return port; 
	}
	
	public static void setClient (Client c) {
		client = c;
	}
	
	public static void setServer (Server s) {
		server = s;
	}
	
	public static void setPlayerNum (int num) {
		playerNum = num;
	}
	
	public static void setPort (int portNum) {
		port = portNum;
	}
	
	public static void waitForPlayers () {
		int TIMEOUT_TIME = 30000;
		InputWaitThread waitObj = new InputWaitThread ();
		Thread inputWaitThread = new Thread (waitObj);
		int numPlayers = 0;
		inputWaitThread.start ();
		long startTime = System.currentTimeMillis ();
		while (true) {
			if (numPlayers != getServer ().getNumPlayers ()) {
				numPlayers = getServer ().getNumPlayers ();
				startTime = System.currentTimeMillis ();
			}
			if (waitObj.inputRecieved()) {
				return;
			}
			if (getServer ().getNumPlayers () > 0 && System.currentTimeMillis() - startTime > TIMEOUT_TIME) {
				return;
			}
			if (getServer ().getNumPlayers () == 4 && getServer ().areAllConnectionsInitialized ()) {
				return;
			}
			try {
				Thread.sleep (1);
			} catch (InterruptedException e) {
				//Do nothing
			}
		}
	}
	
	public static class InputWaitThread implements Runnable {

		private boolean isFinished;
		private String input;
		
		@Override
		public void run() {
			Scanner s = new Scanner (System.in);
			input = s.next ();
			isFinished = true;
			return;
		}
		
		public boolean inputRecieved () {
			return isFinished;
		}
		
		public String getInput () {
			return input;
		}
		
	}
	
}
