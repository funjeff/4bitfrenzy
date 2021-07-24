package titleScreen;

import engine.GameObject;
import engine.Sprite;

public class TitleSlot extends GameObject {

	public static Sprite titleHost = new Sprite ("resources/sprites/title_slot_host.png");
	public static Sprite titleJoin = new Sprite ("resources/sprites/title_slot_join.png");
	public static Sprite titlePerks = new Sprite ("resources/sprites/title_slot_perks.png");
	public static Sprite titleSettings = new Sprite ("resources/sprites/title_slot_settings.png");
	public static Sprite titleHelp = new Sprite ("resources/sprites/title_slot_help.png");
	
	private boolean selected = false;
	
	public TitleSlot (Sprite spr) {
		setSprite (spr);
		setRenderPriority (100);
		setHitboxAttributes (84, 90);
	}
	
	public boolean isSelected () {
		return selected;
	}
	
	@Override
	public void frameEvent () {
		if (isColliding ("TitleRegister")) {
			selected = true;
		}
	}
	
	@Override
	public void forget () {
		selected = false;
		super.forget ();
	}
	
}
