package titleScreen;

import java.util.Random;

import engine.GameObject;
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
	}
	
	@Override
	public void draw () {
		for (int i = 0; i < codeLines.length; i++) {
			codeLines[i].setX(0);
			codeLines[i].setY(i*36);			
			codeLines[i].draw();
		}
	}
	
}
