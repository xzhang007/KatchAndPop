import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class CollisionDetector {
	GameEvents gameEvents;
	GameWorld game;
	
	public CollisionDetector(GameWorld game) {
		this.game = game;
		gameEvents = game.getObservable();
	}
	
	public void collideWith(Unit caller, Thing target) {
		if (caller.getRec().intersects(target.getRec())) {
			if (target instanceof Katch) {
				Katch katch = (Katch) target;
				gameEvents.setValue(caller, target, katch.getX(), katch.getWIDTH() / 5);
			} else if (target instanceof Block) {
				Block block = (Block) target;
				Rectangle rect = caller.getRec();
				int number = (rect.intersects(block.getLeftSmallRec()) 
							|| rect.intersects(block.getRightSmallRec())) ? 1 : 0;
				gameEvents.setValue(caller, target, number);
			} else {
				gameEvents.setValue(caller, target);
			}
		}
	}
	
	public void collideWith(Unit caller, List<? extends Thing> things) {
		for (int i = 0; i < things.size(); i++) { // important! don't use iterator loop, otherwise show up ConcurrentModificationException
			collideWith(caller, things.get(i));
		}
	}
}
