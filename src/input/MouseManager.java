package input;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;

import main.Launcher;
import states.GameState;
import states.State;

public class MouseManager extends JFrame implements MouseListener {
	
	private static final long serialVersionUID = -8994576182964289862L;
	
	private InputData inputData = new InputData();

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getPoint().x;
		int y = e.getPoint().y;
		if (State.getState() == Launcher.gameState) {
			Launcher.gameState.buttonHandler.click(x, y);
			int file = Mouse.pixelToFile(x - GameState.BOARD_X);
			int rank = Mouse.pixelToRank(y - GameState.BOARD_Y);
			Launcher.inputData.clickSquare(new Point(file, rank));
		} else {
			Launcher.settingState.buttonHandler.click(x, y);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	public InputData getInputData() {
		return inputData;
	}
	
}
