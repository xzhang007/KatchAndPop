import java.awt.*;
import java.awt.event.*;
import java.awt.Image.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;


public class GameWorld extends JPanel implements Runnable {
	private Thread thread;
	// GameWorld is a singleton class!
    private static final GameWorld game = new GameWorld();
    
	private static final int GAMEWIDTH = 640;
	private static final int GAMEHEIGHT = 480;
	private BufferedImage bimg = null;
	private Graphics2D g2;
	private int screenNumber = 0;
	private MapLoader mapLoader = MapLoader.getInstance();    // singleton
	private GameEvents gameEvents = new GameEvents();
	private CollisionDetector collisionDetector = new CollisionDetector(this);
	
	private Katch katch;
	private Pop pop;
	private List<BigLeg> bigLegs = new ArrayList<BigLeg>();
	//List<? extends Thing> bigLegs = new ArrayList<BigLeg>();
	private List<Thing> things = new ArrayList<Thing>();
	
	private static Image background, startImage, endImage, smallKatchImage;
	private static Image [] buttonImg = new Image[5];
	private JButton startButton;
	private SoundPlayer sp;
	private int score;
	private int life = 10;
	
	public void init() {
		//addKeyListener(new KeyControl());
		try {
			background = ImageIO.read(GameWorld.class.getResource("Resources/background1.png"));
			startImage = ImageIO.read(GameWorld.class.getResource("Resources/Title.png"));
			endImage = ImageIO.read(GameWorld.class.getResource("Resources/Title2.png"));
			smallKatchImage = ImageIO.read(GameWorld.class.getResource("Resources/Katch_small.png"));
			buttonImg[0] = ImageIO.read(GameWorld.class.getResource("Resources/Button_start.png"));
			buttonImg[1] = ImageIO.read(GameWorld.class.getResource("Resources/Button_load.png"));
			buttonImg[2] = ImageIO.read(GameWorld.class.getResource("Resources/Button_help.png"));
			buttonImg[3] = ImageIO.read(GameWorld.class.getResource("Resources/Button_scores.png"));
			buttonImg[4] = ImageIO.read(GameWorld.class.getResource("Resources/Button_quit.png"));
			startButton = new JButton(new ImageIcon(buttonImg[0]));
			//startButton.setFocusable(false);    // important!
			startButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent event) {
					screenNumber++;
					initialScreen();
				}
			});
			startButton.setFocusable(false);    // important!
			sp = new SoundPlayer(1, "Resources/Music.wav");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void drawStartPannel(Graphics g) {
		g.drawImage(background, 0, 0, 640, 480, this);
		g.drawImage(startImage, 0, 0, this);
		int x = 16, y = 416;
		int size = buttonImg.length;
		for (int i = 0; i < size; i++) {
			g.drawImage(buttonImg[i], x, y, this);
			x += 128;
		}
		add(startButton);
		startButton.setBounds(16, 416, 96, 32);
	}
	
	
	private void drawBackGroundWithTileImage(Graphics g) {
		g.drawImage(background, 0, 0, this);
	}
	
	
	private void addToThingsList(List<Thing> things, List<Location> thingsMap) {
		int size = thingsMap.size();
		for (int i = 0; i < size; i++) {
			Location location = thingsMap.get(i);
			int x = location.getX();
			int y = location.getY();
			switch (location.getThing()) {
			case "Wall":
				Thing singleWall = new Wall(x, y, this);
				things.add(singleWall);
			break;
			case "Block":
				Thing block = new Block(x, y, this, location.getNumber());
				things.add(block);
			break;
			}
		}
 	}
	
	
	private void getWallList() {
		mapLoader.read(screenNumber == 1 ? "Resources/wallMap1" : "Resources/wallMap2");
		List<Location> thingsMap = mapLoader.getThingsMap();
		addToThingsList(things, thingsMap);
	}
	
	private void drawWall(Graphics g) {
		if (things.isEmpty()) {
			getWallList();
		}
		//int size = things.size();
		for (int i = 0; i < things.size(); i++) {    // important! not size but things.size()
			things.get(i).draw(g);
		}
	}
	
	private void drawSmallKatch(Graphics g) {
		int xPos = 40;
		for (int i = 0; i < life; i++) {
			g.drawImage(smallKatchImage, xPos, 420, null);
			xPos += smallKatchImage.getWidth(null) + 10;
		}
	}
	
	private void cleanList() {
		things.clear();
	}
	
	private void initialScreen() {
		if (1 == screenNumber) {
			katch = new Katch(280, 400, 20, this);
			pop = new Pop(280, 300, 5, this);
			bigLegs.add(new BigLeg(280, 50, true, this));
			//bigLegs.add(new BigLeg(120, 50, false, this));
			//bigLegs.add(new BigLeg(440, 50, false, this));
			//bigLegs.add(new MovingBigLeg(440, 50, false, this));
		} else if (2 == screenNumber) {
			bigLegs.add(new MovingBigLeg(440, 20, true, this));
			bigLegs.add(new MovingBigLeg(240, 120, false, this));
			katch = new Katch(280, 400, 10, this);
			pop = new Pop(280, 300, 5, this);
		}
	}
	
	private void drawDemo(Graphics g) {
		switch (screenNumber) {
		case 0:
			drawStartPannel(g);
		break;
		case 1:
			if (bigLegs.isEmpty()) {
				cleanList();
				screenNumber++;
				initialScreen();
			}
			
			drawScreen(g);
		break;
		case 2:
			if (bigLegs.isEmpty()) {
				cleanList();
				screenNumber++;
				//initialScreen();
			}
			
			drawScreen(g);
		break;
		case 3:
			drawFinalPannel(g);
		}
	}
	
	private void testLife() {
		if (pop.isDamaged()) {    // loose a life
			life--;
			pop = new Pop(280, 300, 5, this);
			pop.setDamaged(false);    // important
		}
	}
	
	private void drawScreen(Graphics g) {
		testLife();
		
		if (life < 0) {    // game over
			screenNumber = 3;
			return;
		}
		
		drawBackGroundWithTileImage(g);
		drawWall(g);
		drawSmallKatch(g);
		
		for (int i = 0; i < bigLegs.size(); i++) {
			Thing bigLeg = bigLegs.get(i);
			bigLeg.draw(g);
			if (bigLeg instanceof MovingBigLeg) {
				MovingBigLeg movingBigLeg = (MovingBigLeg) bigLeg;
				//movingBigLeg.collideWith(things);
				collisionDetector.collideWith(movingBigLeg, things);
			}
		}
		
		collisionDetector.collideWith(pop, things);
		collisionDetector.collideWith(pop, bigLegs);
		collisionDetector.collideWith(pop, katch);
		katch.draw(g);
		pop.draw(g);
		Font font = g.getFont();
		Font newFont = new Font("Dialog", Font.BOLD, 12);
		g.setFont(newFont);
		g.drawString("Score: " + score, 40, 400);
		g.setFont(font);
	}
	
	private void drawFinalPannel(Graphics g) {
		g.drawImage(background, 0, 0, 640, 480, this);
		g.drawImage(endImage, 0, 0, this);
		
		Font font = g.getFont();
		Font newFont = new Font("Dialog", Font.BOLD, 50);
		g.setFont(newFont);
		g.drawString("Score: " + score, 200, 380);
		g.setFont(font);
	}
	
	public void paint(Graphics g) {
		if (bimg == null) {
			//Dimension windowSize = getSize();
			//bimg = (BufferedImage) createImage(windowSize.width, windowSize.height);
			bimg = (BufferedImage) createImage(GAMEWIDTH, GAMEHEIGHT);
			g2 = bimg.createGraphics();
		}
		drawDemo(g2);
		g.drawImage(bimg, 0, 0, this);
		
	}
	
	public void start() {
        thread = new Thread(this);
        thread.setPriority(Thread.MIN_PRIORITY);
        thread.start();
    }
	
	public void run() {
        Thread me = Thread.currentThread();
        while (thread == me) {
            repaint();  
          try {
                thread.sleep(23);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
	
	
	public static GameWorld getInstance(){
    	return game;
    }
	
	public List<Thing> getListThings() {
		return things;
	}
	
	public List<BigLeg> getListBigLegs() {
		return bigLegs;
	}
	
	public GameEvents getObservable() {
		return gameEvents;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public static int getGamewidth() {
		return GAMEWIDTH;
	}

	public static int getGameheight() {
		return GAMEHEIGHT;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	
}




