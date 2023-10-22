package engine;

import java.util.ArrayList;
import java.util.List;

import checkers.Move;
import checkers.MoveGenerator;
import checkers.Position;
import data.DataHandler;
import hashing.Hashing;
import main.Launcher;
import states.GameState;

public class Engine {
	
	public static float currentEval = 0;
	public static Move move;
	public static float evalTime = 0;
	public static int nodes = 0;
	public static float aggressionWhite = 0, aggressionBlack = 0;
	
	public static List<Move> predictedContinuation(Position p, int depth) {
		Position position = p.clone();
		List<Move> continuation = new ArrayList<Move>();
		for (int i = depth; i > 0; i--) {
			Move m = bestMove(position, depth);
			if (m == null) {
				return continuation;
			}
			continuation.add(m);
			m.playMove(position);
		}
		return continuation;
	}
	
	public static Move analyseMoves(Position position) {
		
		Move bestMove = new Move();
		nodes = 0;
		evalTime = 0;
		long start = System.nanoTime();
		GameState.achievedDepth = 0;
		
		int depth = 0;
		while (evalTime < GameState.maxSearchTime / 2) {
			depth++;
			Launcher.game.render();
			currentEval = 0;
			bestMove = analyseMovesStraight(position, depth);
			evalTime = (System.nanoTime() - start) / 1e9f;
			move = bestMove;
			if (GameState.computerAsWhite && currentEval > 10000f) {
				Launcher.game.render();
				Hashing.transpositionTable.clear();
				GameState.achievedDepth = depth;
				if (currentEval > 10000) {
					currentEval++;
				} else if (currentEval < -10000) {
					currentEval--;
				}
				Hashing.transpositionTable.clear();
				return bestMove;
			}
			else if (GameState.computerAsBlack && currentEval < -10000f) {
				Launcher.game.render();
				Hashing.transpositionTable.clear();
				GameState.achievedDepth = depth;
				if (currentEval > 10000) {
					currentEval++;
				} else if (currentEval < -10000) {
					currentEval--;
				}
				Hashing.transpositionTable.clear();
				return bestMove;
			}
			GameState.achievedDepth = depth;
			Launcher.game.render();
		}
		
		Hashing.transpositionTable.clear();
		return bestMove;
		
	}

	public static Move analyseMovesStraight(Position position, int depth) {
		
		nodes = 0;

		List<Move> legalMoves = MoveGenerator.generateLegalMoves(position);
		float alpha = Float.NEGATIVE_INFINITY, beta = Float.POSITIVE_INFINITY;
		
		Position positionClone = position.clone();
		
		if (legalMoves.isEmpty()) {
			currentEval = position.getTurn() ? -100000f : 100000f;
			return null;
		}
		
		Move bestMove = legalMoves.get(0);
		
		if (position.getTurn()) {
			float maxEval = Float.NEGATIVE_INFINITY;
			for (Move move : legalMoves) {
				move.playMove(positionClone);
				String hash = Hashing.hash(positionClone);
				float eval;
				if (Hashing.transpositionTable.containsKey(hash)) {
					float[] tableResult = Hashing.transpositionTable.get(hash);
					if (tableResult[1] == depth - 1) {
						eval = tableResult[0];
					} else {
						eval = Evaluation.eval(positionClone, depth, depth - 1, alpha, beta);
						Hashing.transpositionTable.put(hash, new float[] {eval, depth - 1});
					}
				} else {
					eval = Evaluation.eval(positionClone, depth, depth - 1, alpha, beta);
					Hashing.transpositionTable.put(hash, new float[] {eval, depth - 1});
				}
				if (eval > maxEval) {
					maxEval = eval;
					bestMove = move;
				}
				alpha = Math.max(alpha, eval);
				if (beta <= alpha) {
					break;
				}
				positionClone = position.clone();
			}
			currentEval = maxEval;
		} else {
			float minEval = Float.POSITIVE_INFINITY;
			for (Move move : legalMoves) {
				move.playMove(positionClone);
				String hash = Hashing.hash(positionClone);
				float eval;
				if (Hashing.transpositionTable.containsKey(hash)) {
					float[] tableResult = Hashing.transpositionTable.get(hash);
					if (tableResult[1] == depth - 1) {
						eval = tableResult[0];
					} else {
						eval = Evaluation.eval(positionClone, depth, depth - 1, alpha, beta);
						Hashing.transpositionTable.put(hash, new float[] {eval, depth - 1});
					}
				} else {
					eval = Evaluation.eval(positionClone, depth, depth - 1, alpha, beta);
					Hashing.transpositionTable.put(hash, new float[] {eval, depth - 1});
				}
				if (eval < minEval) {
					minEval = eval;
					bestMove = move;
				}
				beta = Math.min(beta, eval);
				if (beta <= alpha) {
					break;
				}
				positionClone = position.clone();
			}
			currentEval = minEval;
		}
		
		Hashing.transpositionTable.clear();
		
		return bestMove;

	}

