package checkers;

public class Position {

	public int[][] board;
	public int moveNumber = 1;
	private boolean turn = true;
	public Move previousMove = new Move();
	
	public Position(int[][] board) {
		this.board = board;
	}
	
	public int[][] getBoard() {
		return board;
	}
	
	public boolean getTurn() {
		return turn;
	}
	
	public void invertTurn() {
		turn = !turn;
	}
	
	public Position clone() {
		Position p = new Position(MoveGenerator.clone(board));
		p.turn = turn;
		p.moveNumber = moveNumber;
		return p;
	}

}
