package titleScreen;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;
import gameObjects.PointyBoi;

public class TitleRegister extends GameObject {

	public static final int HINT_TIME = 600;
	public static final int FLASH_TIME = 45;
	
	public static Sprite sprite = new Sprite ("resources/sprites/title_register.png");
	public static Sprite hint = new Sprite ("resources/sprites/register_hint.png");
	
	public int hintTimer;
	
	public TitleRegister () {
		
		setSprite (sprite);
		setRenderPriority (100);
		setHitboxAttributes (98, 42);
		new PointyBoi ().declare (0, 0);
		
	}
	
	@Override
	public boolean goX (double val) {
		hintTimer = Integer.MIN_VALUE;
		if (val < 24 || val > GameCode.getSettings ().getResolutionX () - this.hitbox ().width - 24) {
			return false;
		}
		double prevX = getX ();
		setX (val);
		return true;
	}
	
	@Override
	public boolean goY (double val) {
		hintTimer = Integer.MIN_VALUE;
		if (val < 24 || val > GameCode.getSettings ().getResolutionY () - this.hitbox ().height - 24) {
			return false;
		}
		double prevY = getY ();
		setY (val);
		return true;
	}
	
	@Override
	public void frameEvent () {
		hintTimer++;
	}
	
	@Override
	public void draw () {
		super.draw ();
		if (hintTimer > HINT_TIME && ((hintTimer - HINT_TIME) / FLASH_TIME) % 2 == 0) {
			hint.draw ((int)getX () - 3, (int)getY () - 3);
		}
	}
	
}
