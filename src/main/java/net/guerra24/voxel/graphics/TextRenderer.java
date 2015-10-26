package net.guerra24.voxel.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.util.ResourceLoader;

import net.guerra24.voxel.graphics.opengl.Display;

public class TextRenderer {
	private TrueTypeFont font;

	public TextRenderer(String font) {
		init(font);
	}

	private void init(String fontPath) {
		InputStream inputStream = ResourceLoader.getResourceAsStream("assets/fonts/" + fontPath + ".ttf");

		Font awtFont2;
		try {
			awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont2 = awtFont2.deriveFont(20f);
			font = new TrueTypeFont(awtFont2, false);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

	}

	public void begin() {
		glDisable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		glMatrixMode(GL_MODELVIEW);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
	}

	public void end() {
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
	}

	public void renderString(String string, int x, int y) {
		font.drawString(x, y, string);
	}

	public void renderString(String string, int x, int y, Color color) {
		font.drawString(x, y, string, color);
	}
}
