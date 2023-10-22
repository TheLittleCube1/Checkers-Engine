package states;

import java.awt.BasicStroke;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import checkers.Move;
import checkers.MoveGenerator;
import checkers.Position;
import data.DataHandler;
import data.Pieces;
import data.Start;
import data.Toolbox;
import engine.Engine;
import engine.Evaluation;
import gui.AnalysisButton;
import gui.ButtonHandler;
import gui.SettingsButton;
import input.InputData;
import input.Mouse;
import main.Game;
import main.Launcher;

public class GameState extends State {

	public static int MARGIN = 40;
	public static int BOARD_SIZE = Launcher.WIDTH - MARGIN * 2;
	public static int SQUARE_SIZE = BOARD_SIZE / 8;
	public static int BOARD_X = MARGIN, BOARD_Y = MARGIN;
	public static final int ICON_DIAMETER = 25;
	public static final String[] FILES = { "a", "b", "c", "d", "e", "f", "g", "h" };

	public static final int ICON_X = Launcher.WIDTH - MARGIN / 2 - ICON_DIAMETER / 2;
	public static final int SETTINGS_ICON_Y = MARGIN / 2 - ICON_DIAMETER / 2;
	public static final int SWITCH_PERSPECTIVE_ICON_Y = MARGIN;

	public SettingsButton settingsButton;
	public AnalysisButton analysisButton;
	public ButtonHandler buttonHandler;

	public static int[][] board = MoveGenerator.clone(Start.STARTING_BOARD);
	public static Position position = new Position(board);

	public static boolean analysis = true;

	public static boolean computerAsWhite = false, computerAsBlack = true;
	public static boolean showTerritoryMap = false;
	public static boolean gameFinished = false;

	public static float maxSearchTime = 5;
	public static int achievedDepth = 0;

	public static List<Position> previousPositions = new ArrayList<Position>();

	public GameState(Game game) {
		super(game);
	}

	@Override
	public void initialize() {
		settingsButton = new SettingsButton(ICON_X, SETTINGS_ICON_Y, new BufferedImage[] { Game.settingsIcon });
		analysisButton = new AnalysisButton(ICON_X, SETTINGS_ICON_Y + ICON_DIAMETER * 13 / 10,
				new BufferedImage[] { Game.analysisIcon });
		buttonHandler = new ButtonHandler(settingsButton, analysisButton);
		setNumbers();
	}

