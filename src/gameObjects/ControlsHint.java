package gameObjects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;

public class ControlsHint extends GameObject {
	
	public static Sprite buttonSprite = new Sprite ("resources/sprites/text button border.png");
	
	private int mode;
	
	public static final int MODE_NOT_VISIBLE = 0;
	public static final int MODE_GRAB_REGISTER = 1;
	public static final int MODE_GAMBLE = 2;
	
	public ControlsHint () {
		this.setRenderPriority(69);
	}
	
	@Override
	public void draw () {
		
		if (this.isVisable() && mode != MODE_NOT_VISIBLE) {
			
			Graphics g = RenderLoop.wind.getBufferGraphics();
			
			//Border attributes
			int borderWidth = 2;
			int borderHeight = 2;
			int boxWidth = 78;
			int boxHeight = 42;
			Color innerColor = new Color (0x001000);
			Color outerColor = new Color (0x005000);
			
			//Text attributes
			int textOffsetX = borderWidth + 4;
			int textOffsetY = borderHeight + 4;
			Color textColor = new Color (0xFFFFFF);
			
			//Draw border
			g.setColor (outerColor);
			g.fillRect ((int)getX (), (int)getY (), boxWidth, boxHeight);
			g.setColor (innerColor);
			g.fillRect ((int)getX () + borderWidth, (int)getY () + borderHeight, boxWidth - borderWidth * 2, boxHeight - borderHeight * 2);
			
			if (mode == MODE_GRAB_REGISTER) {
				
				//Draw button
				buttonSprite.draw ((int)getX () + textOffsetX + 30, (int)getY () + textOffsetY);
				g.setColor (textColor);
				g.drawString("shift", (int)this.getX() + textOffsetX + 34, (int)this.getY() + textOffsetY + 12);
				
				//Draw text
				g.drawString("hold", (int)this.getX () + textOffsetX + 1, (int)this.getY () + textOffsetY + 14);
				g.drawString("to grab", (int)this.getX () + textOffsetX + 10, (int)this.getY () + textOffsetY + 28);
			
			}
			
			if (mode == MODE_GAMBLE) {
				
				//Draw button
				buttonSprite.draw ((int)getX () + textOffsetX + 34, (int)getY () + textOffsetY);
				g.setColor (textColor);
				g.drawString("shift", (int)this.getX() + textOffsetX + 37, (int)this.getY() + textOffsetY + 12);
				
				//Draw text
				g.drawString("press", (int)this.getX () + textOffsetX + 1, (int)this.getY () + textOffsetY + 14);
				g.drawString("to gamble", (int)this.getX () + textOffsetX + 5, (int)this.getY () + textOffsetY + 28);
				
			}
		
		}
	}

	public int getMode () {
		return mode;
	}
	
	public void showNoHint () {
		mode = MODE_NOT_VISIBLE;
	}
	
	public void showGrabHint () {
		mode = MODE_GRAB_REGISTER;
	}
	
	public void showGambleHint () {
		mode = MODE_GAMBLE;
	}
	
}
