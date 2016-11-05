package control;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

import model.ClumpOfOysters;
import model.ConcreteWalls;
import model.Gabion;
import model.GabionBuilder;
import model.Oysters;
import model.Shore;
import model.Wave;
import view.Game;
import view.Scale;

/**
 * 
 * @author Jackson Jorss
 * @author Jael Flaquer
 * @author Ben Clark
 * @author Robert Lee
 * 
 *
 */

public class GameLoopController {
	private Game game;
	private Scale scale;
	private Point click;
	private GabionBuilder gb = new GabionBuilder();
	
	
	// list of entities
	private ArrayList<Wave> waves = new ArrayList<Wave>();
	private ArrayList<Gabion> gabions = new ArrayList<Gabion>();
	private ArrayList<ClumpOfOysters> oysters = new ArrayList<ClumpOfOysters>();
	private ArrayList<ConcreteWalls>concreteWalls = new ArrayList<ConcreteWalls>();
	// list of rectangles
	private ArrayList<Rectangle2D> waveRects = new ArrayList<Rectangle2D>();
	private ArrayList<Rectangle2D> gabionRects = new ArrayList<Rectangle2D>();
	private ArrayList<Rectangle2D> oysterRects = new ArrayList<Rectangle2D>();
	private ArrayList<Rectangle2D> concreteRects = new ArrayList<Rectangle2D>();

	private Shore shore = new Shore(0, 0);
	// must be initialized so collision does throw a null pointer exception
	private Rectangle2D shore1;
	private Rectangle2D builder;
	private Rectangle2D gabionBuilder;
	private Rectangle2D plantBuilder;
	
	private double gX ;
	private double gY;

	

	public GameLoopController(Game game, Scale scale) {
		this.game = game;
		this.scale = scale;
		System.out.println(game.getBounds().getWidth());
		
		
//		concreteWalls.add(new ConcreteWalls((int)shore1.getWidth(), 0));
//		concreteWalls.add(new ConcreteWalls((int)shore1.getWidth(), (int)shore1.getHeight()/4));
//		concreteWalls.add(new ConcreteWalls((int)shore1.getWidth(), (int)(2*(shore1.getHeight()/4))));
//		concreteWalls.add(new ConcreteWalls((int)shore1.getWidth(), (int)(3*(shore1.getHeight()/4))));
//		for (int i = 0; i < concreteWalls.size(); i++) {
//			concreteRects.add(new Rectangle2D.Double((double)concreteWalls.get(i).getX(),(double)concreteWalls.get(i).getY(),
//					20, (double)shore1.getHeight()/4));
//		}
	}
	public void init() {
		waves.add(new Wave(1, 120, 10));
		waves.add(new Wave(1, 120, 20));
		waves.add(new Wave(1, 130, 10));
		waveRects.add(new Rectangle2D.Double(0, 0, 0, 0));
		waveRects.add(new Rectangle2D.Double(0, 0, 0, 0));
		waveRects.add(new Rectangle2D.Double(0,0,0,0));
		
		//shore1 = new Rectangle2D.Double(shore.getX() * game.getScale().getGridSize(), shore.getY() * game.getScale().getGridSize(), 300, 800);
		gX = game.getScale().getWidth() - 27*game.getScale().getGridSize();
		gY = game.getScale().getHeight() - 26*game.getScale().getGridSize();
		
		shore1 = new Rectangle2D.Double(shore.getX() * game.getScale().getGridSize(), shore.getY() * game.getScale().getGridSize(),
				60*game.getScale().getGridSize(), game.getScale().getHeight());
		
		//shore = new Shore((int)shore1.getWidth(), (int)shore1.getHeight());
		builder = new Rectangle2D.Double(120 *game.getScale().getGridSize(),58 *game.getScale().getGridSize(),300,200);
		gabionBuilder = new Rectangle2D.Double(gX,gY , 27 * game.getScale().getGridSize(), 26*game.getScale().getGridSize());
		plantBuilder = new Rectangle2D.Double(120*game.getScale().getGridSize(), 58*game.getScale().getGridSize(), 100, 200);
	}
	
	

	/**
	 * The main loop for the game where all the instantiated object's tick methods
	 * get called.
	 */
	public void loop() {
		for (int i = 0; i < waves.size(); i++) {
			if (waves.get(i).isVisable()) {
				waves.get(i).move();
			} else {
				waves.remove(i);
				waveRects.remove(i);
			}
		}
		//System.out.println(waves.size());
		for (int i = 0; i < oysters.size(); i++) {
			if (!oysters.get(i).isVisible()) {
				oysters.remove(i);
				oysterRects.remove(i);
			}
		}
		for (int i = 0; i < concreteWalls.size(); i++) {
			if (!concreteWalls.get(i).isVisible()) {
				concreteWalls.remove(i);
				concreteRects.remove(i);
			}
		}
		// oyster logic
		if (oysters.size() < 4) {
			Random rand = new Random(); 
			int shore = (int)shore1.getWidth() / scale.getGridSize();
			System.out.println(shore);
			int x = rand.nextInt(100) + shore;
			x = x*scale.getGridSize() + (int)(shore1.getWidth() / scale.getGridSize());
			int y = (rand.nextInt(50)+ 10) * scale.getGridSize();
			oysters.add(new ClumpOfOysters(x, y));
			oysterRects.add(new Rectangle2D.Double(x,y,10,10));
			System.out.println("X:" + x + "\t" + "Y: " + y);
		}
		
		
		
		// collision detections
		collision();
		
		//System.out.println("I'm looping");
	}

