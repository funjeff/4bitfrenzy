package titleScreen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import map.Roome;
import resources.Textbox;

public class TitleCodeWalls extends GameObject  {
	
	public Textbox [] codeLines = new Textbox [20];
	
	
	public TitleCodeWalls () {
		
		Random rand = new Random ();
		String toWrite = Roome.getCodeWallLines().get(rand.nextInt(Roome.getCodeWallLines().size()));
		int index = 0;
		for (int i = 0; i < 5; i++) {		
			String writeTo = "~Ctext (lime green) code~";
			for (int j = 0; j < 75; j++) {
				try {
					if (i == 2 && j == 28) {
						writeTo = writeTo + "~Ctext (red) code~ 4 BIT FRENZY ~Ctext (lime green) code~";
						j = j + 14;
					} else if (i == 3 && j == 13) {
						writeTo = writeTo + "~Ctext (red) code~ A GAME BY JEFFREY MARSH AND NATHAN SCHMIDT ~Ctext (lime green) code~";
						j = j + 44;
					}else {
						writeTo = writeTo + toWrite.charAt(index);
					}
				} catch (StringIndexOutOfBoundsException e) {
					index = 0;
					toWrite = Roome.getCodeWallLines().get(rand.nextInt(Roome.getCodeWallLines().size()));
				}
				index = index + 1;
			}
			codeLines[i] = new Textbox (writeTo.toUpperCase());
		}
		
		for (int i = 0; i < 13; i++) {
			String writeTo = "~Ctext (lime green) code~";
			for (int j = 0; j < 12; j++) {
				try {
					writeTo = writeTo + toWrite.charAt(index);
				} catch (StringIndexOutOfBoundsException e) {
					index = 0;
					toWrite = Roome.getCodeWallLines().get(rand.nextInt(Roome.getCodeWallLines().size()));
				}
				index = index + 1;
			}
			for (int j = 0; j < 48; j++) {
				writeTo = writeTo + " ";
			}
			for (int j = 0; j < 12; j++) {
				try {
					writeTo = writeTo + toWrite.charAt(index);
				} catch (StringIndexOutOfBoundsException e) {
					index = 0;
					toWrite = Roome.getCodeWallLines().get(rand.nextInt(Roome.getCodeWallLines().size()));
				}
				index = index + 1;
			}
			codeLines[i + 5] = new Textbox (writeTo.toUpperCase());
		}
		
		for (int i = 0; i < 2; i++) {		
			String writeTo = "~Ctext (lime green) code~";
			for (int j = 0; j < 75; j++) {
				try {
					writeTo = writeTo + toWrite.charAt(index);
				} catch (StringIndexOutOfBoundsException e) {
					index = 0;
					toWrite = Roome.getCodeWallLines().get(rand.nextInt(Roome.getCodeWallLines().size()));
				}
				index = index + 1;
			}
			codeLines[i + 18] = new Textbox (writeTo.toUpperCase());
		}
		for (int i = 0; i < codeLines.length; i++) {
			codeLines[i].changeBoxVisability();
		}
	}
	
	@Override
	public void draw () {
		for (int i = 0; i < codeLines.length; i++) {
			codeLines[i].setX(0);
			codeLines[i].setY(i*36);			
			codeLines[i].draw();
		}
	}
	
	@Override
	public boolean isColliding (GameObject obj) {
		Rectangle objR = obj.hitbox();
		
		Rectangle r1 = new Rectangle (0 + (int)this.getX(),0 + (int)this.getY(),75*16,4*36 + 12);
		Rectangle r2 = new Rectangle (0 + (int)this.getX(),4*36 + (int)this.getY(),12*16,14*36);
		Rectangle r3 = new Rectangle (60*16 + (int)this.getX(),4*36 + (int)this.getY(),12*16,14*36);
		Rectangle r4 = new Rectangle (0 + (int)this.getX(),18*36 + (int)this.getY(),75*16,2*36);
		
		
//		Graphics g = RenderLoop.wind.getBufferGraphics();
//		
//		g.setColor(new Color (0xFFFFFF));
//		
//		g.drawRect(0 + (int)this.getX(),0 + (int)this.getY(),75*16,5*36);
//		g.drawRect(0 + (int)this.getX(),5*36 + (int)this.getY(),12*16,13*36);
//		g.drawRect(60*16 + (int)this.getX(),5*36 + (int)this.getY(),12*16,13*36);
//		g.drawRect(0 + (int)this.getX(),18*36 + (int)this.getY(),75*16,2*36);
//		
		
		
		if (objR.intersects(r1) || objR.intersects(r2) || objR.intersects(r3) || objR.intersects(r4)) {
			return true;
		} else {
			return false;
		}
		
		
		
	}
	
}
