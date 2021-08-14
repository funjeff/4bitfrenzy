package menu;

import engine.GameObject;

public class ObjectComponite extends MenuComponite {
	GameObject g;
	
		public ObjectComponite (GameObject g, Menu menu) {
			super (g.hitbox().width, g.hitbox().height, menu);
			this.g = g;
		
		}
		
		public ObjectComponite (int width, int height, GameObject g, Menu menu) {
			super (width, height, menu);			
			this.g = g;
			
		}
		
		@Override
		public void compointeFrame () {
			g.frameEvent();
		}
		
		@Override
		public void draw () {
			g.setX(this.getX());
			g.setY(this.getY());
			
			g.draw();
		}
}
