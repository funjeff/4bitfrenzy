package map;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;

public class Roome extends GameObject {
	public static Roome [][] map = new Roome [10][10];
	
	
	//true = open false = closed
	boolean topJunction;
	boolean bottomJunction;
	boolean leftJunction;
	boolean rightJunction;
	
	int roomPosX; // the location of the room in the map array (x coordinate)
	int roomPosY; // the location of the room in the map array (y coordinate)
	
	
	
	public Roome ()
	{
		this.setSprite(new Sprite ("resources/sprites/room.png"));
	}
	
	
	public boolean inRoom (double x, double y) {
		if (x > (roomPosX * 1080) && x < (roomPosX * 1080) + 1080 && y > (roomPosY * 720) && y < (roomPosY * 720) + 720) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * gets the room that an object in this location would be in
	 * @param x x coordiate of object
	 * @param y y coordinate of object
	 * @return the room that that object is in
	 */
	public static Roome getRoom (double x, double y) {
		return map [(int)(y/720)][(int) (x/1080)];
	}
	public static void generateMap () {
		for (int wy = 0; wy < map.length; wy++) {
			for (int wx = 0; wx < map[0].length; wx++) {
				Roome working = new Roome ();
				
				Random rand = new Random ();
				
				working.bottomJunction = rand.nextDouble () > .4;
				working.rightJunction = rand.nextDouble () > .4;
				
		
				if (wy != 0) {
					working.topJunction = map[wy-1][wx].bottomJunction;
				}
				if (wx != 0) {
					working.leftJunction = map[wy][wx -1].rightJunction;
				}
				
				if (wx == 0) {
					working.leftJunction = false;
				}
				if (wx == map.length - 1) {
					working.rightJunction = false;
				}
				if (wy == 0) {
					working.topJunction = false;
				}
				if (wy == map[0].length - 1) {
					working.bottomJunction = false;
				}
				
				working.roomPosX = wx;
				working.roomPosY = wy;
				
				working.declare(wx * 1080, wy * 720);
				
				map[wy][wx] = working;
				
			}
		}
		areAllAccessable ();
	}
	
	public static boolean areAllAccessable () {
		
		HashMap<Point, Boolean> accessables = new HashMap<Point, Boolean> ();
		checkAccessable (0, 0, accessables);
		return true;
		
	}
	
	private static boolean checkAccessable (int x, int y, HashMap<Point,Boolean> map) {
		
		int[] xoffs = {0, 0, -1, 1};
		int[] yoffs = {-1, 1, 0, 0};
		
		for (int i = 0; i < 4; i++) {
			int px = x + xoffs [i];
			int py = y + yoffs [i];
			if (px < 0 || py < 0 || px >= 10 || py >= 10) {
				//
			} else {
				if (map.containsKey (new Point (px, py))) {
					//
				} else {
					map.put (new Point (px, py), null);
					checkAccessable (px, py, map);
					//
				}
			}
		}
		return false;
		
	}
	
	public static void draw (Graphics g) {
		
		for (int wy = 0; wy < map.length; wy++) {
			for (int wx = 0; wx < map[0].length; wx++) {
				int sx = wx * 16 + 256;
				int sy = wy * 16 + 256;
				Roome r = map [wy][wx];
				if (!r.topJunction) g.drawLine (sx + 2, sy + 2, sx + 12, sy + 2);
				if (!r.leftJunction) g.drawLine (sx + 2, sy + 2, sx + 2, sy + 12);
				if (!r.bottomJunction) g.drawLine (sx + 12, sy + 12, sx + 2, sy + 12);
				if (!r.rightJunction) g.drawLine (sx + 12, sy + 12, sx + 12, sy + 2);
			}
		}
	
	}
	@Override
	public boolean isColliding (GameObject obj) {
		Rectangle objHitbox = new Rectangle (obj.hitbox());
		
		objHitbox.x = objHitbox.x - (1080 * roomPosX);
		objHitbox.y = objHitbox.y -  (720 * roomPosY);
		
		
		int displacedX = GameCode.getViewX() - (1080 * roomPosX);
		int displacedY = GameCode.getViewY() - (720 * roomPosY);
		
		displacedX = displacedX * -1;
		displacedY = displacedY * -1;
		
		objHitbox.x = objHitbox.x + displacedX;
		objHitbox.y = objHitbox.y + displacedY;
		
		System.out.println("x " + displacedX);
		System.out.println("y " + displacedY);
		
	
		Rectangle rect1 = new Rectangle (0 + displacedX, 0 + displacedY, 432, 144);
		Rectangle rect2 = new Rectangle (0 + displacedX,144 + displacedY,216,108);
		Rectangle rect3 = new Rectangle (648 + displacedX,0 + displacedY,432,144);
		Rectangle rect4 = new Rectangle (864 + displacedX,144 + displacedY,216,108);
		Rectangle rect5 = new Rectangle ( 0 + displacedX,468 + displacedY,216,108);
		Rectangle rect6 = new Rectangle (0 + displacedX,576 + displacedY,432,144);
		Rectangle rect7 = new Rectangle (864 + displacedX,468 + displacedY,216,108);
		Rectangle rect8 = new Rectangle ( 648 + displacedX,576 + displacedY,432,144);
		
		Rectangle rect9 = new Rectangle (432 + displacedX,0 + displacedY,216,144);
		Rectangle rect10 = new Rectangle (0 + displacedX,252 + displacedY, 216, 216);
		Rectangle rect11 = new Rectangle (864 + displacedX, 252 + displacedY, 216, 216);
		Rectangle rect12 = new Rectangle (432 + displacedX,576 + displacedY,216,144);
		
	Graphics2D grapics =(Graphics2D) RenderLoop.wind.getBufferGraphics();
		
		grapics.drawRect(rect1.x,rect1.y,rect1.width,rect1.height);
		grapics.drawRect(rect2.x,rect2.y,rect2.width,rect2.height);
		grapics.drawRect(rect3.x,rect3.y,rect3.width,rect3.height);
		grapics.drawRect(rect4.x,rect4.y,rect4.width,rect4.height);
		grapics.drawRect(rect5.x,rect5.y,rect5.width,rect5.height);
		grapics.drawRect(rect6.x,rect6.y,rect6.width,rect6.height);
		grapics.drawRect(rect7.x,rect7.y,rect7.width,rect7.height);
		grapics.drawRect(rect8.x,rect8.y,rect8.width,rect8.height);
		grapics.drawRect(rect9.x,rect9.y,rect9.width,rect9.height);
		grapics.drawRect(rect10.x,rect10.y,rect10.width,rect10.height);
		grapics.drawRect(rect11.x,rect11.y,rect11.width,rect11.height);
		grapics.drawRect(rect12.x,rect12.y,rect12.width,rect12.height);
		
		grapics.drawRect(objHitbox.x, objHitbox.y, objHitbox.width, objHitbox.height);
	
		if ((!objHitbox.intersects(rect12) || bottomJunction) && (!objHitbox.intersects(rect11) || rightJunction) && (!objHitbox.intersects(rect10) || leftJunction) && (!objHitbox.intersects(rect9) || topJunction) && !objHitbox.intersects(rect8) && !objHitbox.intersects(rect7) && !objHitbox.intersects(rect6) && !objHitbox.intersects(rect5) && !objHitbox.intersects(rect4) && !objHitbox.intersects(rect3) && !objHitbox.intersects(rect2) && !objHitbox.intersects(rect1)) {
			return false;
		} else {
			return true;
		}
		
	}
}
