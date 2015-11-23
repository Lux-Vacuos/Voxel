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

package net.guerra24.voxel.client.graphics;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.shaders.SkyboxShader;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.Loader;
import net.guerra24.voxel.client.resources.models.RawModel;
import net.guerra24.voxel.universal.util.vector.Matrix4f;

/**
 * Skybox Rendering
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class SkyboxRenderer {
	/**
	 * Skybox Data
	 */
	private final float[] VERTICES = { -VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE,
			-VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE,
			-VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE,
			VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE,

			-VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE,
			-VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE,
			VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE,
			-VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE,

			VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE,
			VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE,
			VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE,
			VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE,

			-VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE,
			VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE,
			VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE,
			-VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE,

			-VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE,
			-VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE,
			VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE,
			-VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE,

			-VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE,
			-VoxelVariables.SIZE, VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE,
			VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE, -VoxelVariables.SIZE,
			VoxelVariables.SIZE, VoxelVariables.SIZE, -VoxelVariables.SIZE, VoxelVariables.SIZE };

	private String[] TEXTURE_FILES = { "day/right", "day/left", "day/top", "day/bottom", "day/front", "day/back" };
	private String[] NIGHT_TEXTURE_FILES = { "night/right", "night/left", "night/top", "night/bottom", "night/front",
			"night/back" };
	private RawModel cube;
	private int texture;
	private int nightTexture;
	private SkyboxShader shader;
	private float time = 0;
	private float blendFactor = 0;

	/**
	 * Constructor, Initializes the Skybox model, Textures and Shader
	 * 
	 * @param loader
	 *            Game Loader
	 * @param projectionMatrix
	 *            Matrix4f, Projection Matrix
	 */
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		cube = loader.loadToVAO(VERTICES, 3);
		texture = loader.loadCubeMap(TEXTURE_FILES);
		nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
		shader = new SkyboxShader();
		shader.start();
		shader.connectTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		time = 6100;
	}

	/**
	 * Render the Skybox
	 * 
	 * @param camera
	 *            A Camera
	 * @param r
	 *            Fog Red Color
	 * @param g
	 *            Fog Green Color
	 * @param b
	 *            Fog Blue Color
	 * @param delta
	 *            Delta
	 */
	public void render(float r, float g, float b, float delta, GameResources gm) {
		shader.start();
		shader.loadViewMatrix(gm.getCamera(), delta);
		shader.loadFog(r, g, b);
		glBindVertexArray(cube.getVaoID());
		glEnableVertexAttribArray(0);
		bindTextures();
		glDrawArrays(GL_TRIANGLES, 0, cube.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	/**
	 * Update world time
	 * 
	 * @param delta
	 *            Delta
	 */
	public float update(float delta) {
		time += delta * 10;
		time %= 24000;
		float res = time * 0.015f;
		return res - 90;
	}

	/**
	 * Updates the Skybox Textures
	 * 
	 */
	private void bindTextures() {
		int texture1;
		int texture2;
		if (time >= 0 && time < 5000) {
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = (time - 0) / (5000 - 0);
		} else if (time >= 5000 && time < 8000) {
			texture1 = nightTexture;
			texture2 = texture;
			blendFactor = (time - 5000) / (8000 - 5000);
		} else if (time >= 8000 && time < 15500) {
			texture1 = texture;
			texture2 = texture;
			blendFactor = 1f;
		} else if (time >= 15500 && time < 18000) {
			texture1 = texture;
			texture2 = nightTexture;
			blendFactor = (time - 15500) / (18000 - 15500);
		} else {
			texture1 = nightTexture;
			texture2 = nightTexture;
			blendFactor = 1f;
		}

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texture1);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_CUBE_MAP, texture2);
		shader.loadBlendFactor(blendFactor);
	}

	/**
	 * Get the blend factor
	 * 
	 * @return time
	 */
	public float getBlendFactor() {
		return blendFactor;
	}

	/**
	 * Get the world time
	 * 
	 * @return time
	 */
	public float getTime() {
		return time;
	}
}
