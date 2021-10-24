package proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerInstance {

	public Process proc;
	
	private int port;
	
	private boolean started = false;
	
	private int numConnections = 0;
	private ArrayList<Pipe> clientConnections;
		
	public ServerInstance (int port) {
		
		if (port == 0) {
			ServerSocket s;
			try {
				s = new ServerSocket (0);
				port = s.getLocalPort();
				s.close ();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		this.port = port;
		
		try {
			proc = Runtime.getRuntime ().exec ("java -jar " + InstanceHandler.JAR_PATH + " -server" + " -port" + port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void addConnection (Socket clientPipe, Socket serverPipe) {
		
		if (clientConnections == null) {
			clientConnections = new ArrayList<Pipe> ();
		}
		Pipe curr = new Pipe (clientPipe, serverPipe);
		curr.setServerInstance (this);
		clientConnections.add (curr);
		curr.start ();
		numConnections++;
		
	}
	
	public void setGameStarted () {
		started = true;
	}
	
	public int getNumConnections () {
		return numConnections;
	}
	
	public int getPort () {
		return port;
	}
	
	public boolean gameStarted () {
		return started;
	}
	
}
