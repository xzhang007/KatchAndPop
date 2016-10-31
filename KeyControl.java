import java.awt.event.*;

public class KeyControl extends KeyAdapter {
	private GameEvents gameEvents;
	
	public KeyControl(GameEvents gameEvents) {
		this.gameEvents = gameEvents;
	}
	
	public void keyPressed(KeyEvent e) {
		gameEvents.setValue(e);
	}
		
	/*public void keyReleased(KeyEvent e) {
	}*/
}
