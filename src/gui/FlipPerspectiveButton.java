package gui;

import java.awt.image.BufferedImage;

import data.Start;
import states.SettingState;

public class FlipPerspectiveButton extends Button {

	public FlipPerspectiveButton(int x, int y, BufferedImage[] textures) {
		super(x, y, SettingState.SWITCH_PERSPECTIVE_ICON_DIAMETER, SettingState.SWITCH_PERSPECTIVE_ICON_DIAMETER, textures);
		if (!Start.perspective) {
			textureIndex = 1;
		}
	}

	@Override
	public void click() {
		Start.perspective = !Start.perspective;
		textureIndex = (textureIndex == 0) ? 1 : 0;
	}

	@Override
	public boolean pixelWithinButton(int x, int y) {
		if ((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) <= width * width) {
			return true;
		}
		return false;
	}
	
}
