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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import io.github.guerra24.voxel.client.kernel.core.Kernel;
import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.Display;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.VoxelGL33;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.EntityShader;
import io.github.guerra24.voxel.client.kernel.resources.Loader;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.block.BlockEntity;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;
import io.github.guerra24.voxel.client.kernel.world.entities.IEntity;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;

/**
 * Game Master Renderer
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
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
	 * Batcher of BlockEntity
	 */
	private Map<TexturedModel, List<BlockEntity>> blockEntities = new HashMap<TexturedModel, List<BlockEntity>>();
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
	 * @param cubes1temp
	 *            A list of BlockEntity
	 * @param lights
	 *            A list of Lights
	 * @param camera
	 *            A Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void renderChunk(Queue<BlockEntity> cubes1temp, List<Light> lights, Camera camera) {
		for (BlockEntity entity : cubes1temp) {
			processBlockEntity(entity);
		}
		renderChunk(lights, camera);
	}

	/**
	 * Render the Entity's
	 * 
	 * @param list.get(index)
	 *            A list of Entity's
	 * @param lights
	 *            A list of Lights
	 * @param camera
	 *            A Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void renderEntity(List<IEntity> list, List<Light> lights, Camera camera) {
		for (IEntity entity : list) {
			if (entity != null)
				if (entity.getEntity() != null)
					if (Kernel.gameResources.frustum.pointInFrustum(entity.getEntity().getPosition().x,
							entity.getEntity().getPosition().y, entity.getEntity().getPosition().z))
						processEntity(entity.getEntity());
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
		shader.loadSkyColour(KernelConstants.RED, KernelConstants.GREEN, KernelConstants.BLUE);
		shader.loadLights(lights);
		shader.loadviewMatrix(camera);
		shader.loadDirectLightDirection(new Vector3f(-80, -100, -40));
		shader.loadblendFactor(Kernel.gameResources.skyboxRenderer.getBlendFactor());
		shader.loadTime(Kernel.gameResources.skyboxRenderer.getTime());
		entityRenderer.renderBlockEntity(blockEntities);
		shader.stop();
		blockEntities.clear();
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
		shader.loadDirectLightDirection(new Vector3f(-80, -100, -40));
		entityRenderer.renderEntity(entities);
		shader.stop();
		entities.clear();
	}

	/**
	 * Add the BlockEntity to the batcher map
	 * 
	 * @param BlockEntity
	 *            An Entity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void processBlockEntity(BlockEntity entity) {
		TexturedModel entityModel = entity.getModel();
		List<BlockEntity> batch = blockEntities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<BlockEntity> newBatch = new ArrayList<BlockEntity>();
			newBatch.add(entity);
			blockEntities.put(entityModel, newBatch);
		}
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
		VoxelGL33.glClearColor(KernelConstants.RED, KernelConstants.GREEN, KernelConstants.BLUE, 1);
	}

	/**
	 * Creates the Projection Matrix
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void createProjectionMatrix() {
		aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(KernelConstants.FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = KernelConstants.FAR_PLANE - KernelConstants.NEAR_PLANE;

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
