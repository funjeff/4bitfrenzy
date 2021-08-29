package engine;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.JFrame;

/**
 * The window which the game is displayed in. Also handles keyboard and mouse events.
 * @author nathan
 *
 */
public class GameWindow extends JFrame {

	/**
	 * Serial version ID, as specified by Swing?
	 */
	private static final long serialVersionUID = 8537802541411424289L;
	/**
	 * The image used as a drawing buffer
	 */
	private BufferedImage buffer;
	/**
	 * The image used as a rendering buffer (to the window)
	 */
	private BufferedImage windowBuffer;
	/**
	 * The InputManager used to detect input for this window
	 */
	private InputManager inputManager;
	
	/**
	 * Constructs a new GameWindow with the given width and height.
	 * @param width The initial width, in pixels, of the window content
	 * @param height The initial height, in pixels, of the window content
	 */
	public GameWindow (int width, int height) {
		//Makes sure that java closes when the window closes
		this.addWindowListener (new WindowAdapter() {
			public void windowClosing (WindowEvent e) {
				System.exit (0);
			}
		});
		//Sets the initial size of the window and makes it visible
		getContentPane ().setPreferredSize (new Dimension (1280, 720));
		pack ();
		setVisible (true);
		//Initializes the buffer for drawing
		buffer = new BufferedImage (width, height, BufferedImage.TYPE_3BYTE_BGR);
		//Sets up the input detection
		inputManager = new InputManager (this, this.getContentPane ());
	}
	
	/**
	 * Renders the contents of the buffer onto the window.
	 */
	public void refresh () {
		
		//Get the graphics
		Graphics bufferGraphics = getBufferGraphics ();
		Graphics windowGraphics = getContentPane ().getGraphics ();
		
		int xOffs = 0;
		int yOffs = 0;
		int screenWidth = getScreenWidth();
		int screenHeight = getScreenHeight();
		//Calculate the sizing of the content
		int scalingMode = GameCode.getSettings ().getScalingMode ();
		if (scalingMode == GameCode.GameSettings.SCALE_MODE_HORIZONTAL_BORDER) {
			xOffs = getOffsetX();
		}
		if (scalingMode == GameCode.GameSettings.SCALE_MODE_FULL_BORDER) {
			xOffs = getOffsetX();
			yOffs = getOffsetY();
		}
	
		//Set the colors
		windowGraphics.setColor (new Color (0x000000));
		bufferGraphics.setColor (new Color (0x000000));
		
		//Fill the blank space on the sides
		if (scalingMode == GameCode.GameSettings.SCALE_MODE_HORIZONTAL_BORDER || scalingMode == GameCode.GameSettings.SCALE_MODE_FULL_BORDER) {
			windowGraphics.fillRect (0, 0, xOffs, getContentPane ().getHeight ());
			windowGraphics.fillRect (xOffs + screenWidth, 0, xOffs + 5, getContentPane ().getHeight ());
		}
		if (scalingMode == GameCode.GameSettings.SCALE_MODE_FULL_BORDER) {
			windowGraphics.fillRect (0, 0, getContentPane ().getWidth (), yOffs);
			windowGraphics.fillRect (0, yOffs + screenHeight, getContentPane ().getWidth (), yOffs + 5);
		}
		//Render the content
		if (scalingMode == GameCode.GameSettings.SCALE_MODE_FULL) {
			windowGraphics.drawImage (buffer, 0, 0, null);
		} else {
			windowGraphics.drawImage (buffer, xOffs, yOffs, screenWidth, screenHeight, null);
		}
		bufferGraphics.fillRect (0, 0, buffer.getWidth (), buffer.getHeight ());
		
		//Update the resolution if in full mode
		if (scalingMode == GameCode.GameSettings.SCALE_MODE_FULL) {
			if (getContentPane ().getWidth () != buffer.getWidth () || getContentPane ().getHeight () != buffer.getHeight ()) {
				GameCode.getSettings ().changeResolution (getContentPane ().getWidth (), getContentPane ().getHeight ());
			}
		} else {
			GameCode.getSettings().resetRes();
		}
	}
	
	public int getOffsetX() {
		
		int screenWidth;
		
		int screenHeight = getContentPane ().getHeight ();
		
		if (GameCode.getSettings().getScaleMode() == GameCode.GameSettings.SCALE_MODE_HORIZONTAL_BORDER) {
			screenWidth = (int)(((double)buffer.getWidth() / buffer.getHeight ()) * screenHeight);
		} else if (GameCode.getSettings().getScaleMode() ==  GameCode.GameSettings.SCALE_MODE_FULL_BORDER){
			screenWidth = GameCode.getSettings ().getResolutionX();
		} else {
			return 0;
		}
		return (getContentPane ().getWidth () - screenWidth) / 2;
	}
	
	public int getOffsetY() {
		int screenHeight = GameCode.getSettings ().getResolutionY ();
		if (GameCode.getSettings().getScaleMode() == GameCode.GameSettings.SCALE_MODE_FULL_BORDER) {
			return (getContentPane ().getHeight () - screenHeight) / 2;
		} else {
			return 0;
		}
	}
	public int getScreenWidth () {
		int screenHeight = getContentPane ().getHeight ();
		
		if (GameCode.getSettings().getScaleMode() == GameCode.GameSettings.SCALE_MODE_HORIZONTAL_BORDER) {
			return (int)(((double)buffer.getWidth() / buffer.getHeight ()) * screenHeight);
		} else if (GameCode.getSettings().getScaleMode() ==  GameCode.GameSettings.SCALE_MODE_FULL_BORDER){
			return GameCode.getSettings ().getResolutionX();
		} else {
			return getContentPane ().getWidth ();
		}
	}
	public int getScreenHeight () {
		if (GameCode.getSettings().getScaleMode() == GameCode.GameSettings.SCALE_MODE_FULL_BORDER) {
			return GameCode.getSettings ().getResolutionY ();
		} else {
			return getContentPane ().getHeight ();
		}
	}
	/**
	 * Gets the graphics object referring to this GameWindow's buffer.
	 * @return The buffer for this GameWindow's graphics
	 */
	public Graphics getBufferGraphics () {
		return buffer.getGraphics ();
	}
	
	/**
	 * Gets the dimensions of the buffer, e.g. the resolution of the output.
	 * @return The dimensions of this GameWindow's buffer as an int array, in the format [width, height]
	 */
	public int[] getResolution () {
		return new int[] {buffer.getWidth (), buffer.getHeight ()};
	}
	
	/**
	 * Sets the resolution of the buffer to the given width and height; erases its contents.
	 * @param width The width to use, in pixels
	 * @param height The height to use, in pixels
	 */
	public void setResolution (int width, int height) {
		buffer = new BufferedImage (width, height, BufferedImage.TYPE_3BYTE_BGR);
	}
	
	public InputManager getInputImage () {
		return inputManager.createImage ();
	}
	
	public void resetInputBuffers () {
		inputManager.resetKeyBuffers ();
		inputManager.resetMouseBuffers ();
	}
	
	public void resizeWindow (int width, int height) {
		getContentPane ().setPreferredSize (new Dimension (width, height));
		pack ();
	}

}