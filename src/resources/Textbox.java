package resources;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import engine.GameCode;
import engine.GameObject;
import engine.SpriteParser;
import engine.Sprite;


public class Textbox extends GameObject {
	//Jeffrey please comment your code
	//Alternatively, Tbox can be used
	int timer;
	int spaceManipulation;
	public Sprite textBoxTop;
	public Sprite textBoxBottum;
	public Sprite textBoxBackground;
	private boolean unPause = true;
	public Sprite textBoxSides;
	public Sprite fontSheet;
	String message;
	private boolean unfrezeMenu = false;
	int isScrolled = 0;
	String text1;
	String name;
	int width1;
	int height1;
	int time = -1;
	String [] extentions = {"7Z","JAVA","MP3","AI","AVI","BAS","C","C++","CD","CDF","CLASS","CMD","CSV","CSPROJ","D","D64","DAF","DAT","DB","DCI","DEV","DFL","DHP","DLC","DMO","DMP","DOC","DOG","E","EXE","EXP","EXS","F01","F4V","FA","FLV","GBR","GGB","GIF","GO","GPX","H!","H","H++","HACK","HDMP","HTA","HTML","HUM","ICO","IGC","ISO","IT","JAR","JNLP","JPEG","JS","JSON","LISP","LUA","LZ","M","MDI","MDG","MDS","MEX","MID","MOB","MOD","MOV","MP2","MP4","MPEG","MPG","MSI","NC","NEO","NPR","NUMBERS","O","OBJ","OBS","OXT","OWL","OST","P","PAL","PACK","PAK","PAM","PAS","PDF","PDN","PHP","PIE","PIT","PMA","PPTX","PSD","PTF","PS1","PUP","PY","QT","RAD","RAM","RAR","RB","RBXM","RBXL","RC","RES","RTF","RUN","SAV","SB3","SEQ","SIG","SM","SPIN","ST","STD","SWF","SWIFT","TAK","TORRENT","TAR","TSF","TTF","UI","UT!","V","V64","VB","VFD","VMG","VOB","WAV","WMA","XAR","XCF","XEX","XLS","XP","XYZ","ZIP","ZS"};
	boolean renderBox;
	// put filepath of fontsheet to use as the font
	public Textbox (String textToDisplay){
		super();
	renderBox = true;
	ArrayList <String> parserQuantitys = new ArrayList<String> ();
	parserQuantitys.add("grid 16 16");
	ArrayList <String> parserQuantitiys2 = new ArrayList<String>();
	parserQuantitiys2.add("rectangle 0 0 8 8");
	ArrayList <String> parserQuantitiys3 = new ArrayList<String>();
	parserQuantitiys3.add("rectangle 24 0 8 1");
	ArrayList <String> parserQuantitiys4 = new ArrayList<String>();
	parserQuantitiys4.add("rectangle 16 0 1 8");
	ArrayList <String> parserQuantitiys5 = new ArrayList<String>();
	parserQuantitiys5.add("rectangle 8 0 8 8");
	textBoxTop = new Sprite ("resources/sprites/Text/windowspritesBlack.png", new SpriteParser(parserQuantitiys2));
	fontSheet = new Sprite ("resources/sprites/Text/text (red).png", new SpriteParser(parserQuantitys));
	textBoxBottum = new Sprite ("resources/sprites/Text/windowspritesBlack.png", new SpriteParser(parserQuantitiys3));
	textBoxSides = new Sprite ("resources/sprites/Text/windowspritesBlack.png", new SpriteParser(parserQuantitiys4));
	textBoxBackground = new Sprite ("resources/sprites/Text/windowspritesBlack.png", new SpriteParser(parserQuantitiys5));
	spaceManipulation = 0;
	text1 = textToDisplay;
	width1 = 200;
	height1 = 100;
	
	name = "null";
	this.setRenderPriority(1);
	}
	//changes wheather or not to unpause the game after the textbox is done
	public void changeUnpause () {
		unPause = !unPause;
	}
	public void changeWidth (int newWidth) {
		width1 = newWidth;
	}
	public void unfrezeMenu() {
		unfrezeMenu = true;
	}
	public void changeHeight(int newHeigh) {
		height1 = newHeigh;
	}
	public void changeBoxVisability () {
		renderBox = !renderBox;
	}
	public void changeText(String newText) {
		text1 = newText;
		message = newText;
	}
	public void setFont (String fontName) {
		ArrayList <String> parserQuantitys = new ArrayList<String> ();
		parserQuantitys.add("grid 16 16");
		fontSheet = new Sprite ("resources/sprites/Text/" + fontName + ".png", new SpriteParser(parserQuantitys));
	}
	public void giveName (String boxName) {
		if (!boxName.equals("null")) {
			name = boxName;
			Random rand = new Random();
			name = name + "." +extentions[rand.nextInt(extentions.length)];
		}
	}
	public void setBox (String color) {
		ArrayList <String> parserQuantitiys2 = new ArrayList<String>();
		parserQuantitiys2.add("rectangle 0 0 8 8");
		ArrayList <String> parserQuantitiys3 = new ArrayList<String>();
		parserQuantitiys3.add("rectangle 24 0 8 1");
		ArrayList <String> parserQuantitiys4 = new ArrayList<String>();
		parserQuantitiys4.add("rectangle 16 0 1 8");
		ArrayList <String> parserQuantitiys5 = new ArrayList<String>();
		parserQuantitiys5.add("rectangle 8 0 8 8");
		textBoxTop = new Sprite ("resources/sprites/Text/windowsprites" + color + ".png", new SpriteParser(parserQuantitiys2));
		textBoxBottum = new Sprite ("resources/sprites/Text/windowsprites" + color + ".png", new SpriteParser(parserQuantitiys3));
		textBoxSides= new Sprite ("resources/sprites/Text/windowsprites" + color + ".png", new SpriteParser(parserQuantitiys4));
		textBoxBackground = new Sprite ("resources/sprites/Text/windowsprites" + color + ".png", new SpriteParser(parserQuantitiys5));
	}
	
