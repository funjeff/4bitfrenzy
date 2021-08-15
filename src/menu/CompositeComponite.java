package menu;

import java.util.ArrayList;

import engine.GameObject;

public class CompositeComponite extends MenuComponite {

	ArrayList <MenuComponite> componites;
	
	
	public CompositeComponite (MenuComponite left, MenuComponite right, Menu m) {
		super(left.width + right.width,getLargerHeight(left.getHeight(), right.getHeight()), m);
		componites = new ArrayList <MenuComponite> ();
		componites.add(left);
		componites.add(right);
	}
	
	public CompositeComponite (ArrayList <MenuComponite> compointes, Menu m) {
		super(getWidth(compointes),getLargestHeight(compointes), m);
		componites = compointes;
	}
	
	
	private static int getLargerHeight (int left, int right) {
		if (left>right) {
			return left;
		} else {
			return right;
		}
	}
	
	private static int getLargestHeight (ArrayList <MenuComponite> comp) {
		int height = 0;
		
		for (int i = 0; i < comp.size(); i++) {
			if (comp.get(i).getHeight() > height) {
				height = comp.get(i).getHeight();
			}
		}
		return height;
	}
	
	private static int getWidth (ArrayList <MenuComponite> componites) {
		int width = 0;
		for (int i = 0; i < componites.size(); i++) {
			width = width + componites.get(i).width;
		}
		return width;
	}
	
	@Override
	public void compointeFrame () {
		
		for (int i = 0; i < componites.size(); i++) {
			componites.get(i).compointeFrame();
		}
		
		if (this.getWidth() != getWidth (componites)) {
			this.setWidth(getWidth (componites));
		}
		
		int height = getLargestHeight(componites);
		
		
		if (this.getHeight() != height) {
			this.setHeight(height);
		}
		
	}
	
	@Override
	public ArrayList <GameObject> getAllObjs(){
		ArrayList <GameObject> returnArray = new ArrayList <GameObject> ();
		
		for (int i = 0; i < componites.size(); i++) {
			returnArray.addAll(componites.get(i).getAllObjs());
		}
		return returnArray;
	}
	
	@Override
	public void draw () {
		
		int iterativeWidth = 0; 
		
		for (int i =0; i < componites.size(); i++) {
			
			componites.get(i).setX(this.getX() + iterativeWidth);
			componites.get(i).setY(this.getY());
			componites.get(i).draw();
			iterativeWidth = iterativeWidth + componites.get(i).getWidth();
		}
	}
}
