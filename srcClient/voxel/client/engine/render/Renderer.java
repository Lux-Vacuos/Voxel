package voxel.client.engine.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

import voxel.client.engine.entities.Entity;
import voxel.client.engine.render.shaders.StaticShader;
import voxel.client.engine.resources.models.RawModel;
import voxel.client.engine.resources.models.TexturedModel;
import voxel.client.engine.vectors.Maths;

public class Renderer {

	private static final float FOV = 90f;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 10f;

	private Matrix4f projectionMatrix;

	public Renderer(StaticShader shader) {
		createProjectionMatrix();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void prepare() {
		glClear(GL_COLOR_BUFFER_BIT);
		glClearColor(0.381f, 0.555f, 0.612f, 1);
	}

	public void render(Entity entity, StaticShader shader) {
		TexturedModel model = entity.getModel();
		RawModel rawmodel = model.getRawModel();
		glBindVertexArray(rawmodel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				entity.getPosition(), entity.getRotX(), entity.getRotY(),
				entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());
		glDrawElements(GL_TRIANGLES, rawmodel.getVertexCount(),
				GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth()
				/ (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = FAR_PLANE - NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustrum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustrum_length);
		projectionMatrix.m33 = 0;
	}
}
