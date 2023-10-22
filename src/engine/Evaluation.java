package engine;

import java.util.HashMap;
import java.util.List;

import checkers.Move;
import checkers.MoveGenerator;
import checkers.Position;
import data.Pieces;
import data.Toolbox;
import hashing.Hashing;

public class Evaluation {

	public static HashMap<Integer, Float> pieceValues = new HashMap<Integer, Float>();

	public static void initializePieceValues() {
		pieceValues.put(0, 0f);
		pieceValues.put(1, 1f);
		pieceValues.put(2, -1f);
		pieceValues.put(3, 2.5f);
		pieceValues.put(4, -2.5f);
	}

	public static float eval(Position position, int totalDepth, int depth, float alpha, float beta) {

		List<Move> legalMoves = MoveGenerator.generateLegalMoves(position);

		if (legalMoves.isEmpty()) {
			return Toolbox.mateIn(totalDepth - depth, !position.getTurn());
		}

		if (depth == 0) {
			return evalNeutral(position, totalDepth);
		}

		if (position.getTurn()) {
			float maxEval = Float.NEGATIVE_INFINITY;
			Position positionClone = position.clone();
			for (Move move : legalMoves) {
				move.playMove(positionClone);
				String hash = Hashing.hash(positionClone);
				float eval;
				if (Hashing.transpositionTable.containsKey(hash)) {
					float[] tableResult = Hashing.transpositionTable.get(hash);
					if (tableResult[1] == depth - 1) {
						eval = tableResult[0];
					} else {
						eval = eval(positionClone, totalDepth, depth - 1, alpha, beta);
						Hashing.transpositionTable.put(hash, new float[] { eval, depth - 1 });
					}
				} else {
					eval = eval(positionClone, totalDepth, depth - 1, alpha, beta);
					Hashing.transpositionTable.put(hash, new float[] { eval, depth - 1 });
				}
				maxEval = Math.max(maxEval, eval);
				alpha = Math.max(alpha, eval);
				if (beta <= alpha) {
					break;
				}

				positionClone = position.clone();
			}
			return maxEval;
		} else {
			float minEval = Float.POSITIVE_INFINITY;
			Position positionClone = position.clone();
			for (Move move : legalMoves) {
				move.playMove(positionClone);
				String hash = Hashing.hash(positionClone);
				float eval;
				if (Hashing.transpositionTable.containsKey(hash)) {
					float[] tableResult = Hashing.transpositionTable.get(hash);
					if (tableResult[1] == depth - 1) {
						eval = tableResult[0];
					} else {
						eval = eval(positionClone, totalDepth, depth - 1, alpha, beta);
						Hashing.transpositionTable.put(hash, new float[] { eval, depth - 1 });
					}
				} else {
					eval = eval(positionClone, totalDepth, depth - 1, alpha, beta);
					Hashing.transpositionTable.put(hash, new float[] { eval, depth - 1 });
				}
				minEval = Math.min(minEval, eval);
				beta = Math.min(beta, eval);
				if (beta <= alpha) {
					break;
				}

				positionClone = position.clone();
			}
			return minEval;
		}

	}

	public static float evalNeutral(Position position, int depth) {
		float whiteSum = 0;
		float whiteProtection = 0;
		float blackSum = 0;
		float blackProtection = 0;
		float aggression = Engine.calculateAggression(position.getTurn(), Engine.currentEval);
		float distanceSum = 0;
		int d = 0;
		float factor = 1;
		int[][] board = position.getBoard();

		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				factor = 1;
				float value = pieceValues.get(board[rank][file]);
				if (value > 0) {

					if (position.getTurn()) {
						for (int f = 0; f < 8; f++) {
							for (int r = 0; r < 8; r++) {
								if (pieceValues.get(board[r][f]) < 0) {
									distanceSum += stepsBetweenSquares(f, file, r, rank);
									d++;
								}
							}
						}
					}

					// Protection of pieces
					if (rank != 0) {
						if (file != 0) {
							int piece = board[rank - 1][file - 1];
							if (piece == Pieces.WHITE_CHECKER || piece == Pieces.WHITE_KING) {
								whiteProtection += 0.1;
							}
						}
						if (file != 7) {
							int piece = board[rank - 1][file + 1];
							if (piece == Pieces.WHITE_CHECKER || piece == Pieces.WHITE_KING) {
								whiteProtection += 0.1;
							}
						}
					}
					float distance = (float) (Math.abs(file - 3.5) + Math.abs(rank - 3.5));
					factor += Toolbox.map(distance, 0.5f, 3.5f, 0.95f, 1.05f);
					whiteSum += value * factor;
				} else if (value < 0) {

					if (!position.getTurn()) {
						for (int f = 0; f < 8; f++) {
							for (int r = 0; r < 8; r++) {
								if (pieceValues.get(board[r][f]) > 0) {
									distanceSum += stepsBetweenSquares(f, file, r, rank);
									d++;
								}
							}
						}
					}

					// Protection of pieces
					if (rank != 7) {
						if (file != 0) {
							int piece = board[rank + 1][file - 1];
							if (piece == Pieces.BLACK_CHECKER || piece == Pieces.BLACK_KING) {
								blackProtection += 0.1;
							}
						}
						if (file != 7) {
							int piece = board[rank + 1][file + 1];
							if (piece == Pieces.BLACK_CHECKER || piece == Pieces.BLACK_KING) {
								blackProtection += 0.1;
							}
						}
					}
					float distance = (float) (Math.abs(file - 3.5) + Math.abs(rank - 3.5));
					factor += Toolbox.map(distance, 0.5f, 3.5f, 0.95f, 1.05f);
					blackSum -= value * factor;
				}
			}
		}

		Engine.nodes++;

		List<Move> moves = MoveGenerator.generateLegalMoves(position);
		if (moves.size() == 0) {
			return position.getTurn() ? Float.NEGATIVE_INFINITY : Float.POSITIVE_INFINITY;
		}

		float averageDistance = distanceSum / d;
		float distanceAggressionProduct = averageDistance * aggression * 0.3f;
		float withPerspective = position.getTurn() ? distanceAggressionProduct : -distanceAggressionProduct;

		return whiteSum + whiteProtection - blackSum - blackProtection + withPerspective;
	}

	public static boolean greaterWithPerspective(float left, float right, boolean perspective) {
		boolean base = left > right;
		return !(base ^ perspective);
	}

	public static float evalToFraction(float eval) {
		// White won means 1 and black won means 0
		if (eval > 10000) {
			return 0;
		} else if (eval < -10000) {
			return 1;
		} else {
			return modifiedSigmoid(eval);
		}
	}

	public static String evalToString(float eval) {
		if (eval == 100000) {
			return "W";
		} else if (eval == -100000) {
			return "B";
		} else if (eval > 10000f) {
			return "M" + (100000 - (int) eval);
		} else if (eval < -10000f) {
			return "-M" + (100000 + (int) eval);
		} else {
			return "" + eval;
		}
	}

	public static float modifiedSigmoid(float x) {
		return 0.8f / (1 + (float) Math.exp(x * 0.3f)) + 0.1f;
	}

	public static int stepsBetweenSquares(int x1, int y1, int x2, int y2) {
		return Math.abs(y2 - y1) + Math.abs(x2 - x1);
	}
}
