package control;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import model.Animation;
import model.ConcreteWalls;
import model.Gabion;
import model.GabionBuilder;
import model.GameState;
import model.BlueCrab;
import model.PlantBuilder;
import model.Shore;
import model.Timer;
import model.TutorialState;
import view.Game;
import view.Scale;

/**
 * 
 * @author Jackson Jorss
 * @author Jael Flaquer
 * @author Ben Clark
 * @author Robert Ley
 * 
 *
 */

public class GameLoopController implements Serializable{
	private Game game;
	private BufferedImageController bic;
	private Scale scale;
	private Point click;
	private GabionBuilderController gb;
	private Spawner spawner;
	private Timer timer;
	private Timer plantTimer;
	private Timer textTimer;
	private Timer runOffTimer;
	private Timer cleanWaterTimer;
	private Timer crabSpeakingTimer;
	private Timer waveAnimationTimer;
	private PlantBuilderController pb;
	private BlueCrabController helperHorse;
	private AnimationController ac;

	private ArrayList<Integer> numOfGabionsInRow;
	private ArrayList<Integer> numOfWavesInRow;

	// list of entities
	private ArrayList<WaveController> waves;
	private ArrayList<GabionController> gabions;
	private ArrayList<OysterController> oysters;
	private ArrayList<ConcreteWallController> concreteWalls;
	private ArrayList<PlantController> plants;
	private ArrayList<RunOffController> runOff;
	private ArrayList<BlueCrabController> hsCrab;
	private ArrayList<AnimationController> animations;
	// list of rectangles
	private ArrayList<Rectangle2D> waveRows;
	private ArrayList<Rectangle2D> plantRows;

	private ShoreController shore;

	private Rectangle2D GAMEBOX;
	private Rectangle2D UIBOX;
	private Rectangle2D MENU;
	private RoundRectangle2D gameOverBox;
	private RoundRectangle2D creditsBox;
	// private Rectangle2D gabionBuilder;
	// private Rectangle2D plantBuilder;
	private Rectangle2D uiGabion;
	private Rectangle2D uiPlant;
	private Rectangle2D helperHorseRect;

	private Rectangle2D tutorialButton;
	private Rectangle2D playButton;
	private Rectangle2D creditButton;
	private Rectangle2D backButton;

	private boolean renderDragGabion;
	private boolean renderDragPlant;
	private boolean eroded;
	private boolean isRunOff;
	private boolean placedFirstGabion;
	private boolean placedWrongGabion;
	private boolean placedWrongPlant;
	private boolean placedFirstPlant;
	private boolean ableToPlaceGabion;
	private boolean ableToPlacePlant;
	private boolean init;
	private boolean hittingPlant;
	private boolean hittingWater;
	private boolean speaking;
	private boolean renderCredits;

	private double gX;
	private double gY;
	private double gbPadding;
	private double gabionWidth;
	private double gabionHeight;
	private double uiPlantWidth;
	private double uiPlantHeight;
	private double concreteWallWidth;

	private int fontSize;
	private int numOfRows = 7;
	private int totalNumOfGabions;

	private Font f1;

	private String time;
	private String message = "";
	private String grade = "";

	private GameState currentGameState = GameState.LOADING;

	private TutorialState currentTutorialState = TutorialState.OYSTERS;

	private Color ShoreColor = new Color(255, 200, 100, 255);
	private Color dirtyWater = new Color(184, 138, 0, 0);

	public GameLoopController(Game game, Scale scale) {
		this.game = game;
		this.scale = scale;
		System.out.println(game.getBounds().getWidth());
		//this.init();

	}

	/**
	 * Initializes all objects in the game and sets up the game.
	 */
	public void init() {
		bic = new BufferedImageController();
		gb = new GabionBuilderController(new GabionBuilder(), new Rectangle2D.Double(0, 0, 0, 0));
		timer = new Timer();
		spawner = new Spawner(this, game, timer);
		plantTimer = new Timer();
		textTimer = new Timer();
		runOffTimer = new Timer();
		cleanWaterTimer = new Timer();
		crabSpeakingTimer = new Timer();
		waveAnimationTimer = new Timer();
		pb = new PlantBuilderController(new PlantBuilder(plantTimer), new Rectangle2D.Double(0, 0, 0, 0));

		numOfGabionsInRow = new ArrayList<Integer>();
		numOfWavesInRow = new ArrayList<Integer>();

		// list of entities
		waves = new ArrayList<WaveController>();
		gabions = new ArrayList<GabionController>();
		oysters = new ArrayList<OysterController>();
		concreteWalls = new ArrayList<ConcreteWallController>();
		plants = new ArrayList<PlantController>();
		runOff = new ArrayList<RunOffController>();
		hsCrab = new ArrayList<BlueCrabController>();
		animations = new ArrayList<AnimationController>();

		waveRows = new ArrayList<Rectangle2D>();
		plantRows = new ArrayList<Rectangle2D>();

		shore = new ShoreController(new Shore(0, 0), new Rectangle2D.Double(0, 0, 0, 0));

		GAMEBOX = new Rectangle2D.Double(0, 0, 0, 0);
		UIBOX = new Rectangle2D.Double(0, 0, 0, 0);
		MENU = new Rectangle2D.Double(0, 0, 0, 0);
		gameOverBox = new RoundRectangle2D.Double(0,0,0,0,0,0);
		creditsBox = new RoundRectangle2D.Double(0,0,0,0,0,0);
		uiGabion = new Rectangle2D.Double(0, 0, 0, 0);
		uiPlant = new Rectangle2D.Double(0, 0, 0, 0);
		helperHorseRect = new Rectangle2D.Double(0, 0, 0, 0);

		tutorialButton = new Rectangle2D.Double(0, 0, 0, 0);
		playButton = new Rectangle2D.Double(0, 0, 0, 0);
		creditButton = new Rectangle2D.Double(0, 0, 0, 0);
		backButton = new Rectangle2D.Double(0,0,0,0);

		renderDragGabion = false;
		renderDragPlant = false;
		eroded = false;
		isRunOff = false;
		placedFirstGabion = false;
		placedWrongGabion = false;
		placedWrongPlant = false;
		placedFirstPlant = false;
		ableToPlaceGabion = false;
		ableToPlacePlant = false;
		init = false;
		hittingPlant = false;
		hittingWater = false;
		speaking = false;
		renderCredits = false;

		time = "" + timer.getTime();

		ShoreColor = new Color(255, 200, 100, 255);
		dirtyWater = new Color(184, 138, 0, 0);
		
		int scale = this.game.getScale().getGridSize();
		double width = this.game.getScale().getWidth();
		double height = this.game.getScale().getHeight();
		double uiBoxHeight = this.game.getScale().getHeight() * 0.165;
		double shoreWidth = this.game.getScale().getWidth() * 0.35;
		double menuWidth = width * 0.2;
		double menuHeight = height * 0.4;

		concreteWallWidth = width * 0.01;
		gbPadding = width * 0.015;

		spawner = new Spawner(this, this.game, this.timer);
		UIBOX = new Rectangle2D.Double(0, 0, width, uiBoxHeight);
		GAMEBOX = new Rectangle2D.Double(0, this.UIBOX.getHeight(), width, height - this.UIBOX.getHeight());
		MENU = new Rectangle2D.Double((width / 2) - (menuWidth / 2), (height / 2) - (menuHeight / 2), menuWidth,
				menuHeight);
		double goWidth = game.getScale().getWidth() * 0.4;
		double goHeight = game.getScale().getHeight() * 0.4;
		gameOverBox = new RoundRectangle2D.Double((game.getScale().getWidth()/2) - (goWidth/2),
				(game.getScale().getHeight()/2) - (goHeight/2),goWidth,goHeight, 50, 50);
		creditsBox = new RoundRectangle2D.Double((game.getScale().getWidth()/2) - (goWidth/2),
				(game.getScale().getHeight()/2) - (goHeight/2),goWidth,goHeight, 50, 50);
		
		backButton = new Rectangle2D.Double((int)creditsBox.getX(), (int)creditsBox.getY(),
				(int)(creditsBox.getWidth()*0.2), (int)(creditsBox.getHeight()*0.18));
				
		tutorialButton = new Rectangle2D.Double(MENU.getX(), MENU.getY(), menuWidth, menuHeight*0.3);
		playButton = new Rectangle2D.Double(MENU.getX(), MENU.getY() + menuHeight / 3, menuWidth,
				menuHeight*0.3);
		creditButton = new Rectangle2D.Double(MENU.getX(), MENU.getY() + ((menuHeight / 3) * 2), menuWidth,
				menuHeight*0.3);

		this.shore.setShore(new Shore((int) this.GAMEBOX.getX(), (int) this.GAMEBOX.getY()));
		this.shore.setRect(new Rectangle2D.Double(shore.getShore().getX(), shore.getShore().getY(), (int) shoreWidth,
				GAMEBOX.getHeight()));

		fontSize = (int) (width * 0.03);
		f1 = new Font("Arial", Font.PLAIN, this.fontSize);
		this.totalNumOfGabions = 0;
		
		for (int i = 0; i < this.numOfRows; i++) {
			waveRows.add(new Rectangle2D.Double(shore.getRect().getWidth() + concreteWallWidth,
					(UIBOX.getHeight() + (GAMEBOX.getHeight() / this.numOfRows) * i),
					GAMEBOX.getWidth() - shore.getRect().getWidth(), GAMEBOX.getHeight() / this.numOfRows));
			plantRows.add(new Rectangle2D.Double(shore.getRect().getWidth() - (shore.getRect().getWidth() * 0.2),
					(UIBOX.getHeight() + (GAMEBOX.getHeight() / this.numOfRows) * i), shore.getRect().getWidth() * 0.2,
					GAMEBOX.getHeight() / this.numOfRows));

			concreteWalls.add(new ConcreteWallController(
					new ConcreteWalls((int) shore.getRect().getWidth(),
							(int) (GAMEBOX.getHeight() / this.numOfRows) * i),
					new Rectangle2D.Double(shore.getRect().getWidth(),
							(UIBOX.getHeight() + (GAMEBOX.getHeight() / this.numOfRows) * i), concreteWallWidth,
							GAMEBOX.getHeight() / this.numOfRows)));

			// in initializing row araylist
			this.numOfGabionsInRow.add(0);
			this.numOfWavesInRow.add(0);
			spawner.getPlantsInRow().add(0);
			Random rand = new Random();
			int pattern = rand.nextInt(3) + 1;
			spawner.getPatternInRow().add(pattern);
			spawner.getRunOffInRow().add(false);
			// spawner.spawnPlants(i);
			// spawner.spawnPlants(i);

		}

		gabionWidth = waveRows.get(0).getHeight() - gbPadding;
		gabionHeight = waveRows.get(0).getHeight() - gbPadding;
		uiPlantHeight = UIBOX.getHeight() * 0.7;
		uiPlantWidth = uiPlantHeight * (2.0 / 3.0);

		double gWidth = UIBOX.getWidth() * 0.125;
		gX = UIBOX.getWidth() - gWidth;
		gY = UIBOX.getY();
		gb.setRect(new Rectangle2D.Double(gX, gY, gWidth, UIBOX.getHeight()));

		uiGabion = new Rectangle2D.Double(gb.getRect().getX(), gb.getRect().getCenterY() - (gabionHeight / 2),
				gabionWidth, gabionHeight);

		pb.setRect(
				new Rectangle2D.Double(UIBOX.getX(), UIBOX.getY(), UIBOX.getHeight() * (2.0 / 3.0), UIBOX.getHeight()));

		uiPlant = new Rectangle2D.Double(pb.getRect().getCenterX() - (uiPlantWidth / 2),
				pb.getRect().getCenterY() - (uiPlantHeight / 2.0), uiPlantWidth, uiPlantHeight);

		double cWidth = UIBOX.getWidth() * 0.05;
		double cX = UIBOX.getWidth()*0.2;
		double helperHorseWidth = width * 0.1;
		double helperHorseHeight = height * 0.15;
		BlueCrab helperHorseCrab = new BlueCrab((int) (cX + cWidth), (int) (UIBOX.getY()));

		Rectangle2D helperHorseRect = new Rectangle2D.Double(helperHorseCrab.getX(), helperHorseCrab.getY(),
				helperHorseWidth, helperHorseHeight);
		helperHorse = new BlueCrabController(helperHorseCrab, helperHorseRect);
		hsCrab.add(helperHorse);

		
		
		bic.loadBufferedImage();
		ac = new AnimationController(this, bic, null, null, 0);
		this.init = true;
		System.out.println("game ready");
	}

