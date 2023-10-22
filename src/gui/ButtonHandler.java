package gui;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class ButtonHandler {
	
	public List<Button> buttons = new ArrayList<Button>();
	
	public ButtonHandler(Button...buttons) {
		for (Button button : buttons) {
			this.buttons.add(button);
		}
	}
	
	public void render(Graphics2D g) {
		for (Button button : buttons) {
			button.render(g);
		}
	}
	
	public void click(int x, int y) {
		for (Button button : buttons) {
			if (button.pixelWithinButton(x, y)) {
				button.click();
				return;
			}
		}
	}
	
	public String toString() {
		return "Button handler [" + buttons.size() + " button" + ((buttons.size() == 1) ? "" : "s") + "]";
	}
	
}
