package testing;

import java.util.List;

import checkers.Move;
import checkers.MoveGenerator;
import checkers.Position;
import data.DataHandler;
import states.GameState;

public class Test {
	
	public static void perftReport() {
		
		int depth = 6;
		long start = System.nanoTime();
		int result = perft(GameState.position, depth);
		System.out.println("Depth: " + depth);
		System.out.println("Result: " + result);
		long time = System.nanoTime() - start;
		System.out.println("Time: " + DataHandler.nanoToString(time));
		System.out.println("Speed: " + result * 1000000000L / time + " positions/second\n");
		
	}
	
	public static int perft(Position position, int depth) {
		
		if (depth == 0) {
			return 1;
		}
		
		int result = 0;
		List<Move> moves = MoveGenerator.generateLegalMoves(position);
		Position positionClone = position.clone();
		for (Move move : moves) {
			move.playMove(positionClone);
			result += perft(positionClone, depth - 1);
			positionClone = position.clone();
		}
		
		return result;
		
	}
	
}
