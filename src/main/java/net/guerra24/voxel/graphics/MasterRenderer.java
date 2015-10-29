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

package net.guerra24.voxel.graphics;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.guerra24.voxel.core.VoxelVariables;
import net.guerra24.voxel.graphics.opengl.Display;
import net.guerra24.voxel.graphics.opengl.VoxelGL33;
import net.guerra24.voxel.graphics.shaders.EntityShader;
import net.guerra24.voxel.graphics.shaders.WaterShader;
import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.Loader;
import net.guerra24.voxel.resources.models.TexturedModel;
import net.guerra24.voxel.resources.models.WaterTile;
import net.guerra24.voxel.util.vector.Matrix4f;
import net.guerra24.voxel.world.block.BlockEntity;
import net.guerra24.voxel.world.entities.Entity;
import net.guerra24.voxel.world.entities.IEntity;

/**
 * Game Master Renderer
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class MasterRenderer {

	/**
	 * Master Renderer Data
	 */
	private Matrix4f projectionMatrix;
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private Map<TexturedModel, List<BlockEntity>> blockEntities = new HashMap<TexturedModel, List<BlockEntity>>();
	private EntityShader shader = new EntityShader();
	private Queue<WaterTile> waterTiles = new ConcurrentLinkedQueue<>();
	private EntityRenderer entityRenderer;
	private WaterShader waterShader;
	private WaterRenderer waterRenderer;
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
		projectionMatrix = createProjectionMatrix(Display.getWidth(), Display.getHeight(), VoxelVariables.FOV,
				VoxelVariables.NEAR_PLANE, VoxelVariables.FAR_PLANE);
		entityRenderer = new EntityRenderer(shader, projectionMatrix);
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(loader, waterShader, projectionMatrix);
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
		VoxelGL33.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	/**
	 * Render the Chunk
	 * 
	 * @param cubes1temp
	 *            A list of BlockEntity
	 * @param lights1
	 *            A list of Lights
	 * @param camera
	 *            A Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void renderChunk(Queue<Object> cubes, GameResources gm) {
		for (Object entity : cubes) {
			if (entity.getClass().equals(BlockEntity.class))
				processBlockEntity((BlockEntity) entity);
			if (entity.getClass().equals(WaterTile.class))
				waterTiles.add((WaterTile) entity);
		}
		renderBlocks(gm);
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
	public void renderEntity(List<IEntity> list, GameResources gm) {
		for (IEntity entity : list) {
			if (entity != null)
				if (entity.getEntity() != null)
					if (gm.getFrustum().pointInFrustum(entity.getEntity().getPosition().x,
							entity.getEntity().getPosition().y, entity.getEntity().getPosition().z))
						processEntity(entity.getEntity());
		}
		renderEntity(gm);
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
	private void renderBlocks(GameResources gm) {
		shader.start();
		shader.loadSkyColour(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE);
		shader.loadviewMatrix(gm.getCamera());
		entityRenderer.renderBlockEntity(blockEntities, gm);
		shader.stop();
		blockEntities.clear();
		waterRenderer.render(waterTiles, gm);
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
	private void renderEntity(GameResources gm) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadviewMatrix(gm.getCamera());
		entityRenderer.renderEntity(entities, gm);
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
		VoxelGL33.glClearColor(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, 1);
	}

	/**
	 * Creates the Projection Matrix
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public Matrix4f createProjectionMatrix(int width, int height, float fov, float nearPlane, float farPlane) {
		aspectRatio = (float) width / (float) height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = farPlane - nearPlane;

		Matrix4f projectionMatrix = new Matrix4f();
		projectionMatrix.setIdentity();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((farPlane + nearPlane) / frustrum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustrum_length);
		projectionMatrix.m33 = 0;
		return projectionMatrix;
	}

	/**
	 * Clear the Shader
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void cleanUp() {
		shader.cleanUp();
		waterShader.cleanUp();
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

	public void setProjectionMatrix(Matrix4f matrix) {
		projectionMatrix = matrix;
	}

	public WaterRenderer getWaterRenderer() {
		return waterRenderer;
	}
}
