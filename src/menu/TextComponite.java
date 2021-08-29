package menu;

import java.util.Random;

import engine.GameCode;
import resources.Textbox;

public class TextComponite extends MenuComponite{

	Textbox t;
	
	public TextComponite(int width, int height, Menu m) {
		super(width, height, m);
		t = new Textbox ("");
		t.changeWidth(width);
		t.changeHeight(height);
		t.setLineSpacing(1);
		t.changeBoxVisability();
		
	}
	
	public TextComponite(Menu m, String text) {
		super(getTextSpace(text), 16, m);
		t = new Textbox (text);
		t.changeWidth(text.length());
		t.changeHeight(1);
		t.setLineSpacing(1);
		t.changeBoxVisability();
	}
	
	public static int getTextSpace (String text) {
		Textbox t = new Textbox(text);
		return t.getTextSpaceUsage()[0];
	}
	
	public void setText(String message) {
		t.changeText(message);
	}
	
	public Textbox getTextbox () {
		return t;
	}
	
	@Override
	public void draw() {
		t.setX(this.getX());
		t.setY(this.getY());
		
		t.draw();
	}
	
	
	
}
