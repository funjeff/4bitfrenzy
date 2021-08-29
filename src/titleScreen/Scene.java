package titleScreen;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import engine.GameObject;
import engine.Sprite;

public class Scene extends GameObject {
	
	public ArrayList<SceneObject> objs;
	public HashMap<String, SceneObject> sceneObjMap;
	
	private Scanner s;
	private ArrayList<String> frame;
	private int repeatCount;
	boolean playing = false;
	
	public Scene (String path) {
		File f = new File (path);
		objs = new ArrayList<SceneObject> ();
		sceneObjMap = new HashMap<String, SceneObject> ();
		try {
			s = new Scanner (f);
			while (s.hasNextLine ()) {
				String curr = s.nextLine ();
				if (curr.equals ("END")) {
					break;
				} else {
					Scanner s2 = new Scanner (curr);
					String objName = s2.next ();
					int objX = s2.nextInt ();
					int objY = s2.nextInt ();
					String objIcon = s2.next ();
					Sprite objSpr = new Sprite (objIcon);
					SceneObject obj = new SceneObject (objSpr, objX, objY, 0);
					objs.add (obj);
					sceneObjMap.put (objName, obj);
					s2.close();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setRenderPriority (420);
		
	}
	
	@Override
	public void frameEvent () {
		if (!s.hasNextLine () && repeatCount == 0) {
			playing = false;
			forget ();
		}
		if (repeatCount == 0) {
			frame = new ArrayList<String> ();
			while (s.hasNextLine ()) {
				String curr = s.nextLine ();
				Scanner s2 = new Scanner (curr);
				if (curr.length () >= 5 && s2.next ().equals ("FRAME")) {
					repeatCount = s2.nextInt ();
					break;
				} else {
					frame.add (curr);
				}
				s2.close ();
			}
		}
		for (int i = 0; i < frame.size (); i++) {
			String curr = frame.get (i);
			Scanner s2 = new Scanner (curr);
			String objName = s2.next ();
			int objX = s2.nextInt ();
			int objY = s2.nextInt ();
			sceneObjMap.get (objName).x += objX;
			sceneObjMap.get (objName).y += objY;
			s2.close ();
		}
		if (repeatCount > 0) {
			repeatCount--;
		}
	}
	
	@Override
	public void draw () {
		for (int i = 0; i < objs.size (); i++) {
			objs.get (i).draw ();
		}
	}
	public void play () {
		playing = true;
	}
	public void stop () {
		playing = false;
	}
	public boolean isPlaying () {
		return playing;
	}
	public class SceneObject {
		
		public double x;
		public double y;
		public Sprite sprite;
		public int frame;
		
		public SceneObject (Sprite spr, int x, int y, int frame) {
			sprite = spr;
			this.x = x;
			this.y = y;
			this.frame = frame;
		}
		
		public void draw () {
			sprite.draw ((int)x + getDrawX (), (int)y + getDrawY (), frame);
		}
		
		@Override
		public String toString () {
			return "[" + x + ", " + y + "]";
		}
		
	}
	
}
