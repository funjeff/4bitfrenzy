package network;

public class NetworkHandler {

	private static boolean isHost;
	private static Server server;
	private static Client client;
	
	public static void setHost (boolean host) {
		isHost = host;
	}
	
	public static boolean isHost () {
		return isHost;
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
	
}
