package resources;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
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
	public Sprite textBoxSides;
	public Sprite fontSheet;
	
	String text;
	String font;
	
	int width;
	int height;
	int textSize = 16;
	
	int largestSize = 16;
	
	int lineSpacing = 2;

	ArrayList <double []> shakeInfo = new ArrayList <double []> ();
	
	String [] extentions = {"7Z","JAVA","MP3","AI","AVI","BAS","C","C++","CD","CDF","CLASS","CMD","CSV","CSPROJ","D","D64","DAF","DAT","DB","DCI","DEV","DFL","DHP","DLC","DMO","DMP","DOC","DOG","E","EXE","EXP","EXS","F01","F4V","FA","FLV","GBR","GGB","GIF","GO","GPX","H!","H","H++","HACK","HDMP","HTA","HTML","HUM","ICO","IGC","ISO","IT","JAR","JNLP","JPEG","JS","JSON","LISP","LUA","LZ","M","MDI","MDG","MDS","MEX","MID","MOB","MOD","MOV","MP2","MP4","MPEG","MPG","MSI","NC","NEO","NPR","NUMBERS","O","OBJ","OBS","OXT","OWL","OST","P","PAL","PACK","PAK","PAM","PAS","PDF","PDN","PHP","PIE","PIT","PMA","PPTX","PSD","PTF","PS1","PUP","PY","QT","RAD","RAM","RAR","RB","RBXM","RBXL","RC","RES","RTF","RUN","SAV","SB3","SEQ","SIG","SM","SPIN","ST","STD","SWF","SWIFT","TAK","TORRENT","TAR","TSF","TTF","UI","UT!","V","V64","VB","VFD","VMG","VOB","WAV","WMA","XAR","XCF","XEX","XLS","XP","XYZ","ZIP","ZS"};
	boolean renderBox;
	
	private static HashMap<String, Sprite> resourceCache = new HashMap<String, Sprite> ();

	
	// put filepath of fontsheet to use as the font
	public Textbox (String textToDisplay){
		super();
	renderBox = true;
	this.setFont ("text (red)");
	this.setBox ("Black");
	spaceManipulation = 0;
	text = textToDisplay;
	width = 3200;
	height = 1600;
	
	
	this.setRenderPriority(1);
	}
	//changes wheather or not to unpause the game after the textbox is done
	public void changeWidth (int newWidth) {
		width = newWidth * 16;
	}
	public void changeHeight(int newHeigh) {
		height = newHeigh * 16;
	}
	public void changeBoxVisability () {
		renderBox = !renderBox;
	}
	public void changeText(String newText) {
		text = newText;
	}
	public String getText () {
		return text;
	}
	public int getLineSpacing() {
		return lineSpacing;
	}
	public void setLineSpacing(int lineSpacing) {
		this.lineSpacing = lineSpacing;
	}
	public static Sprite getTextboxResource (String path, String parseStr) {
		//Construct the cache string
		String cacheStr = path + ":" + parseStr;
		if (resourceCache.containsKey (cacheStr)) {
			//Resource is cached
			return resourceCache.get (cacheStr);
		} else {
			//Resource is not cached, load it
			ArrayList<String> parseStrs = new ArrayList<String> ();
			parseStrs.add (parseStr);
			Sprite spr = new Sprite (path, new SpriteParser (parseStrs));
			resourceCache.put (cacheStr, spr);
			return spr;
		}
	}
	public void setFont (String fontName) {
		fontSheet = getTextboxResource ("resources/sprites/Text/" + fontName + ".png", "grid 16 16");
		font = fontName;
	}
	//I think Im gonna rewrite this at some point
	/*public void giveName (String boxName) {
		if (!boxName.equals("null")) {
			name = boxName;
			Random rand = new Random();
			name = name + "." +extentions[rand.nextInt(extentions.length)];
		}
	}*/
	public void setBox (String color) {
		textBoxTop = getTextboxResource ("resources/sprites/Text/windowsprites" + color + ".png", "rectangle 0 0 8 8");
		textBoxBottum = getTextboxResource ("resources/sprites/Text/windowsprites" + color + ".png", "rectangle 24 0 8 1");
		textBoxSides= getTextboxResource ("resources/sprites/Text/windowsprites" + color + ".png", "rectangle 16 0 1 8");
		textBoxBackground = getTextboxResource ("resources/sprites/Text/windowsprites" + color + ".png", "rectangle 8 0 8 8");
	}
	
	public int getSpace () {
		return (width * height)/256;
	}
	public void setTextSize(int textSize) {
		this.textSize = textSize;
		
		if (textSize > largestSize) {
			largestSize = textSize;
		}

		Sprite.scale(fontSheet, textSize, textSize);
	}

	
	//2017 Jeffrey appologizes for this garbage code (he would never admit it though)
	//EDIT I FINALLY FUCKIN REWROTE IT AFTER 5 FUCKING YEARS geez I can't belive ive been doing this for so long
