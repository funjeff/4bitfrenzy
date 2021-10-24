package gui;

import engine.GameObject;
import menu.Menu;
import menu.TextComponite;

public class MapDisplay extends GameObject {

	private Menu mapDispMenu;
	
	private TextComponite roomTitle;
	
	public MapDisplay () {
		
		mapDispMenu = new Menu ();
		mapDispMenu.setBackgroundColor (0x000000);
		
		roomTitle = new TextComponite (64, 12, mapDispMenu);
		roomTitle.setText ("HELLO");
		mapDispMenu.addComponite (roomTitle);
		
		//mapDispMenu.open (); Uncomment to show map display
		
		setRenderPriority (69420);
		
	}
	
	public Menu getMenu () {
		return mapDispMenu;
	}
	
	@Override
	public void frameEvent () {
		//mapDispMenu.frameEvent ();
	}
	
	@Override
	public void draw () {
		mapDispMenu.draw ();
	}
	
}
