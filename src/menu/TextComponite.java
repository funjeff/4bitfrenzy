package menu;

import engine.GameCode;
import resources.Textbox;

public class TextComponite extends MenuComponite{

	Textbox t;
	
	public TextComponite(int width, int height, Menu m) {
		super((width *2) + 2, height * 8, m);
		t = new Textbox ("");
		t.changeWidth(width);
		t.changeHeight(height);
		t.setLineSpacing(1);
		t.changeBoxVisability();
		
	}
	
	public void setText(String message) {
		t.changeText(message);
	}
	
	@Override
	public void draw() {
		t.setX(this.getX() + GameCode.viewX);
		t.setY(this.getY() + GameCode.viewY);
		
		t.draw();
	}
	
	
	
}
