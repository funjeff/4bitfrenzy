package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Stream;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.RenderLoop;
import engine.Sprite;
import gameObjects.DataSlot;
import gameObjects.PixelBitch;
import gameObjects.Register;
import items.Bombs;
import items.DataScrambler;
import items.Glue;
import items.Speed;
import items.Teleporter;
import network.NetworkHandler;
import resources.Textbox;

public class Roome extends GameObject {
	public static Roome [][] map = new Roome [10][10];
	public static HashMap<Point, Integer> distMap;
	
	
	//true = open false = closed
	boolean topJunction;
	boolean bottomJunction;
	boolean leftJunction;
	boolean rightJunction;
	
	int roomPosX; // the location of the room in the map array (x coordinate)
	int roomPosY; // the location of the room in the map array (y coordinate)
	
	int id;
	int color;
	
	boolean inRoomcollsions;
	
	Textbox [] boxes = new Textbox [12];
	
	public Register r = null;
	public DataSlot ds = null;
	
	public PixelBitch biatch;
	
	
	
	public Roome ()
	{
		
	}
	public void init (int id, int colorNum) {
		
		if (biatch == null) {
			
			Random rand = new Random ();
			
			
			String toUse = "";
			
			

			int lineNum = id; // thers probably a more elegant way for me to do this but I can't think of it so I just put the number of lines here
			this.id = id;

			
			//thanks stack overflow :)
			try (Stream<String> lines = Files.lines(Paths.get("resources/sprites/config/rooms.txt"))) {
				
			    toUse = lines.skip(lineNum).findFirst().get();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			this.setSprite(new Sprite (toUse));
			
			biatch = new PixelBitch (216 + this.getX(),144 + this.getY(),648,432,this.getSprite().getFrame(0).getSubimage(216, 144, 648, 432));
			
			
				if (rand.nextInt(10) == 4) {
					
					int [] spawnCoords = biatch.getPosibleCoords(32, 32);
					
					switch (rand.nextInt(5)) {
					case 0:
						Glue glue = new Glue ();
						glue.declare(spawnCoords[0], spawnCoords[1]);
						break;
					case 1:
						Bombs bombs = new Bombs ();
						bombs.declare(spawnCoords[0], spawnCoords[1]);
						break;
					case 2:
						Speed speed = new Speed ();
						speed.declare(spawnCoords[0], spawnCoords[1]);
						break;
					case 3:
						Teleporter tele = new Teleporter ();
						tele.declare(spawnCoords[0], spawnCoords[1]);
						break;
					case 4:
						DataScrambler scrambler = new DataScrambler ();
						scrambler.declare(spawnCoords[0], spawnCoords[1]);
						break;
					}
							
					
					
				}
			
			
				if (rand.nextInt(20) == 13) {
				
				int memNum = rand.nextInt(256);
				
				Register r = new Register(memNum);
				
				int [] spawnPoint = biatch.getPosibleCoords(r.hitbox().width, r.hitbox().height);
				
				r.declare(spawnPoint[0],spawnPoint[1]);
				
				this.r = r;
				
				
				
				int xCoord = rand.nextInt(3);
				
				if (rand.nextBoolean()) {
					xCoord = xCoord * -1;
				}
				
				xCoord = roomPosX + xCoord;
				
				
				int yCoord = rand.nextInt(3);
				
				if (rand.nextBoolean()) {
					yCoord = yCoord * -1;
				}
				
				yCoord = roomPosY + yCoord;
				
				if (xCoord > 9) {
					xCoord = 9;
				}
				
				if (xCoord < 0) {
					xCoord = 0;
				}
				if (yCoord > 9) {
					yCoord = 9;
				}
				
				if (yCoord < 0) {
					yCoord = 0;
				}
				
				Roome dataRoom = map [xCoord][yCoord];
				
	
				
				if (dataRoom.biatch == null) {
					dataRoom.init(1, 1); //TODO change this! it NEEDS to be changed
				}
				
				
				DataSlot ds = new DataSlot (memNum);
				
				int [] otherPoint = dataRoom.biatch.getPosibleCoords(ds.hitbox().width, ds.hitbox().height);
				
				
				ds.declare(otherPoint[0],otherPoint[1]);
				
				dataRoom.ds = ds;
			}
			
			
			
			boxes[0] = new Textbox("");
			boxes[0].changeBoxVisability();
			boxes[0].changeWidth(432/16);
			boxes[0].changeHeight(144/16);
			boxes[0].declare((int)this.getX(), (int)this.getY());
			
			boxes[1] = new Textbox("");
			boxes[1].changeBoxVisability();
			boxes[1].changeWidth(216/16);
			boxes[1].changeHeight(108/16);
			boxes[1].declare((int)this.getX(), (int)this.getY() + 144);
			
			
			boxes[2] = new Textbox("");
			boxes[2].changeBoxVisability();
			boxes[2].changeWidth(432/16);
			boxes[2].changeHeight(144/16);
			boxes[2].declare((int)this.getX() + 648, (int)this.getY());
			
			boxes[3] = new Textbox("");
			boxes[3].changeBoxVisability();
			boxes[3].changeWidth(216/16);
			boxes[3].changeHeight(108/16);
			boxes[3].declare((int)this.getX() + 864, (int)this.getY() + 144);
			
			boxes[4] = new Textbox("");
			boxes[4].changeBoxVisability();
			boxes[4].changeWidth(216/16);
			boxes[4].changeHeight(108/16);
			boxes[4].declare((int)this.getX(), (int)this.getY() + 468);
			
			boxes[5] = new Textbox("");
			boxes[5].changeBoxVisability();
			boxes[5].changeWidth(432/16);
			boxes[5].changeHeight(144/16);
			boxes[5].declare((int)this.getX(), (int)this.getY() + 576);
			
			boxes[6] = new Textbox("");
			boxes[6].changeBoxVisability();
			boxes[6].changeWidth(216/16);
			boxes[6].changeHeight(108/16);
			boxes[6].declare((int)this.getX() + 864, (int)this.getY() + 468);
			
			boxes[7] = new Textbox("");
			boxes[7].changeBoxVisability();
			boxes[7].changeWidth(432/16);
			boxes[7].changeHeight(144/16);
			boxes[7].declare((int)this.getX() + 648, (int)this.getY() + 576);
			
			boxes[8] = new Textbox("");
			boxes[8].changeBoxVisability();
			boxes[8].changeWidth(216/16);
			boxes[8].changeHeight(144/16);
			
			
			if (!topJunction) {
				boxes[8].declare((int)this.getX() + 432, (int)this.getY());
			}
			
			boxes[9] = new Textbox("");
			boxes[9].changeBoxVisability();
			boxes[9].changeWidth(216/16);
			boxes[9].changeHeight(216/16);
			
			if (!leftJunction) {
				boxes[9].declare((int)this.getX(), (int)this.getY() + 252);
			}
			
			boxes[10] = new Textbox("");
			boxes[10].changeBoxVisability();
			boxes[10].changeWidth(216/16);
			boxes[10].changeHeight(216/16);
			if (!rightJunction) {
				boxes[10].declare((int)this.getX() + 864, (int)this.getY() + 252);
			}
			
			boxes[11] = new Textbox("");
			boxes[11].changeBoxVisability();
			boxes[11].changeWidth(216/16);
			boxes[11].changeHeight(144/16);
			if (!bottomJunction) {
				boxes[11].declare((int)this.getX() + 432, (int)this.getY() + 576);
			}
			
			color = colorNum;
			String color = "";
			
			//thanks stack overflow :)
			try (Stream<String> lines = Files.lines(Paths.get("resources/sprites/config/colors.txt"))) {
			  color = (lines.skip(colorNum).findFirst().get());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			for (int i = 0; i < boxes.length; i++) {
				
				String finalMessage = "";
				while (boxes[i].getSpace()/2 > finalMessage.length()) {
					int lineNum2 = rand.nextInt(315); // thers probably a more elegant way for me to do this but I can't think of it so I just put the number of lines here
					
					//thanks stack overflow :)
					try (Stream<String> lines = Files.lines(Paths.get("resources/sprites/config/code.txt"))) {
					    finalMessage = finalMessage + " " + lines.skip(lineNum2).findFirst().get();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				boxes[i].changeText(finalMessage.toUpperCase());
				boxes[i].setFont(color);
				
			}
		}
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
		do {
			ArrayList <GameObject> oldRooms = ObjectHandler.getObjectsByName("Roome");
			if (oldRooms != null) {
				while (!oldRooms.isEmpty() && (oldRooms.get (0) != null)) {
					oldRooms.get(0).forget();
				}
			}

			
			for (int wy = 0; wy < map.length; wy++) {
				for (int wx = 0; wx < map[0].length; wx++) {
					Roome working = new Roome ();
					
					Random rand = new Random ();
					
					working.bottomJunction = rand.nextDouble () > .45;
					working.rightJunction = rand.nextDouble () > .45;
					
			
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
		} while (!areAllAccessable ());
		
		Random r = new Random ();
		ArrayList <GameObject> finalRooms = ObjectHandler.getObjectsByName("Roome");
		for (int i = 0; i < finalRooms.size(); i++) {
			Roome working = (Roome)finalRooms.get(i);
			if (working != null) {
				working.init(r.nextInt (12), r.nextInt (5));
			}
		}

	}
	
	public static void loadMap (String mapString) {
		
		ArrayList <GameObject> oldRooms = ObjectHandler.getObjectsByName("Roome");
		if (oldRooms != null) {
			while (!oldRooms.isEmpty() && (oldRooms.get (0) != null)) {
				oldRooms.get(0).forget();
			}
		}
		oldRooms = ObjectHandler.getObjectsByName("Textbox");
		if (oldRooms != null) {
			while (!oldRooms.isEmpty() && (oldRooms.get (0) != null)) {
				oldRooms.get(0).forget();
			}
		}
		String[] roomStrings = mapString.split (",");
		
		int [] ids = new int [100];
		int [] colors = new int [100];
		
		for (int i = 0; i < 100; i++) {
			String curr = roomStrings[i];
			Roome r = new Roome ();
			r.declare ();
			r.topJunction = curr.charAt (0) == 'y' ? true : false;
			r.leftJunction = curr.charAt (1) == 'y' ? true : false;
			r.rightJunction = curr.charAt (2) == 'y' ? true : false;
			r.bottomJunction = curr.charAt (3) == 'y' ? true : false;
			ids[i] = Integer.parseInt (curr.substring (4, 6));
			colors[i] = Integer.parseInt (curr.substring (6, 7));
			r.setX ((i % 10) * 1080);
			r.setY ((i / 10) * 720);
			
			map [i / 10][i % 10] = r;
		}
		
		for (int i = 0; i < 100; i++) {
			Roome r = new Roome ();
			
			r = map [i / 10][i % 10];
			r.init (ids[i], colors[i]);
			r.roomPosX = (i % 10);
			r.roomPosY = (i / 10);
		}
		
		String[] objsToRemove = new String[] {};
		for (int i = 0; i < objsToRemove.length; i++) {
			oldRooms = ObjectHandler.getObjectsByName(objsToRemove[i]);
			if (oldRooms != null) {
				while (!oldRooms.isEmpty() && (oldRooms.get (0) != null)) {
					oldRooms.get(0).forget();
				}
			}
		}
		
	}
	
	public static String saveMap () {
		String val = "";
		for (int i = 0; i < 100; i++) {
			Roome r = map [i / 10][i % 10];
			if (i != 99) {
				val += r.toString () + ",";
			} else {
				val += r.toString ();
			}
		}
		return val;
	}
	
	@Override
	public String toString () {
		String val = "";
		val += topJunction ? 'y' : 'n';
		val += leftJunction ? 'y' : 'n';
		val += rightJunction ? 'y' : 'n';
		val += bottomJunction ? 'y' : 'n';
		val += id >= 10 ? String.valueOf (id) : "0" + String.valueOf (id);
		val += String.valueOf (color);
		return val;
	}
	
	public static boolean areAllAccessable () {
		
		HashMap<Point, Integer> accessables = new HashMap<Point, Integer> ();
		fillDistMap (5, 5, accessables);
		if (accessables.size () == 100) {
			distMap = accessables;
			return true;
		}
		return false;
		
	}
	
	private static void fillDistMap (int x, int y, HashMap<Point,Integer> visitMap) {
		
		int[] xoffs = {0, 0, -1, 1};
		int[] yoffs = {-1, 1, 0, 0}; //Top, Bottom, Left, Right
		
		LinkedList<Point> toCheck = new LinkedList<Point> ();
		toCheck.add (new Point (x, y));
		visitMap.put (new Point (x, y), 0);
		
		while (toCheck.size () != 0) {
			int dist = visitMap.get (toCheck.getLast ());
			int cx = toCheck.getLast ().x;
			int cy = toCheck.getLast ().y;
			for (int i = 0; i < 4; i++) {
				int px = cx + xoffs [i];
				int py = cy + yoffs [i];
				Roome r = map [cy][cx];
				if (px < 0 || py < 0 || px >= 10 || py >= 10) {
					//
				} else {
					boolean canVisit = false;
					if (i == 0 && r.topJunction) {canVisit = true;}
					if (i == 1 && r.bottomJunction) {canVisit = true;}
					if (i == 2 && r.leftJunction) {canVisit = true;}
					if (i == 3 && r.rightJunction) {canVisit = true;}
					if (canVisit) {
						Point pt = new Point (px, py);
						if (!visitMap.containsKey (pt)) {
							visitMap.put (pt, dist + 1);
							toCheck.addFirst (pt);
						}
					}
				}
			}
			toCheck.removeLast ();
		}
		
	}

	public static void draw (Graphics g) {
		Color green = new Color (0x00FF00);
		Color white = new Color (0xFFFFFFF);
		for (int wy = 0; wy < map.length; wy++) {
			for (int wx = 0; wx < map[0].length; wx++) {
				int sx = wx * 16 + 256;
				int sy = wy * 16 + 256;
				Roome r = map [wy][wx];
				g.setColor (white);
				if (!r.topJunction) g.drawLine (sx + 2, sy + 2, sx + 12, sy + 2);
				if (!r.leftJunction) g.drawLine (sx + 2, sy + 2, sx + 2, sy + 12);
				if (!r.bottomJunction) g.drawLine (sx + 12, sy + 12, sx + 2, sy + 12);
				if (!r.rightJunction) g.drawLine (sx + 12, sy + 12, sx + 12, sy + 2);
				g.setColor (green);
				//g.drawBytes (new byte[]{(byte) ('A' + distMap.get (new Point (wx, wy)))}, 0, 1, sx + 4, sy + 12);
			}
		}
	
	}
	@Override
	public void draw () {
		super.draw();
		
//		int displacedX = GameCode.getViewX() - (1080 * roomPosX);
//		int displacedY = GameCode.getViewY() - (720 * roomPosY);
//		
//		displacedX = displacedX * -1;
//		displacedY = displacedY * -1;
//		
//		
//		Rectangle rect1 = new Rectangle (0 + displacedX, 0 + displacedY, 432, 144);
//		Rectangle rect2 = new Rectangle (0 + displacedX,144 + displacedY,216,108);
//		Rectangle rect3 = new Rectangle (648 + displacedX,0 + displacedY,432,144);
//		Rectangle rect4 = new Rectangle (864 + displacedX,144 + displacedY,216,108);
//		Rectangle rect5 = new Rectangle ( 0 + displacedX,468 + displacedY,216,108);
//		Rectangle rect6 = new Rectangle (0 + displacedX,576 + displacedY,432,144);
//		Rectangle rect7 = new Rectangle (864 + displacedX,468 + displacedY,216,108);
//		Rectangle rect8 = new Rectangle ( 648 + displacedX,576 + displacedY,432,144);
//		
//		Rectangle rect9 = new Rectangle (432 + displacedX,0 + displacedY,216,144);
//		Rectangle rect10 = new Rectangle (0 + displacedX,252 + displacedY, 216, 216);
//		Rectangle rect11 = new Rectangle (864 + displacedX, 252 + displacedY, 216, 216);
//		Rectangle rect12 = new Rectangle (432 + displacedX,576 + displacedY,216,144);
//		
//	Graphics2D grapics =(Graphics2D) RenderLoop.wind.getBufferGraphics();
//		
//		grapics.setColor(new Color (0x5afa48));
//	
//		grapics.drawRect(rect1.x,rect1.y,rect1.width,rect1.height);
//		grapics.drawRect(rect2.x,rect2.y,rect2.width,rect2.height);
//		grapics.drawRect(rect3.x,rect3.y,rect3.width,rect3.height);
//		grapics.drawRect(rect4.x,rect4.y,rect4.width,rect4.height);
//		grapics.drawRect(rect5.x,rect5.y,rect5.width,rect5.height);
//		grapics.drawRect(rect6.x,rect6.y,rect6.width,rect6.height);
//		grapics.drawRect(rect7.x,rect7.y,rect7.width,rect7.height);
//		grapics.drawRect(rect8.x,rect8.y,rect8.width,rect8.height);
//		if (!topJunction) {
//			grapics.drawRect(rect9.x,rect9.y,rect9.width,rect9.height);
//		}
//		if (!leftJunction) {
//			grapics.drawRect(rect10.x,rect10.y,rect10.width,rect10.height);
//		}
//		if (!rightJunction) {
//			grapics.drawRect(rect11.x,rect11.y,rect11.width,rect11.height);
//		}
//		if (!bottomJunction) {
//			grapics.drawRect(rect12.x,rect12.y,rect12.width,rect12.height);
//		}
//	
	}
	public void destroyTopWall() {
		
		try {
			Roome nextRoom = map[roomPosY - 1][roomPosX];
			nextRoom.bottomJunction = true;
			nextRoom.boxes[11].forget();
			
		} catch (IndexOutOfBoundsException e) {
				
			return;
			
		}
		topJunction = true;
		boxes[8].forget();
		
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("destroy top: " + roomPosX + " " + roomPosY);
		}
	}
	public void destroyBottomWall() {
		try {
			Roome nextRoom = map[roomPosY + 1][roomPosX];
			nextRoom.topJunction = true;
			nextRoom.boxes[8].forget();
			
		} catch (IndexOutOfBoundsException e) {
				
			return;
			
		}
		bottomJunction = true;
		boxes[11].forget();
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("destroy bottom: " + roomPosX + " " + roomPosY);
		}
	}
	public void destroyRightWall() {
		try {
			Roome nextRoom = map[roomPosY][roomPosX + 1];
			nextRoom.leftJunction = true;
			nextRoom.boxes[9].forget();
			
		} catch (IndexOutOfBoundsException e) {
				
			return;
			
		}
		rightJunction = true;
		boxes[10].forget();
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("destroy right: " + roomPosX + " " + roomPosY);
		}
	}
	public void destroyLeftWall() {
		try {
			Roome nextRoom = map[roomPosY][roomPosX - 1];
			nextRoom.rightJunction = true;
			nextRoom.boxes[10].forget();
			
		} catch (IndexOutOfBoundsException e) {
				
			return;
			
		}
		
		leftJunction = true;
		boxes[9].forget();
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("destroy left: " + roomPosX + " " + roomPosY);
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
		
		
			if (biatch.isColliding(obj)) {
				return true;
			}
//	Graphics2D grapics =(Graphics2D) RenderLoop.wind.getBufferGraphics();
//		
//		grapics.drawRect(rect1.x,rect1.y,rect1.width,rect1.height);
//		grapics.drawRect(rect2.x,rect2.y,rect2.width,rect2.height);
//		grapics.drawRect(rect3.x,rect3.y,rect3.width,rect3.height);
//		grapics.drawRect(rect4.x,rect4.y,rect4.width,rect4.height);
//		grapics.drawRect(rect5.x,rect5.y,rect5.width,rect5.height);
//		grapics.drawRect(rect6.x,rect6.y,rect6.width,rect6.height);
//		grapics.drawRect(rect7.x,rect7.y,rect7.width,rect7.height);
//		grapics.drawRect(rect8.x,rect8.y,rect8.width,rect8.height);
//		grapics.drawRect(rect9.x,rect9.y,rect9.width,rect9.height);
//		grapics.drawRect(rect10.x,rect10.y,rect10.width,rect10.height);
//		grapics.drawRect(rect11.x,rect11.y,rect11.width,rect11.height);
//		grapics.drawRect(rect12.x,rect12.y,rect12.width,rect12.height);
//		
//		grapics.drawRect(objHitbox.x, objHitbox.y, objHitbox.width, objHitbox.height);
//	
		if ((!objHitbox.intersects(rect12) || bottomJunction) && (!objHitbox.intersects(rect11) || rightJunction) && (!objHitbox.intersects(rect10) || leftJunction) && (!objHitbox.intersects(rect9) || topJunction) && !objHitbox.intersects(rect8) && !objHitbox.intersects(rect7) && !objHitbox.intersects(rect6) && !objHitbox.intersects(rect5) && !objHitbox.intersects(rect4) && !objHitbox.intersects(rect3) && !objHitbox.intersects(rect2) && !objHitbox.intersects(rect1)) {
			return false;
		} else {
			return true;
		}
		
	}
}
