/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

package net.guerra24.voxel.client.rendering.api.opengl;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.rendering.api.opengl.shaders.SkyboxShader;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.Loader;
import net.guerra24.voxel.client.resources.models.RawModel;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

/**
 * Skybox Rendering
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class SkyboxRenderer {

	private RawModel dome;
	private SkyboxShader shader;
	private float time = 0;
	private float globalTime = 0;

	private static final float TIME_MULTIPLIER = 10;

	/**
	 * Constructor, Initializes the Skybox model, Textures and Shader
	 * 
	 * @param loader
	 *            Game Loader
	 * @param projectionMatrix
	 *            Matrix4f, Projection Matrix
	 */
	public SkyboxRenderer(Loader loader, Matrix4f projectionMatrix) {
		dome = loader.getObjLoader().loadObjModel("SkyDome");
		shader = new SkyboxShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadTransformationMatrix(Maths.createTransformationMatrix(new Vector3f(), 0, 0, 0, 360));
		shader.stop();
		time = 11000;
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
		glDepthMask(false);
		shader.start();
		shader.loadProjectionMatrix(gm.getRenderer().getProjectionMatrix());
		shader.loadTransformationMatrix(Maths.createTransformationMatrix(gm.getCamera().getPosition(), 0, 0, 0, 370));
		shader.loadViewMatrix(gm.getCamera());
		shader.loadFog(r, g, b);
		shader.loadTime(globalTime);
		shader.loadLightPosition(gm.getLightPos());
		glBindVertexArray(dome.getVaoID());
		glEnableVertexAttribArray(0);
		glDrawElements(GL_TRIANGLES, dome.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
		glDepthMask(true);
	}

	/**
	 * Update world time
	 * 
	 * @param delta
	 *            Delta
	 */
	public float update(float delta) {
		if (!VoxelVariables.onServer) {
			time += delta * TIME_MULTIPLIER;
			time %= 24000;
		}
		globalTime += delta * TIME_MULTIPLIER;
		float res = time * 0.015f;
		return res - 90;
	}

	/**
	 * Get the world time
	 * 
	 * @return time
	 */
	public float getTime() {
		return time;
	}

	public void setTime(float time) {
		this.time = time;
	}

}
