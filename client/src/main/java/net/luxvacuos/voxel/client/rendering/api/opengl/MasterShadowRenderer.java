/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.rendering.api.opengl;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.glCullFace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.voxel.client.rendering.api.glfw.Display;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.EntityBasicShader;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.TexturedModel;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.GameEntity;

public class MasterShadowRenderer {

	private Map<TexturedModel, List<GameEntity>> entities = new HashMap<TexturedModel, List<GameEntity>>();
	private EntityBasicShader shader;
	private ShadowRenderer renderer;
	private FrameBuffer fbo;
	private Matrix4f projectionMatrix;
	private Display display;
	private int textureSize = 4096;

	public MasterShadowRenderer(Display display) throws Exception {
		this.display = display;
		shader = new EntityBasicShader();
		projectionMatrix = Maths.orthographic(-80, 80, -80, 80, -80, 80, false);
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

	public void renderEntity(ImmutableArray<Entity> immutableArray, GameResources gm) {
		for (Entity entity : immutableArray) {
			if (entity instanceof GameEntity)
				processEntity((GameEntity) entity);
		}
		renderEntity(gm);
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
