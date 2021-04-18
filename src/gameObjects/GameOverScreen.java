package gameObjects;

import engine.GameObject;
import engine.RenderLoop;
import engine.Sprite;

public class GameOverScreen extends GameObject {
	
	public GameOverScreen () {
		this.setSprite(new Sprite ("resources/sprites/gameOver.png"));
		this.setRenderPriority(1000000);
	}
	
	@Override
	public void frameEvent () {
		if (!getKeyEvents().isEmpty()) {
			RenderLoop.running = false;
		}
	}

}
