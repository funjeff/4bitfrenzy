package gameObjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

import engine.GameObject;
import engine.RenderLoop;
import util.Vector2D;

public class MovableRectHighlight extends GameObject {

	Rectangle from;
	Rectangle to;
	int moveTime;
	int elapsedTime;
	
	boolean isMoving = false;
	
	public MovableRectHighlight (Rectangle r) {
		from = r;
		declare ();
	}
	
	public void moveTo (Rectangle toRect, int time) {
		to = toRect;
		moveTime = time;
		elapsedTime = 0;
		isMoving = true;
	}
	
	@Override
	public void frameEvent () {
		if (isMoving) {
			elapsedTime++;
			if (elapsedTime == moveTime) {
				isMoving = false;
				elapsedTime = 0;
				from = to;
			}
		}
	}
	
	@Override
	public void draw () {
		Graphics2D g = (Graphics2D)RenderLoop.wind.getBufferGraphics ();
		g.setColor (Color.WHITE);
		g.setStroke (new BasicStroke (2));
		if (isMoving) {
			Vector2D p1From = new Vector2D (from.x, from.y);
			Vector2D p2From = new Vector2D (from.x + from.width, from.y + from.height);
			Vector2D p1To = new Vector2D (to.x, to.y);
			Vector2D p2To = new Vector2D (to.x + to.width, to.y + to.height);
			double lerpAmt = (double)(elapsedTime) / moveTime;
			p1From.lerp (p1To, lerpAmt);
			p2From.lerp (p2To, lerpAmt);
			g.drawRect ((int)p1From.x, (int)p1From.y, (int)(p2From.x - p1From.x), (int)(p2From.y - p1From.y));
		} else {
			g.drawRect (from.x, from.y, from.width, from.height);
		}
	}
	
}