	/**
	 * The main loop for the game were all the instantiated object's render
	 * methods get called.
	 */

	public void render(Graphics g, int scale) {
		Graphics2D g2 = (Graphics2D) g;
		// wave1 = new Rectangle2D.Double(wave.getX()* scale,wave.getY()* scale,
		// 50, 50);
		// creating new rectangles and tieing them to waves
		for (int i = 0; i < waveRects.size(); i++) {
			waveRects.set(i, new Rectangle2D.Double(waves.get(i).getX() * scale, waves.get(i).getY() * scale, 50, 50));
		}
		
		// abstract way
		g2.setColor(Color.BLUE);
		for (Rectangle2D rect : waveRects) {
			g2.draw(rect);
			g2.fill(rect);
		}

		g2.setColor(Color.BLACK);
		for (Rectangle2D gabions : gabionRects) {
			g2.draw(gabions);
			g2.fill(gabions);
		}
		g2.setColor(Color.GRAY);
		for (Rectangle2D oyster : oysterRects) {
			g2.draw(oyster);
			g2.fill(oyster);
		}
		for (Rectangle2D wall : concreteRects) {
			g2.draw(wall);
			g2.fill(wall);
		}

		// single way
		shore1 = new Rectangle2D.Double(shore.getX() * game.getScale().getGridSize(), shore.getY() * game.getScale().getGridSize(),
				60*game.getScale().getGridSize(), game.getScale().getHeight());
		g2.setColor(Color.YELLOW);
		g2.fill(shore1);
		g2.draw(shore1);
		
		
		// UI
		//---------------------------
		// Gabion builder/Plant builder
		
		
		
		
		g2.setColor(Color.BLACK);
		g2.draw(builder);
		
		g2.setColor(Color.GRAY);
		g2.fill(gabionBuilder);
		g2.draw(gabionBuilder);
		
		Font f1 = new Font("Arial", 50, 100);
		g2.setFont(f1);
		g2.setColor(Color.CYAN);
		g2.drawString("" + gb.getNumberOfOysters(), 135 * scale,80*scale);
		g2.setColor(Color.WHITE);
		g2.drawString("" + gb.getGabions(), 150 * scale,80*scale);
		
		g2.setColor(Color.GREEN);
		g2.fill(plantBuilder);
		g2.draw(plantBuilder);
		
		
	}

	public void collision() {
		for (int i = 0; i < waveRects.size(); i++) {
			if (waveRects.get(i).intersects(shore1.getX(), shore1.getY(), shore1.getWidth(), shore1.getHeight())) {
				// wave hit shore
				// erode shore
				shore.erode();
				// set wave to not visible to get deleted in logic
				System.out.println("Wave Hit Shore");
				waves.get(i).setVisable(false);
			}

		}

		for (int i = 0; i < waveRects.size(); i++) {
			// if a wave hits a gabion, remove wave
			// PUT CHANGE OF HEALTH HERE IF WE DECIDE TO GO WITH HEALTH FOR
			// GABIONS
			for (int j = 0; j < gabionRects.size(); j++) {
				
				if (gabionRects.get(j).intersects(waveRects.get(i).getX(), waveRects.get(i).getY(),
						waveRects.get(i).getWidth(), waveRects.get(i).getHeight())) {
					// set wave to not visible to get deleted in logic
					waves.get(i).setVisable(false);
				}
			}
		}
		
		for (int i = 0; i < waveRects.size(); i++) {
			for (int j = 0; j < concreteRects.size(); j++) {
				if (concreteRects.get(j).intersects(waveRects.get(i).getX(), waveRects.get(i).getY(),
						waveRects.get(i).getWidth(), waveRects.get(i).getHeight())) {
					waves.get(i).setVisable(false);
					concreteWalls.get(j).setVisible(false);
				}
			}
		}

	}

	public void handlePlaceGabion(Point p) {
		System.out.println(p.getX() + ", " + p.getY());
		// spawn gabion
		if (gb.getGabions() != 0) {
			gabions.add(new Gabion((int) p.getX(), (int) p.getY()));
			gabionRects.add(new Rectangle.Double(p.getX(), p.getY(), 50, 50));
			gb.setGabions(gb.getGabions() - 1);
		}
	}
	
	public void handleCollectOyster(Point p, int i) {
		int numOfClusters = 0;
		
		numOfClusters = oysters.get(i).getNumOfOystersInClump();
		oysters.get(i).setVisible(false);
		// adding GB stuff		
		gb.build(numOfClusters);
		System.out.println(p);
	}
	
	public void handleClick(Point p) {
		for (int i = 0; i < oysterRects.size(); i++) {
			if (oysterRects.get(i).contains(p)) {
				handleCollectOyster(p, i);
				return;
			}
		}
		handlePlaceGabion(p);
		return;
	}

}
