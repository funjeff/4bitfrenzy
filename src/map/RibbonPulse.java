package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import main.MainLoop;
import util.ColorMap;

public class RibbonPulse extends GameObject {
	
	public static final int PULSE_SIZE = 120;
	public static final int PULSE_HALF = PULSE_SIZE / 2;
	
	private static ColorMap cm;
	
	private int loopId;
	private int t;
	
	public RibbonPulse (int loopId, int t) {
		this.loopId = loopId;
		this.t = t;
		if (cm == null) {
			cm = new ColorMap ();
			cm.addColor (Ribbon.defaultColor, 0);
			cm.addColor (Color.GREEN, .9);
		}
	}
	
	@Override
	public void frameEvent () {
		//t++;
	}
	
	@Override
	public void draw () {
		Graphics g = RenderLoop.wind.getBufferGraphics ();
		for (int i = 0; i < PULSE_SIZE; i++) {
			int pos = i > PULSE_HALF ? i - PULSE_SIZE : i;
			double colorAmt = (double)Math.abs (PULSE_HALF - i) / PULSE_HALF;
			g.setColor (cm.getColor (colorAmt));
			Point displayPt = Ribbon.getRibbonPoint (loopId, t + pos);
			g.fillRect (displayPt.x - 2 - GameCode.getViewX (), displayPt.y - 2 - GameCode.getViewY (), 4, 4);
		}
		t += 4;
	}

}
