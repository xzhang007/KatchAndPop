import java.awt.event.KeyEvent;

abstract public class Player extends MovableObject {
	protected Player(int x, int y, GameWorld game) {
		super(x, y, game);
	}
}