	public static Move bestMove(Position position, int depth) {
		
		List<Move> legalMoves = MoveGenerator.generateLegalMoves(position);
		float alpha = Float.NEGATIVE_INFINITY, beta = Float.POSITIVE_INFINITY;

		if (legalMoves.isEmpty()) {
			return null;
		}

		Move bestMove = legalMoves.get(0);

		Position positionClone = position.clone();
		
		if (position.getTurn()) {
			float maxEval = Float.NEGATIVE_INFINITY;
			for (Move move : legalMoves) {
				move.playMove(positionClone);
				String hash = Hashing.hash(positionClone);
				float eval;
				if (Hashing.transpositionTable.containsKey(hash)) {
					float[] tableResult = Hashing.transpositionTable.get(hash);
					if (tableResult[1] == depth - 1) {
						eval = tableResult[0];
					} else {
						eval = Evaluation.eval(positionClone, depth, depth - 1, alpha, beta);
						Hashing.transpositionTable.put(hash, new float[] {eval, depth - 1});
					}
				} else {
					eval = Evaluation.eval(positionClone, depth, depth - 1, alpha, beta);
					Hashing.transpositionTable.put(hash, new float[] {eval, depth - 1});
				}
				if (eval > maxEval) {
					maxEval = eval;
					bestMove = move;
				}
				alpha = Math.max(alpha, eval);
				if (beta <= alpha) {
					break;
				}
				
				positionClone = position.clone();
			}
			currentEval = maxEval;
			return bestMove;
		} else {
			float minEval = Float.POSITIVE_INFINITY;
			for (Move move : legalMoves) {
				move.playMove(positionClone);
				String hash = Hashing.hash(positionClone);
				float eval;
				if (Hashing.transpositionTable.containsKey(hash)) {
					float[] tableResult = Hashing.transpositionTable.get(hash);
					if (tableResult[1] == depth - 1) {
						eval = tableResult[0];
					} else {
						eval = Evaluation.eval(positionClone, depth, depth - 1, alpha, beta);
						Hashing.transpositionTable.put(hash, new float[] {eval, depth - 1});
					}
				} else {
					eval = Evaluation.eval(positionClone, depth, depth - 1, alpha, beta);
					Hashing.transpositionTable.put(hash, new float[] {eval, depth - 1});
				}
				if (eval < minEval) {
					minEval = eval;
					bestMove = move;
				}
				beta = Math.min(beta, eval);
				if (beta <= alpha) {
					break;
				}
				
				positionClone = position.clone();
			}
			currentEval = minEval;
			return bestMove;
		}

	}
	
	public static float calculateAggression(boolean perspective, float eval) {
		if (!perspective) {
			if (eval >= 10) {
				return -1;
			} else if (eval <= -10) {
				return 1;
			} else {
				return (float) (-0.001 * Math.pow(eval, 3) - 0.001 * eval * eval + 0.1);
			}
		} else {
			if (eval >= 10) {
				return 1;
			} else if (eval <= -10) {
				return -1;
			} else {
				return (float) (0.001 * Math.pow(eval, 3) - 0.001 * eval * eval + 0.1);
			}
		}
	}

}
