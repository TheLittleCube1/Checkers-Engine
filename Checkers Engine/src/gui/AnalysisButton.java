package gui;

import java.awt.image.BufferedImage;

import states.GameState;

public class AnalysisButton extends Button {

	public AnalysisButton(int x, int y, BufferedImage[] textures) {
		super(x, y, GameState.ICON_DIAMETER, GameState.ICON_DIAMETER, textures);
	}

	@Override
	public void click() {
		if (GameState.analysis) {
			GameState.analysis = false;
		} else {
			GameState.analysis = true;
		}
		GameState.setNumbers();
	}

	@Override
	public boolean pixelWithinButton(int x, int y) {
		if ((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) <= width * width) {
			return true;
		}
		return false;
	}
	
}
