/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Guerra24
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.guerra24.voxel.client.kernel.graphics;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.DisplayManager;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.VoxelGL33;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.WaterShader;
import io.github.guerra24.voxel.client.kernel.resources.Loader;
import io.github.guerra24.voxel.client.kernel.resources.models.RawModel;
import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;

import java.util.List;
import java.util.Queue;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class WaterRenderer {

	private static final String DUDV_MAP = "dudvMap";
	private static final String NORMAL_MAP = "normalMap";

	private RawModel quad;
	private WaterShader shader;

	private float moveFactor = 0;

	private int dudvTexture;
	private int normalTexture;

	public WaterRenderer(Loader loader, WaterShader shader,
			Matrix4f projectionMatrix) {
		this.shader = shader;
		dudvTexture = loader.loadTextureBlocks(DUDV_MAP);
		normalTexture = loader.loadTextureBlocks(NORMAL_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(Queue<WaterTile> waters, Camera camera) {
		prepareRender(camera);
		for (WaterTile tile : waters) {
			if (Frustum.getFrustum().pointInFrustum(tile.getX(),
					tile.getHeight(), tile.getZ())) {
				Matrix4f modelMatrix = Maths
						.createTransformationMatrix(new Vector3f(tile.getX(),
								tile.getHeight(), tile.getZ()), 0, 0, 0,
								WaterTile.TILE_SIZE);
				shader.loadModelMatrix(modelMatrix);
				VoxelGL33.glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
			}
		}
		unbind();
	}

	public void render(List<WaterTile> water, Camera camera, Light light) {
		prepareRender(camera, light);
		for (WaterTile tile : water) {
			if (Frustum.getFrustum().pointInFrustum(tile.getX(),
					tile.getHeight(), tile.getZ())) {
				Matrix4f modelMatrix = Maths
						.createTransformationMatrix(new Vector3f(tile.getX(),
								tile.getHeight(), tile.getZ()), 0, 0, 0,
								WaterTile.TILE_SIZE);
				shader.loadModelMatrix(modelMatrix);
				VoxelGL33.glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
			}
		}
		unbind();
	}

	private void prepareRender(Camera camera, Light light) {
		shader.start();
		shader.loadSkyColour(KernelConstants.RED, KernelConstants.GREEN,
				KernelConstants.BLUE);
		shader.loadViewMatrix(camera);
		moveFactor += KernelConstants.WAVE_SPEED
				* DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		if (Display.wasResized())
			shader.loadProjectionMatrix(Kernel.gameResources.renderer
					.getProjectionMatrix());
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(light);
		VoxelGL33.glDisable(GL_CULL_FACE);
		VoxelGL33.glEnable(GL_BLEND);
		VoxelGL33.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, dudvTexture);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, normalTexture);
	}

	private void prepareRender(Camera camera) {
		shader.start();
		shader.loadSkyColour(KernelConstants.RED, KernelConstants.GREEN,
				KernelConstants.BLUE);
		shader.loadViewMatrix(camera);
		moveFactor += KernelConstants.WAVE_SPEED
				* DisplayManager.getFrameTimeSeconds();
		moveFactor %= 1;
		if (Display.wasResized())
			shader.loadProjectionMatrix(Kernel.gameResources.renderer
					.getProjectionMatrix());
		shader.loadMoveFactor(moveFactor);
		shader.loadDirectLightDirection(new Vector3f(-80, -100, -40));
		VoxelGL33.glDisable(GL_CULL_FACE);
		VoxelGL33.glEnable(GL_BLEND);
		VoxelGL33.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, dudvTexture);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, normalTexture);
	}

	private void unbind() {
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		VoxelGL33.glDisable(GL_BLEND);
		VoxelGL33.glEnable(GL_CULL_FACE);
		shader.stop();
	}

	private void setUpVAO(Loader loader) {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}