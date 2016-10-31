import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Random;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.List;

public class Katch extends Player {
	private static int WIDTH, HEIGHT;
	private static final int BRICKWIDTH = 20;
	//private int life = 100;
	private static BufferedImage [] katchImg = new BufferedImage[24];
	//private int clock = 10;
	//private int score = 0;
	
	static {
		try {
			BufferedImage tempImg = ImageIO.read(GameWorld.class.getResource("Resources/Katch_strip24.png"));
			for (int i = 0, xCoord = 0, yCoord = 0, width = tempImg.getWidth() / 24, height = tempImg.getHeight(); i < 24; i++) {
				katchImg[i] = tempImg.getSubimage(xCoord, yCoord, width, height);
				xCoord += width;
			}
			WIDTH = katchImg[0].getWidth();
			HEIGHT = katchImg[0].getHeight();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Katch(int x, int y, int speed, GameWorld game) {
		super(x, y, game);
		super.setSpeed(speed);
	}
	

	@Override
	public void draw(Graphics g) {
		
		int directionIndex = super.getDirectionIndex();
		if (directionIndex < 0) {
			super.setDirectionIndex(directionIndex + 24);
		}
		move();
		g.drawImage(katchImg[super.getDirectionIndex()], super.getX(), super.getY(), null);
		
	}
	
	
	@Override
	protected void move() {   
		int x = super.getX();
	    if (x < BRICKWIDTH) {
	    	super.setX(BRICKWIDTH);
	    }
	    
	    GameWorld game = super.getGame();
	    if (x + WIDTH > game.getWidth() - BRICKWIDTH) {
	    	super.setX(game.getWidth() - BRICKWIDTH - WIDTH);
	    }
	}
	
	
	
	public Rectangle getRec() {
		return new Rectangle(super.getX(), super.getY(), WIDTH, HEIGHT);
	}
	
	@Override
	public void update(Observable obj, Object arg) {
		GameEvents ge = (GameEvents) arg;
		if (1 == ge.getType()) {
			KeyEvent e = (KeyEvent) ge.getEvent();
			int directionIndex = super.getDirectionIndex();
			int x = super.getX();
			switch (e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					super.setDirectionIndex((++directionIndex) % 24);
					super.setX(x - super.getSpeed());
			break;
				case KeyEvent.VK_RIGHT:
					super.setDirectionIndex((--directionIndex) % 24);
					super.setX(x + super.getSpeed());
			break;
			} 
		}
	}
	

	/*public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}*/
	
	/*public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}*/
	
	
	public static int getWIDTH() {
		return WIDTH;
	}

	public static int getHEIGHT() {
		return HEIGHT;
	}
}
