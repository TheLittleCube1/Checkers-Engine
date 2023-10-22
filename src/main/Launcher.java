package main;

import engine.Evaluation;
import input.InputData;
import input.MouseManager;
import states.GameState;
import states.SettingState;

public class Launcher {
	
	public static final int WIDTH = 720, HEIGHT = 720;
	public static Game game;
	public static GameState gameState;
	public static SettingState settingState;
	
	public static MouseManager mouseManager;
	public static InputData inputData;

	public static void main(String[] args){
		
		prepare();
		
		game = new Game("Hopper", WIDTH, HEIGHT);
		game.start();
		gameState = game.gameState;
		settingState = game.settingState;
		
	}
	
	public static void prepare() {
		Evaluation.initializePieceValues();
	}

}