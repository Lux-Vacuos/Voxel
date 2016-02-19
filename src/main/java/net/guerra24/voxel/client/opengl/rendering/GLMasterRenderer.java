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

package net.guerra24.voxel.client.opengl.rendering;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_STENCIL_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.opengl.rendering.shaders.EntityShader;
import net.guerra24.voxel.client.opengl.rendering.shaders.WaterShader;
import net.guerra24.voxel.client.rendering.MasterRenderer;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.models.TexturedModel;
import net.guerra24.voxel.client.world.block.BlockEntity;
import net.guerra24.voxel.client.world.entities.GameEntity;

/**
 * Game Master Renderer
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class GLMasterRenderer extends MasterRenderer {

	/**
	 * Master Renderer Data
	 */
	private WaterShader waterShader;
	private WaterRenderer waterRenderer;
	private EntityShader shader = new EntityShader();
	private GLEntityRenderer entityRenderer;

	/**
	 * Constructor, Initializes the OpenGL code, creates the projection matrix,
	 * entity renderer
	 * 
	 * @param loader
	 *            Game Loader
	 */
	public GLMasterRenderer(GameResources gm) {
		super(gm);
		projectionMatrix = createProjectionMatrix(gm.getDisplay().getDisplayWidth(), gm.getDisplay().getDisplayHeight(),
				VoxelVariables.FOV, VoxelVariables.NEAR_PLANE, VoxelVariables.FAR_PLANE);
		entityRenderer = new GLEntityRenderer(shader, gm, projectionMatrix);
		waterShader = new WaterShader();
		waterRenderer = new WaterRenderer(gm, projectionMatrix);
	}

	@Override
	public void init() {
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void processChunk(List<Object> cubes, GameResources gm) {
		for (Object entity : cubes) {
			if (entity instanceof BlockEntity)
				processBlockEntity((BlockEntity) entity);
		}
		renderChunk(gm);
	}

	@Override
	public void renderEntity(ImmutableArray<Entity> immutableArray, GameResources gm) {
		for (Entity entity : immutableArray) {
			if (entity instanceof GameEntity) {
				GameEntity ent = (GameEntity) entity;
				if (gm.getFrustum().pointInFrustum(ent.getPosition().x, ent.getPosition().y, ent.getPosition().z))
					processEntity(ent);
			}
		}
		renderEntity(gm);
	}

	@Override
	protected void renderChunk(GameResources gm) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadviewMatrix(gm.getCamera());
		shader.loadLightMatrix(gm);
		shader.useShadows(VoxelVariables.useShadows);
		entityRenderer.renderBlockEntity(blockEntities, gm);
		shader.stop();
		blockEntities.clear();
	}

	@Override
	protected void renderEntity(GameResources gm) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadviewMatrix(gm.getCamera());
		entityRenderer.renderEntity(entities, gm);
		shader.stop();
		entities.clear();
	}

	@Override
	protected void processBlockEntity(BlockEntity entity) {
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

	@Override
	protected void processEntity(GameEntity entity) {
		TexturedModel entityModel = entity.getModel();
		List<GameEntity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<GameEntity> newBatch = new ArrayList<GameEntity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	@Override
	public void prepare() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClearColor(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, 1);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void cleanUp() {
		shader.cleanUp();
		waterShader.cleanUp();
	}

	@Override
	public WaterRenderer getWaterRenderer() {
		return waterRenderer;
	}

}
