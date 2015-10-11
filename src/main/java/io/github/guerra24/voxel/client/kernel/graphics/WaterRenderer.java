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
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.Queue;

import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.VoxelGL33;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.WaterShader;
import io.github.guerra24.voxel.client.kernel.resources.GameResources;
import io.github.guerra24.voxel.client.kernel.resources.Loader;
import io.github.guerra24.voxel.client.kernel.resources.models.RawModel;
import io.github.guerra24.voxel.client.kernel.resources.models.WaterTile;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;

/**
 * Water Renderer
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class WaterRenderer {
	/**
	 * Water Data
	 */
	private final String DUDV_MAP = "dudvMap";
	private final String NORMAL_MAP = "normalMap";
	private RawModel quad;
	private WaterShader shader;
	private float moveFactor = 0;
	private int dudvTexture;
	private int normalTexture;

	/**
	 * Constructor, Initializes the Water Shaders, Textures and VAOs
	 * 
	 * @param loader
	 *            Game Loader
	 * @param shader
	 *            Water Shader
	 * @param projectionMatrix
	 *            A Matrix4f Projection
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public WaterRenderer(Loader loader, WaterShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		dudvTexture = loader.loadTextureBlocks(DUDV_MAP);
		normalTexture = loader.loadTextureBlocks(NORMAL_MAP);
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	/**
	 * Renders the Water Tiles in the List
	 * 
	 * @param waters
	 *            A list of Water Tiles
	 * @param camera
	 *            A Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void render(Queue<WaterTile> waters, GameResources gm) {
		prepareRender(gm);
		for (WaterTile tile : waters) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, WaterTile.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			VoxelGL33.glDrawArrays(GL_TRIANGLES, 0, quad.getVertexCount());
		}
		unbind();
	}

	/**
	 * Water Tile Prepare PipeLine
	 * 
	 * @param camera
	 *            A Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void prepareRender(GameResources gm) {
		shader.start();
		shader.loadSkyColour(KernelConstants.RED, KernelConstants.GREEN, KernelConstants.BLUE);
		shader.loadViewMatrix(gm.getCamera());
		shader.loadProjectionMatrix(gm.getRenderer().getProjectionMatrix());
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
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, gm.getWaterFBO().getTexture());
	}

	/**
	 * Updates the water
	 * 
	 * @param delta
	 *            Delta
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void update(float delta) {
		moveFactor += KernelConstants.WAVE_SPEED * delta;
		moveFactor %= 1;
	}

	/**
	 * Unbinds the VAOs
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void unbind() {
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		VoxelGL33.glDisable(GL_BLEND);
		VoxelGL33.glEnable(GL_CULL_FACE);
		shader.stop();
	}

	/**
	 * Creates the VAOs
	 * 
	 * @param loader
	 *            Game Loader
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void setUpVAO(Loader loader) {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

}