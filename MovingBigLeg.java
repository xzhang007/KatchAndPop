import java.awt.Graphics;
//import java.util.List;
import java.util.Observable;

public class MovingBigLeg extends BigLeg {
	private int SPEED = 5;
	private int oldX;
	
	public MovingBigLeg(int x, int y, boolean big, GameWorld game) {
		super(x, y, big, game);
		oldX = super.getX();
	}
	
	
	
	@Override
	public void draw(Graphics g) {
		super.draw(g);
		move();
	}


	private void move() {
		int x = super.getX();
		oldX = x;
		super.setX(x + SPEED);
	}
	
	@Override
	public void update(Observable obj, Object arg) {
		GameEvents ge = (GameEvents) arg;
		if (2 == ge.getType() && (this == ge.getCaller() || this == ge.getTarget())) {
			Thing thing = (Thing) ge.getTarget();
			if (thing instanceof Wall || thing instanceof Block) {    // collide with Wall or Block
				super.setX(oldX);
				SPEED = 0 - SPEED;
			} else {    // collide with Pop
				super.update(obj, arg);
			}
		}
	}
	
}
