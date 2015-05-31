package io.github.guerra24.voxel.client.kernel.render.types;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.render.shaders.types.WaterShader;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.WaterFrameBuffers;
import io.github.guerra24.voxel.client.resources.Loader;
import io.github.guerra24.voxel.client.resources.models.RawModel;
import io.github.guerra24.voxel.client.resources.models.WaterTile;
import io.github.guerra24.voxel.client.world.Water;
import io.github.guerra24.voxel.client.world.entities.types.Camera;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class WaterRenderer {

	private RawModel quad;
	private WaterShader shader;
	private WaterFrameBuffers fbos;
	private WaterFrameBuffers fbos2;

	public WaterRenderer(Loader loader, WaterShader shader,
			Matrix4f projectionMatrix, WaterFrameBuffers fbos,
			WaterFrameBuffers fbos2) {
		this.shader = shader;
		this.fbos = fbos;
		this.fbos2 = fbos2;
		shader.start();
		shader.connectTextureUnits();
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
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, fbos.getReflectionTexture());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, fbos2.getRefractionTexture());
	}

	private void unbind() {
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	public void setReflection() {
		Kernel.gameResources.fbos.bindReflectionFrameBuffer();
		WaterReflection.reflectionCam();
		Kernel.gameResources.renderer.renderScene(
				Kernel.gameResources.allEntities, Kernel.gameResources.lights,
				Kernel.gameResources.camera,
				new Vector4f(0, 1, 0, -Water.water.getHeight()));
		Kernel.gameResources.renderer.renderSceneNoPrepare(
				Kernel.gameResources.allObjects, Kernel.gameResources.lights,
				Kernel.gameResources.camera,
				new Vector4f(0, 1, 0, -Water.water.getHeight()));
		WaterReflection.restoreCam();
		Kernel.gameResources.fbos.unbindCurrentFrameBuffer();
		Kernel.gameResources.fbos2.bindRefractionFrameBuffer();
		Kernel.gameResources.renderer.renderScene(
				Kernel.gameResources.allEntities, Kernel.gameResources.lights,
				Kernel.gameResources.camera,
				new Vector4f(0, -1, 0, Water.water.getHeight()));
		Kernel.gameResources.renderer.renderSceneNoPrepare(
				Kernel.gameResources.allObjects, Kernel.gameResources.lights,
				Kernel.gameResources.camera,
				new Vector4f(0, -1, 0, Water.water.getHeight()));
		Kernel.gameResources.fbos2.unbindCurrentFrameBuffer();
	}

	private void setUpVAO(Loader loader) {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}
