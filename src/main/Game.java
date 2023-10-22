package main;

import java.awt.Color;

import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import display.Display;
import graphics.ImageLoader;
import graphics.TextureAtlas;
import states.GameState;
import states.SettingState;
import states.State;

public class Game implements Runnable {

	private Display display;
	public int width, height;
	public String title;

	private boolean running = false;
	private Thread thread;

	public int frameCount = 0;

	private BufferStrategy bs;
	public Graphics2D g;

	public GameState gameState = new GameState(this);
	public SettingState settingState = new SettingState(this);

	public static BufferedImage piecesImage, analysisIcon, whitePerspectiveIcon, blackPerspectiveIcon, settingsIcon, territoryMapEnabledIcon, territoryMapDisabledIcon;
	public static TextureAtlas sheet;

	public Game(String title, int width, int height) {
		this.width = width;
		this.height = height;
		this.title = title;
	}

	private void init() {
		// Create Display
		display = new Display(title, width, height);
		
		// Initialize
		prepareImages();
		State.setState(gameState);
		gameState.initialize();
		settingState.initialize();
	}

	public void tick() {

		if (State.getState() != null) {
			State.getState().tick(g);
		}

	}

	public void render() {
		bs = display.getCanvas().getBufferStrategy();
		if (bs == null) {
			display.getCanvas().createBufferStrategy(3);
			return;
		}

		g = (Graphics2D) bs.getDrawGraphics();

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);

		if (State.getState() != null) {
			State.getState().render(g);
		}

		bs.show();
		g.dispose();
	}

	public void run() {

		init();

		int fps = 60;
		double timePerTick = 1000000000 / fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;

		while (running) {
			now = System.nanoTime();
			delta += (now - lastTime) / timePerTick;
			timer += now - lastTime;
			lastTime = now;

			if (delta >= 1) {
				tick();
				render();
				frameCount++;
				delta--;
			}

			if (timer >= 1e9) {
				timer = 0;
			}
		}

		stop();

	}

	public synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	public synchronized void stop() {
		if (!running) {
			return;
		}
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Display getDisplay() {
		return display;
	}

	public Graphics2D getGraphics2D() {
		return g;
	}

	public static void prepareImages() {
		piecesImage = ImageLoader.loadImage("/textures/piecesSheet.png");
		analysisIcon = ImageLoader.loadImage("/textures/Analysis Icon.png");
		whitePerspectiveIcon = ImageLoader.loadImage("/textures/White Perspective Icon.png");
		blackPerspectiveIcon = ImageLoader.loadImage("/textures/Black Perspective Icon.png");
		settingsIcon = ImageLoader.loadImage("/textures/Settings Icon.png");
		territoryMapEnabledIcon = ImageLoader.loadImage("/textures/Territory Map Enabled Icon.png");
		territoryMapDisabledIcon = ImageLoader.loadImage("/textures/Territory Map Disabled Icon.png");
		sheet = new TextureAtlas(piecesImage);
	}

}