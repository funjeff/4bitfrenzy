package menu;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;
import resources.Textbox;
import titleScreen.TitleScreen.Button;

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

	public Button closeButton = null; 
	
	public Menu () {
		
	}
	
	@Override
	public void draw () {
		
		if (open) {
			
			if (backgroundColor != -1) {
			
				Graphics g = RenderLoop.wind.getBufferGraphics();
			
				g.setColor(new Color (backgroundColor));
			
				
				g.fillRect((int)this.getDrawX() + 2, (int)this.getDrawY(), (int)width * 8 - 1, (int)height * 8 + 8);
			}
			
			if (width != 1 && height != -1) {
				//draws the box
				for (int i = 0; i < width -1; i++) {
						top.draw((int)(this.getDrawX() + (i*8)), (int)(this.getDrawY() - (top.getHeight() - 8)));
						bottomBar.draw((int)(this.getDrawX() + (i*8)), (int)(this.getDrawY() + ((height + 1)*8)));
				}
				for (int i = 0; i < height; i++) {
					verticalBar.draw((int)(this.getDrawX()), (int)(this.getDrawY() + (i*8)));
					verticalBar.draw((int)(this.getDrawX() +(width*8) - 2), (int)(this.getDrawY() + (i*8)));
				}
			}
			
			if (closeButton != null) {
				closeButton.setX(this.getX() + (this.getWidth() * 8) - closeButton.getSprite().getWidth());
				closeButton.setY(this.getY() - (closeButton.getSprite().getHeight() - 8));
				closeButton.draw();
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
			if (closeButton != null) {
				closeButton.frameEvent();
				if (closeButton.isPressed()) {
					closeButton.reset();
					this.close();
				}
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
	public ArrayList <GameObject> getAllObjs (){
		ArrayList <GameObject> objs = new ArrayList <GameObject>();
		
		for (int i = 0; i < componites.size(); i++) {
			objs.addAll(componites.get(i).getAllObjs());
		}
		
		return objs;
		
	}
	
	public void setCloseButton(Button closeButton) {
		this.closeButton = closeButton;
	}

	public boolean containtsComponite (MenuComponite comp) {
		return componites.contains(comp);
		
	}
	
	public void close () {
		open = false;
	}
	public boolean isClosed () {
		return !open;
	}
	
	public void setName (String name) {
		menuName = new Textbox ("~S" + (top.getHeight() - 2) +"~" + name);
		menuName.changeBoxVisability();
		menuName.setFont("normal");
	}
	public void removeComponite (MenuComponite comp) {
		componites.remove(comp);	
		if (!fixedHeight){
			height = height - (comp.getHeight()/8.0);
		}

	}
	
}
