package map;

import java.awt.Color;
import java.awt.Graphics;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;

public class Ribbon extends GameObject {

	Color defaultColor = new Color (0x157F41);
	
	int xFrom;
	int yFrom;
	int xTo;
	int yTo;
	
	public Ribbon (int xFrom, int yFrom, int xTo, int yTo) {
		this.xFrom = xFrom;
		this.yFrom = yFrom;
		this.xTo = xTo;
		this.yTo = yTo;
		declare (xFrom, yFrom);
	}
	
	@Override
	public void draw () {
		Graphics g = RenderLoop.wind.getBufferGraphics ();
		g.setColor (defaultColor);
		if (xFrom == xTo) {
			g.fillRect (xFrom - 2 - GameCode.getViewX (), yFrom - GameCode.getViewY (), 4, yTo - yFrom);
		} else if (yFrom == yTo) {
			g.fillRect (xFrom - GameCode.getViewX (), yFrom - 2 - GameCode.getViewY (), xTo - xFrom, 4);
		}
	}
	
}
