package data;

import java.awt.image.BufferedImage;

import main.Game;

public class Start {

	public static boolean perspective = true;

	public static BufferedImage[] loadPieces() {
		BufferedImage[] p = new BufferedImage[5];
		p[0] = Game.sheet.crop(0, 0, 256, 256); // Blank
		p[1] = Game.sheet.crop(256, 0, 256, 256); // White checker
		p[2] = Game.sheet.crop(512, 0, 256, 256); // Black checker
		p[3] = Game.sheet.crop(768, 0, 256, 256); // White king
		p[4] = Game.sheet.crop(1024, 0, 256, 256); // Black king
		return p;
	}

	// Initial Position
	public static final int[][] STARTING_BOARD = {
			// White seat
//			 a  b  c  d  e  f  g  h
			{ 1, 0, 1, 0, 1, 0, 1, 0 }, // 1
			{ 0, 1, 0, 1, 0, 1, 0, 1 }, // 2
			{ 1, 0, 1, 0, 1, 0, 1, 0 }, // 3
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 4
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, // 5
			{ 0, 2, 0, 2, 0, 2, 0, 2 }, // 6
			{ 2, 0, 2, 0, 2, 0, 2, 0 }, // 7
			{ 0, 2, 0, 2, 0, 2, 0, 2 }, // 8
			// Black seat
	};

	// Test Position 1
//	public static final int[][] STARTING_BOARD = { 
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
//			{ 0, 0, 0, 4, 0, 0, 0, 0 }, 
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
//			{ 0, 3, 0, 0, 0, 0, 0, 0 }, 
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
//			{ 0, 3, 0, 0, 0, 0, 0, 0 }, 
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
//	};

//	// Test Position 2
//	public static final int[][] STARTING_BOARD = {
//			{ 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 2, 0, 2, 0, 2, 0 },
//			{ 0, 0, 0, 3, 0, 0, 0, 0 },
//			{ 0, 0, 2, 0, 0, 0, 2, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0 },
//			{ 0, 0, 2, 0, 2, 0, 2, 0 },
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }
//	};

	// Test Position 3
//	public static final int[][] STARTING_BOARD = { 
//			{ 4, 0, 1, 0, 0, 0, 0, 0 }, 
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
//			{ 2, 0, 0, 0, 1, 0, 0, 0 }, 
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
//			{ 0, 0, 0, 3, 0, 0, 0, 0 }, 
//			{ 0, 0, 0, 0, 0, 0, 0, 0 }, 
//			{ 0, 3, 0, 0, 0, 0, 0, 0 }, 
//	};

}
