package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
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
import npcs.Dirt;
import npcs.NPC;
import resources.Textbox;

public class Roome extends GameObject {
	public static Roome [][] map;
	public static HashMap<Point, Integer> distMap;
	
	private static int mapWidth = 10;
	private static int mapHeight = 10;
	
	private static HashMap<Point, HashMap<Point, Integer>> distMaps;
	
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
	
	GameObject [] walls = new GameObject [12];
	
	public Register r = null;
	public DataSlot ds = null;
	
	private PixelBitch spawningBitch;
	private PixelBitch collisionBitch;
	
	public static ArrayList<String> roomData;
	public static ArrayList<Integer> roomPool;
	public static ArrayList<String> codeWallLines;
	public static ArrayList<String> codeWallColors;
	public static HashMap<Integer, ArrayList<Roome>> roomeIdMap;
	
	public Roome ()
	{
		
	}
	
	public static ArrayList<String> getRoomeData () {
		
		//Init the room data if null
		if (roomData == null) {
			roomData = new ArrayList<String> ();
			File f = new File ("resources/sprites/config/rooms.txt");
			Scanner s;
			try {
				s = new Scanner (f);
				while (s.hasNextLine ()) {
					roomData.add (s.nextLine ());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Get the room data
		return roomData;
		
	}
	
	public static int rollRoomeId () {
		ArrayList<Integer> ids = getRoomeIdPool ();
		int idx = (int)(Math.random () * ids.size ());
		return ids.get (idx);
	}
	
	public static ArrayList<Integer> getRoomeIdPool () {
		
		//Get the room data
		ArrayList<String> roomDat = getRoomeData ();
		
		//Make the room pool if it's not yet made
		if (roomPool == null) {
			roomPool = new ArrayList<Integer> ();
			for (int i = 0; i < roomDat.size (); i++) {
				Scanner s = new Scanner (roomDat.get (i));
				s.next ();
				int amt = s.nextInt ();
				for (int j = 0; j < amt; j++) {
					roomPool.add (i);
				}
			}
		}
		
		//Return the room pool
		return roomPool;
		
	}
	
	public static void populateRoomes () {
		
		Random rand = new Random ();
		
		if (roomData == null) {
			getRoomeData (); //Populate roomData if it's currently null
		}
		
		Scanner s1 = null;
		Scanner s2 = null;
		Scanner s3 = null;
		
		HashMap<Integer, ArrayList<Roome>> roomeMap;
		
		for (int wx = 0; wx < mapWidth; wx++) {
			for (int wy = 0; wy < mapHeight; wy++) {
				
				//Get the roome data
				Roome r = map[wy][wx];
				String dat = roomData.get (r.id);
				s1 = new Scanner (dat);
				s1.next ();
				s1.next ();
				
				//Find the object file and make the object
				if (s1.hasNext ()) {
					File f2 = new File (s1.next ());
					try {
						s2 = new Scanner (f2);
						//Scan through the object list
						while (s2.hasNextLine ()) {
							//Scan through the object text and create the appropraite object
							s3 = new Scanner (s2.nextLine ());
							String objType = s3.next ();
							double objX = r.getX () + s3.nextInt ();
							double objY = r.getY () + s3.nextInt ();
							try {
								Class.forName ("npcs." + objType).getConstructor (Double.TYPE, Double.TYPE).newInstance (objX, objY);
							} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
									| InvocationTargetException | NoSuchMethodException | SecurityException
									| ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				try {
					s1.close ();
					s2.close ();
					s3.close ();
				} catch (Exception e) {
					//Do nothing, s2-4 were never made
				}
			}
		}
		
		//Load in hardcoded objects (with special conditions)
		ArrayList<Roome> flowerRooms = getRoomeIdMap ().get (3);
		if (flowerRooms != null && flowerRooms.size () >= 1) {
			int room1 = rand.nextInt (flowerRooms.size ());
			int room2;
			do {
				room2 = rand.nextInt (flowerRooms.size ());
			} while (room1 == room2);
			int[] coords1 = flowerRooms.get (room1).spawningBitch.getPosibleCoords (31, 32);
			int[] coords2 = flowerRooms.get (room2).spawningBitch.getPosibleCoords (31, 32);
			new Dirt (coords1[0], coords1[1]);
			new Dirt (coords2[0], coords2[1]);
		}
		
	}
	
	public ArrayList<String> getCodeWallLines () {
		
		//Populate the code wall lines list if it's empty
		if (codeWallLines == null) {
			codeWallLines = new ArrayList<String> ();
			File f = new File ("resources/sprites/config/code.txt");
			Scanner s;
			try {
				s = new Scanner (f);
				while (s.hasNextLine ()) {
					codeWallLines.add (s.nextLine ());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Return the code wall lines
		return codeWallLines;
		
	}
	
	public ArrayList<String> getCodeWallColors () {
		
		//Populate the code wall colors list if it's empty
		if (codeWallColors == null) {
			codeWallColors = new ArrayList<String> ();
			File f = new File ("resources/sprites/config/colors.txt");
			Scanner s;
			try {
				s = new Scanner (f);
				while (s.hasNextLine ()) {
					codeWallColors.add (s.nextLine ());
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Return the code wall colors
		return codeWallColors;
		
	}
	
	public static HashMap<Integer, ArrayList<Roome>> getRoomeIdMap () {
		
		if (roomeIdMap == null) {
			roomeIdMap = new HashMap<Integer, ArrayList<Roome>> ();
			for (int wx = 0; wx < getMapWidth (); wx++) {
				for (int wy = 0; wy < getMapHeight (); wy++) {
					Roome r = map[wy][wx];
					ArrayList<Roome> currList = roomeIdMap.get (r.id);
					if (currList == null) {
						currList = new ArrayList<Roome> ();
						roomeIdMap.put (r.id, currList);
					}
					currList.add (r);
				}
			}
		}
		
		return roomeIdMap;
		
	}
	
	public void init (int id, int colorNum) {
		
		if (spawningBitch == null || collisionBitch == null) {
			
			Random rand = new Random ();
			
			String toUse = "";

			int lineNum = id; // thers probably a more elegant way for me to do this but I can't think of it so I just put the number of lines here
			this.id = id;

			String roomPath = getRoomeData ().get (lineNum).split (" ")[0];
			Sprite bgSprite = new Sprite (roomPath + "background.png");
			Sprite spawnMask = new Sprite (roomPath + "spawn_mask.png");
			Sprite collisionMask = new Sprite (roomPath + "collision_mask.png");

			this.setSprite (bgSprite);
			this.spawningBitch = new PixelBitch (216 + this.getX(),144 + this.getY(),648,432,spawnMask.getFrame(0).getSubimage(216, 144, 648, 432));
			this.collisionBitch = new PixelBitch (216 + this.getX(),144 + this.getY(),648,432,collisionMask.getFrame(0).getSubimage(216, 144, 648, 432)); 
			
			if (!GameCode.devMode ()) {
				walls[0] = new Textbox("");
				((Textbox)walls[0]).changeBoxVisability();
				((Textbox)walls[0]).changeWidth(432/16);
				((Textbox)walls[0]).changeHeight(144/16);
				((Textbox)walls[0]).declare((int)this.getX(), (int)this.getY());
				
				walls[1] = new Textbox("");
				((Textbox)walls[1]).changeBoxVisability();
				((Textbox)walls[1]).changeWidth(216/16);
				((Textbox)walls[1]).changeHeight(108/16);
				((Textbox)walls[1]).declare((int)this.getX(), (int)this.getY() + 144);
				
				
				walls[2] = new Textbox("");
				((Textbox)walls[2]).changeBoxVisability();
				((Textbox)walls[2]).changeWidth(432/16);
				((Textbox)walls[2]).changeHeight(144/16);
				((Textbox)walls[2]).declare((int)this.getX() + 648, (int)this.getY());
				
				walls[3] = new Textbox("");
				((Textbox)walls[3]).changeBoxVisability();
				((Textbox)walls[3]).changeWidth(216/16);
				((Textbox)walls[3]).changeHeight(108/16);
				((Textbox)walls[3]).declare((int)this.getX() + 864, (int)this.getY() + 144);
				
				walls[4] = new Textbox("");
				((Textbox)walls[4]).changeBoxVisability();
				((Textbox)walls[4]).changeWidth(216/16);
				((Textbox)walls[4]).changeHeight(108/16);
				((Textbox)walls[4]).declare((int)this.getX(), (int)this.getY() + 468);
				
				walls[5] = new Textbox("");
				((Textbox)walls[5]).changeBoxVisability();
				((Textbox)walls[5]).changeWidth(432/16);
				((Textbox)walls[5]).changeHeight(144/16);
				((Textbox)walls[5]).declare((int)this.getX(), (int)this.getY() + 576);
				
				walls[6] = new Textbox("");
				((Textbox)walls[6]).changeBoxVisability();
				((Textbox)walls[6]).changeWidth(216/16);
				((Textbox)walls[6]).changeHeight(108/16);
				((Textbox)walls[6]).declare((int)this.getX() + 864, (int)this.getY() + 468);
				
				walls[7] = new Textbox("");
				((Textbox)walls[7]).changeBoxVisability();
				((Textbox)walls[7]).changeWidth(432/16);
				((Textbox)walls[7]).changeHeight(144/16);
				((Textbox)walls[7]).declare((int)this.getX() + 648, (int)this.getY() + 576);
				
				walls[8] = new Textbox("");
				((Textbox)walls[8]).changeBoxVisability();
				((Textbox)walls[8]).changeWidth(216/16);
				((Textbox)walls[8]).changeHeight(144/16);
				
				
				if (!topJunction) {
					((Textbox)walls[8]).declare((int)this.getX() + 432, (int)this.getY());
				}
				
				walls[9] = new Textbox("");
				((Textbox)walls[9]).changeBoxVisability();
				((Textbox)walls[9]).changeWidth(216/16);
				((Textbox)walls[9]).changeHeight(216/16);
				
				if (!leftJunction) {
					((Textbox)walls[9]).declare((int)this.getX(), (int)this.getY() + 252);
				}
				
				walls[10] = new Textbox("");
				((Textbox)walls[10]).changeBoxVisability();
				((Textbox)walls[10]).changeWidth(216/16);
				((Textbox)walls[10]).changeHeight(216/16);
				if (!rightJunction) {
					((Textbox)walls[10]).declare((int)this.getX() + 864, (int)this.getY() + 252);
				}
				
				walls[11] = new Textbox("");
				((Textbox)walls[11]).changeBoxVisability();
				((Textbox)walls[11]).changeWidth(216/16);
				((Textbox)walls[11]).changeHeight(144/16);
				if (!bottomJunction) {
					((Textbox)walls[11]).declare((int)this.getX() + 432, (int)this.getY() + 576);
				}
			} else {
				walls[0] = new WallBox ((int)this.getX (), (int)this.getY (), 432, 144);
				new Ribbon ((int)getX () + 216, (int)getY () + 144, (int)getX () + 432, (int)getY () + 144);
				walls[1] = new WallBox ((int)this.getX (), (int)this.getY () + 144, 216, 108);
				walls[2] = new WallBox ((int)this.getX () + 648, (int)this.getY (), 432, 144);
				walls[3] = new WallBox ((int)this.getX () + 864, (int)this.getY () + 144, 216, 108);
				walls[4] = new WallBox ((int)this.getX (), (int)this.getY () + 468, 216, 108);
				walls[5] = new WallBox ((int)this.getX (), (int)this.getY () + 576, 432, 144);
				walls[6] = new WallBox ((int)this.getX () + 864, (int)this.getY () + 468, 216, 108);
				walls[7] = new WallBox ((int)this.getX () + 648, (int)this.getY () + 576, 432, 144);
				//Startpoints/endpoints are arranged in counter-clockwise order
				//Top
				if (!topJunction) {
					walls[8] = new WallBox ((int)this.getX () + 432, (int)this.getY (), 216, 144);
					new Ribbon ((int)getX () + 864, (int)getY () + 144, (int)getX () + 216, (int)getY () + 144);
				} else {
					new Ribbon ((int)getX () + 432, (int)getY () + 144, (int)getX () + 216, (int)getY () + 144);
					new Ribbon ((int)getX () + 864, (int)getY () + 144, (int)getX () + 648, (int)getY () + 144);
					new Ribbon ((int)getX () + 432, (int)getY () + 0, (int)getX () + 432, (int)getY () + 144);
					new Ribbon ((int)getX () + 648, (int)getY () + 144, (int)getX () + 648, (int)getY () + 0);
				}
				//Left
				if (!leftJunction) {
					walls[9] = new WallBox ((int)this.getX (), (int)this.getY () + 252, 216, 216);
					new Ribbon ((int)getX () + 216, (int)getY () + 144, (int)getX () + 216, (int)getY () + 576);
				} else {
					new Ribbon ((int)getX () + 216, (int)getY () + 144, (int)getX () + 216, (int)getY () + 252);
					new Ribbon ((int)getX () + 216, (int)getY () + 468, (int)getX () + 216, (int)getY () + 576);
					new Ribbon ((int)getX () + 216, (int)getY () + 252, (int)getX () + 0, (int)getY () + 252);
					new Ribbon ((int)getX () + 0, (int)getY () + 468, (int)getX () + 216, (int)getY () + 468);
				}
				//Right
				if (!rightJunction) {
					walls[10] = new WallBox ((int)this.getX () + 864, (int)this.getY () + 252, 216, 216);
					new Ribbon ((int)getX () + 864, (int)getY () + 576, (int)getX () + 864, (int)getY () + 144);
				} else {
					new Ribbon ((int)getX () + 864, (int)getY () + 252, (int)getX () + 864, (int)getY () + 144);
					new Ribbon ((int)getX () + 864, (int)getY () + 576, (int)getX () + 864, (int)getY () + 468);
					new Ribbon ((int)getX () + 1080, (int)getY () + 252, (int)getX () + 864, (int)getY () + 252);
					new Ribbon ((int)getX () + 864, (int)getY () + 468, (int)getX () + 1080, (int)getY () + 468);
				}
				//Bottom
				if (!bottomJunction) {
					walls[11] = new WallBox ((int)this.getX () + 432, (int)this.getY () + 576, 216, 216);
					new Ribbon ((int)getX () + 216, (int)getY () + 576, (int)getX () + 864, (int)getY () + 576);
				} else {
					new Ribbon ((int)getX () + 216, (int)getY () + 576, (int)getX () + 432, (int)getY () + 576);
					new Ribbon ((int)getX () + 648, (int)getY () + 576, (int)getX () + 864, (int)getY () + 576);
					new Ribbon ((int)getX () + 432, (int)getY () + 576, (int)getX () + 432, (int)getY () + 720);
					new Ribbon ((int)getX () + 648, (int)getY () + 720, (int)getX () + 648, (int)getY () + 576);
				}
			}
			
			color = colorNum;
			String color = "";
			
			//thanks stack overflow :)
			color = getCodeWallColors ().get (colorNum);
			for (int i = 0; i < walls.length; i++) {
				
				StringBuilder finalMessage = new StringBuilder ();
				if (walls[i] instanceof Textbox) { //Only relevant for testing\
					Textbox working = (Textbox)walls[i];
					while (working.getSpace()/2 > finalMessage.length()) {
						int lineNum2 = rand.nextInt (getCodeWallLines ().size ());
						finalMessage.append (' ');
						finalMessage.append (getCodeWallLines ().get (lineNum2));
					}
					working.changeText(finalMessage.toString ().toUpperCase());
					working.setFont(color);
				}
				
			}
			
		}
	}
	
	public static void fillDistMaps () {
		
		//Make the distance maps
		distMaps = new HashMap<Point, HashMap<Point, Integer>> ();
		
		//Fill all the maps
		for (int wy = 0; wy < mapHeight; wy++) {
			for (int wx = 0; wx < mapWidth; wx++) {
				HashMap<Point, Integer> working = new HashMap<Point, Integer> ();
				fillDistMap (wx, wy, working);
				distMaps.put (new Point (wx, wy), working);
			}
		}
		
	}
	
	public static int distBetween (Point from, Point to) {
		
		//Fill the dist maps if needed
		if (distMaps == null) {
			fillDistMaps ();
		}
		
		//Retrieve the distance
		return distMaps.get (from).get (to);
		
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
		//This consistently fails for relatively large maps (as small as 12x12) - this is related to the open corridor probability
		map = new Roome[mapHeight][mapWidth];
		do {
			ArrayList <GameObject> oldRooms = ObjectHandler.getObjectsByName("Roome");
			if (oldRooms != null) {
				while (!oldRooms.isEmpty() && (oldRooms.get (0) != null)) {
					oldRooms.get(0).forget();
				}
			}

			
			for (int wy = 0; wy < mapHeight; wy++) {
				for (int wx = 0; wx < mapWidth; wx++) {
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
					if (wx == mapWidth - 1) {
						working.rightJunction = false;
					}
					if (wy == 0) {
						working.topJunction = false;
					}
					if (wy == mapHeight - 1) {
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
				working.init(rollRoomeId (), r.nextInt (5));
			}
		}

		if (GameCode.devMode ()) {
			Ribbon.constructPath ();
		}
		
		fillDistMaps ();
		
	}
	
	public static void loadMap (String mapString) {
		String[] mapStrings = mapString.split (";");
		String[] roomStrings = mapStrings[1].split (",");
		
		String[] dims = mapStrings[0].split (",");
		mapWidth = Integer.parseInt (dims[0]);
		mapHeight = Integer.parseInt (dims[1]);
		
		if (map == null) {
			map = new Roome[mapHeight][mapWidth];
		}
		
		int [] ids = new int [mapWidth * mapHeight];
		int [] colors = new int [mapWidth * mapHeight];
		
		for (int i = 0; i < mapWidth * mapHeight; i++) {
			String curr = roomStrings[i];
			Roome r = new Roome ();
			r.declare ();
			r.topJunction = curr.charAt (0) == 'y' ? true : false;
			r.leftJunction = curr.charAt (1) == 'y' ? true : false;
			r.rightJunction = curr.charAt (2) == 'y' ? true : false;
			r.bottomJunction = curr.charAt (3) == 'y' ? true : false;
			ids[i] = Integer.parseInt (curr.substring (4, 7));
			colors[i] = Integer.parseInt (curr.substring (7, 8));
			r.setX ((i % mapWidth) * 1080);
			r.setY ((i / mapWidth) * 720);
			
			map [i / mapWidth][i % mapWidth] = r;
		}
		
		for (int i = 0; i < mapWidth * mapHeight; i++) {
			Roome r = new Roome ();
			
			r = map [i / mapWidth][i % mapWidth];
			r.init (ids[i], colors[i]);
			r.roomPosX = (i % mapWidth);
			r.roomPosY = (i / mapWidth);
		}
		
		
	}
	
	public static String saveMap () {
		String val = "";
		val += mapWidth + "," + mapHeight + ";";
		for (int i = 0; i < mapWidth * mapHeight; i++) {
			Roome r = map [i / mapWidth][i % mapWidth];
			if (i != mapWidth * mapHeight - 1) {
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
		if (id >= 100) {
			val += id;
		} else if (id >= 10) {
			val += "0" + id;
		} else {
			val += "00" + id;
		}
		val += String.valueOf (color);
		return val;
	}
	
	public static boolean areAllAccessable () {
		
		HashMap<Point, Integer> accessables = new HashMap<Point, Integer> ();
		fillDistMap (mapWidth / 2, mapHeight / 2, accessables);
		if (accessables.size () == mapWidth * mapHeight) {
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
				if (px < 0 || py < 0 || px >= map[0].length || py >= map.length) {
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
			nextRoom.walls[11].forget();
			
		} catch (IndexOutOfBoundsException e) {
				
			return;
			
		}
		topJunction = true;
		walls[8].forget();
		
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("DESTROY:top:" + roomPosX + ":" + roomPosY);
		}
	}
	public void destroyBottomWall() {
		try {
			Roome nextRoom = map[roomPosY + 1][roomPosX];
			nextRoom.topJunction = true;
			nextRoom.walls[8].forget();
			
		} catch (IndexOutOfBoundsException e) {
				
			return;
			
		}
		bottomJunction = true;
		walls[11].forget();
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("DESTROY:bottom:" + roomPosX + ":" + roomPosY);
		}
	}
	public void destroyRightWall() {
		try {
			Roome nextRoom = map[roomPosY][roomPosX + 1];
			nextRoom.leftJunction = true;
			nextRoom.walls[9].forget();
			
		} catch (IndexOutOfBoundsException e) {
				
			return;
			
		}
		rightJunction = true;
		walls[10].forget();
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("DESTROY:right:" + roomPosX + ":" + roomPosY);
		}
	}
	public void destroyLeftWall() {
		try {
			Roome nextRoom = map[roomPosY][roomPosX - 1];
			nextRoom.rightJunction = true;
			nextRoom.walls[10].forget();
			
		} catch (IndexOutOfBoundsException e) {
				
			return;
			
		}
		
		leftJunction = true;
		walls[9].forget();
		if (NetworkHandler.isHost()) {
			NetworkHandler.getServer().sendMessage("DESTROY:left:" + roomPosX + ":" + roomPosY);
		}
	}
	
	public static int getMapWidth () {
		return mapWidth;
	}
	
	public static int getMapHeight () {
		return mapHeight;
	}
	
	public static void setMapWidth (int width) {
		mapWidth = width;
	}
	
	public static void setMapHeight (int height) {
		mapHeight = height;
	}
	
	public void loadMapFromFile (String filepath) {
		
	}
	
	public PixelBitch getCollisionMask () {
		return collisionBitch;
	}
	
	public PixelBitch getSpawningMask () {
		return spawningBitch;
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
		
		//ROOMS ARE 648x432
		
		
			if (collisionBitch.isColliding(obj)) {
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
