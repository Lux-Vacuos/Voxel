package voxel.client.engine.render.gui;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import voxel.client.engine.resources.Loader;
import voxel.client.engine.resources.models.RawModel;
import voxel.client.engine.util.Maths;

public class GuiRenderer {

	private final RawModel quad;
	private GuiShader shader;

	public GuiRenderer(Loader loader) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}

	public void render(List<GuiTexture> guis) {
		shader.start();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		for (GuiTexture gui : guis) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(
					gui.getPosition(), gui.getScale());
			shader.loadTransformation(matrix);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}
