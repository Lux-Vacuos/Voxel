package net.guerra24.voxel.menu.button;

import org.newdawn.slick.Color;

import net.guerra24.voxel.graphics.TextRenderer;

public class Text {

	private int x, y;
	private String texts;
	private Color color;

	public Text(int x, int y, String text) {
		this.x = x;
		this.y = y;
		this.texts = text;
		this.color = Color.white;
	}

	public Text(int x, int y, int width, int height, String text, Color color) {
		this.x = x;
		this.y = y;
		this.texts = text;
		this.color = color;
	}

	public void render(TextRenderer textRenderer) {
		textRenderer.renderString(texts, x, y, color);
	}

	public void render(TextRenderer textRenderer, String texts) {
		textRenderer.renderString(texts, x, y, color);
	}

}
