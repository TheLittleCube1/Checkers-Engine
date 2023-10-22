package checkers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import data.CaptureFoundObject;
import data.Pieces;

public class MoveGenerator {

	public static List<Move> generateLegalMoves(Position position) {
		CaptureFoundObject captureFound = new CaptureFoundObject(false);
		boolean newCapture = false;
		List<Move> moves = new ArrayList<Move>();
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				List<Move> m = generateMovesFromSquare(position, file, rank, captureFound);
				if (m.size() != 0) {
					if (Math.abs(m.get(0).getSquares().get(0).x - m.get(0).getSquares().get(1).x) == 2 && !newCapture) {
						moves.clear();
						newCapture = true;
					}
				}
				moves.addAll(m);
			}
		}
		return moves;
	}

	public static List<Move> generateMovesFromSquare(Position position, int file, int rank,
			CaptureFoundObject captureFound) {
		int[][] board = position.getBoard();
		int piece = board[rank][file];

		if (position.getTurn() != Pieces.nonEmptyColor(piece) || piece == 0) {
			return new ArrayList<Move>();
		}

		if (piece == Pieces.WHITE_CHECKER) {
			return generateWhiteCheckerMovesFromSquare(position, file, rank, captureFound);
		} else if (piece == Pieces.BLACK_CHECKER) {
			return generateBlackCheckerMovesFromSquare(position, file, rank, captureFound);
		} else if (piece == Pieces.WHITE_KING) {
			return generateWhiteKingMovesFromSquare(position, file, rank, captureFound);
		} else if (piece == Pieces.BLACK_KING) {
			return generateBlackKingMovesFromSquare(position, file, rank, captureFound);
		} else {
			return new ArrayList<Move>();
		}

	}

	public static List<Move> generateWhiteCheckerMovesFromSquare(Position position, int file, int rank,
			CaptureFoundObject captureFound) {
		List<Move> moves = new ArrayList<Move>();
		List<Move> captures = new ArrayList<Move>();
		int[][] board = position.getBoard();

		List<ArrayList<Point>> captureChains = generateWhiteCheckerCaptureChainsFromSquare(position, file, rank);
		for (ArrayList<Point> captureChain : captureChains) {
			if (captureChain.size() > 1) {
				Move currentMove = new Move();
				currentMove.setSquares(captureChain);
				captures.add(currentMove);
			}
		}
		if (captures.size() != 0) {
			captureFound.v = true;
			return captures;
		}

		if (!captureFound.v) {
			if (withinBoard(file - 1, rank + 1)) {
				int capturedSquare = board[rank + 1][file - 1];

				// Moving Left
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file - 1, rank + 1)));
				}

			}

			if (withinBoard(file + 1, rank + 1)) {

				// Moving Right
				int capturedSquare = board[rank + 1][file + 1];
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file + 1, rank + 1)));
				}

			}
		}

		return moves;
	}

	public static List<Move> generateBlackCheckerMovesFromSquare(Position position, int file, int rank,
			CaptureFoundObject captureFound) {
		List<Move> moves = new ArrayList<Move>();
		List<Move> captures = new ArrayList<Move>();
		int[][] board = position.getBoard();

		List<ArrayList<Point>> captureChains = generateBlackCheckerCaptureChainsFromSquare(position, file, rank);
		for (ArrayList<Point> captureChain : captureChains) {
			if (captureChain.size() > 1) {
				Move currentMove = new Move();
				currentMove.setSquares(captureChain);
				captures.add(currentMove);
			}
		}
		if (captures.size() != 0) {
			captureFound.v = true;
			return captures;
		}

		if (!captureFound.v) {
			if (withinBoard(file - 1, rank - 1)) {
				int capturedSquare = board[rank - 1][file - 1];

				// Moving Left
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file - 1, rank - 1)));
				}

			}

			if (withinBoard(file + 1, rank - 1)) {

				// Moving Right
				int capturedSquare = board[rank - 1][file + 1];
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file + 1, rank - 1)));
				}

			}
		}

		return moves;
	}

	public static List<Move> generateWhiteKingMovesFromSquare(Position position, int file, int rank,
			CaptureFoundObject captureFound) {
		List<Move> moves = new ArrayList<Move>();
		List<Move> captures = new ArrayList<Move>();
		int[][] board = position.getBoard();

		List<ArrayList<Point>> captureChains = generateWhiteKingCaptureChainsFromSquare(position, file, rank);
		for (ArrayList<Point> captureChain : captureChains) {
			if (captureChain.size() > 1) {
				Move currentMove = new Move();
				currentMove.setSquares(captureChain);
				captures.add(currentMove);
			}
		}
		if (captures.size() != 0) {
			captureFound.v = true;
			return captures;
		}

		if (!captureFound.v) {
			if (withinBoard(file - 1, rank + 1)) {

				int capturedSquare = board[rank + 1][file - 1];
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file - 1, rank + 1)));
				}

			}

			if (withinBoard(file + 1, rank + 1)) {

				int capturedSquare = board[rank + 1][file + 1];
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file + 1, rank + 1)));
				}

			}

			if (withinBoard(file - 1, rank - 1)) {

				int capturedSquare = board[rank - 1][file - 1];
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file - 1, rank - 1)));
				}

			}

			if (withinBoard(file + 1, rank - 1)) {

				int capturedSquare = board[rank - 1][file + 1];
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file + 1, rank - 1)));
				}

			}
		}

		return moves;
	}

	public static List<Move> generateBlackKingMovesFromSquare(Position position, int file, int rank,
			CaptureFoundObject captureFound) {
		List<Move> moves = new ArrayList<Move>();
		List<Move> captures = new ArrayList<Move>();
		int[][] board = position.getBoard();

		List<ArrayList<Point>> captureChains = generateBlackKingCaptureChainsFromSquare(position, file, rank);
		for (ArrayList<Point> captureChain : captureChains) {
			if (captureChain.size() > 1) {
				Move currentMove = new Move();
				currentMove.setSquares(captureChain);
				captures.add(currentMove);
			}
		}
		if (captures.size() != 0) {
			captureFound.v = true;
			return captures;
		}

		if (!captureFound.v) {
			if (withinBoard(file - 1, rank + 1)) {

				int capturedSquare = board[rank + 1][file - 1];
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file - 1, rank + 1)));
				}

			}

			if (withinBoard(file + 1, rank + 1)) {

				int capturedSquare = board[rank + 1][file + 1];
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file + 1, rank + 1)));
				}

			}

			if (withinBoard(file - 1, rank - 1)) {

				int capturedSquare = board[rank - 1][file - 1];
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file - 1, rank - 1)));
				}

			}

			if (withinBoard(file + 1, rank - 1)) {

				int capturedSquare = board[rank - 1][file + 1];
				if (capturedSquare == Pieces.EMPTY) {
					moves.add(new Move(new Point(file, rank), new Point(file + 1, rank - 1)));
				}

			}
		}

		return moves;
	}

	public static List<ArrayList<Point>> generateWhiteCheckerCaptureChainsFromSquare(Position position, int file,
			int rank) {
		List<ArrayList<Point>> continuations = new ArrayList<ArrayList<Point>>();
		ArrayList<Point> continuation = new ArrayList<Point>();
		int[][] board = position.getBoard();
		boolean left = false, right = false;

		if (withinBoard(file - 2, rank + 2)) {
			int capturedSquare = board[rank + 1][file - 1];
			if (capturedSquare == Pieces.BLACK_CHECKER || capturedSquare == Pieces.BLACK_KING) {
				if (board[rank + 2][file - 2] == Pieces.EMPTY) {
					left = true;
					List<ArrayList<Point>> newContinuations = generateWhiteCheckerCaptureChainsFromSquare(position,
							file - 2, rank + 2);
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (withinBoard(file + 2, rank + 2)) {
			int capturedSquare = board[rank + 1][file + 1];
			if (capturedSquare == Pieces.BLACK_CHECKER || capturedSquare == Pieces.BLACK_KING) {
				if (board[rank + 2][file + 2] == Pieces.EMPTY) {
					right = true;
					List<ArrayList<Point>> newContinuations = generateWhiteCheckerCaptureChainsFromSquare(position,
							file + 2, rank + 2);
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (!left && !right) {
			continuation = new ArrayList<Point>();
			continuation.add(new Point(file, rank));
			continuations.add(continuation);
		}

		return continuations;
	}

	public static List<ArrayList<Point>> generateBlackCheckerCaptureChainsFromSquare(Position position, int file,
			int rank) {
		List<ArrayList<Point>> continuations = new ArrayList<ArrayList<Point>>();
		ArrayList<Point> continuation = new ArrayList<Point>();
		int[][] board = position.getBoard();
		boolean left = false, right = false;

		if (withinBoard(file - 2, rank - 2)) {
			int capturedSquare = board[rank - 1][file - 1];
			if (capturedSquare == Pieces.WHITE_CHECKER || capturedSquare == Pieces.WHITE_KING) {
				if (board[rank - 2][file - 2] == Pieces.EMPTY) {
					left = true;
					List<ArrayList<Point>> newContinuations = generateBlackCheckerCaptureChainsFromSquare(position,
							file - 2, rank - 2);
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (withinBoard(file + 2, rank - 2)) {
			int capturedSquare = board[rank - 1][file + 1];
			if (capturedSquare == Pieces.WHITE_CHECKER || capturedSquare == Pieces.WHITE_KING) {
				if (board[rank - 2][file + 2] == Pieces.EMPTY) {
					right = true;
					List<ArrayList<Point>> newContinuations = generateBlackCheckerCaptureChainsFromSquare(position,
							file + 2, rank - 2);
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (!left && !right) {
			continuation = new ArrayList<Point>();
			continuation.add(new Point(file, rank));
			continuations.add(continuation);
		}

		return continuations;
	}

	public static List<ArrayList<Point>> generateWhiteKingCaptureChainsFromSquare(Position position, int file,
			int rank) {
		List<ArrayList<Point>> continuations = new ArrayList<ArrayList<Point>>();
		ArrayList<Point> continuation = new ArrayList<Point>();
		int[][] board = position.getBoard();
		boolean c = false;

		if (withinBoard(file - 2, rank + 2)) {
			int capturedSquare = board[rank + 1][file - 1];
			if (capturedSquare == Pieces.BLACK_CHECKER || capturedSquare == Pieces.BLACK_KING) {
				if (board[rank + 2][file - 2] == Pieces.EMPTY) {
					c = true;
					board[rank][file] = Pieces.EMPTY;
					board[rank + 1][file - 1] = Pieces.EMPTY;
					board[rank + 2][file - 2] = Pieces.WHITE_KING;
					List<ArrayList<Point>> newContinuations = generateWhiteKingCaptureChainsFromSquare(position,
							file - 2, rank + 2);
					board[rank][file] = Pieces.WHITE_KING;
					board[rank + 1][file - 1] = capturedSquare;
					board[rank + 2][file - 2] = Pieces.EMPTY;
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (withinBoard(file + 2, rank + 2)) {
			int capturedSquare = board[rank + 1][file + 1];
			if (capturedSquare == Pieces.BLACK_CHECKER || capturedSquare == Pieces.BLACK_KING) {
				if (board[rank + 2][file + 2] == Pieces.EMPTY) {
					c = true;
					board[rank][file] = Pieces.EMPTY;
					board[rank + 1][file + 1] = Pieces.EMPTY;
					board[rank + 2][file + 2] = Pieces.WHITE_KING;
					List<ArrayList<Point>> newContinuations = generateWhiteKingCaptureChainsFromSquare(position,
							file + 2, rank + 2);
					board[rank][file] = Pieces.WHITE_KING;
					board[rank + 1][file + 1] = capturedSquare;
					board[rank + 2][file + 2] = Pieces.EMPTY;
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (withinBoard(file - 2, rank - 2)) {
			int capturedSquare = board[rank - 1][file - 1];
			if (capturedSquare == Pieces.BLACK_CHECKER || capturedSquare == Pieces.BLACK_KING) {
				if (board[rank - 2][file - 2] == Pieces.EMPTY) {
					c = true;
					board[rank][file] = Pieces.EMPTY;
					board[rank - 1][file - 1] = Pieces.EMPTY;
					board[rank - 2][file - 2] = Pieces.WHITE_KING;
					List<ArrayList<Point>> newContinuations = generateWhiteKingCaptureChainsFromSquare(position,
							file - 2, rank - 2);
					board[rank][file] = Pieces.WHITE_KING;
					board[rank - 1][file - 1] = capturedSquare;
					board[rank - 2][file - 2] = Pieces.EMPTY;
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (withinBoard(file + 2, rank - 2)) {
			int capturedSquare = board[rank - 1][file + 1];
			if (capturedSquare == Pieces.BLACK_CHECKER || capturedSquare == Pieces.BLACK_KING) {
				if (board[rank - 2][file + 2] == Pieces.EMPTY) {
					c = true;
					board[rank][file] = Pieces.EMPTY;
					board[rank - 1][file + 1] = Pieces.EMPTY;
					board[rank - 2][file + 2] = Pieces.WHITE_KING;
					List<ArrayList<Point>> newContinuations = generateWhiteKingCaptureChainsFromSquare(position,
							file + 2, rank - 2);
					board[rank][file] = Pieces.WHITE_KING;
					board[rank - 1][file + 1] = capturedSquare;
					board[rank - 2][file + 2] = Pieces.EMPTY;
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (!c) {
			continuation = new ArrayList<Point>();
			continuation.add(new Point(file, rank));
			continuations.add(continuation);
		}

		return continuations;
	}

	public static List<ArrayList<Point>> generateBlackKingCaptureChainsFromSquare(Position position, int file,
			int rank) {
		List<ArrayList<Point>> continuations = new ArrayList<ArrayList<Point>>();
		ArrayList<Point> continuation = new ArrayList<Point>();
		int[][] board = position.getBoard();
		boolean c = false;

		if (withinBoard(file - 2, rank + 2)) {
			int capturedSquare = board[rank + 1][file - 1];
			if (capturedSquare == Pieces.WHITE_CHECKER || capturedSquare == Pieces.WHITE_KING) {
				if (board[rank + 2][file - 2] == Pieces.EMPTY) {
					c = true;
					board[rank][file] = Pieces.EMPTY;
					board[rank + 1][file - 1] = Pieces.EMPTY;
					board[rank + 2][file - 2] = Pieces.BLACK_KING;
					List<ArrayList<Point>> newContinuations = generateBlackKingCaptureChainsFromSquare(position,
							file - 2, rank + 2);
					board[rank][file] = Pieces.BLACK_KING;
					board[rank + 1][file - 1] = capturedSquare;
					board[rank + 2][file - 2] = Pieces.EMPTY;
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (withinBoard(file + 2, rank + 2)) {
			int capturedSquare = board[rank + 1][file + 1];
			if (capturedSquare == Pieces.WHITE_CHECKER || capturedSquare == Pieces.WHITE_KING) {
				if (board[rank + 2][file + 2] == Pieces.EMPTY) {
					c = true;
					board[rank][file] = Pieces.EMPTY;
					board[rank + 1][file + 1] = Pieces.EMPTY;
					board[rank + 2][file + 2] = Pieces.BLACK_KING;
					List<ArrayList<Point>> newContinuations = generateBlackKingCaptureChainsFromSquare(position,
							file + 2, rank + 2);
					board[rank][file] = Pieces.BLACK_KING;
					board[rank + 1][file + 1] = capturedSquare;
					board[rank + 2][file + 2] = Pieces.EMPTY;
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (withinBoard(file - 2, rank - 2)) {
			int capturedSquare = board[rank - 1][file - 1];
			if (capturedSquare == Pieces.WHITE_CHECKER || capturedSquare == Pieces.WHITE_KING) {
				if (board[rank - 2][file - 2] == Pieces.EMPTY) {
					c = true;
					board[rank][file] = Pieces.EMPTY;
					board[rank - 1][file - 1] = Pieces.EMPTY;
					board[rank - 2][file - 2] = Pieces.BLACK_KING;
					List<ArrayList<Point>> newContinuations = generateBlackKingCaptureChainsFromSquare(position,
							file - 2, rank - 2);
					board[rank][file] = Pieces.BLACK_KING;
					board[rank - 1][file - 1] = capturedSquare;
					board[rank - 2][file - 2] = Pieces.EMPTY;
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (withinBoard(file + 2, rank - 2)) {
			int capturedSquare = board[rank - 1][file + 1];
			if (capturedSquare == Pieces.WHITE_CHECKER || capturedSquare == Pieces.WHITE_KING) {
				if (board[rank - 2][file + 2] == Pieces.EMPTY) {
					c = true;
					board[rank][file] = Pieces.EMPTY;
					board[rank - 1][file + 1] = Pieces.EMPTY;
					board[rank - 2][file + 2] = Pieces.BLACK_KING;
					List<ArrayList<Point>> newContinuations = generateBlackKingCaptureChainsFromSquare(position,
							file + 2, rank - 2);
					board[rank][file] = Pieces.BLACK_KING;
					board[rank - 1][file + 1] = capturedSquare;
					board[rank - 2][file + 2] = Pieces.EMPTY;
					for (ArrayList<Point> newContinuation : newContinuations) {
						continuation = new ArrayList<Point>();
						continuation.add(new Point(file, rank));
						continuation.addAll(newContinuation);
						continuations.add(continuation);
					}

				}
			}
		}

		if (!c) {
			continuation = new ArrayList<Point>();
			continuation.add(new Point(file, rank));
			continuations.add(continuation);
		}

		return continuations;
	}

	public static int[][] clone(int[][] array) {
		int[][] newBoard = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				newBoard[i][j] = array[i][j];
			}
		}
		return newBoard;
	}

	public static boolean withinBoard(int file, int rank) {
		if (file <= -1 || file >= 8) {
			return false;
		}
		if (rank <= -1 || rank >= 8) {
			return false;
		}
		return true;
	}

}
