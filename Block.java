import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Block extends Unit {
	public static int width, HEIGHT = 20;
	private static Image [] blockImg = new Image[11];
	//private static Image blockImg;
	private int number;
	private boolean doubleCollision = false;
	private static final int BONUSPOINTS = 20;
	private static final int BONUSPOINTS2 = 100;
	private static final int SMALLLENGTH = 5;
	
	static {
		try {
			blockImg[0] = ImageIO.read(GameWorld.class.getResource("Resources/Block1.png"));
			blockImg[1] = ImageIO.read(GameWorld.class.getResource("Resources/Block2.png"));
			blockImg[2] = ImageIO.read(GameWorld.class.getResource("Resources/Block3.png"));
			blockImg[3] = ImageIO.read(GameWorld.class.getResource("Resources/Block4.png"));
			blockImg[4] = ImageIO.read(GameWorld.class.getResource("Resources/Block5.png"));
			blockImg[5] = ImageIO.read(GameWorld.class.getResource("Resources/Block6.png"));
			blockImg[6] = ImageIO.read(GameWorld.class.getResource("Resources/Block7.png"));
			blockImg[7] = ImageIO.read(GameWorld.class.getResource("Resources/Block_double.png"));
			blockImg[8] = ImageIO.read(GameWorld.class.getResource("Resources/Block_life.png"));
			blockImg[9] = ImageIO.read(GameWorld.class.getResource("Resources/Block_split.png"));
			blockImg[10] = ImageIO.read(GameWorld.class.getResource("Resources/Block_solid.png"));
			width = blockImg[0].getWidth(null);
			//height = blockImage.getWidth(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Block(int x, int y, GameWorld game, int number) {
		super(x, y, game);
		this.number = number;
	}
	
	@Override
	public void draw(Graphics g) {
		
		g.drawImage(blockImg[number], super.getX(), super.getY(), width, HEIGHT, null);
	}
	
	@Override
	public Rectangle getRec() {
		return new Rectangle(super.getX(), super.getY(), width, HEIGHT);
	}
	
	public Rectangle getLeftSmallRec() {
		return new Rectangle(super.getX(), super.getY() + SMALLLENGTH, SMALLLENGTH, HEIGHT - 2 * SMALLLENGTH);
	}
	
	public Rectangle getRightSmallRec() {
		return new Rectangle(super.getX() + width - SMALLLENGTH, super.getY() + SMALLLENGTH, SMALLLENGTH, HEIGHT - 2 * SMALLLENGTH);
	}

	@Override
	public void update(Observable obj, Object arg) {
		GameEvents ge = (GameEvents) arg;
		if (2 == ge.getType() && this == ge.getTarget() && 10 != number && ge.getCaller() instanceof Pop) {
			if (7 == number && !doubleCollision) {    // block double
				doubleCollision = true;
			} else {
				GameWorld game = super.getGame();
				game.setScore(game.getScore() + ((8 == number) ? BONUSPOINTS2 : BONUSPOINTS));
				if (9 == number) {
					game.setLife(game.getLife() + 1);
				}
				super.setDamaged(true);
				game.getListThings().remove(this);
			}
		}
	}
	
}

