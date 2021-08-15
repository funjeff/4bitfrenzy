package menu;

import java.util.Random;

import engine.GameCode;
import resources.Textbox;

public class TextComponite extends MenuComponite{

	Textbox t;
	
	public TextComponite(int width, int height, Menu m) {
		super(width *16, height * 16, m);
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
	
	public TextComponite(String text) {
		super(getTextSpace(text), 16);
		t = new Textbox (text);
		t.changeWidth(text.length());
		t.changeHeight(1);
		t.setLineSpacing(1);
		t.changeBoxVisability();
	}
	
	public static int getTextSpace (String text) {
		int width = 0;
		int textWidth = 16;
		
		for (int i = 0; i < text.length(); i++) {
			
			char drawChar = text.charAt(i);
			
			if (drawChar == '~') {
				
				i = i + 1;
				char identifyingChar = text.charAt(i);
				switch (identifyingChar) {
					case 'C':
						i = i + 1;
						identifyingChar = text.charAt(i);
						
						while (identifyingChar != '~') {
							i = i + 1;
							identifyingChar = text.charAt(i);
						}
						i = i + 1;
						break;
					case 'S':
						i = i + 1;
						identifyingChar = text.charAt(i);
						
						String size = "";
						while (identifyingChar != '~') {
							size = size + identifyingChar;
							i = i + 1;
							identifyingChar = text.charAt(i);
						}
						
						textWidth = Integer.parseInt(size);
						
						i = i + 1;
						break;
					case 'T':
						i = i + 1;
						identifyingChar = text.charAt(i);
						
						while (identifyingChar != '~') {
							i = i + 1;
							identifyingChar = text.charAt(i);
						}
						break;
					case 'H':
						i = i +1;
						break;
					default:
						break;
					}
				}
			width = width + textWidth;
			
		}
		
		return width;
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
