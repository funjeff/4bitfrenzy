package npcs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import engine.Sprite;
import gameObjects.Key;
import resources.HitboxFilter;
import resources.Textbox;

public class Keyboard extends NPC {

	String typedString = "";
	
	public Keyboard (double x, double y) {
		
		super(x,y);
		
		this.setRenderPriority(-60);
		
		this.setSprite(new Sprite ("resources/sprites/keyboard.png"));
		
		
		this.initkeys();
		
		
	}
	
	public void type (String toType) {
		typedString = typedString + toType;
		if (typedString.length() > (32*3)) {
		
			typedString = typedString.substring(typedString.length() - (32*3));
	
		}
	}
	
	public void delete () {
		try {
			
			typedString = typedString.substring(0, typedString.length() -1);
		
		} catch (IndexOutOfBoundsException e) {
			
		}
	}
	public void clear() {
		typedString = "";
		
	}
	@Override
	public void draw () {
		super.draw();
		
		Textbox tb = new Textbox (typedString);
		tb.changeBoxVisability();
		
		tb.setY(this.getY() - 100);
		
		tb.setX(this.getX());
		
		tb.changeWidth(32);
		
		tb.setFont("text (lime green)");
		
		tb.draw();
		
	}
	
	public void initkeys () {
		HashMap <Integer, ArrayList <ArrayList <Point>>> keyPixels = HitboxFilter.filter(new Sprite ("resources/sprites/keyboard mask.png"));
		
		Key [] keys = new Key [77];
		
		
		keys[0] = new Key("ESC",this);
		keys[1] = new Key("F1",this);
		keys[2] = new Key("F2",this);
		keys[3] = new Key("F3",this);
		keys[4] = new Key("F4",this);
		keys[5] = new Key("F5",this);
		keys[6] = new Key("F6",this);
		keys[7] = new Key("F7",this);
		keys[8] = new Key("F8",this);
		keys[9] = new Key("F9",this);
		keys[10] = new Key("F10",this);
		keys[11] = new Key("F11",this);
		keys[12] = new Key("F12",this);
		keys[13] = new Key("del",this);
		keys[14] = new Key("~",this);
		keys[15] = new Key("1",this);
		keys[16] = new Key("2",this);
		keys[17] = new Key("3",this);
		keys[18] = new Key("4",this);
		keys[19] = new Key("5",this);
		keys[20] = new Key("6",this);
		keys[21] = new Key("7",this);
		keys[22] = new Key("8",this);
		keys[23] = new Key("9",this);
		keys[24] = new Key("0",this);
		keys[25] = new Key("=",this);
		keys[26] = new Key("^",this);
		keys[27] = new Key("Backspace",this);
		keys[28] = new Key("     ",this);
		keys[29] = new Key("Q",this);
		keys[30] = new Key("W",this);
		keys[31] = new Key("E",this);
		keys[32] = new Key("R",this);
		keys[33] = new Key("T",this);
		keys[34] = new Key("Y",this);
		keys[35] = new Key("U",this);
		keys[36] = new Key("I",this);
		keys[37] = new Key("O",this);
		keys[38] = new Key("P",this);
		keys[39] = new Key("/",this);
		keys[40] = new Key("}",this);
		keys[41] = new Key("",this);
		keys[42] = new Key("A",this);
		keys[43] = new Key("S",this);
		keys[44] = new Key("D",this);
		keys[45] = new Key("F",this);
		keys[46] = new Key("G",this);
		keys[47] = new Key("H",this);
		keys[48] = new Key("J",this);
		keys[49] = new Key("K",this);
		keys[50] = new Key("L",this);
		keys[51] = new Key(".",this);
		keys[52] = new Key("!",this);
		keys[53] = new Key("-",this);
		keys[54] = new Key("",this);
		keys[55] = new Key("\\",this);
		keys[56] = new Key("Z",this);
		keys[57] = new Key("X",this);
		keys[58] = new Key("C",this);
		keys[59] = new Key("V",this);
		keys[60] = new Key("B",this);
		keys[61] = new Key("N",this);
		keys[62] = new Key("M",this);
		keys[63] = new Key("?",this);
		keys[64] = new Key(":",this);
		keys[65] = new Key("_",this);
		keys[66] = new Key("",this);
		keys[67] = new Key("",this);
		keys[68] = new Key("",this);
		keys[69] = new Key("",this);
		keys[70] = new Key("",this);
		keys[71] = new Key(" ",this);
		keys[72] = new Key("",this);
		keys[73] = new Key("",this);
		keys[74] = new Key("",this);
		keys[75] = new Key("",this);
		keys[76] = new Key("",this);
		
		ArrayList <ArrayList <Point>> red = keyPixels.get(0xFF0008);
		ArrayList <ArrayList <Point>> blue = keyPixels.get(0x0033FF);
		
		
		
		int key = 0;
		
		for (int i = 0; i < red.size(); i++) {
			for (int j = 0; j < red.get(i).size(); j++) {
		
				keys[key].setHitboxAttributes((blue.get(i).get(j).getX() - red.get(i).get(j).getX()) *0.7, (blue.get(i).get(j).getY() - red.get(i).get(j).getY()) *0.7);
			
				keys[key].declare((int)(red.get(i).get(j).getX() * 0.7 ) + (int)this.getX(), (int)(red.get(i).get(j).getY() *0.7) + (int)this.getY());
				
				key = key + 1;
				
			}
		}
		
	}

	
}
