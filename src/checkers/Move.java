package checkers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import states.GameState;

public class Move {
	
	private List<Point> squares = new ArrayList<Point>();

	public Move(Point...points) {
		squares.addAll(Arrays.asList(points));
	}
	
	public void playMove(Position position) {
		int[][] board = position.getBoard();
		for (int i = 1; i < squares.size(); i++) {
			Point previousSquare = squares.get(i - 1);
			Point currentSquare = squares.get(i);
			if (Math.abs(previousSquare.x - currentSquare.x) == 1) {
				board[currentSquare.y][currentSquare.x] = board[previousSquare.y][previousSquare.x];
				board[previousSquare.y][previousSquare.x] = 0;
			} else {
				board[currentSquare.y][currentSquare.x] = board[previousSquare.y][previousSquare.x];
				board[previousSquare.y][previousSquare.x] = 0;
				board[(previousSquare.y + currentSquare.y) / 2][(previousSquare.x + currentSquare.x) / 2] = 0;
			}
		}
		Point lastSquare = squares.get(squares.size() - 1);
		if (lastSquare.y == 0) {
			if (board[lastSquare.y][lastSquare.x] == 2) {
				board[lastSquare.y][lastSquare.x] = 4;
			}
		}
		if (lastSquare.y == 7) {
			if (board[lastSquare.y][lastSquare.x] == 1) {
				board[lastSquare.y][lastSquare.x] = 3;
			}
		}
		position.invertTurn();
		position.moveNumber++;
		position.previousMove = this.clone();
	}
	
	public String toString() {
		String s = "(";
		for (int i = 0; i < squares.size(); i++) {
			Point square = squares.get(i);
			s += GameState.FILES[square.x];
			s += square.y + 1;
			if (i != squares.size() - 1) {
				s += " to ";
			}
		}
		s += ")";
		return s;
	}
	
	public boolean equals(Move otherMove) {
		if (squares.equals(otherMove.getSquares())) {
			return true;
		} else {
			return false;
		}
	}

	public List<Point> getSquares() {
		return squares;
	}

	public void setSquares(List<Point> squares) {
		this.squares = squares;
	}
	
	public Move clone() {
		Move m = new Move();
		m.getSquares().addAll(squares);
		return m;
	}

}
