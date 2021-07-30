package menu;

import resources.Textbox;

public class TextComponite extends MenuComponite{

	Textbox t;
	
	public TextComponite(int width, int height, Menu m) {
		super(width, height, m);
		t = new Textbox ("");
		t.changeWidth(width);
		t.changeHeight(height);
		t.setTextSize(8);
		t.changeBoxVisability();
		
	}
	
	public void setText(String message) {
		t.changeText(message);
	}
	
	@Override
	public void draw() {
		t.setX(this.getX());
		t.setY(this.getY());
		
		t.draw();
	}
	
	
	
}
