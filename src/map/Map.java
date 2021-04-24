package map;

import java.util.ArrayList;

import engine.GameCode;
import engine.GameObject;
import engine.ObjectHandler;
import engine.Sprite;
import players.Bit;
import resources.Textbox;

public class Map extends GameObject  {
	
	public final Sprite tile = new Sprite ("resources/sprites/mapTile.png");
	
	public final Sprite bridge = new Sprite ("resources/sprites/green square.png");
	
	public Map () {
		this.setRenderPriority(700);
	}
	
	@Override
	public void draw () {
		int drawPosX = 340;
		int drawPosY = 240;
		for (int i = 0; i < Roome.map.length; i++) {
			for (int j = 0; j < Roome.map[i].length;j++) {
				tile.draw(drawPosX + j*32, drawPosY + i*32);
				if (Roome.map[i][j].bottomJunction) {
					bridge.draw(drawPosX + j*32 + 13, drawPosY + i*32 + 26);
				}
				if (Roome.map[i][j].topJunction) {
					bridge.draw(drawPosX + j*32 + 13, drawPosY + i*32);
				}
				if (Roome.map[i][j].rightJunction) {
					bridge.draw(drawPosX + j*32 + 26, drawPosY + i*32 + 13);
				}
				if (Roome.map[i][j].leftJunction) {
					bridge.draw(drawPosX + j*32, drawPosY + i*32 + 13);
				}
				if (Roome.map[i][j].r != null) {
					Textbox tx = new Textbox (Integer.toHexString(Roome.map[i][j].r.getMemAddress()).toUpperCase() );
					tx.setFont("text (lime green)");
					tx.setX(drawPosX + j*32 + 6 + GameCode.getViewX());
					tx.setY(drawPosY + i*32 + 6 + GameCode.getViewY());
					tx.changeBoxVisability();
					tx.setRenderPriority(700);
					tx.draw();
				}
				if (Roome.map[i][j].ds != null) {
					Textbox tx = new Textbox (Integer.toHexString(Roome.map[i][j].ds.getMemAddress()).toUpperCase());
					tx.setFont("text (red)");
					tx.setX(drawPosX + j*32 + 6 + GameCode.getViewX());
					tx.setY(drawPosY + i*32 + 6 + GameCode.getViewY());
					tx.changeBoxVisability();
					tx.setRenderPriority(700);
					tx.draw();
				}
			}
		}
		ArrayList <GameObject> toMark = ObjectHandler.getObjectsByName("Bit");
		for (int i = 0; i < toMark.size(); i++) {
			Bit working = (Bit) toMark.get(i);
			Roome playerRoom = Roome.getRoom(toMark.get(i).getX(), toMark.get(i).getY());
			toMark.get(i).getSprite().draw(playerRoom.roomPosX * 32 + 346, playerRoom.roomPosY * 32 + 240,working.playerNum - 1);
		}	
	}
}
