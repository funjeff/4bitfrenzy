package network;

public class NetworkHandler {

	private static int playerNum = 1;
	private static boolean isHost;
	private static Server server;
	private static Client client;
	
	public static void setHost (boolean host) {
		isHost = host;
	}
	
	public static boolean isHost () {
		return isHost;
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
	
	public static void setClient (Client c) {
		client = c;
	}
	
	public static void setServer (Server s) {
		server = s;
	}
	
	public static void setPlayerNum (int num) {
		playerNum = num;
	}
	
}
