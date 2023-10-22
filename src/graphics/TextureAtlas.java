package graphics;

import java.awt.image.BufferedImage;

public class TextureAtlas {
	
	private BufferedImage sheet;
	
	public TextureAtlas(BufferedImage sheet) {
		this.sheet = sheet;
	}
	
	public BufferedImage crop(int x, int y, int width, int height) {
		return sheet.getSubimage(x, y, width, height);
	}
	
}
