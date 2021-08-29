package titleScreen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;
import menu.CompositeComponite;
import menu.Menu;
import menu.MenuComponite;
import menu.ObjectComponite;

public class PerkStation extends GameObject {

	public static final int[] perkMap = new int[] {15, 0, 1, 2, 4, 3, 5, 6};
	public static final Color[] perkColorMap = new Color[] {new Color (0x00FF00), new Color (0x0000FF), new Color (0x55FF0D), new Color (0xFFFF00), new Color (0xFF00FF), new Color (0xFF0000), new Color (0xFFFFFF), new Color (0x800080)};
	public static final String[] perkNameMap = new String[] {"Default Perk", "Blast Processing", "Grip Strength", "Navigation Bit", "Duplication Bit", "Powerhouse Bit", "Duel Core", "Professional Gambler"};
	public static final int [] perkOffsetMap = new int [] {1,20,50,20,20,50,20,1};
	
	MenuComponite paddingLeft;
	
	private Scene scene;
	private int perk;
	
	public Menu sceenMenu = new Menu ();
	
	public ObjectComponite sceeneComponite = new ObjectComponite (300,500,scene,sceenMenu);
	public CompositeComponite finalComponite;
	
	public PerkStation (int perk) {
		super ();
		this.perk = perk;
		this.setSprite (new Sprite ("resources/sprites/Perk" + perk + ".png"));
		this.setHitboxAttributes (64, 96);
		this.setRenderPriority (70);
		
		sceenMenu.setColor("Notepad");
		
		sceenMenu.setTop(new Sprite ("resources/sprites/Text/notepadTop.png"));
		
		sceenMenu.setName(perkNameMap[perk].toUpperCase());
		
		sceenMenu.setBackgroundColor(0x000000);
		
		sceenMenu.setRenderPriority(140);
		
		PerkBanner banner = new PerkBanner ();
		
		sceenMenu.addComponite(new ObjectComponite(banner,sceenMenu));
		
		paddingLeft = new MenuComponite (perkOffsetMap[perk],8,sceenMenu);
		finalComponite = new CompositeComponite (paddingLeft,sceeneComponite,sceenMenu);
	}
	
	@Override
	public void frameEvent () {
		if (isColliding ("TitleBit")) {
			if (keyPressed (KeyEvent.VK_ENTER)) {
				if (TitleScreen.perkNum != perkMap[perk]) {
					TitleScreen.perkNum = perkMap [perk];
				} else {
					TitleScreen.perkNum = 15;
				}
			}
			if (!TitleScreen.scenePlaying ()) {
				if (sceenMenu.containtsComponite(finalComponite)) {
					sceenMenu.removeComponite(finalComponite);
				}
				scene = TitleScreen.playScene ("resources/scenes/" + perkNameMap [perk] + ".txt", 900, 129);
				scene.play();
				sceeneComponite = new ObjectComponite (300,800,scene,sceenMenu);
				finalComponite = new CompositeComponite (paddingLeft,sceeneComponite,sceenMenu);
				sceenMenu.addComponite(finalComponite);
			} 
			if (sceenMenu.isClosed()) {
				sceenMenu.setX(this.getX() + 64);
				sceenMenu.setY(20);
				sceenMenu.open();
			}
			try {
				scene.frameEvent();
			} catch (NullPointerException e) {
				
			}
		} else {
			if (scene != null && scene.isPlaying()) {
				scene.stop();
			}
			sceenMenu.close();
		}
	}
	
	
	public class PerkBanner extends GameObject {
		public PerkBanner() {
			
		}
		public void draw () {
			
			Graphics g = RenderLoop.wind.getBufferGraphics ();
			Font f = new Font ("Arial", 96, 40);
			g.setFont (f);
			g.setColor (perkColorMap [perk]);
			g.drawString (perkNameMap [perk], (int)this.getX() - GameCode.getViewX (), (int)this.getY() + 32 - GameCode.getViewY ());
			f = new Font ("Arial", 1, 16);
			g.setFont (f);
			if (TitleScreen.perkNum == perkMap [perk]) {
				g.drawString ("You have selected this perk.", (int)this.getX() - GameCode.getViewX (), (int)this.getY() + 64- GameCode.getViewY ());
			} else {
				g.drawString ("Press ENTER to select this perk", (int)this.getX() - GameCode.getViewX (), (int)this.getY() + 64 - GameCode.getViewY ());
			}
		}
	}
}
