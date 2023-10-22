package input;

import data.Start;
import main.Launcher;
import states.GameState;

public class Mouse {
	
	public static int x() {
		return Launcher.mouseManager.getX();
	}

	public static int y() {
		return Launcher.mouseManager.getY();
	}
	
	public static int pixelToFile(int x) {
		if (x < 0) {
			return -1;
		} else if (x > GameState.BOARD_SIZE) {
			return 8;
		}
		if (Start.perspective) {
			return (int) (8.0 * x / GameState.BOARD_SIZE);
		} else {
			return (int) (8 - 8.0 * x / GameState.BOARD_SIZE);
		}
	}
	
	public static int pixelToRank(int y) {
		if (y < 0) {
			return -1;
		} else if (y > GameState.BOARD_SIZE) {
			return 8;
		}
		if (Start.perspective) {
			return (int) (8 - 8.0 * y / GameState.BOARD_SIZE);
		} else {
			return (int) (8.0 * y / GameState.BOARD_SIZE);
		}
	}
	
	public static int fileToPixels(int file) {
		int fileOnScreen = rotatedFile(file);
		return (int) ((fileOnScreen + 0.5f) * GameState.SQUARE_SIZE + GameState.BOARD_X);
	}
	
	public static int rankToPixels(int rank) {
		int rankOnScreen = rotatedRank(rank);
		return (int) ((rankOnScreen + 0.5f) * GameState.SQUARE_SIZE + GameState.BOARD_Y);
	}
	
	public static int rotatedFile(int x) {
		if (Start.perspective) {
			return x;
		} else {
			return 7 - x;
		}
	}
	
	public static int rotatedRank(int y) {
		if (Start.perspective) {
			return 7 - y;
		} else {
			return y;
		}
	}
	
}
