import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.*;

public class BigLeg extends Unit {
	private static int width, height, smallWidth, smallHeight;
	private static BufferedImage [] bigLegImg = new BufferedImage[24];
	private static BufferedImage [] smallBigLegImg = new BufferedImage[24];
	private boolean big = false;
	private int directionIndex = 0;
	private static final int BONUSPOINTS = 200;
	
	static {
		try {
			BufferedImage tempImg = ImageIO.read(GameWorld.class.getResource("Resources/Bigleg_strip24.png"));
			for (int i = 0, xCoord = 0, yCoord = 0, width = tempImg.getWidth() / 24, height = tempImg.getHeight(); i < 24; i++) {
				bigLegImg[i] = tempImg.getSubimage(xCoord, yCoord, width, height);
				xCoord += width;
			}
			width = bigLegImg[0].getWidth();    // width = 80
			height = bigLegImg[0].getHeight();    // height = 80
			
			tempImg = ImageIO.read(GameWorld.class.getResource("Resources/Bigleg_small_strip24.png"));
			for (int i = 0, xCoord = 0, yCoord = 0, width = tempImg.getWidth() / 24, height = tempImg.getHeight(); i < 24; i++) {
				smallBigLegImg[i] = tempImg.getSubimage(xCoord, yCoord, width, height);
				xCoord += width;
			}
			smallWidth = smallBigLegImg[0].getWidth();    // smallWidth = 40
			smallHeight = smallBigLegImg[0].getHeight();    // smallHeight = 40
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public BigLeg(int x, int y, boolean big, GameWorld game) {
		super(x, y, game);
		this.big = big;
	}

	@Override
	public void draw(Graphics g) {
		
		g.drawImage(big ? bigLegImg[directionIndex] : smallBigLegImg[directionIndex], super.getX(), super.getY(), null);
		update();
	}
	
	private void update() {
		directionIndex = (++directionIndex) % 24;
	}

	@Override
	public Rectangle getRec() {
		return big ? new Rectangle(super.getX(), super.getY(), width, height) : new Rectangle(super.getX(), super.getY(), smallWidth, smallHeight);
	}
	
	@Override
	public void update(Observable obj, Object arg) {
		GameEvents ge = (GameEvents) arg;
		if (2 == ge.getType() && this == ge.getTarget()) {
			new SoundPlayer(2, "Resources/Sound_bigleg.wav").play();
			GameWorld game = super.getGame();
			game.setScore(game.getScore() + BONUSPOINTS);
			super.setDamaged(true);
			game.getListBigLegs().remove(this);
		}
	}

	public static int getBonuspoints() {
		return BONUSPOINTS;
	}
}
