package items;

import java.awt.Rectangle;

import engine.GameCode;
import engine.Sprite;
import map.Roome;
import players.Bit;

public class Bombs extends Item {

	boolean thrown = false;
	
	int direction = 0;
	public Bombs () {
		this.setSprite(new Sprite ("resources/sprites/bombs.png"));
		this.setHitboxAttributes(32, 32);
	}
	
	/**
	 * returns true if item use was succsefull false otherwise
	 */
	@Override
	public boolean useItem (Bit user) {
		this.declare((int)user.getX(),(int)user.getY());
		thrown = true;
		direction = user.lastMove;
		pickupablity = false;
		return true;
	}
	
	
	public void refresh(String info) {
		String [] infos = info.split(" ");
		this.setX(Double.parseDouble(infos[0]));
		this.setY(Double.parseDouble(infos[1]));
	}
	
	
	@Override
	public void frameEvent () {
		if (thrown) {
			switch (direction) {
			case 0:
				if (!this.goY(this.getY() - 6)) {
					this.setY(this.getY() - 6);
					Rectangle rect9 = new Rectangle ((int)(432 + Roome.getRoom(this.getX(),this.getY()).getX()), (int)(Roome.getRoom(this.getX(), this.getY()).getY()),216,144);
					if (this.hitbox().intersects(rect9)) {
						Roome.getRoom(this.getX(), this.getY()).destroyTopWall();
					}
					this.forget();
				}
				break;
			case 1:
				if (!this.goY(this.getY() + 6)) {
					this.setY(this.getY() + 6);
					Rectangle rect9 = new Rectangle ((int)(432 + Roome.getRoom(this.getX(),this.getY()).getX()), (int)(576 + Roome.getRoom(this.getX(), this.getY()).getY()),216,144);
					if (this.hitbox().intersects(rect9)) {
						Roome.getRoom(this.getX(), this.getY()).destroyBottomWall();
					}
					this.forget();
				}
				break;
			case 2:
				if (!this.goX(this.getX() + 6)) {
					this.setX(this.getX() + 6);
					Rectangle rect9 = new Rectangle ((int)(864 + Roome.getRoom(this.getX(),this.getY()).getX()), (int)(252 + Roome.getRoom(this.getX(), this.getY()).getY()),216,144);
					if (this.hitbox().intersects(rect9)) {
						Roome.getRoom(this.getX(), this.getY()).destroyRightWall();
					}
					this.forget();
				}
				break;
			case 3:
				if (!this.goX(this.getX() - 6)) {
					this.setX(this.getX() - 6);
					Rectangle rect9 = new Rectangle ((int)(Roome.getRoom(this.getX(),this.getY()).getX()), (int)(252 + Roome.getRoom(this.getX(), this.getY()).getY()),216,144);
					if (this.hitbox().intersects(rect9)) {
						Roome.getRoom(this.getX(), this.getY()).destroyLeftWall();
					}
					this.forget();
				}
				break;
			}
		}
	}
	@Override
	public String toString () {
		return this.getX() + " " + this.getY();
	}
}
