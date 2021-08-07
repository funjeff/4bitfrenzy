package menu;

import java.util.ArrayList;

import engine.GameObject;
import engine.Sprite;
import resources.Textbox;

public class Menu extends GameObject {	
	
	ArrayList <MenuComponite> componites = new ArrayList <MenuComponite> ();
	
	//I kinda gave up on this naming scheme halfway through
	Sprite verticalBar = Textbox.getTextboxResource("resources/sprites/Text/windowspritesGreen.png","rectangle 16 0 1 8");
	Sprite bottomBar = Textbox.getTextboxResource("resources/sprites/Text/windowspritesGreen.png", "rectangle 24 0 8 1");
	Sprite top = Textbox.getTextboxResource("resources/sprites/Text/windowspritesGreen.png", "rectangle 0 0 8 8");
	
	int width = -1;
	int height = -1;
	

	public Menu () {
		
	}
	
	@Override
	public void draw () {
		if (width != 1 && height != -1) {
			//draws the box
			for (int i = 0; i < width; i++) {
				top.draw((int)(this.getX() + (i*8)), (int)(this.getY()));
				bottomBar.draw((int)(this.getX() + (i*8)), (int)(this.getY() + (height*8)));
			}
			for (int i = 0; i < height; i++) {
				verticalBar.draw((int)(this.getX()), (int)(this.getY() + (i*8)));
				verticalBar.draw((int)(this.getX() +(width*8)), (int)(this.getY() + (i*8)));
			}
		}
		
		int place = 16 + (int)this.getY();
		
		for (int i = 0; i < componites.size(); i++) {
			
			componites.get(i).setX((int) this.getX());
			componites.get(i).setY(place);
			
			
			componites.get(i).draw();
			
			place = place + componites.get(i).getHeight();
		}
	}
	
	@Override
	public void frameEvent () {
		for (int i = 0; i <componites.size(); i++) {
			componites.get(i).compointeFrame();
		}
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setColor (String color) {
		verticalBar = Textbox.getTextboxResource("resources/sprites/Text/windowsprites" + color +".png","rectangle 16 0 1 8");
		bottomBar = Textbox.getTextboxResource("resources/sprites/Text/windowsprites" + color +".png", "rectangle 24 0 8 1");
		top = Textbox.getTextboxResource("resources/sprites/Text/windowsprites" + color +".png", "rectangle 0 0 8 8");
	}
	public void addComponite (MenuComponite comp) {
		componites.add(comp);
		if (width < comp.width) {
			width = comp.width;
		}
		height = height + comp.height;
	}
	public void removeComponite (MenuComponite comp) {
		componites.remove(comp);
		height = height - comp.getHeight();
	}
	
}
