package menu;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;
import resources.Textbox;

public class Menu extends GameObject {	
	
	ArrayList <MenuComponite> componites = new ArrayList <MenuComponite> ();
	
	//I kinda gave up on this naming scheme halfway through
	Sprite verticalBar = Textbox.getTextboxResource("resources/sprites/Text/windowspritesGreen.png","rectangle 16 0 1 8");
	Sprite bottomBar = Textbox.getTextboxResource("resources/sprites/Text/windowspritesGreen.png", "rectangle 24 0 8 1");
	Sprite top = Textbox.getTextboxResource("resources/sprites/Text/windowspritesGreen.png", "rectangle 0 0 8 8");
	
	double width = 0;
	double height = 0;
	
	int backgroundColor = 0xd4d4d4; //use -1 for transparent
	
	boolean fixedHeight = false;
	
	Textbox menuName;
	
	public boolean open = false;

	public Menu () {
		
	}
	
	@Override
	public void draw () {
		
		if (open) {
			
			if (backgroundColor != -1) {
			
				Graphics g = RenderLoop.wind.getBufferGraphics();
			
				g.setColor(new Color (backgroundColor));
			
				g.fillRect((int)this.getX() + 2, (int)this.getY(), (int)width * 8 - 1, (int)height * 8 + 8);
			}
			
			if (width != 1 && height != -1) {
				//draws the box
				for (int i = 0; i < width -1; i++) {
						top.draw((int)(this.getX() + (i*8)), (int)(this.getY() - (top.getHeight() - 8)));
						bottomBar.draw((int)(this.getX() + (i*8)), (int)(this.getY() + ((height + 1)*8)));
				}
				for (int i = 0; i < height; i++) {
					verticalBar.draw((int)(this.getX()), (int)(this.getY() + (i*8)));
					verticalBar.draw((int)(this.getX() +(width*8) - 2), (int)(this.getY() + (i*8)));
				}
			}
			
			int place = (int)this.getY() + 8;
			
			for (int i = 0; i < componites.size(); i++) {
				
				componites.get(i).setX((int) this.getX());
				componites.get(i).setY(place);
				
				
				componites.get(i).draw();
				
				place = place + componites.get(i).getHeight();
			}
			if (menuName != null) {
				menuName.setX(this.getX());
				menuName.setY(this.getY() + 2 - (top.getHeight() - 8));
				menuName.draw();
			}
		}
		
		
	}
	
	

	@Override
	public void frameEvent () {
		if (open) {
			for (int i = 0; i <componites.size(); i++) {
				componites.get(i).compointeFrame();
			}
		}
	}
	
	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public double getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		fixedHeight = true;
	}
	
	public void setColor (String color) {
		verticalBar = Textbox.getTextboxResource("resources/sprites/Text/windowsprites" + color +".png","rectangle 16 0 1 8");
		bottomBar = Textbox.getTextboxResource("resources/sprites/Text/windowsprites" + color +".png", "rectangle 24 0 8 1");
	
		if (top.getHeight() == 8) {
			top = Textbox.getTextboxResource("resources/sprites/Text/windowsprites" + color +".png", "rectangle 0 0 8 8");
		}
	}
	public void addComponite (MenuComponite comp) {
		componites.add(comp);
		if (width < comp.width/8.0) {
			width = comp.width/8.0;
		}
		if (!fixedHeight) {
			height = height + comp.height/8.0;
		}
	}
	public boolean isOpen() {
		return open;
	}



	public void setTop(Sprite top) {
		this.top = top;
	}
	
	public void open() {
		this.open = true;
		if (!this.declared()) {
			this.declare((int)this.getX(), (int)this.getY());
		}
		
	}	
	public void close () {
		open = false;
	}
	
	public void setName (String name) {
		menuName = new Textbox ("~S" + (top.getHeight() - 2) +"~" + name);
		menuName.changeBoxVisability();
		menuName.setFont("normal");
	}
	public void removeComponite (MenuComponite comp) {
		componites.remove(comp);	
		height = height - comp.getHeight();
	}
	
}
