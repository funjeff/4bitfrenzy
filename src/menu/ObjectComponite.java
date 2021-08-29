package menu;

import java.util.ArrayList;

import engine.GameObject;

public class ObjectComponite extends MenuComponite {
	GameObject g;
	boolean constantDimentions = false;
	
		public ObjectComponite (GameObject g, Menu menu) {
			super (g.hitbox().width, g.hitbox().height, menu);
			this.g = g;
		
		}
		public ObjectComponite (int width, int height, GameObject g, Menu menu) {
			super (width, height, menu);			
			constantDimentions = true;
			this.g = g;
			
		}
		
		
		@Override
		public void compointeFrame () {
			g.frameEvent();
			if (!constantDimentions) {
				if (this.getWidth() != g.hitbox().width) {
					this.setWidth(g.hitbox().width);
				}
				if (this.getHeight() != g.hitbox().height) {
					this.setHeight(g.hitbox().height);
				}
			}
		}
		
		@Override
		public ArrayList <GameObject> getAllObjs (){
			ArrayList<GameObject> obj = new ArrayList <GameObject> ();
			obj.add(g);
			return obj;
		}
		
		@Override
		public void draw () {
			g.setX(this.getX());
			g.setY(this.getY());
			
			g.draw();
		}
}