public void drawBox () {
	//draws the box itself
	if (renderBox) {
		
		for (int i = 0; i < width/8; i++) {
			for (int j = 0; j < height/8; j++) {
				textBoxBackground.draw((int)((this.getX() - GameCode.getViewX()) + (i * 8)),(int) ( (this.getY() - GameCode.getViewY()) + (j * 8) -10));
			}
		}
		for (int i = 0; i < width/8; i++) {
			textBoxTop.draw((int)((this.getX() - GameCode.getViewX()) + (i*8)), (int)((this.getY() - GameCode.getViewY()) -10));
			textBoxBottum.draw((int)((this.getX() - GameCode.getViewX()) + (i*8)), (int)((this.getY() - GameCode.getViewY()) + (height) -10));
		}
		for (int i = 0; i < height/8; i++) {
			textBoxSides.draw((int)((this.getX() - GameCode.getViewX())), (int)((this.getY() - GameCode.getViewY())+ (i*8) -10));
			textBoxSides.draw((int)((this.getX() - GameCode.getViewX()) +(width)), (int)((this.getY()  - GameCode.getViewY())+ (i*8) -10));
		}
	}
		// translates the charictar in the message to a askii value that is used to specify position on the
		// text sheet run for every for every charitar in the message every frame
	
	int yPos = (int) this.getY();
	int xPos = (int) this.getX();
	
	boolean textShake = false;
	
	int shakeInfoNum = 0;
	

	for (int i = 0; i < text.length(); i++) {
		
		char drawChar = text.charAt(i);
		
		if (drawChar == '~') {
			
			i = i + 1;
			char identifyingChar = text.charAt(i);
			switch (identifyingChar) {
				case 'C':
					i = i + 1;
					identifyingChar = text.charAt(i);
					
					String color = "";
					while (identifyingChar != '~') {
						color = color + identifyingChar;
						i = i + 1;
						identifyingChar = text.charAt(i);
					}
					this.setFont(color);
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
					this.setTextSize(Integer.parseInt(size));
					i = i + 1;
					break;
				case 'T':
					i = i + 1;
					identifyingChar = text.charAt(i);
					
					String transparancy = "";
					while (identifyingChar != '~') {
						transparancy = transparancy + identifyingChar;
						i = i + 1;
						identifyingChar = text.charAt(i);
					}
					fontSheet.setOpacity(Integer.parseInt(transparancy));
					break;
				case 'H':
					textShake = !textShake;
					i = i +1;
					break;
				default:
					xPos = (int) this.getX();
					yPos = yPos + textSize;
					break;
				}
			}
		
		double shakeOffsetX = 0;
		double shakeOffsetY = 0;
		
		if (textShake) {
			double [] usableInfo;
			try {
				
				usableInfo = shakeInfo.get(shakeInfoNum);
				
			} catch (IndexOutOfBoundsException e) {
				Random rand = new Random ();
				
				double [] charInfo = new double [3];
				
				charInfo[0] = xPos;
				charInfo[1] = yPos;
				charInfo[2] = rand.nextInt(628) + 1;
				
				charInfo[2] = charInfo[2]/100;
				
				usableInfo = charInfo;
				
				shakeInfo.add(charInfo);
				
			}
			shakeInfoNum = shakeInfoNum + 1;
			
			usableInfo[0] = usableInfo[0] - Math.cos(usableInfo[2])/2;
			usableInfo[1] = usableInfo[1] - Math.sin(usableInfo[2])/2;
			
			shakeOffsetX = (xPos - usableInfo[0]);
			shakeOffsetY = (yPos - usableInfo[1]);
			
			
			if (Math.pow(shakeOffsetX,2) + Math.pow(shakeOffsetY,2) >= 4) {
			
				Random rand = new Random ();
				double newDirc = (rand.nextInt(157) + 1);
				
				
				if (shakeOffsetX > 0 && shakeOffsetY > 0) {
					if (!(usableInfo[2] > 3.14 && usableInfo[2] < 4.71)) {
						newDirc = newDirc + 314;
					} else {
						newDirc = usableInfo[2] * 100;
					}
				}
				
				if (shakeOffsetX < 0 && shakeOffsetY < 0) {
					if (usableInfo[2] > 1.57) {
						
					} else {
						newDirc = usableInfo[2] * 100;
					}
	
				}
				
				if (shakeOffsetX < 0 && shakeOffsetY > 0) {
					if (usableInfo[2] < 4.71) {
						newDirc = newDirc + 471;
					} else {
						newDirc = usableInfo[2] * 100;
					}
				}
				if (shakeOffsetX > 0 && shakeOffsetY < 0) {
					if (!(usableInfo[2] > 1.57 && usableInfo[2] < 3.14)) {
						newDirc = newDirc + 157;
					} else {
						newDirc = usableInfo[2] * 100;
					}
				}
				
				newDirc = newDirc/100.0;
				
				
				usableInfo[2] = newDirc;
				
			}
			
		}
		
		fontSheet.draw(xPos + (int)shakeOffsetX - GameCode.getViewX (), yPos + (int)shakeOffsetY - GameCode.getViewY (), text.charAt(i));
		
		xPos = xPos + textSize;
		
		if ((xPos - this.getX()) > width) {
			xPos = (int) this.getX();
			yPos = yPos + largestSize * lineSpacing;
			if (yPos - this.getY() > height) {
				break;
			}
		}	
	}
	if (textSize == 16) {
		this.setTextSize(16);
	}
}

@Override
public void draw () {
		Rectangle thisRect = new Rectangle ((int)this.getX(), (int)this.getY(), this.width, this.height);
	
		Rectangle veiwport = new Rectangle ((int) GameCode.getViewX(), (int) GameCode.getViewY(), GameCode.getSettings ().getResolutionX (), GameCode.getSettings ().getResolutionY ());
		if (thisRect.intersects(veiwport)) {
			this.drawBox();
		}
	}
}