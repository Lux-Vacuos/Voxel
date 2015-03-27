package voxel.client.core.engine.resources;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_ENABLE_BIT;
import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_BIT;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import voxel.client.core.engine.color.Color4f;

public class Text {

	public static void renderString(Font font, String string, float x, float y,
			float scale, Color4f color) {
		renderString(font, string, 16, x, y, 0.1f, 0.1f, scale, color);
	}

	public static void renderString(Font font, String string, int gridSize,
			float x, float y, float charWidth, float charHeight, float scale,
			Color4f color) {
		glPushAttrib(GL_TEXTURE_BIT | GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT);
		glEnable(GL_CULL_FACE);
		glEnable(GL_TEXTURE_2D);
		font.bind();

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE);

		glColor4f(color.r, color.g, color.b, color.a);

		glPushMatrix();
		glScalef(scale, scale, 0f);
		glTranslatef(x, y, 0);
		glBegin(GL_QUADS);

		for (int i = 0; i < string.length(); i++) {
			int code = (int) string.charAt(i);
			float cellSize = 1.0f / gridSize;
			float cellX = ((int) code % gridSize) * cellSize;
			float cellY = ((int) code / gridSize) * cellSize;
			glTexCoord2f(cellX, cellY + cellSize);
			glVertex2f(i * charWidth / 3, y);

			glTexCoord2f(cellX + cellSize, cellY + cellSize);
			glVertex2f(i * charWidth / 3 + charWidth / 2, y);

			glTexCoord2f(cellX + cellSize, cellY);
			glVertex2f(i * charWidth / 3 + charWidth / 2, y + charHeight);

			glTexCoord2f(cellX, cellY);
			glVertex2f(i * charWidth / 3, y + charHeight);
		}
		glEnd();
		glPopMatrix();
		glPopAttrib();
	}

}