	/**
	 * The main loop for the game where all the instantiated object's tick
	 * methods get called.
	 */
	public void loop() {
		switch (this.currentGameState) {
		case TUTORIAL:
			collision();
			switch (this.currentTutorialState) {
			case OYSTERS:
				this.moveWaves();
				// System.out.println(this.waves.size());
				this.message = "Collect Oyster Shells!";
				spawner.tutorialSpawnOysters();
				if (gb.getGb().getGabions() >= 2) {
					this.message = "Good Job!";
					// after 3 seconds go to the next state
					textTimer.countUp(3);
					if (textTimer.getTime() >= 3) {
						this.currentTutorialState = TutorialState.WAVES;
						textTimer = new Timer();
					}
				}
				break;
			case WAVES:
				this.moveWaves();
				this.message = "Oh no, waves!";
				spawner.TutorialSpawnWaves();
				textTimer.countUp(3);
				// System.out.println(textTimer.getTime());
				if (textTimer.getTime() >= 3) {
					this.animations.add(new AnimationController(this, bic, Animation.PLACEGABION, null, 1));
					this.animations.add(new AnimationController(this, bic, Animation.PLACEGABION, null, 4));
					this.currentTutorialState = TutorialState.GABIONS;
					textTimer = new Timer();
					this.ableToPlaceGabion = true;
				}
				break;
			case GABIONS:
				if (this.placedWrongGabion) {
					this.message = "Make sure to place your gabions in front of waves.";
				} else {
					this.message = "Stop the waves by placing gabions!";
				}

				// plantTimer.countUp(5);
				pb.getPb().setNumberOfPlants(1);
				// flashing gabion here
				
				
//				if (this.placedFirstGabion) {
//					this.message = "Good Job!";
//					textTimer.countUp(2);
//					if (textTimer.getTime() >= 2) {
//						textTimer = new Timer();
//						animations.clear();
//						this.currentTutorialState = TutorialState.RUNOFF;
//
//					}
//
//				}		
				
				if (this.placedFirstGabion) {
				this.message = "Good Job!";
				textTimer.countUp(7);
				if (textTimer.getTime() >= 2) {
					animations.clear();
					this.message = "Concrete walls are your last line of defense. They are weaker than gabions!";
				}
				if (textTimer.getTime() >= 7) {
					textTimer = new Timer();
					this.currentTutorialState = TutorialState.RUNOFF;
				}
			}
				

				break;
			case RUNOFF:
				this.moveWaves();
				plantTimer.countUp(pb.getPb().getNumOfSecondsPerPlant());
				// textTimer.countUpStop(3);
				spawner.spawnTutorialRunOff();
				if (this.runOff.get(0).getRect().getX() + this.runOff.get(0).getRect()
						.getWidth() >= (shore.getRect().getX() + shore.getRect().getWidth()) / 3) {
					this.message = "Oh no! Runoff!";
					textTimer = new Timer();
					this.animations.add(new AnimationController(this, bic, Animation.PLACEPLANT, null, 2));
					this.currentTutorialState = TutorialState.PLANTS;
				} else {
					this.message = "Look, your Plants are growing!!";
					// flash plant number

				}

				break;
			case PLANTS:
				this.ableToPlacePlant = true;
				plantTimer.countUp(pb.getPb().getNumOfSecondsPerPlant());
				textTimer.countUpStop(2);
				if (textTimer.getTime() >= 2) {
					if (!this.placedWrongPlant) {
						this.message = "Plant plants to filter the dirty runoff.";
					} else {
						this.message = "Make sure to place your plants in front of runoff.";
					}
					if (this.placedFirstPlant) {
						textTimer = new Timer();
						this.message = "Good Job! The water is clean! You now know how to defend your estuary!";
						this.animations.clear();
						this.currentTutorialState = TutorialState.END;
					}
				}
				// spawner.spawnRunOff(1, 0);
				break;
			case END:
				this.moveWaves();
				textTimer.countUpStop(5);
				if (textTimer.getTime() >= 5) {
					textTimer = new Timer();
					timer = new Timer();
					spawner.setTimer(timer);
					this.currentGameState = GameState.GAME;

				}
				break;
			default:
				System.out.println("STATES NOT WORKING, CURRENT STATE: " + this.currentTutorialState);
			}
			break;
		case GAME:
			this.moveWaves();
			timer.countDown();
			spawner.spawn(this.eroded);
			plantTimer.countUp(6);

			if (timer.getTime() == 0) {
				this.grade = this.calculateScore(shore.getShore().getHealth(), this.dirtyWater.getAlpha());
				this.currentGameState = GameState.OVER;
			} else if (shore.getShore().getHealth() <= 25 || this.dirtyWater.getAlpha() >= 200) {
				this.currentGameState = GameState.OVER;
				game.setGameLost(true);
			}
			collision();
			break;
		case PAUSED:
			break;
		case MENU:
			for (Iterator<WaveController> itw = waves.iterator(); itw.hasNext();) {
				WaveController wave = itw.next();
				for (Iterator<GabionController> itg = gabions.iterator(); itg.hasNext();) {
					GabionController gabion = itg.next();
					if (wave.getRect().intersects(gabion.getRect())) {
						itw.remove();
					}
				}
				if (wave.getRect().intersects(shore.getRect())) {
					itw.remove();
				}
			}
			timer.countDown();
			spawner.spawnWaves(5, 0);
			this.moveWaves();
			break;
		case LOADING:
			System.out.println(this.init);
			if (this.init) {
				// System.out.println("Entered");
				this.currentGameState = GameState.MENU;
				gb.getGb().setGabions(this.numOfRows);
				this.ableToPlaceGabion = true;
				this.ableToPlacePlant = true;
				for (int i = 0; i < this.numOfRows; i++) {
					spawner.spawnWaves(5, 0);
					this.handlePlaceGabion(
							new Point((int) this.waveRows.get(i).getX() + 1, (int) this.waveRows.get(i).getY() - 1));
					spawner.spawnPlants((i*2)%(this.numOfRows+1));
				}
				this.ableToPlaceGabion = false;
				this.ableToPlacePlant = false;
			}
			break;
		case OVER:
			game.setGameLost(true);
			break;
		default:
			System.out.println("STATES NOT WORKING, CURRENT STATE: " + this.currentGameState);
		}

		// stuff that always needs to be done

		pb.getPb().build();
		this.removeRunoff();
		this.cleanWater();
		this.removeOyster();
		this.removeAnimations();
	}
	/**
	 * Gets called every tic. Iterates through instantiated waves and moves them.
	 */
	public void moveWaves() {
		// if not gabions and not plants state
		for (WaveController wave : waves) {
			wave.getWave().move();
			Rectangle2D newWave = new Rectangle2D.Double(wave.getWave().getX(), wave.getWave().getY(),
					wave.getRect().getWidth(), wave.getRect().getHeight());
			wave.setRect(newWave);
		}
	}
	/**
	 * If water is below maximum cleanliness this function gradually cleans the water.
	 */
	public void cleanWater() {
		if (!this.hittingWater) {
			cleanWaterTimer.countUp(5);
			int newAlpha = this.dirtyWater.getAlpha();
			if (cleanWaterTimer.getTime() >= 5) {
				newAlpha = this.dirtyWater.getAlpha() - 10;
			}
			if (newAlpha <= 0) {
				newAlpha = 0;
			}
			this.dirtyWater = new Color(this.dirtyWater.getRed(), this.dirtyWater.getGreen(), this.dirtyWater.getBlue(),
					newAlpha);
		}
	}
	/**
	 * Removes animations that have been played.
	 */
	public void removeAnimations() {
		for (AnimationController animation : animations.toArray(new AnimationController[0])) {
			if (animation.isPlayed()) {
				animations.remove(animation);
				System.out.println("removed animation");
			}
		}
//		for (Iterator<AnimationController> it = animations.iterator(); it.hasNext();) {
//		AnimationController a = it.next();
//		if (a.isPlayed()) {
//			it.remove();
//			System.out.println("REMOVED");
//		}
//	}
	}
	/**
	 * Removes oyster after being collected.
	 */
	public void removeOyster() {
		for (Iterator<OysterController> it = oysters.iterator(); it.hasNext();) {
			OysterController oyster = it.next();
			if (!oyster.getOyster().isVisible()) {
				it.remove();
			}
		}
	}
	/**
	 * Removes runoff after the runoff passes the beach. Shrinks runoff once colliding with a plant or the edge of the beach.
	 * Makes water dirty if runoff is at the edge of the beach.
	 */
	public void removeRunoff() {
		for (Iterator<RunOffController> it = runOff.iterator(); it.hasNext();) {
			RunOffController runOff = it.next();
			if (this.currentTutorialState != TutorialState.PLANTS) {
				runOff.getRunOff().move();
			}
			if (shore.getRect().getWidth() + concreteWallWidth + 8 < runOff.getRect().getX()
					+ runOff.getRect().getWidth()) {
				runOff.getRect().setRect(runOff.getRunOff().getX(), runOff.getRunOff().getY(),
						runOff.getRect().getWidth() - runOff.getRunOff().getSpeed(), runOff.getRect().getHeight());
				this.hittingWater = true;
				this.runOffTimer.countUp(1);
				// System.out.println("Plant: " + this.hittingPlant);
				// System.out.println("Timer: " + runOffTimer.getTime());
				// System.out.println("Water: " + this.hittingWater );
				if (!this.hittingPlant && runOffTimer.getTime() >= 1 && this.hittingWater) {
					int newAlpha = this.dirtyWater.getAlpha() + 10;
					if (newAlpha >= 250) {
						newAlpha = 250;
					}
					this.dirtyWater = new Color(this.dirtyWater.getRed(), this.dirtyWater.getGreen(),
							this.dirtyWater.getBlue(), newAlpha);
					// System.out.println("changing color!!!");
				}

				if (runOff.getRect().getWidth() > 0) {
					spawner.getRunOffInRow().set(runOff.getRunOff().getRowNum(), false);

				}
			} else {
				runOff.setRect(new Rectangle2D.Double(runOff.getRunOff().getX(), runOff.getRunOff().getY(),
						runOff.getRect().getWidth(), runOff.getRect().getHeight()));
				this.hittingWater = false;

			}
			if (runOff.getRect().getWidth() <= 0) {
				it.remove();
				// System.out.println("runOff size: " +
				// this.getRunOff().size());
			}

		}
	}