	public int getSpace () {
		return width1 * height1;
	}
	public void setTime (int time) {
		this.time = time;
	}
	// text = the message thats displayed width is the width of the box height is the height of the box 
	//x_orign is the x start point of the box y_orign is the y start point of the box
	
	
	//2017 Jeffrey appologizes for this garbage code (he would never admit it though)
public void drawBox (){
	
	time = time -1;
	String text;
	int width;
	int height;
	int x_origin;
	int y_origin;
	text = text1;
	width = width1;
	height = height1;
	x_origin = (int)this.getX() - GameCode.getViewX();
	y_origin = (int)this.getY() - GameCode.getViewY();
	int space = 0;
	timer = timer + 1;
	int textLength = text.length();
	width = width/8;
	height = height/8;
	int width_start = width;
	int width_beginning = width;
	int width_basis = width;
	int height_start = height;
	height_start = height_start - 2;
	int x_start = x_origin;
	int x_beginning = x_origin;
	int x_basis = x_origin;
	int y_start = y_origin;
	if (!name.equals("null")) {
		int nameX = x_origin;
		int nameY = y_origin - 8;
		for (int i = 0; i < name.length(); i++) {
			textBoxBackground.draw(nameX,nameY);
			fontSheet.draw(nameX,nameY, name.charAt(i));
			nameX = nameX + 8;
		}
	}
	while (width > 1){
	if (renderBox) {
	textBoxTop.draw(x_start, y_start);
	}
	width = width - 1;
	x_start = x_start + 8;
		}
	while (height > 1){
	if (renderBox) {
		textBoxSides.draw(x_origin, y_origin);
		textBoxSides.draw(x_start, y_origin);
		}
	height = height - 1;
	y_origin = y_origin + 8;
		}
	while (width_start > 1){
		if (renderBox) {
			textBoxBottum.draw(x_origin, y_origin);
		}
		width_start = width_start - 1;
		x_origin = x_origin + 8;
			}
	y_origin = y_start;
	x_origin = x_basis;
	int x = 0;
	while (x < height_start){
		width_beginning = width_basis;
		y_start = y_start + 8;
		x_beginning = x_basis;
		x = x + 1;
		while (width_beginning > 1){
			if (renderBox) {
				textBoxBackground.draw(x_beginning, y_start);
			}
			width_beginning = width_beginning - 1;
			x_beginning = x_beginning + 8;
			space = space + 1;
			}
		}
	x_beginning = x_origin;
	int spaceBasis = space;
	int charictarNumber = 0;
	width_beginning = width_basis;
	if (space < text.substring (spaceManipulation, text.length ()).length ()) {
		message = text.substring(spaceManipulation,spaceManipulation + spaceBasis);
	} else {
		//Here's the fix
		message = text.substring (spaceManipulation, text.length ());
	}
	y_origin = y_origin + 8;
		textLength = textLength - isScrolled;
		if ((spaceManipulation + spaceBasis) >= textLength) {
			space = message.length() - 1;
		}
		charictarNumber = 0;
		// translates the charictar in the message to a askii value that is used to specify position on the
		// text sheet run for every for every charitar in the message every frame
			while (charictarNumber < text1.length()){
				char charictarInQuestion = text1.charAt(charictarNumber);
				charictarNumber = charictarNumber + 1;
				fontSheet.draw(x_beginning, y_origin, charictarInQuestion);
				x_beginning = x_beginning + 16;
				space = space - 1;
					if ((x_beginning - x_origin)/16 > width1) {
						x_beginning = x_origin;
						y_origin = y_origin + 32;
						if ((y_origin - (this.getY() - GameCode.getViewY()))/16 > height1) {
							break;
						}
					}
				}
				
			}

@Override
public void draw () {
		Rectangle thisRect = new Rectangle ((int)this.getX(), (int)this.getY(), this.width1 * 16, this.height1 * 16);
	
		Rectangle veiwport = new Rectangle ((int) GameCode.getViewX(), (int) GameCode.getViewY(), 1080, 720);
		if (thisRect.intersects(veiwport)) {	
			this.drawBox();
		}
	}
}