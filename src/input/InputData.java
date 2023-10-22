package input;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import checkers.Move;
import checkers.MoveGenerator;
import checkers.Position;
import data.Pieces;
import main.Launcher;
import states.GameState;

public class InputData {

	private Point selectedSquare = new Point();
	private boolean selecting = false;
	private Point baseSquare = new Point(-1, -1);
	private Move runningMove = new Move();
	public Position position;
	public Position theoreticalPosition;
	public int[][] board;

	public int lastInput = 0;

	public InputData() {
		position = GameState.position;
		board = position.getBoard();
	}

	public void clickSquare(Point square) {
		
		lastInput = Launcher.game.frameCount;
		
		// Point square is from white's perspective

		// Off the board
		if (square.x >= 8 || square.x < 0 || square.y >= 8 || square.y < 0) {
			// Unselect
			setSelectedSquare(null);
			setSelecting(false);
		}

		// In the board
		else {
			if (selecting) {
				if (square.x == selectedSquare.x && square.y == selectedSquare.y) {
					// Unselect
					setBaseSquare(null);
					runningMove.getSquares().clear();
					setSelectedSquare(null);
					setSelecting(false);
				} else if (board[baseSquare.y][baseSquare.x] != 0) {
					// Move if legal
					// Select other piece if not
					int piece = board[baseSquare.y][baseSquare.x];
					boolean color = Pieces.nonEmptyColor(piece);
					if ((color && !GameState.computerAsWhite) || (!color && !GameState.computerAsBlack)) {
						runningMove.getSquares().add(square);
						List<Move> moves;
						if (color == position.getTurn()) {
							moves = MoveGenerator.generateLegalMoves(position);
						} else {
							moves = new ArrayList<Move>();
						}
						if (contains(moves, runningMove)) {
							runningMove.playMove(position);
							setSelectedSquare(null);
							setSelecting(false);
							runningMove.getSquares().clear();
						} else if (containsSoFar(moves, runningMove)) {
							setSelectedSquare(square);
							setSelecting(true);
						} else {
							setBaseSquare(square);
							setSelectedSquare(square);
							runningMove.getSquares().clear();
							runningMove.getSquares().add(square);
							setSelecting(true);
						}
					} else {
						setBaseSquare(square);
						setSelectedSquare(square);
						runningMove.getSquares().clear();
						setSelecting(true);
					}
				} else if (board[selectedSquare.y][selectedSquare.x] == 0) {
					setBaseSquare(square);
					setSelectedSquare(square);
					runningMove.getSquares().clear();
					runningMove.getSquares().add(square);
					setSelecting(true);
				}
			} else {
				setBaseSquare(square);
				setSelectedSquare(square);
				runningMove.getSquares().clear();
				runningMove.getSquares().add(square);
				setSelecting(true);
			}
		}

	}

	public Move getRunningMove() {
		return runningMove;
	}

	public void setRunningMove(Move runningMove) {
		this.runningMove = runningMove;
	}

	public static boolean contains(List<Move> moves, Move move) {
		for (int i = 0; i < moves.size(); i++) {
			if (moves.get(i).equals(move)) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsSoFar(List<Move> moves, Move move) {
		for (Move legalMove : moves) {
			int size = move.getSquares().size();
			if (size > legalMove.getSquares().size()) {
				continue;
			}
			for (int i = 0; i < size; i++) {
				if (legalMove.getSquares().get(i).x != move.getSquares().get(i).x
						|| legalMove.getSquares().get(i).y != move.getSquares().get(i).y) {
					break;
				}
				if (i == size - 1) {
					return true;
				}
			}
		}
		return false;
	}

	public static void replacePiece(int[][] board, int x, int y, int newPiece) {
		board[y][x] = newPiece;
	}

	public boolean isSelecting() {
		return selecting;
	}

	public void setSelecting(boolean selecting) {
		this.selecting = selecting;
	}

	public Point getSelectedSquare() {
		return selectedSquare;
	}

	public void setSelectedSquare(Point selectedSquare) {
		if (selectedSquare == null) {
			this.selectedSquare = null;
			return;
		}
		this.selectedSquare = new Point(selectedSquare.x, selectedSquare.y);
	}

	public Point getBaseSquare() {
		return baseSquare;
	}

	public void setBaseSquare(Point baseSquare) {
		if (baseSquare == null) {
			this.baseSquare = null;
			return;
		}
		this.baseSquare = new Point(baseSquare.x, baseSquare.y);
	}

}
