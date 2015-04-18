package net.guerra24.voxel.client.engine.render.water;

import java.util.List;

import net.guerra24.voxel.client.engine.entities.types.Camera;
import net.guerra24.voxel.client.engine.render.shaders.types.WaterShader;
import net.guerra24.voxel.client.engine.resources.Loader;
import net.guerra24.voxel.client.engine.resources.models.RawModel;
import net.guerra24.voxel.client.engine.util.Maths;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class WaterRenderer {

	private RawModel quad;
	private WaterShader shader;

	public WaterRenderer(Loader loader, WaterShader shader,
			Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<WaterTile> water, Camera camera) {
		prepareRender(camera);
		for (WaterTile tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()),
					0, 0, 0, WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}

	private void prepareRender(Camera camera) {
		shader.start();
		shader.loadViewMatrix(camera);
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
	}

	private void unbind() {
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
