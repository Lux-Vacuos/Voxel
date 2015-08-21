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

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.VoxelGL33;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.EntityShader;
import io.github.guerra24.voxel.client.kernel.resources.Loader;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Game Master Renderer
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.2 Build-57
 * @since 0.0.1 Build-52
 * @category Rendering
 */
public class MasterRenderer {

	/**
	 * Matrix4f Projection
	 */
	private Matrix4f projectionMatrix;
	/**
	 * Batcher of entity
	 */
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	/**
	 * Entity Shader
	 */
	public EntityShader shader = new EntityShader();
	/**
	 * Entity Renderer
	 */
	public EntityRenderer entityRenderer;
	/**
	 * Game Aspect Ratio
	 */
	public float aspectRatio;

	/**
	 * Constructor, Initializes the OpenGL code, creates the projection matrix,
	 * entity renderer and skybox renderer
	 * 
	 * @param loader
	 *            Game Loader
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public MasterRenderer(Loader loader) {
		initGL();
		createProjectionMatrix();
		entityRenderer = new EntityRenderer(shader, projectionMatrix);
	}

	/**
	 * Initialize the OpenGL Code
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void initGL() {
		VoxelGL33.glEnable(GL_DEPTH_TEST);
		VoxelGL33.glEnable(GL_CULL_FACE);
		VoxelGL33.glCullFace(GL_BACK);
	}

	/**
	 * Render the Chunk
	 * 
	 * @param cubes
	 *            A list of Cubes
	 * @param lights
	 *            A list of Lights
	 * @param camera
	 *            A Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void renderChunk(Queue<Entity> cubes, List<Light> lights,
			Camera camera) {
		for (Entity entity : cubes) {
			processEntity(entity);
		}
		renderChunk(lights, camera);
	}

	/**
	 * Render the Entity's
	 * 
	 * @param entities
	 *            A list of Entity's
	 * @param lights
	 *            A list of Lights
	 * @param camera
	 *            A Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void renderEntity(List<Entity> entities, List<Light> lights,
			Camera camera) {
		for (Entity entity : entities) {
			if (Kernel.gameResources.frustum.pointInFrustum(
					entity.getPosition().x, entity.getPosition().y,
					entity.getPosition().z))
				processEntity(entity);
		}
		renderEntity(lights, camera);
	}

	/**
	 * Chunk Rendering PipeLine
	 * 
	 * @param lights
	 *            A list of lights
	 * @param camera
	 *            A Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void renderChunk(List<Light> lights, Camera camera) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadSkyColour(KernelConstants.RED, KernelConstants.GREEN,
				KernelConstants.BLUE);
		shader.loadDirectLightDirection(new Vector3f(-80, -100, -40));
		shader.loadLights(lights);
		shader.loadviewMatrix(camera);
		entityRenderer.render(entities);
		shader.stop();
		entities.clear();
	}

	/**
	 * Entity's Rendering PipeLine
	 * 
	 * @param lights
	 *            A list of Lights
	 * @param camera
	 *            A Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void renderEntity(List<Light> lights, Camera camera) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadLights(lights);
		shader.loadviewMatrix(camera);
		entityRenderer.render(entities);
		shader.stop();
		entities.clear();
	}

	/**
	 * Add the Entity to the batcher map
	 * 
	 * @param entity
	 *            An Entity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	/**
	 * Clear the OpenGL Buffers
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void prepare() {
		VoxelGL33.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		VoxelGL33.glClearColor(KernelConstants.RED, KernelConstants.GREEN,
				KernelConstants.BLUE, 1);
	}

	/**
	 * Creates the Projection Matrix
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void createProjectionMatrix() {
		aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math
				.toRadians(KernelConstants.FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = KernelConstants.FAR_PLANE
				- KernelConstants.NEAR_PLANE;

		projectionMatrix = new Matrix4f();
		projectionMatrix.setIdentity();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((KernelConstants.FAR_PLANE + KernelConstants.NEAR_PLANE) / frustrum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * KernelConstants.NEAR_PLANE * KernelConstants.FAR_PLANE) / frustrum_length);
		projectionMatrix.m33 = 0;
	}

	/**
	 * Clear the Shader
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void cleanUp() {
		shader.cleanUp();
	}

	/**
	 * Gets the Projection matrix
	 * 
	 * @return A Projection Matrix
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
