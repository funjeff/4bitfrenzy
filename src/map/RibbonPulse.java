package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import util.ColorMap;

public class RibbonPulse extends GameObject {
	
	public static final int PULSE_SIZE = 120;
	public static final int PULSE_HALF = PULSE_SIZE / 2;
	
	private static ColorMap[] colors;
	
	private int loopId;
	private int t;
	
	public RibbonPulse (int loopId, int t) {
		this.loopId = loopId;
		this.t = t;
		if (colors == null) {
			colors = new ColorMap[5];
			colors [0] = new ColorMap ();
			colors [0].addColor (Ribbon.defaultColors [0], 0);
			colors [0].addColor (new Color (0x00FFFF), .9);
			colors [1] = new ColorMap ();
			colors [1].addColor (Ribbon.defaultColors [1], 0);
			colors [1].addColor (new Color (0x00FF00), .9);
			colors [2] = new ColorMap ();
			colors [2].addColor (Ribbon.defaultColors [2], 0);
			colors [2].addColor (new Color (0xFF0000), .9);
			colors [3] = new ColorMap ();
			colors [3].addColor (Ribbon.defaultColors [3], 0);
			colors [3].addColor (new Color (0xFFFF00), .9);
			colors [4] = new ColorMap ();
			colors [4].addColor (Ribbon.defaultColors [4], 0);
			colors [4].addColor (new Color (0xFFFFFF), .9);
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
			Point displayPt = Ribbon.getRibbonPoint (loopId, t + pos);
			g.setColor (colors [Roome.getRoom (displayPt.x, displayPt.y).color].getColor (colorAmt));
			g.fillRect (displayPt.x - 2 - GameCode.getViewX (), displayPt.y - 2 - GameCode.getViewY (), 4, 4);
		}
		t += 4;
	}

}
