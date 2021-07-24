package titleScreen;

import engine.GameCode;
import engine.GameObject;
import engine.Sprite;

public class TitleRegister extends GameObject {

	public static Sprite sprite = new Sprite ("resources/sprites/title_register.png");
	
	public TitleRegister () {
		
		setSprite (sprite);
		setRenderPriority (100);
		setHitboxAttributes (98, 42);
		
	}
	
	@Override
	public boolean goX (double val) {
		if (val < 24 || val > GameCode.getSettings ().getResolutionX () - this.hitbox ().width - 24) {
			return false;
		}
		double prevX = getX ();
		setX (val);
		return true;
	}
	
	@Override
	public boolean goY (double val) {
		if (val < 24 || val > GameCode.getSettings ().getResolutionY () - this.hitbox ().height - 24) {
			return false;
		}
		double prevY = getY ();
		setY (val);
		if (val > 200) {

		}
		return true;
	}
	
}
