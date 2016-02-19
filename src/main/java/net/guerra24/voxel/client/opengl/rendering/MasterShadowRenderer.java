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
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.glCullFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import net.guerra24.voxel.client.opengl.Display;
import net.guerra24.voxel.client.opengl.GLUtil;
import net.guerra24.voxel.client.opengl.rendering.shaders.EntityBasicShader;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.models.TexturedModel;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.block.BlockEntity;
import net.guerra24.voxel.client.world.entities.GameEntity;
import net.guerra24.voxel.universal.util.vector.Matrix4f;

public class MasterShadowRenderer {

	private Map<TexturedModel, List<GameEntity>> entities = new HashMap<TexturedModel, List<GameEntity>>();
	private Map<TexturedModel, List<BlockEntity>> blockEntities = new HashMap<TexturedModel, List<BlockEntity>>();
	private EntityBasicShader shader;
	private ShadowRenderer renderer;
	private FrameBuffer fbo;
	private Matrix4f projectionMatrix;
	private Display display;
	private int textureSize = 4096;

	/**
	 * Constructor, Initializes the OpenGL code, creates the projection matrix,
	 * entity renderer and skybox renderer
	 * 
	 * @param loader
	 *            Game Loader
	 */
	public MasterShadowRenderer(Display display) {
		this.display = display;
		shader = new EntityBasicShader();
		projectionMatrix = Maths.orthographic(-80, 80, -80, 80, -80, 80);
		renderer = new ShadowRenderer(shader, projectionMatrix);
		if (textureSize > GLUtil.getTextureMaxSize())
			textureSize = GLUtil.getTextureMaxSize();
		fbo = new FrameBuffer(true, textureSize, textureSize, display);
	}

	public void being() {
		fbo.begin(textureSize, textureSize);
	}

	public void end() {
		fbo.end(display);
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
	 */
	public void renderChunk(List<Object> cubes, GameResources gm) {
		for (Object entity : cubes) {
			if (entity instanceof BlockEntity)
				processBlockEntity((BlockEntity) entity);
		}
		renderBlocks(gm);
	}

	public void renderEntity(ImmutableArray<Entity> immutableArray, GameResources gm) {
		for (Entity entity : immutableArray) {
			if (entity instanceof GameEntity)
				processEntity((GameEntity) entity);
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
	 */
	private void renderBlocks(GameResources gm) {
		glCullFace(GL_FRONT);
		shader.start();
		shader.loadviewMatrix(gm.getSun_Camera());
		renderer.renderBlockEntity(blockEntities, gm);
		shader.stop();
		blockEntities.clear();
		glCullFace(GL_BACK);
	}

	private void renderEntity(GameResources gm) {
		glCullFace(GL_FRONT);
		shader.start();
		shader.loadviewMatrix(gm.getSun_Camera());
		renderer.renderEntity(entities, gm);
		shader.stop();
		entities.clear();
		glCullFace(GL_BACK);
	}

	/**
	 * Add the BlockEntity to the batcher map
	 * 
	 * @param BlockEntity
	 *            An Entity
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
	 */
	private void processEntity(GameEntity entity) {
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

	public FrameBuffer getFbo() {
		return fbo;
	}

	/**
	 * Clear the Shader
	 */
	public void cleanUp() {
		shader.cleanUp();
		fbo.cleanUp();
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

}