	/**
	 * The main method for the game were all the instantiated object's render
	 * methods get called.
	 */

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		switch (this.currentGameState) {
		case TUTORIAL:
			// UIBOX Sky

			g2.drawImage(bic.getImageAtIndex(Image.SKY.getIndex()), (int) UIBOX.getX(), (int) UIBOX.getY(),
					(int) UIBOX.getWidth(), (int) (UIBOX.getHeight()), null);
			// GAMEBOX
			g2.setColor(new Color(163, 232, 255));
			g2.draw(GAMEBOX);
			g2.fill(GAMEBOX);

			this.renderGabions(g2);
			this.renderWaves(g2);
			this.renderConcreteWalls(g2);
			this.renderShore(g2);
			this.renderHorseshoeCrab(g2);
			this.renderPlants(g2);

			// UI
			// ---------------------------
			// Gabion builder/Plant builder
			// this.renderGabionBuilder(g2);

			// GabionBuilder Meter
			// this.renderGabionMeter(g2);
			this.renderUIGabion(g2);
			this.renderDragGabion(g2);
			this.renderNumberOfGabions(g2);

			this.renderOysters(g2);

			// plant meter
			this.renderPlantMeter(g2);
			this.renderNumberOfPlants(g2);
			this.renderDragPlant(g2);

			this.renderRunoff(g2);

			this.renderAnimation(g2);
			switch (this.currentTutorialState) {
			case OYSTERS:
				break;
			case WAVES:
				break;
			case GABIONS:
				break;
			case RUNOFF:
				break;
			case PLANTS:
				break;
			case END:
				break;
			default:
				break;
			}
			break;
		case GAME:
			// UIBOX Sky
			g2.drawImage(bic.getImageAtIndex(Image.SKY.getIndex()), (int) UIBOX.getX(), (int) UIBOX.getY(),
					(int) UIBOX.getWidth(), (int) (UIBOX.getHeight()), null);
			// GAMEBOX
			this.renderSun(g2);
			g2.setColor(new Color(163, 232, 255));
			g2.draw(GAMEBOX);
			g2.fill(GAMEBOX);
			this.renderDirtyWater(g2);
			this.renderGabions(g2);
			this.renderWaves(g2);
			this.renderConcreteWalls(g2);
			this.renderShore(g2);
			this.renderPlants(g2);

			// UI
			// ---------------------------
			// Gabion builder/Plant builder
			//this.renderGameTimer(g2);
			// this.renderGabionBuilder(g2);

			// GabionBuilder Meter
			// this.renderGabionMeter(g2);
			this.renderUIGabion(g2);
			this.renderDragGabion(g2);
			this.renderNumberOfGabions(g2);

			this.renderOysters(g2);

			// plant meter
			this.renderPlantMeter(g2);
			this.renderNumberOfPlants(g2);
			this.renderDragPlant(g2);

			this.renderRunoff(g2);

			this.renderAnimation(g2);
			break;
		case MENU:
			// UIBOX Sky
			g2.drawImage(bic.getImageAtIndex(Image.SKY.getIndex()), (int) UIBOX.getX(), (int) UIBOX.getY(),
					(int) UIBOX.getWidth(), (int) (UIBOX.getHeight()), null);
			// System.out.println(Image.HAND.getIndex() + " " +
			// Image.HAND.getPath() + " " + bic.getImages().get(2));
			// GAMEBOX
			this.renderSun(g2);
			g2.setColor(new Color(163, 232, 255));
			g2.draw(GAMEBOX);
			g2.fill(GAMEBOX);

			this.renderGabions(g2);
			this.renderWaves(g2);
			this.renderConcreteWalls(g2);
			
			this.renderShore(g2);
			this.renderPlants(g2);
			this.renderMenu(g2);
			break;
		case PAUSED:
			// UIBOX Sky
			g2.drawImage(bic.getImageAtIndex(Image.SKY.getIndex()), (int) UIBOX.getX(), (int) UIBOX.getY(),
					(int) UIBOX.getWidth(), (int) (UIBOX.getHeight()), null);
			// GAMEBOX
			g2.setColor(new Color(163, 232, 255));
			g2.draw(GAMEBOX);
			g2.fill(GAMEBOX);
			this.renderDirtyWater(g2);
			this.renderGabions(g2);
			this.renderWaves(g2);
			this.renderConcreteWalls(g2);
			this.renderSun(g2);
			this.renderShore(g2);
			this.renderPlants(g2);

			// UI
			// ---------------------------
			// Gabion builder/Plant builder
			this.renderGameTimer(g2);
			// this.renderGabionBuilder(g2);

			// GabionBuilder Meter
			// this.renderGabionMeter(g2);
			this.renderUIGabion(g2);
			this.renderDragGabion(g2);
			this.renderNumberOfGabions(g2);

			this.renderOysters(g2);

			// plant meter
			this.renderPlantMeter(g2);
			this.renderNumberOfPlants(g2);
			this.renderDragPlant(g2);

			this.renderRunoff(g2);

			this.renderAnimation(g2);
			break;
		case LOADING:
			break;
		case OVER:
			// UIBOX Sky
			g2.drawImage(bic.getImageAtIndex(Image.SKY.getIndex()), (int) UIBOX.getX(), (int) UIBOX.getY(),
					(int) UIBOX.getWidth(), (int) (UIBOX.getHeight()), null);
			// GAMEBOX
			this.renderSun(g2);
			g2.setColor(new Color(163, 232, 255));
			g2.draw(GAMEBOX);
			g2.fill(GAMEBOX);
			this.renderDirtyWater(g2);
			this.renderGabions(g2);
			this.renderWaves(g2);
			this.renderConcreteWalls(g2);
			this.renderShore(g2);
			this.renderPlants(g2);

			// UI
			// ---------------------------
			// Gabion builder/Plant builder
//			this.renderGameTimer(g2);
			// this.renderGabionBuilder(g2);

			// GabionBuilder Meter
			// this.renderGabionMeter(g2);
			this.renderUIGabion(g2);
			this.renderDragGabion(g2);
			this.renderNumberOfGabions(g2);

			this.renderOysters(g2);

			// plant meter
			this.renderPlantMeter(g2);
			this.renderNumberOfPlants(g2);
			this.renderDragPlant(g2);

			this.renderRunoff(g2);

			this.renderAnimation(g2);
			this.renderOver(g2);
		default:
			break;

		}

	}
	/**
	 * Renders an overlaying rectangle representing the water and determines the color based on how dirty the water is.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderDirtyWater(Graphics2D g2) {
		g2.setColor(this.dirtyWater);
		double width = this.GAMEBOX.getWidth() - this.shore.getRect().getWidth();
		double height = this.GAMEBOX.getHeight();
		Rectangle2D dirtywaterRect = new Rectangle2D.Double(
				this.shore.getRect().getX() + this.shore.getRect().getWidth(),
				this.UIBOX.getY() + this.UIBOX.getHeight(), width, height);
		g2.draw(dirtywaterRect);
		g2.fill(dirtywaterRect);
	}
	/**
	 * Renders the Gabions.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderGabions(Graphics2D g2) {
		for (GabionController gabion : gabions.toArray(new GabionController[0])) {
			
			if (gabion.getGabion().getHealth() == 3) {
				//g2.setColor(new Color(0, 0, 0, 175));
				g2.drawImage(bic.getImageAtIndex(Image.GABION.getIndex()), (int) gabion.getRect().getX(), (int) gabion.getRect().getY(),
						(int) gabion.getRect().getWidth(), (int) gabion.getRect().getHeight(), null);
			} else if (gabion.getGabion().getHealth() == 2) {
				//g2.setColor(new Color(0, 0, 0, 100));
				g2.drawImage(bic.getImageAtIndex(Image.GABIONFADE1.getIndex()), (int) gabion.getRect().getX(), 
						(int) gabion.getRect().getY(),(int) gabion.getRect().getWidth(), (int) gabion.getRect().getHeight(), null);
			} else {
				//g2.setColor(Color.BLACK);
				g2.drawImage(bic.getImageAtIndex(Image.GABIONFADE2.getIndex()), (int) gabion.getRect().getX(),
						(int) gabion.getRect().getY(), (int) gabion.getRect().getWidth(), (int) gabion.getRect().getHeight(), null);
			}
			// g2.draw(gabions.get(i).getRect());
			// g2.fill(gabions.get(i).getRect());
			
		}
	}
	/**
	 * Renders the waves
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderWaves(Graphics2D g2) {
		//g2.setColor(Color.cyan);
		for (WaveController wc : waves.toArray(new WaveController[0])) {
			// g2.draw(wc.getRect());
			// g2.fill(wc.getRect());
			this.waveAnimationTimer.countUp(750.0);
			//System.out.println(this.waveAnimationTimer.getTimeMili());
			if (this.waveAnimationTimer.getTimeMili() < 250.0) {
				g2.drawImage(bic.getImageAtIndex(Image.WAVE1.getIndex()), (int) wc.getRect().getX(),
						(int) wc.getRect().getY(), (int) wc.getRect().getWidth(), (int) wc.getRect().getHeight(), null);
			} else if (this.waveAnimationTimer.getTimeMili() < 500.0) {
				g2.drawImage(bic.getImageAtIndex(Image.WAVE2.getIndex()), (int) wc.getRect().getX(),
						(int) wc.getRect().getY(), (int) wc.getRect().getWidth(), (int) wc.getRect().getHeight(), null);
			} else {
				g2.drawImage(bic.getImageAtIndex(Image.WAVE3.getIndex()), (int) wc.getRect().getX(),
						(int) wc.getRect().getY(), (int) wc.getRect().getWidth(), (int) wc.getRect().getHeight(), null);
			}
			// g2.drawImage(bic.getImageAtIndex(Image.WAVE1.getIndex()), (int)
			// wc.getRect().getX(),
			// (int) wc.getRect().getY(), (int) wc.getRect().getWidth(), (int)
			// wc.getRect().getHeight(), null);
		}

	}
	/**
	 * Renders the concrete walls.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderConcreteWalls(Graphics2D g2) {
		for (ConcreteWallController wall : concreteWalls.toArray(new ConcreteWallController[0])) {
			//g2.setColor(Color.LIGHT_GRAY);
			//g2.draw(wall.getRect());
			//g2.setColor(Color.DARK_GRAY);
			//g2.fill(wall.getRect());
			g2.drawImage(bic.getImageAtIndex(Image.WALL.getIndex()), (int)wall.getRect().getX(),
					(int)wall.getRect().getY(), (int)wall.getRect().getWidth(), (int)wall.getRect().getHeight(), null);
		}
	}
	/**
	 * Renders the shore.
	 * @param g2 Graphic2D The graphics object.
	 */
	public void renderShore(Graphics2D g2) {
		g2.setColor(this.ShoreColor);
		g2.fill(shore.getRect());
		g2.draw(shore.getRect());
	}
	/**
	 * Renders and animates the blue crab.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderHorseshoeCrab(Graphics2D g2) {
		// g2.setColor(Color.PINK);

		for (BlueCrabController crab : hsCrab.toArray(new BlueCrabController[0])) {
			// g2.draw(hsCrab);
			// g2.fill(hsCrab);
			if(this.speaking) {
				this.crabSpeakingTimer.countUp(1000.0);
				if(this.crabSpeakingTimer.getTimeMili() % 3 == 0) {
					g2.drawImage(bic.getImageAtIndex(Image.BLUECRAB.getIndex()), (int) crab.getRect().getX(),
							(int) crab.getRect().getY(), (int) crab.getRect().getWidth(),
							(int) crab.getRect().getHeight(), null);
				} else if(this.crabSpeakingTimer.getTimeMili() % 3 == 1) {
					g2.drawImage(bic.getImageAtIndex(Image.BLUECRAB2.getIndex()), (int) crab.getRect().getX(),
							(int) crab.getRect().getY(), (int) crab.getRect().getWidth(),
							(int) crab.getRect().getHeight(), null);
				} else {
					g2.drawImage(bic.getImageAtIndex(Image.BLUECRAB3.getIndex()), (int) crab.getRect().getX(),
							(int) crab.getRect().getY(), (int) crab.getRect().getWidth(),
							(int) crab.getRect().getHeight(), null);
				}
			} else{
				g2.drawImage(bic.getImageAtIndex(Image.BLUECRAB.getIndex()), (int) crab.getRect().getX(),
						(int) crab.getRect().getY(), (int) crab.getRect().getWidth(),
						(int) crab.getRect().getHeight(), null);
			}
			
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Arial", Font.ITALIC, (int) (game.getScale().getWidth() * 0.015)));
			
			ac.playTextAnimation(g2, (int) (crab.getRect().getX() + crab.getRect().getWidth() + g2.getFont().getSize()),
					(int) (crab.getRect().getCenterY() - game.getScale().getHeight() * 0.01));
		}

	}
	// DONT USE
	public void renderGameTimer(Graphics2D g2) {
		g2.setFont(f1);
		g2.setColor(Color.BLACK);
		g2.drawString(timer.getTime() + "", (int) UIBOX.getCenterX(), (int) UIBOX.getCenterY());

	}
	/**
	 * Renders the plants.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderPlants(Graphics2D g2) {
		//g2.setColor(Color.red);
		for (PlantController plant : plants.toArray(new PlantController[0])) {
			if (plant.getPlant().isVisible()) {
				//g2.draw(plant.getRect());
				//g2.fill(plant.getRect());
				if (plant.getPlant().getHealth() > (int) (plant.getPlant().getMaxHealth() * .66)) {
					g2.drawImage(bic.getImageAtIndex(Image.GRASS1.getIndex()), (int) plant.getRect().getX(),
							(int) plant.getRect().getY(), (int) (plant.getRect().getWidth() * 1.8),
							(int) (plant.getRect().getHeight() * 1.5), null);
				} else if (plant.getPlant().getHealth() > plant.getPlant().getMaxHealth() * 0.33) {
					g2.drawImage(bic.getImageAtIndex(Image.GRASS2.getIndex()), (int) plant.getRect().getX(),
							(int) plant.getRect().getY(), (int) (plant.getRect().getWidth() * 1.8),
							(int) (plant.getRect().getHeight() * 1.5), null);
				} else {
					g2.drawImage(bic.getImageAtIndex(Image.GRASS3.getIndex()), (int) plant.getRect().getX(),
							(int) plant.getRect().getY(), (int) (plant.getRect().getWidth() * 1.8),
							(int) (plant.getRect().getHeight() * 1.5), null);
				}
			}
		}
	}
	//DONT USE
	public void renderGabionBuilder(Graphics2D g2) {
		g2.setColor(Color.WHITE);
		g2.fill(gb.getRect());
		g2.draw(gb.getRect());
	}
	//DONT USE
	public void renderGabionMeter(Graphics2D g2) {
		g2.setColor(Color.ORANGE);
		double gbPercentage = (double) gb.getGb().getNumberOfOysters() / (double) gb.getGb().getMaxGabionCapacity();
		double maxHeight = gb.getRect().getHeight();
		double height = maxHeight * gbPercentage;
		double width = gb.getRect().getWidth() / 10;
		double x = gb.getRect().getX(); // - (width/2);
		double y = maxHeight - height;
		Rectangle2D gabionMeter = new Rectangle2D.Double(x, y, width, height);
		g2.draw(gabionMeter);
		g2.fill(gabionMeter);
	}
	/**
	 * Renders the gabions in the UI.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderUIGabion(Graphics2D g2) {
		if (gb.getGb().getGabions() == 0) {
			g2.setColor(new Color(0, 0, 0, 50));
		} else {
			g2.setColor(new Color(0, 0, 0, 255));
		}
		// g2.draw(uiGabion);
		// g2.fill(uiGabion);

		if (this.gb.getGb().getNumberOfOysters() == 0 && this.gb.getGb().getGabions() == 0) {
			g2.drawImage(bic.getImageAtIndex(Image.GABION1.getIndex()), (int) uiGabion.getX(), (int) uiGabion.getY(),
					(int) uiGabion.getWidth(), (int) uiGabion.getHeight(), null);
		} else if (this.gb.getGb().getNumberOfOysters() == 1) {
			g2.drawImage(bic.getImageAtIndex(Image.GABION2.getIndex()), (int) uiGabion.getX(), (int) uiGabion.getY(),
					(int) uiGabion.getWidth(), (int) uiGabion.getHeight(), null);
		} else if (this.gb.getGb().getNumberOfOysters() == 2) {
			g2.drawImage(bic.getImageAtIndex(Image.GABION3.getIndex()), (int) uiGabion.getX(), (int) uiGabion.getY(),
					(int) uiGabion.getWidth(), (int) uiGabion.getHeight(), null);
		} else if (this.gb.getGb().getNumberOfOysters() == 3) {
			g2.drawImage(bic.getImageAtIndex(Image.GABION4.getIndex()), (int) uiGabion.getX(), (int) uiGabion.getY(),
					(int) uiGabion.getWidth(), (int) uiGabion.getHeight(), null);
		} else if (this.gb.getGb().getNumberOfOysters() == 4 || this.gb.getGb().getNumberOfOysters() == 5) {
			g2.drawImage(bic.getImageAtIndex(Image.GABION5.getIndex()), (int) uiGabion.getX(), (int) uiGabion.getY(),
					(int) uiGabion.getWidth(), (int) uiGabion.getHeight(), null);
		} else {
			g2.drawImage(bic.getImageAtIndex(Image.GABIONFULL.getIndex()), (int) uiGabion.getX(), (int) uiGabion.getY(),
					(int) uiGabion.getWidth(), (int) uiGabion.getHeight(), null);
		}
	}
	/**
	 * Renders the drag gabion.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderDragGabion(Graphics2D g2) {
		if (this.renderDragGabion) {
			g2.draw(this.createDragGabion(game.getMouseCords()));
			g2.setColor(Color.gray);
			for (Rectangle2D row : waveRows) {
				g2.draw(row);

			}
		}
	}
	/**
	 * Renders the oysters.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderOysters(Graphics2D g2) {
		g2.setColor(Color.GRAY);
		for (OysterController oyster : oysters.toArray(new OysterController[0])) {
			// OysterController oyster = oysters.get(i);
			// g2.draw(oyster.getRect());
			// g2.fill(oyster.getRect());
			g2.drawImage(bic.getImageAtIndex(Image.OYSTER.getIndex()), (int) oyster.getRect().getX(),
					(int) oyster.getRect().getY(), (int) oyster.getRect().getWidth(),
					(int) (oyster.getRect().getHeight()), null);
		}
	}
	/**
	 * Renders the gabion counter.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderNumberOfGabions(Graphics2D g2) {
		g2.setFont(f1);
		g2.setColor(Color.WHITE);
		if (gb.getGb().getGabions() >= 10) {
			g2.drawString("x" + gb.getGb().getGabions(),
					(int) ((int) (gb.getRect().getCenterX() + (this.uiGabion.getWidth() / 2)) - (f1.getSize() * .75)),
					(int) uiGabion.getCenterY() + (f1.getSize() / 2));
		} else {
			g2.drawString("x" + gb.getGb().getGabions(),
					(int) (gb.getRect().getCenterX() + (this.uiGabion.getWidth() / 2)) - (f1.getSize() / 2),
					(int) uiGabion.getCenterY() + (f1.getSize() / 2));
		}
	}
	/**
	 * Renders the plant meter.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderPlantMeter(Graphics2D g2) {
		double maxHeight = pb.getRect().getHeight();
		double height = ((double) plantTimer.getTimeMili() / (pb.getPb().getNumOfSecondsPerPlant() * 1000)) * maxHeight;
		double width = pb.getRect().getWidth();
		double x = pb.getRect().getX();
		double y = maxHeight - height;
		Rectangle2D plantMeter = new Rectangle2D.Double(x, y, width, height);

		g2.setColor(new Color(163, 255, 173));
		// g2.fill(plantBuilder.getRect());
		// g2.draw(plantBuilder.getRect());
		//g2.draw(plantMeter);
		g2.fill(plantMeter);

		// g2.setColor(Color.GREEN);
		// g2.draw(uiPlant);
		// g2.fill(uiPlant);

		g2.drawImage(bic.getImageAtIndex(Image.GRASS1.getIndex()), (int) uiPlant.getX(), (int) uiPlant.getY(),
				(int) uiPlant.getWidth(), (int) (uiPlant.getHeight()), null);

	}
	/**
	 * Renders the plant counter.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderNumberOfPlants(Graphics g2) {
		g2.setColor(Color.WHITE);
		g2.setFont(f1);
		g2.drawString("x" + pb.getPb().getNumberOfPlants(),
				(int) (pb.getRect().getX() + pb.getRect().getWidth() + (f1.getSize() / 2)),
				(int) uiGabion.getCenterY() + (f1.getSize() / 2));
	}
	/**
	 * Renders the drag plant.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderDragPlant(Graphics2D g2) {

		if (this.renderDragPlant) {
			// g2.setColor(Color.GREEN);
			// g2.draw(this.renderDragPlant(game.getMouseCords()));
			Rectangle2D r = this.createDragPlant(game.getMouseCords());
			g2.drawImage(bic.getImageAtIndex(Image.GRASS1.getIndex()), (int) r.getX(), (int) r.getY(),
					(int) r.getWidth(), (int) r.getHeight(), null);
			g2.setColor(Color.LIGHT_GRAY);
			for (Rectangle2D row : plantRows) {
				g2.draw(row);
			}
		} else {
			g2.setColor(this.ShoreColor);
			for (Rectangle2D row : plantRows) {
				// g2.fill(row);
			}
		}
	}
	/**
	 * Renders runoff.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderRunoff(Graphics2D g2) {
		//g2.setColor(runOffColor);
		for (RunOffController runOffs : runOff.toArray(new RunOffController[0])) {
//			g2.draw(runOff.getRect());
//			g2.fill(runOff.getRect());
			g2.drawImage(bic.getImageAtIndex(Image.RUNOFF.getIndex()), (int)runOffs.getRect().getX(),
					(int)runOffs.getRect().getY(), (int)runOffs.getRect().getWidth(), (int)runOffs.getRect().getHeight(), null);
		}
	}

//	public void renderCrabFishMeter(Graphics2D g2) {
//		g2.setColor(Color.DARK_GRAY);
//		g2.draw(cfMeter.getRect());
//		g2.fill(cfMeter.getRect());
//
//		g2.setColor(Color.WHITE);
//		g2.drawString("" + cfMeter.getCfm().getPhLevels(), (int) cfMeter.getRect().getCenterX(),
//				(int) cfMeter.getRect().getCenterY());
//		g2.setColor(Color.YELLOW);
//	}
	/**
	 * Renders the sun.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderSun(Graphics2D g2) {

		double sunDim = game.getScale().getWidth() * 0.06;
		double startX = UIBOX.getX() + pb.getRect().getX() + pb.getRect().getWidth() + UIBOX.getWidth() * 0.05;
		double maxX = this.gb.getRect().getX() - (sunDim / 2);
		double sunX = ((((180 * 1000) - timer.getTimeMili()) / (180.0 * 1000)) * (maxX - startX)) + startX;
		double maxY = 0;
		double startY = UIBOX.getCenterY() + (sunDim / 2);
		// double h = maxX - startX;
		// ((-.01)*(UIBOX.getWidth()))*(sunX*sunX)+(UIBOX.getHeight()*100);
		// (-1*(sunHeight/2)) * ((timer.getTimeMili() / (180 * 1000))^2) +
		// UIBOX.getHeight();
		// System.out.println(sunY);
		// Ellipse2D sun = new Ellipse2D.Double(sunX, startY, sunDim,
		// sunDim);
		// g2.draw(sun);
		// g2.fill(sun);
		double x1 = startX;
		double x3 = maxX;
		double x2 = (x3 - x1) / 2;
		double x = sunX;
		double y1 = startY;
		double y3 = startY;
		double y2 = maxY;
		double y = ((x - x2) * (x - x3)) / ((x1 - x2) * (x1 - x3)) * y1
				+ ((x - x1) * (x - x3)) / ((x2 - x1) * (x2 - x3)) * y2
				+ ((x - x1) * (x - x2)) / ((x3 - x1) * (x3 - x2)) * y3;
		g2.drawImage(bic.getImageAtIndex(Image.SUN.getIndex()), (int) sunX, (int) y, (int) sunDim, (int) sunDim, null);

	}
	/**
	 * Renders the OVER game state.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderOver(Graphics2D g2) {
		g2.setColor(new Color(255,255,255,220));
		g2.fill(gameOverBox);
		g2.draw(gameOverBox);
		if (!game.isGameLost()) {
			int width = (int)(gameOverBox.getWidth()*0.8);
			int height = (int) (gameOverBox.getHeight()*0.33);
			int youWinX = (int) ((gameOverBox.getX()+(gameOverBox.getWidth()/2)) - (width/2));
			g2.drawImage(bic.getImageAtIndex(Image.YOUWIN.getIndex()), youWinX, (int)gameOverBox.getY(), width, height, null);
			int gradeWidth = (int) (gameOverBox.getWidth()*0.2);
			int aPlusGradeWidth = (int) (gameOverBox.getWidth()*0.4);
			int gradeHeight = (int) (gameOverBox.getHeight()*0.4);
			int gradeX = (int)((gameOverBox.getX() + (gameOverBox.getWidth()/2)) - (gradeWidth/2));
			int gradeY = (int) (gameOverBox.getY()+height);
			switch(this.grade) {
			case "A+":
				g2.drawImage(bic.getImageAtIndex(Image.APLUS.getIndex()), gradeX, gradeY, aPlusGradeWidth, gradeHeight, null);
				break;
			case "A":
				g2.drawImage(bic.getImageAtIndex(Image.A.getIndex()), gradeX, gradeY, gradeWidth, gradeHeight, null);
				break;
			case "B":
				g2.drawImage(bic.getImageAtIndex(Image.B.getIndex()), gradeX, gradeY, gradeWidth, gradeHeight, null);
				break;
			case "C":
				g2.drawImage(bic.getImageAtIndex(Image.C.getIndex()), gradeX, gradeY, gradeWidth, gradeHeight, null);
				break;
			case "D":
				g2.drawImage(bic.getImageAtIndex(Image.D.getIndex()), gradeX, gradeY, gradeWidth, gradeHeight, null);
				break;
			}
			g2.setFont(new Font("Arial", Font.BOLD, this.fontSize/2));
			g2.setColor(Color.black);
			g2.drawString("Waves: " + spawner.getTotalNumOfWaves() + "    " + "Gabions: " + this.totalNumOfGabions +
					"    " + "Plants: " + spawner.getTotalNumOfPlants() + "    " + "Runoff: " + spawner.getTotalNumOfRunoff(),
					(int)(gameOverBox.getX() + gameOverBox.getWidth()*0.1),
					(int) ((gameOverBox.getY() + gameOverBox.getHeight()) - this.fontSize/2));
		} else {
			int width = (int)(gameOverBox.getWidth()*0.8);
			int height = (int) (gameOverBox.getHeight()*0.33);
			int youLoseX = (int) ((gameOverBox.getX()+(gameOverBox.getWidth()/2)) - (width/2));
			g2.drawImage(bic.getImageAtIndex(Image.YOULOSE.getIndex()), youLoseX, (int)gameOverBox.getY(), width, height, null);
			
			int heightFail = (int) (gameOverBox.getHeight()*0.5);
			int failX = (int)((gameOverBox.getX() + (gameOverBox.getWidth()/2)) - (width/2));
			int failY = (int) (gameOverBox.getY()+heightFail);
			
			g2.drawImage(bic.getImageAtIndex(Image.FAIL.getIndex()), failX, (int)failY, width, heightFail, null);
		}
	
	}
	
	
	/**
	 * Renders animation
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderAnimation(Graphics2D g2) {
		for (AnimationController ac : animations.toArray(new AnimationController[0])) {
			switch (ac.getAnimation()) {
			case OYSTER:
				ac.playCollectOysterAnimation(g2);
				break;
			case PLACEGABION:
				ac.playGabionPlacementAnimation(g2);
				// ac.playGabionPlacementAnimation(g2);
				break;
			case PLACEPLANT:
				ac.playPlantPlacementAnimation(g2);
				break;
			default:
				break;
			}
		}
	}
	/**
	 * Renders the menu.
	 * @param g2 Graphics2D The graphics object.
	 */
	public void renderMenu(Graphics2D g2) {
		if (!this.renderCredits) {
			g2.setColor(Color.ORANGE);
			g2.drawImage(bic.getImageAtIndex(Image.TUTORIAL.getIndex()), (int)tutorialButton.getX(), (int)tutorialButton.getY(),
					(int)tutorialButton.getWidth(), (int)tutorialButton.getHeight(), null);
			g2.setColor(Color.PINK);
			g2.drawImage(bic.getImageAtIndex(Image.PLAY.getIndex()), (int)playButton.getX(), (int)playButton.getY(),
					(int)playButton.getWidth(), (int)playButton.getHeight(), null);
			g2.setColor(Color.MAGENTA);
			g2.drawImage(bic.getImageAtIndex(Image.CREDITS.getIndex()), (int)creditButton.getX(), (int)creditButton.getY(),
					(int)creditButton.getWidth(), (int)creditButton.getHeight(), null);
		} else {
			g2.setColor(new Color(255,255,255,220));
			g2.fill(creditsBox);
			g2.draw(creditsBox);
			g2.setColor(Color.BLUE);
			g2.draw(backButton);
			g2.fill(backButton);
			g2.setColor(Color.WHITE);
			g2.setFont(f1);
			g2.drawString("Back", (int)this.backButton.getX(), (int)(this.backButton.getY() + (this.backButton.getHeight()/2)));
			g2.setColor(Color.BLACK);
			g2.drawString("Jackson Jorss", (int)((creditsBox.getX()+(creditsBox.getWidth()/2)) - (f1.getSize()/2)), 
					(int)(creditsBox.getY() + (creditsBox.getHeight()/4)*0.5));
			g2.drawString("Robert Ley", (int)((creditsBox.getX()+(creditsBox.getWidth()/2)) - (f1.getSize()/2)), 
					(int)(creditsBox.getY() + ((creditsBox.getHeight()/4)*1)));
			g2.drawString("Ben Clark", (int)((creditsBox.getX()+(creditsBox.getWidth()/2)) - (f1.getSize()/2)), 
					(int)(creditsBox.getY() + ((creditsBox.getHeight()/4)*1.5)));
			g2.drawString("Jael Flaquer", (int)((creditsBox.getX()+(creditsBox.getWidth()/2)) - (f1.getSize()/2)), 
					(int)(creditsBox.getY() + ((creditsBox.getHeight()/4)*2)));
			
		}
	}
	/**
	 * Calls all collision methods. Main collision method that gets called every tic.
	 */
	public void collision() {
		this.handleShoreCollision();
		this.handleGabionCollision();
		this.handleConcreteWallCollision();
		this.handlePlantCollision();
	}
	/**
	 * Handles all changes to the game state after a wave hits the shore.
	 * Erodes shore. Moves plants with shore.
	 */
	public void handleShoreCollision() {
		for (Iterator<WaveController> itw = waves.iterator(); itw.hasNext();) {
			WaveController wave = itw.next();
			if (wave.getRect().intersects(shore.getRect())) {
				// wave hit shore
				// erode shore
				if (shore.getShore().erode()) {
					this.eroded = true;
					//this.ShoreColor = new Color(255, 200, 100);
					shore.setRect(new Rectangle2D.Double(shore.getRect().getX(), shore.getRect().getY(),
							(shore.getRect().getWidth() - ((this.gabionWidth - this.gbPadding)/2)),
							shore.getRect().getHeight()));
					for (Rectangle2D plantRow : plantRows) {
						plantRow.setRect(shore.getRect().getWidth() - plantRow.getWidth(), plantRow.getY(),
								plantRow.getWidth(), plantRow.getHeight());
					}

					for (PlantController plant : plants) {
						plant.setRect(new Rectangle2D.Double(plant.getRect().getX() - ((this.gabionWidth - this.gbPadding)/2),
								plant.getRect().getY(), plant.getRect().getWidth(), plant.getRect().getHeight()));
					}
				} else {
					this.ShoreColor = new Color(255, 200, 100, 230);

				}
				System.out.println("Wave Hit Shore");
				itw.remove();
			}
		}
	}
	/**
	 * Handles gabion/wave collisions. Changes the gabion health and removes the wave.
	 */
	public void handleGabionCollision() {
		for (Iterator<WaveController> itw = waves.iterator(); itw.hasNext();) {
			WaveController wave = itw.next();
			for (Iterator<GabionController> itg = gabions.iterator(); itg.hasNext();) {
				GabionController gabion = itg.next();

				if (gabion.getRect().intersects(wave.getRect())) {
					itw.remove();
					gabion.getGabion().changeHealth(gabion.getGabion().getHealth() - 1);
					System.out.println("wave hit gabion");
					
					if (gabion.getGabion().getHealth() <= 0) {
						itg.remove();
						this.numOfGabionsInRow.set(gabion.getGabion().getRowNum(),
								this.numOfGabionsInRow.get(gabion.getGabion().getRowNum()) - 1);

					}
				}
			}
		}
	}
	/**
	 * Handles concrete wall collisions. Removes wave and concrete wall.
	 */
	public void handleConcreteWallCollision() {
		for (Iterator<WaveController> itw = waves.iterator(); itw.hasNext();) {
			WaveController wave = itw.next();
			for (Iterator<ConcreteWallController> itc = concreteWalls.iterator(); itc.hasNext();) {
				ConcreteWallController concreteWall = itc.next();
				if (concreteWall.getRect().intersects(wave.getRect())) {
					itc.remove();
					itw.remove();
				}
			}
		}
	}
	/**
	 * Handles plant/runoff collisions. Changes plant health and removes it if it is equal to or below 0.
	 */
	public void handlePlantCollision() {
		for (Iterator<RunOffController> itr = runOff.iterator(); itr.hasNext();) {
			RunOffController runOff = itr.next();
			for (Iterator<PlantController> itp = plants.iterator(); itp.hasNext();) {
				PlantController plant = itp.next();
				if (runOff.getRect().intersects(plant.getRect())) {
					this.hittingPlant = true;
					plant.getPlant().changeHealth(plant.getPlant().getHealth() - 1);
					runOff.getRect().setRect(runOff.getRect().getX(), runOff.getRect().getY(),
							runOff.getRect().getWidth() - runOff.getRunOff().getSpeed(), runOff.getRect().getHeight());
					if (plant.getPlant().getHealth() <= 0) {
						itp.remove();
					}
				} else {
					this.hittingPlant = false;
				}

			}
		}
	}
	/**
	 * Handles plant placement. Ensures the placement is valid and adds the plant. Rejects placement otherwise.
	 * @param p Point Coordinates of the mouse on the JPanel.
	 */
	public void handlePlacePlant(Point p) {
		if (pb.getPb().getNumberOfPlants() != 0 && this.ableToPlacePlant) {
			for (int i = 0; i < plantRows.size(); i++) {
				Rectangle2D row = plantRows.get(i);
				if (row.contains(p)) {
					spawner.spawnPlants(i);
					if (this.currentGameState == GameState.TUTORIAL
							&& this.currentTutorialState == TutorialState.PLANTS) {
						if (i == spawner.getRowForRunOff()) {
							this.placedFirstPlant = true;
						} else {
							this.placedWrongPlant = true;
							this.plants.remove(this.plants.size() - 1);
							this.pb.getPb().setNumberOfPlants(this.pb.getPb().getNumberOfPlants() + 1);
						}
					}
				}
			}
		}
		this.renderDragPlant = false;
	}
	/**
	 * Handles placing gabions. Ensures that the placement is valid and adds the gabion. Reject placement otherwise.
	 * @param p Point Coordinates of the mouse on the JPanel.
	 */
	public void handlePlaceGabion(Point p) {
		// System.out.println(p.getX() + ", " + p.getY());

		// spawn gabion
		if (gb.getGb().getGabions() != 0 && this.ableToPlaceGabion) {
			for (int i = 0; i < waveRows.size(); i++) {
				Rectangle2D row = waveRows.get(i);
				if (row.contains(p) && this.numOfGabionsInRow.get(i) < 5) {
					double y = row.getCenterY() - ((gabionHeight) / 2);
					double x = ((gabionWidth + gbPadding) * this.numOfGabionsInRow.get(i)) + row.getX();

					Gabion gab = new Gabion((int) x, (int) y, i);
					Rectangle2D gabRect = new Rectangle.Double(x + gbPadding, y, gabionWidth, gabionHeight);
					gabions.add(new GabionController(gab, gabRect));
					gb.getGb().setGabions(gb.getGb().getGabions() - 1);
					this.numOfGabionsInRow.set(i, this.numOfGabionsInRow.get(i) + 1);
					this.totalNumOfGabions++;
					if (this.currentGameState != GameState.MENU && this.currentGameState == GameState.TUTORIAL
							&& this.currentTutorialState == TutorialState.GABIONS) {
						// System.out.println("waves: " +
						// this.getNumOfWavesInRow().get(i));
						// System.out.println("Gabions: " +
						// this.numOfGabionsInRow.get(i));
						if (this.getNumOfWavesInRow().get(i) > 0 && this.numOfGabionsInRow.get(i) == 1) {
							if (this.gabions.size() == 2) {
								this.placedFirstGabion = true;
							}
						} else {
							this.gabions.remove(this.gabions.size() - 1);
							this.placedWrongGabion = true;
							gb.getGb().setGabions(gb.getGb().getGabions() + 1);
							this.numOfGabionsInRow.set(i, this.numOfGabionsInRow.get(i) - 1);
							this.totalNumOfGabions--;
						}
					}
				}
			}
		}
		this.renderDragGabion = false;

	}
	/**
	 * Handles oyster collection. Animates the oyster after being collected, removes the oyster from the game, and builds
	 * gabion in the gabion builder.
	 * @param p Point Coordinates of the mouse on the JPanel.
	 * @param i int Index of the oyster in the oysters collection.
	 */
	public void handleCollectOyster(Point p, int i) {
		//int numOfClusters = 0;
//		numOfClusters = oysters.get(i).getOyster().getNumOfOystersInClump();
		Rectangle2D o = oysters.get(i).getRect();
		gb.getGb().build();
		// this.oysters.get(i).setCollected(true);
		this.animations.add(new AnimationController(this, this.bic, Animation.OYSTER, o, 0));
		oysters.get(i).getOyster().setVisible(false);

	}
	/**
	 * Creates the Rectangle2D for the drag gabion.
	 * @param p Point Coordinates of the mouse on the JPanel.
	 * @return the Rectangle2D created.
	 */
	public Rectangle2D createDragGabion(Point p) {
		Rectangle2D r = new Rectangle2D.Double(p.getX() - (uiGabion.getWidth() / 2),
				p.getY() - (uiGabion.getWidth() / 2), uiGabion.getWidth(), uiGabion.getHeight());
		return r;
	}
	/**
	 * Creates the Rectangle2D for the drag plant.
	 * @param p Point Coordinates of the mouse on the JPanel.
	 * @return the Rectangle 2D created.
	 */
	public Rectangle2D createDragPlant(Point p) {
		double width = this.getPlantRows().get(0).getWidth() * 0.2;
		double height = this.getPlantRows().get(0).getWidth() * 0.3;
		Rectangle2D r = new Rectangle2D.Double(p.getX() - (width / 2), p.getY() - (height / 2), width, height);
		return r;
	}
	/**
	 * Handles dragging. Determines if game should render plant or gabion drag.
	 * @param p Point Coordinates of the mouse on the JPanel.
	 */
	public void handleDrag(Point p) {
		if (uiGabion.contains(p)) {
			System.out.println("uiGabion clicked");
			this.renderDragGabion = true;
			game.setDragging(true);
		}
		if (uiPlant.contains(p)) {
			System.out.println("uiPlant Clicked");
			this.renderDragPlant = true;
			game.setDragging(true);
		}
	}
	/**
	 * Getter for mousePressed.
	 * @param p Point Coordinates of the mouse on the JPanel.
	 * @return The current instance of p.
	 */
	public Point getMousePressed(Point p) {
		return p;
	}
	/**
	 * Performs required actions when a user clicks at a Point p.
	 * @param Point p - the point that the user clicks
	 */
	public void handlePressed(Point p) {
		if (this.currentGameState == GameState.MENU) {
			if (this.tutorialButton.contains(p)) {
				this.init();
				this.currentGameState = GameState.TUTORIAL;
				// this shouldn't be necessary
				this.waves.clear();
				this.plants.clear();
				spawner.setTotalNumOfWaves(0);
				spawner.setTotalNumOfPlants(0);
				for (int i = 0; i < this.numOfWavesInRow.size(); i++) {
					this.numOfWavesInRow.set(i, 0);
					spawner.getPlantsInRow().set(i, 0);
				}

			} else if (this.playButton.contains(p)) {
				this.init();
				this.currentGameState = GameState.GAME;
				this.waves.clear();
				this.plants.clear();
				spawner.setTotalNumOfWaves(0);
				spawner.setTotalNumOfPlants(0);
				for (int i = 0; i < this.numOfWavesInRow.size(); i++) {
					this.numOfWavesInRow.set(i, 0);
					spawner.getPlantsInRow().set(i, 0);
				}
				this.ableToPlaceGabion = true;
				this.ableToPlacePlant = true;
				
			} else if (this.creditButton.contains(p)) {
				this.renderCredits = true;
			} else if (this.renderCredits) {
				if(this.backButton.contains(p)) {
					this.renderCredits = false;
				}
			}
		}
		for (int i = 0; i < oysters.size(); i++) {
			if (oysters.get(i).getRect().contains(p)) {
				handleCollectOyster(p, i);
				return;
			}
		}
		this.handleDrag(p);

	}
	/**
	 * Calculates the final score based on how help the user played that specific game. It's 
	 * based on the shoreHealth and the quality of the water.
	 * @param int shoreHealth - an int between 25-100 with 100 being the best shore health
	 * @param alphaWater - the alpha value of the dirty water overlay 255 the worst; 0 is the best and clean/clear water
	 * @return
	 */
	public String calculateScore(int shoreHealth, int alphaWater) {
		double score = (((double)shoreHealth/(double)shore.getShore().getMaxHealth())*0.5) + (((255.0-(double)alphaWater)/255.0)*0.5);
		if (score == 1) {
			return "A+";
		} else if (score >= 0.9 ) {
			return "A";
		} else if (score >= 0.8) {
			return "B"; 
		} else if (score >= 0.7) {
			return "C";
		} else {
			return "D";
		}
	}
	/**
	 * Saves the game state for serializable.
	 */
	public void save() {
		try {
			FileOutputStream fos = new FileOutputStream("saveStateGLC.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Getter for the ArrayList of WaveControllers called waves
	 * @return waves, an arraylist of wavecontrollers
	 */
	public ArrayList<WaveController> getWaves() {
		return waves;
	}
	/**	Getter for the ArrayList of Rectangle2D called waveRows
	 * @return waveRows, an arraylist of Rectangle2D
	 */
	public ArrayList<Rectangle2D> getWaveRows() {
		return waveRows;
	}
	/**
	 * Getter for an ArrayList of OysterControllers, called oysters
	 * @return oysters, an ArrayList of OysterControllers
	 */
	public ArrayList<OysterController> getOysters() {
		return oysters;
	}
	/**
	 * Getter for the GAMEBOX, the rectangle where the game play takes place
	 * @return GAMEBOX
	 */
	public Rectangle2D getGAMEBOX() {
		return GAMEBOX;
	}
	/**
	 * Getter for the ArrayList of Integers, numOfGabionsInRow
	 * @return int numOfGabionsInRow
	 */
	public ArrayList<Integer> getNumOfGabionsInRow() {
		return numOfGabionsInRow;
	}
	/**
	 * Getter for gbPadding, the padding for gabions
	 * @return gbPadding, the padding for gabions
	 */
	public double getGbPadding() {
		return gbPadding;
	}
	/**
	 * Getter for gaboinWidth
	 * @return int gabionWidth
	 */
	public double getGabionWidth() {
		return gabionWidth;
	}
	/**
	 * Getter for ArrayList of PlantControllers, called plants
	 * @return plants
	 */
	public ArrayList<PlantController> getPlants() {
		return plants;
	}
	/**
	 * Getter for an ArrayList of Rectangle2Ds, plantRows
	 * @return plantRows
	 */
	public ArrayList<Rectangle2D> getPlantRows() {
		return plantRows;
	}
	/**
	 * Getter for the PlantBuilder pb.
	 * @return pb - a PlantBuilder
	 */
	public PlantBuilder getPb() {
		return pb.getPb();
	}
	/**
	 * Setter for the PlantBuilder pb.
	 * @param pb
	 */
	public void setPb(PlantBuilder pb) {
		this.pb.setPb(pb);
	}
	/**
	 * Getter for the boolean renderDragGabion.
	 * @return boolean renderDragGabion
	 */
	public boolean isRenderDragGabion() {
		return renderDragGabion;
	}
	/**
	 * Getter for the boolean renderDragPlants. To deside whether or not to render drag plants.
	 * @return boolean renderDragPlant
	 */
	public boolean isRenderDragPlant() {
		return renderDragPlant;
	}
	/**
	 * Getter for ArrayList of RunOffController, runOff
	 * @return runOff
	 */
	public ArrayList<RunOffController> getRunOff() {
		return runOff;
	}
	/**
	 * Getter for boolean eroded. True if the shore has eroded.
	 * @return boolean eroded
	 */
	public boolean isEroded() {
		return eroded;
	}
	/**
	 * Setter for boolean eroded. True if the shore has eroded.
	 * @param eroded
	 */
	public void setEroded(boolean eroded) {
		this.eroded = eroded;
	}
	/**
	 * Getter for int numOfRows
	 * @return numOfRows
	 */
	public int getNumOfRows() {
		return numOfRows;
	}
	/**
	 * Setter for numOfRows.
	 * @param numOfRows
	 */
	public void setNumOfRows(int numOfRows) {
		this.numOfRows = numOfRows;
	}
	/**
	 * Getter for uiGabion
	 * @return Rectangle2D uiGabion
	 */
	public Rectangle2D getUiGabion() {
		return uiGabion;
	}
	/**
	 * Getter for numOfWavesInRow.
	 * @return ArrayList<Integer> numOfWavesInRow.
	 */
	public ArrayList<Integer> getNumOfWavesInRow() {
		return numOfWavesInRow;
	}
	/**
	 * Getter for message.
	 * @return String message.
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * Getter for currentGameState
	 * @return GameState currentGamestate.
	 */
	public GameState getCurrentGameState() {
		return currentGameState;
	}
	/**
	 * Setter for currentGameState.
	 * @param gs GameState game state of the game (e.g. LOADING, MENU, OVER)
	 */
	public void setCurrentGameState(GameState gs) {
		this.currentGameState = gs;
	}
	/**
	 * Getter for uiPlant
	 * @return Rectangle2D uiPlant.
	 */
	public Rectangle2D getUiPlant() {
		return uiPlant;
	}
	/**
	 * Getter for speaking
	 * @return boolean speaking.
	 */
	public boolean isSpeaking() {
		return speaking;
	}
	/**
	 * Setter for speaking
	 * @param speaking boolean True if blue crab is speaking false otherwise.
	 */
	public void setSpeaking(boolean speaking) {
		this.speaking = speaking;
	}
	/**
	 * Getter for gabions.
	 * @return ArrayList<GabionController> gabions.
	 */
	public ArrayList<GabionController> getGabions() {
		return gabions;
	}
	/**
	 * Getter for gabionBuilder.
	 * @return GabionBuilderController gb.
	 */
	public GabionBuilderController getGb() {
		return gb;
	}
	/**
	 * Getter for ableToPlaceGabion
	 * @return ableToPlaceGabion
	 */
	public boolean isAbleToPlaceGabion() {
		return ableToPlaceGabion;
	}
	/**
	 * 
	 * Setter for ableToPlaceGabion
	 * @param ableToPlaceGabion boolean True if able to place gabion. False otherwise.
	 */
	public void setAbleToPlaceGabion(boolean ableToPlaceGabion) {
		this.ableToPlaceGabion = ableToPlaceGabion;
	}
	/**
	 * Getter for ableToPlacePlant.
	 * @return ableToPlacePlant.
	 */
	public boolean isAbleToPlacePlant() {
		return ableToPlacePlant;
	}
	/**
	 * Setter for ableToPlacePlant.
	 * @param ableToPlacePlant boolean True if able to place plant. False otherwise.
	 */
	public void setAbleToPlacePlant(boolean ableToPlacePlant) {
		this.ableToPlacePlant = ableToPlacePlant;
	}
	/**
	 * Getter for bic.
	 * @return bic.
	 */
	public BufferedImageController getBic() {
		return bic;
	}
	/**
	 * Setter for bic.
	 * @param bic BufferedImageController object that handles imaging processing.
	 */
	public void setBic(BufferedImageController bic) {
		this.bic = bic;
	}
	/**
	 * Getter for game.
	 * @return game.
	 */
	public Game getGame() {
		return game;
	}
	/**
	 * Setter for game.
	 * @param game Game.
	 */
	public void setGame(Game game) {
		this.game = game;
	}
	/**
	 * Getter for timer.
	 * @return timer.
	 */
	public Timer getTimer() {
		return timer;
	}
	/**
	 * Setter for timer.
	 * @param timer Timer.
	 */
	public void setTimer(Timer timer) {
		this.timer = timer;
	}

}
