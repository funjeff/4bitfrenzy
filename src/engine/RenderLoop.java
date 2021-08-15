package engine;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * The loop for rendering the game to the GameWindow. Runs on the event dispatching thread.
 * @author nathan
 *
 */
public class RenderLoop {
	
	/**
	 * The maximum framerate the game can run at
	 */
	public static final double maxFramerate = GameLoop.stepsPerSecond;
	/**
	 * The time of the last update to the GameWindow, in nanoseconds.
	 */
	static private long lastUpdate;
	
	/**
	 * The system time when this frame's rendering began
	 */
	static private long frameTime;
	
	static public GameWindow wind;
	
	static public Thread renderThread;
	static public Thread gameThread;
	static public GameLoop gameLoop;
	
	static public boolean running = true;
	
	static boolean paused = false;
	
	public static volatile boolean useMultithreading = false;
	
	public static boolean isPaused() {
		return paused;
	}

	public static void pause() {
		paused = true;
	}
	public static void unPause () {
		paused = false;
	}

	public static void main (String[] args) {
		//Sets the initial frame time
		frameTime = System.currentTimeMillis ();
		//Create the GameWindow
		wind = new GameWindow (1280, 720);
		GameCode.testBitch();
		//Start the game logic loop on a separate thread
		gameLoop = new GameLoop ();
		gameThread = new Thread (gameLoop);
		GameCode.init ();
		//initMultithreading ();
		//TODO swap between single-threaded and multi-threaded as necessary
		//Initializes lastUpdate to the current time
		lastUpdate = System.nanoTime ();
		renderThread = Thread.currentThread ();
		
		while (running) {
				//Get the target time in nanoseconds for this iteration; should be constant if the framerate doesn't change
				long targetNanoseconds = (long)(1000000000 / maxFramerate);
				//Get the time before refreshing the window
				long startTime = System.nanoTime ();
				frameTime = System.currentTimeMillis ();
				if (!paused) {
				//Run the game loop
				if (!useMultithreading && !gameThread.isAlive()) {
					gameThread.run ();
				}
				//Render the window
				GameCode.renderFunc ();
				ObjectHandler.renderAll ();
				wind.refresh();
				//Calculate elapsed time and time to sleep for
				lastUpdate = System.nanoTime ();
				}
				
				long elapsedTime = lastUpdate - startTime;
				int sleepTime = (int)((targetNanoseconds - elapsedTime) / 1000000) - 1;
				if (sleepTime < 0) {
					sleepTime = 0;
				}
			//Sleep until ~1ms before it's time to redraw the frame (to account for inaccuracies in Thread.sleep)
			try {
				Thread.currentThread ().sleep (sleepTime);
			} catch (InterruptedException e) {
				//Do nothing; the while loop immediately after handles this case well
			}
			//Wait until the frame should be redrawn
			while (System.nanoTime () - startTime < targetNanoseconds) {
				
			}
		}
		wind.dispose();
	}
	
	/**
	 * Gets the value returned by System.currentTimeMillis() at the start of the frame being rendered.
	 * @return The start time of the current frame
	 */
	public static long frameStartTime () {
		return frameTime;
	}
	
	public static void pauseRender (int ms) {
		try {
			renderThread.sleep (ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean isMultithreaded () {
		return useMultithreading;
	}
	
	public static void initMultithreading () {
		if (!useMultithreading) {
			useMultithreading = true;
			gameThread.start ();
		} else {
			throw new IllegalStateException ("Cannot init multithreading while multithreading is enabled");
		}
	}
	
	public static void stopMultithreading () {
		if (useMultithreading) {
			useMultithreading = false;
		} else {
			throw new IllegalStateException ("Cannot stop multithreading while multithreading is disabled");
		}
	}
	
}
