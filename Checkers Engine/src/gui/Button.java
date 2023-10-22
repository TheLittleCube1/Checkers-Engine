package gui;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public abstract class Button {
	
	public int x, y, width, height;
	public BufferedImage[] textures;
	public int textureIndex;
	
	public abstract void click();

	public void render(Graphics2D g) {
		g.drawImage(textures[textureIndex], x, y, width, height, null);
	}
	
	public Button(int x, int y, int width, int height, BufferedImage[] textures) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.textures = textures;
		this.textureIndex = 0;
	}
	
	public abstract boolean pixelWithinButton(int x, int y);

	public BufferedImage getTexture() {
		return textures[textureIndex];
	}
	
	public void setTextureIndex(int index) {
		this.textureIndex = index;
	}
	
}
