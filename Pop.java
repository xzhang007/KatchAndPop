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

public class Pop extends MovableObject {
	private static int WIDTH, HEIGHT;
	private static final int BRICKWIDTH = 20;
	private static Random generator = new Random();
	private int rotationIndex = generator.nextInt(45);
	private static final double GRAVITY = 0.01;
	private double xVelocity = 0, yVelocity = 0;
	private static BufferedImage [] popImg = new BufferedImage[45];
	
	static {
		try {
			BufferedImage tempImg = ImageIO.read(GameWorld.class.getResource("Resources/Pop_strip45.png"));
			for (int i = 0, xCoord = 0, yCoord = 0, width = tempImg.getWidth() / 45, height = tempImg.getHeight(); i < 45; i++) {
				popImg[i] = tempImg.getSubimage(xCoord, yCoord, width, height);
				xCoord += width;
			}
			WIDTH = popImg[0].getWidth();    // WIDTH = 35
			HEIGHT = popImg[0].getHeight();    // HEIGHT = 35
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Pop(int x, int y, int speed, GameWorld game) {
		super(x, y, game);
		super.setSpeed(speed);
		super.setDirectionIndex(11);
		xVelocity += Math.cos(Math.toRadians(super.getDirectionIndex() * 8)) * speed;
		yVelocity -= Math.sin(Math.toRadians(super.getDirectionIndex() * 8)) * speed;
	}
	
	@Override
	public void draw(Graphics g) {
		
		move();
		g.drawImage(popImg[rotationIndex], super.getX(), super.getY(), null);
		
	}
	
	@Override
	protected void move() {  
		GameWorld game = super.getGame();
		
		if (super.getY() > game.getGameheight()) {    // drop off
			new SoundPlayer(2,"Resources/Sound_lost.wav").play();
			super.setDamaged(true);
		}
		
		int x = super.getX();
		int y = super.getY();
		super.setOldX(x);
		super.setOldY(y);
		
		rotationIndex = (++rotationIndex) % 45;
		
		int directionIndex = super.getDirectionIndex();
		if (directionIndex < 0) {
			super.setDirectionIndex(directionIndex + 45);
		}
		
		x += xVelocity;
		super.setX(x);
		yVelocity += GRAVITY;
		y += yVelocity;
		super.setY(y);
		
		
	    if (super.getX() < BRICKWIDTH || super.getX() + WIDTH > game.getWidth() - BRICKWIDTH) {
	    	new SoundPlayer(2, "Resources/Sound_wall.wav").play();
	    	
	    	xVelocity = 0 - xVelocity;
	    	super.setX(super.getOldX());
	    }
	    
	    if (super.getY() < BRICKWIDTH) {
	    	new SoundPlayer(2, "Resources/Sound_wall.wav").play();
	    	
	    	yVelocity = 0 - yVelocity;
	    	super.setY(super.getOldY());
	    }
	    
	}
	
	@Override
	public Rectangle getRec() {
		return new Rectangle(super.getX(), super.getY(), WIDTH, HEIGHT);
	}
	
	
	@Override
	public void update(Observable obj, Object arg) {
		GameEvents ge = (GameEvents) arg;
		if (2 == ge.getType() && this == ge.getCaller()) {
			Thing thing = (Thing) ge.getTarget();
			if (thing instanceof Katch) {
				new SoundPlayer(2, "Resources/Sound_katch.wav").play();
				
				int x1 = ge.getNumber()[0];
				int x2 = ge.getNumber()[1];
				int x = super.getX();
				if (x < x1 + x2) {
					super.setDirectionIndex(16);
				} else if (x < x1 + 2 * x2) {
					super.setDirectionIndex(14);
				} else if (x < x1 + 3 * x2) {
					super.setDirectionIndex(11);
				} else if (x < x1 + 4 * x2) {
					super.setDirectionIndex(9);
				} else {
					super.setDirectionIndex(6);
				}
				
				xVelocity += Math.cos(Math.toRadians(super.getDirectionIndex() * 8)) * super.getSpeed();
				yVelocity -= Math.sin(Math.toRadians(super.getDirectionIndex() * 8)) * super.getSpeed();
			} else if (thing instanceof Block) {
				int number = ge.getNumber()[0];
				if (0 == number) {    // up or down collision
					yVelocity = 0 - yVelocity;
					super.setY(super.getOldY());
				} else {    // left or right collision
					xVelocity = 0 - xVelocity;
					super.setX(super.getOldX());
				}
				
				new SoundPlayer(2, "Resources/Sound_block.wav").play();
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

