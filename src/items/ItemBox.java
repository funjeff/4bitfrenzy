package items;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import engine.GameCode;
import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;
import players.Bit;

public class ItemBox extends GameObject {
	
	private Item item = null;
	
	private int itemHintTime;
	private Point itemHintPos;
	
	public static final int ITEM_HINT_TIME = 300;
	public static final int ITEM_HINT_FADE = 50;
	
	public static final int TEXT_OFFSET_X = -240;
	public static final int TEXT_OFFSET_Y = 112;
	public static final int LINE_HEIGHT = 24;
	public static final int DESC_OFFSET = 30;
	
	public static final int PADDING_TOP = 8;
	public static final int PADDING_LEFT = 8;
	public static final int PADDING_BOTTOM = 8;
	public static final int PADDING_RIGHT = 8;
	
	public ItemBox () {
		this.setSprite(new Sprite ("resources/sprites/itemBox.png"));
		this.setRenderPriority(42000);
		itemHintTime = 0;
	}
	
	
	public Item getItem() {
		return item;
	}


	public void putItem (Item item) {
		this.item = item;
		itemHintPos = new Point ((int)getX (), (int)getY ());
		itemHintTime = ITEM_HINT_TIME;
	}


	public void useItem (Bit user) {
		if (item != null) {
			if (item.useItem(user)) {
				if (user.perk != 4) {
					item = null;
				} else {
					item.uses = item.uses - 1;
					if (item.uses == 0) {
						item = null;
					}
				}
			}
		}
	}
	
	@Override
	public void draw () {
		
		//Draw the item box and the item inside it
		this.setX(1153);
		this.setY(61);
		super.drawAbsolute ();
		if (item != null && isVisable()) {
			item.setX(this.getX());
			item.setY(this.getY());
			item.drawAbsolute();
		}
		
		//Draw item text if needed
		if (itemHintTime != 0 && item != null) {
			
			//Set up the drawing color
			Graphics g = RenderLoop.wind.getBufferGraphics ();
			float alpha = itemHintTime < ITEM_HINT_FADE ? (float)itemHintTime / ITEM_HINT_FADE : 1;
			
			//Draw the text
			Font f = new Font ("Arial", 24, 24);
			g.setFont (f);
			int messageX = (int)(itemHintPos.getX ());
			int messageY = (int)(itemHintPos.getY ());
			String[] lines = item.getDesc ().split ("\n");
			
			//Draw the background
			int totalWidth = -1;
			for (int i = 0; i < lines.length; i++) {
				int currWidth = g.getFontMetrics ().stringWidth (lines [i]);
				if (currWidth > totalWidth) {
					totalWidth = currWidth;
				}
			}
			int totalHeight = LINE_HEIGHT * lines.length + DESC_OFFSET; 
			g.setColor (new Color (0, 0, 0, alpha));
			g.fillRect (messageX + TEXT_OFFSET_X - PADDING_LEFT, messageY + TEXT_OFFSET_Y - PADDING_TOP - g.getFontMetrics ().getAscent (), totalWidth + PADDING_LEFT + PADDING_RIGHT, totalHeight + PADDING_TOP + PADDING_BOTTOM);
			
			//Draw the text
			g.setColor (new Color (1, 1, 1, alpha));
			g.drawString (item.getName (), messageX + TEXT_OFFSET_X, messageY + TEXT_OFFSET_Y);
			for (int i = 0; i < lines.length; i++) {
				g.drawString (lines [i], messageX + TEXT_OFFSET_X, messageY + LINE_HEIGHT * i + DESC_OFFSET + TEXT_OFFSET_Y);
			}
			
			//Count down the hint time
			itemHintTime--;
			
		}
		
	}
	@Override
	public String toString () {
		if (item == null) {
			return "null";
		} else {
			return item.getClass().getSimpleName();
		}
	}
}
