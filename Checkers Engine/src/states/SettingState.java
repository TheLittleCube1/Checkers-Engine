package states;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import data.Start;
import data.Toolbox;
import gui.ButtonHandler;
import gui.FlipPerspectiveButton;
import gui.SettingsButton;
import main.Game;
import main.Launcher;

public class SettingState extends State {
	
	public static final int MARGIN = 40;
	public static final int BOARD_SIZE = Launcher.WIDTH - MARGIN - MARGIN;
	public static final int SQUARE_SIZE = BOARD_SIZE / 8;
	public static final int BOARD_X = MARGIN, BOARD_Y = MARGIN;
	public static final int SETTINGS_ICON_DIAMETER = 25;
	public static final int SWITCH_PERSPECTIVE_ICON_DIAMETER = 30;
	
	public static final int SETTINGS_ICON_X = Launcher.WIDTH - MARGIN / 2 - SETTINGS_ICON_DIAMETER / 2;
	public static final int SETTINGS_ICON_Y = MARGIN / 2 - SETTINGS_ICON_DIAMETER / 2;
	public static final int STATE_ICON_Y = MARGIN / 2 - SETTINGS_ICON_DIAMETER / 2;

	public SettingsButton settingsButton;
	public FlipPerspectiveButton flipPerspectiveButton;
	public ButtonHandler buttonHandler;
	
	public SettingState(Game game) {
		super(game);
	}

	@Override
	public void initialize() {
		settingsButton = new SettingsButton(SETTINGS_ICON_X, SETTINGS_ICON_Y, new BufferedImage[] {Game.settingsIcon});
		flipPerspectiveButton = new FlipPerspectiveButton(50, 70, new BufferedImage[] {Game.whitePerspectiveIcon, Game.blackPerspectiveIcon});
		buttonHandler = new ButtonHandler(settingsButton, flipPerspectiveButton);
	}

	@Override
	public void tick(Graphics2D g) {
		
	}

	@Override
	public void render(Graphics2D g) {
		g.setFont(new Font("serif", Font.PLAIN, 20));
		g.setColor(Color.WHITE);
		g.drawString("Settings", 50, 50);
		
		Toolbox.setAlign(Toolbox.ALIGN_LEFT, Toolbox.ALIGN_CENTER);
		
		if (Start.perspective) {
			Toolbox.drawText(g, "White's perspective", 50 + (int) (SWITCH_PERSPECTIVE_ICON_DIAMETER * 1.5), 70 + SWITCH_PERSPECTIVE_ICON_DIAMETER / 2);
		} else {
			Toolbox.drawText(g, "Black's perspective", 50 + (int) (SWITCH_PERSPECTIVE_ICON_DIAMETER * 1.5), 70 + SWITCH_PERSPECTIVE_ICON_DIAMETER / 2);
		}
		
		buttonHandler.render(g);
	}
	
	public String toString() {
		return "Settings State";
	}

}
