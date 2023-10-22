package data;

import java.awt.image.BufferedImage;

public class Pieces {
	
	public static final int EMPTY = 0, WHITE_CHECKER = 1, BLACK_CHECKER = 2, WHITE_KING = 3, BLACK_KING = 4;

	public static final String[] NAMES = { 
			"Empty", // 0
			"White Checker", // 1
			"Black Checker", // 2
			"White King", // 3
			"Black King", // 4
	};

	public static int color(int piece) {
		if (piece == 0) {
			return 0;
		} else if (piece % 2 == 0) {
			return 2;
		} else {
			return 1;
		}
	}

	public static boolean nonEmptyColor(int piece) {
		if (piece % 2 == 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public static final BufferedImage[] pieces = Start.loadPieces();

}
