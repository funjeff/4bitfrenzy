package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import main.MainLoop;

public class RibbonPulse extends GameObject {
	
	private int loopId;
	private int t;
	
	public RibbonPulse (int loopId, int t) {
		this.loopId = loopId;
		this.t = t;
	}
	
	@Override
	public void frameEvent () {
		//t++;
	}
	
	@Override
	public void draw () {
		Graphics g = RenderLoop.wind.getBufferGraphics ();
		g.setColor (Color.GREEN);
		Point displayPt = Ribbon.getRibbonPoint (loopId, t += 4);
		g.fillRect (displayPt.x - 2 - GameCode.getViewX (), displayPt.y - 2 - GameCode.getViewY (), 4, 4);
	}

}
