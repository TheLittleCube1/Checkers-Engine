package hashing;

import java.util.HashMap;

import checkers.Position;

public class Hashing {
	
	public static HashMap<String, float[]> transpositionTable = new HashMap<String, float[]>();
	
	public static String hash(Position position) {
		
		String hash = "";
		
		int[][] board = position.getBoard();
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				hash += board[i][j];
			}
		}
		
		hash += position.getTurn() ? 1 : 0;
		hash += position.moveNumber;
		
		return hash;
		
	}
	
}