	@Override
	public void tick(Graphics2D g) {
		float e = Evaluation.evalNeutral(position, 0);
		Engine.nodes--;
		if (e == 100000f) {
			Engine.currentEval = 100000f;
		} else if (e == -100000f) {
			Engine.currentEval = -100000f;
		}
		if (((position.getTurn() && computerAsWhite) || (!position.getTurn() && computerAsBlack))
				&& Launcher.game.frameCount > 4 && Launcher.game.frameCount - Launcher.inputData.lastInput > 3) {
			if (!gameFinished && Math.abs(e) < 100000) {
				if (MoveGenerator.generateLegalMoves(position).isEmpty()) {
					gameFinished = true;
				}
//				System.out.println(Engine.predictedContinuation(position, 35).size());
				Move move = Engine.analyseMoves(position);
				if (move != null) {
					move.playMove(position);
					Launcher.inputData.lastInput = Launcher.game.frameCount;
				}
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		buttonHandler.render(g);
		drawBoard(g, Start.perspective);
		if (analysis) {
			renderAnalysis(g);
		}
	}

	// GameState Methods

	public static void drawBoard(Graphics2D g, boolean perspective) {
		drawSquares(g);
		renderPieces(g, perspective);
		writeCoordinates(g, perspective);
		if (Launcher.inputData.isSelecting()) {
			int file = Launcher.inputData.getSelectedSquare().x;
			int rank = Launcher.inputData.getSelectedSquare().y;
			if (position.board[rank][file] != 12) {
				boolean color = Pieces.nonEmptyColor(position.board[rank][file]);
				boolean friendlyPiece = (color && !computerAsWhite) || (!color && !computerAsBlack);
				if (friendlyPiece) {
					drawLegalMoves(g, position);
				}
			}
		}
	}

	public static void drawSquares(Graphics2D g) {
		InputData inputData = Launcher.inputData;
		boolean selected = inputData.isSelecting();
		Point selectedSquare = inputData.getSelectedSquare();
		Move runningMove = inputData.getRunningMove();
		boolean c = false;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				c = false;
				if (selected && selectedSquare.x == i && selectedSquare.y == j) {
					g.setColor(new Color(176, 103, 91));
					c = true;
				} else if (runningMove.getSquares().contains(new Point(i, j))) {
					g.setColor(new Color(200, 120, 100));
					c = true;
				}

				// Light Square
				if ((i + j) % 2 == 1) {
					if (!c) {
						// Not Selected
						g.setColor(new Color(238, 239, 211));
					}
				}

				// Dark Square
				else {
					if (!c) {
						// Not Selected
						g.setColor(new Color(119, 150, 87));
					}
				}

				g.fillRect(BOARD_X + Mouse.rotatedFile(i) * SQUARE_SIZE, BOARD_Y + Mouse.rotatedRank(j) * SQUARE_SIZE,
						SQUARE_SIZE, SQUARE_SIZE);

			}
		}

		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(3));
		for (Point p : position.previousMove.getSquares()) {
			g.drawRect(BOARD_X + Mouse.rotatedFile(p.x) * SQUARE_SIZE, BOARD_Y + Mouse.rotatedRank(p.y) * SQUARE_SIZE,
					SQUARE_SIZE, SQUARE_SIZE);
		}
		g.setStroke(new BasicStroke(1));

	}

	public static void renderPieces(Graphics2D g, boolean perspective) {
		// Loop through the board and drawing the pieces
		for (int file = 0; file < 8; file++) {
			for (int rank = 0; rank < 8; rank++) {
				BufferedImage piece = Pieces.pieces[position.board[7 - rank][file]];
				renderPiece(g, piece, file * SQUARE_SIZE, rank * SQUARE_SIZE, perspective);
			}
		}
	}

	public static void renderPiece(Graphics2D g, BufferedImage piece, int x, int y, boolean perspective) {
		// Draw piece
		if (perspective) {
			// White's perspective
			g.drawImage(piece, BOARD_X + x, BOARD_Y + y, SQUARE_SIZE, SQUARE_SIZE, null);
		} else {
			// Black's perspective
			g.drawImage(piece, BOARD_X + BOARD_SIZE - SQUARE_SIZE - x, BOARD_Y + BOARD_SIZE - SQUARE_SIZE - y,
					SQUARE_SIZE, SQUARE_SIZE, null);
		}
	}

	public static void writeCoordinates(Graphics2D g, boolean perspective) {
		int fontSize = SQUARE_SIZE / 4;
		g.setColor(Color.WHITE);
		g.setFont(new Font("serif", Font.BOLD, fontSize));
		for (int i = 0; i < 8; i++) {
			String f;
			String r;
			if (perspective) {
				f = FILES[i];
				r = "" + (i + 1);
			} else {
				f = FILES[7 - i];
				r = "" + (8 - i);
			}
			Toolbox.setAlign(Toolbox.ALIGN_CENTER, Toolbox.ALIGN_TOP);
			Toolbox.drawText(g, f, BOARD_X + (int) ((i + 0.5f) * SQUARE_SIZE),
					BOARD_Y + BOARD_SIZE + (int) (0.1f * SQUARE_SIZE));
			Toolbox.setAlign(Toolbox.ALIGN_RIGHT, Toolbox.ALIGN_CENTER);
			Toolbox.drawText(g, r, BOARD_X - SQUARE_SIZE / 8, BOARD_Y + BOARD_SIZE - SQUARE_SIZE / 2 - i * SQUARE_SIZE);
		}
	}

	public static void replacePiece(int x, int y, int newPiece) {
		board[y][x] = newPiece;
	}

	public static void drawLegalMoves(Graphics2D g, Position position) {
		Point baseSquare = Launcher.inputData.getBaseSquare();
		List<Move> legalMoves = MoveGenerator.generateLegalMoves(position);
		for (int i = 0; i < legalMoves.size(); i++) {
			Move move = legalMoves.get(i);
			if (move.getSquares().get(0).x == baseSquare.x && move.getSquares().get(0).y == baseSquare.y) {
				for (int j = 1; j < move.getSquares().size(); j++) {
					int pX = Mouse.fileToPixels(move.getSquares().get(j - 1).x);
					int pY = Mouse.rankToPixels(move.getSquares().get(j - 1).y);
					int x = Mouse.fileToPixels(move.getSquares().get(j).x);
					int y = Mouse.rankToPixels(move.getSquares().get(j).y);
					g.setColor(Color.BLACK);
					g.drawLine(pX, pY, x, y);
					g.setColor(new Color(95, 154, 232));
					g.fillOval(x - GameState.SQUARE_SIZE / 10, y - GameState.SQUARE_SIZE / 10,
							GameState.SQUARE_SIZE / 5, GameState.SQUARE_SIZE / 5);
				}
			}
		}
	}

	public static void setNumbers() {
		if (!analysis) {
			BOARD_SIZE = Launcher.WIDTH - MARGIN * 2;
			BOARD_X = (Launcher.WIDTH - BOARD_SIZE) / 2;
		} else {
			BOARD_SIZE = (int) (Launcher.WIDTH * 0.7f);
			BOARD_X = (Launcher.WIDTH - BOARD_SIZE) / 2;
		}
		SQUARE_SIZE = BOARD_SIZE / 8;
	}

	public static void renderAnalysis(Graphics2D g) {
		g.setColor(Color.WHITE);
		if (Engine.move != null) {
			Toolbox.setAlign(Toolbox.ALIGN_LEFT, Toolbox.ALIGN_TOP);
			String evalInterpretation;
			if (Engine.currentEval == 100000) {
				evalInterpretation = "White wins";
			} else if (Engine.currentEval == -100000) {
				evalInterpretation = "Black wins";
			} else if (Engine.currentEval > 90000) {
				evalInterpretation = "White wins in " + (int) (100000 - Engine.currentEval) + " moves";
			} else if (Engine.currentEval < -90000) {
				evalInterpretation = "Black wins in " + (int) (100000 + Engine.currentEval) + " moves";
			} else if (Engine.currentEval > 0.5) {
				evalInterpretation = DataHandler.roundFloat(Engine.currentEval, 2) + "";
			} else if (Engine.currentEval < -0.5) {
				evalInterpretation = DataHandler.roundFloat(Engine.currentEval, 2) + "";
			} else {
				evalInterpretation = DataHandler.roundFloat(Engine.currentEval, 2) + "";
			}
			g.setFont(new Font("serif", Font.PLAIN, 16));
			Toolbox.drawText(g, "Move Number " + position.moveNumber, BOARD_X, BOARD_Y + BOARD_SIZE + 30);
			Toolbox.drawText(g, "Evaluation of position: " + evalInterpretation, BOARD_X, BOARD_Y + BOARD_SIZE + 53);
			Toolbox.drawText(g, "Best move: " + Engine.move, BOARD_X, BOARD_Y + BOARD_SIZE + 76);
			Toolbox.drawText(g, "Computation time: " + DataHandler.roundFloat(Engine.evalTime, 3) + "s", BOARD_X,
					BOARD_Y + BOARD_SIZE + 99);
			Toolbox.drawText(g, "Consequences evaluated: " + DataHandler.formatInt(Engine.nodes), BOARD_X,
					BOARD_Y + BOARD_SIZE + 122);
			Toolbox.drawText(g, (position.getTurn()) ? "White's turn to move" : "Black's turn to move", BOARD_X,
					BOARD_Y + BOARD_SIZE + 145);
		}
		g.setFont(new Font("serif", Font.PLAIN, 18));
		if (achievedDepth != 0) {
			Toolbox.setAlign(Toolbox.ALIGN_LEFT, Toolbox.ALIGN_TOP);
			Toolbox.drawText(g,
					"Search Depth: " + achievedDepth + " move" + ((achievedDepth == 1) ? "" : "s") + " into the future",
					10, 10);
		}
		if (Start.perspective) {
			float fraction = Evaluation.evalToFraction(Engine.currentEval);
			g.setColor(new Color(255, 229, 153));
			g.fillRect(30, BOARD_Y, 40, BOARD_SIZE);
			g.setColor(new Color(126, 97, 0));
			g.fillRect(30, BOARD_Y, 40, (int) (BOARD_SIZE * fraction));
			Toolbox.setAlign(Toolbox.ALIGN_CENTER, Toolbox.ALIGN_BOTTOM);
			g.setColor(Color.BLACK);
			g.setFont(new Font("serif", Font.PLAIN, 15));
			Toolbox.drawText(g, "" + Evaluation.evalToString(DataHandler.roundFloat(Engine.currentEval, 2)), 50,
					BOARD_Y + BOARD_SIZE - 5);
		} else {
			float fraction = Evaluation.evalToFraction(-Engine.currentEval);
			g.setColor(new Color(126, 97, 0));
			g.fillRect(30, BOARD_Y, 40, BOARD_SIZE);
			g.setColor(new Color(255, 229, 153));
			g.fillRect(30, BOARD_Y, 40, (int) (BOARD_SIZE * fraction));
			Toolbox.setAlign(Toolbox.ALIGN_CENTER, Toolbox.ALIGN_BOTTOM);
			g.setColor(Color.BLACK);
			g.setFont(new Font("serif", Font.PLAIN, 15));
			Toolbox.drawText(g, "" + Evaluation.evalToString(DataHandler.roundFloat(Engine.currentEval, 2)), 50,
					BOARD_Y + BOARD_SIZE - 5);
		}
	}

	public String toString() {
		return "Game State";
	}

}