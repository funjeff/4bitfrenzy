package menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import engine.GameCode;

public class GraveyardText extends Menu {
	public GraveyardText () {
		ArrayList <TextComponite> possibleText = this.getTexts();
		Random rand = new Random ();
		
		this.addComponite(new MenuComponite (10,40,this));
		this.addComponite(possibleText.get(rand.nextInt(possibleText.size())));
		this.setColor("Green");
		this.setBackgroundColor(0x000000);
		this.enterClose();
		this.setRenderPriority(400);
		this.addComponite(new MenuComponite (10,40,this));
	}
	
	public ArrayList <TextComponite> getTexts (){
		File f = new File ("resources/text/graveyardText.txt");
		ArrayList <TextComponite> texts = new ArrayList <TextComponite> ();
		try {
			Scanner s = new Scanner (f);
			while (s.hasNextLine()) {
				TextComponite t = new TextComponite (this, s.nextLine());
				texts.add(t);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return texts;
	}
	@Override
	public void open () {
		this.setX(GameCode.getViewX() + 100);
		this.setY(GameCode.getViewY() + 300);
		super.open();
	}
}
