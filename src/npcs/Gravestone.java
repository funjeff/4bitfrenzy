package npcs;

import menu.GraveyardText;

public class Gravestone extends TalkableNPC{

	public Gravestone(double x, double y) {
		super(x - 20, y - 20);
		GraveyardText text = new GraveyardText ();
		this.setMenu(text);
		this.setHitboxAttributes(105, 120); //note ULTRA JANK
		this.setRenderPriority(400);
	}
}
