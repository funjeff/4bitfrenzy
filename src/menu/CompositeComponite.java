package menu;

public class CompositeComponite extends MenuComponite {

	MenuComponite left;
	MenuComponite right;
	
	
	public CompositeComponite (MenuComponite left, MenuComponite right, Menu m) {
		super(left.width + right.width,getLargerHeight(left.getHeight(), right.getHeight()), m);
		this.left = left;
		this.right = right;
	}
	
	
	private static int getLargerHeight (int left, int right) {
		if (left>right) {
			return left;
		} else {
			return right;
		}
	}
	
	@Override
	public void compointeFrame () {
		left.compointeFrame();
		right.compointeFrame();
	}
	
	@Override
	public void draw () {
		left.setX(this.getX());
		left.setY(this.getY());
		left.draw();
		
		right.setX(this.getX() + left.width);
		right.setY(this.getY());
		right.draw();
	}
}
