package gui;

import java.awt.image.BufferedImage;

import main.Launcher;
import states.GameState;
import states.State;

public class SettingsButton extends Button {

	public SettingsButton(int x, int y, BufferedImage[] textures) {
		super(x, y, GameState.ICON_DIAMETER, GameState.ICON_DIAMETER, textures);
	}

	@Override
	public void click() {
		if (State.getState() == Launcher.gameState) {
			State.setState(Launcher.settingState);
		} else {
			State.setState(Launcher.gameState);
		}
	}

	@Override
	public boolean pixelWithinButton(int x, int y) {
		if ((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) <= width * width) {
			return true;
		}
		return false;
	}

}
