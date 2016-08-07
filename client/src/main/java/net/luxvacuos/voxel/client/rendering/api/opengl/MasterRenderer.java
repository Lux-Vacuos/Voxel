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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.EntityShader;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.TexturedModel;
import net.luxvacuos.voxel.client.world.entities.AbstractEntity;
import net.luxvacuos.voxel.client.world.entities.components.RendereableComponent;

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
	private EntityShader shader;
	private EntityRenderer entityRenderer;
	private Matrix4f projectionMatrix;
	private Map<TexturedModel, List<AbstractEntity>> entities = new HashMap<TexturedModel, List<AbstractEntity>>();

	public MasterRenderer(GameResources gm) {
		shader = new EntityShader();
		projectionMatrix = createProjectionMatrix(gm.getDisplay().getDisplayWidth(), gm.getDisplay().getDisplayHeight(),
				VoxelVariables.FOV, VoxelVariables.NEAR_PLANE, VoxelVariables.FAR_PLANE);
		entityRenderer = new EntityRenderer(shader, gm, projectionMatrix);
	}

	public void init() {
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public void renderEntity(ImmutableArray<Entity> immutableArray, GameResources gm) {
		for (Entity entity : immutableArray) {
			if (entity instanceof AbstractEntity && entity.getComponent(RendereableComponent.class) != null) {
				processEntity((AbstractEntity) entity);
			}
		}
		renderEntity(gm);
	}

	protected void renderEntity(GameResources gm) {
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.loadviewMatrix(gm.getCamera());
		shader.loadLightMatrix(gm);
		shader.useShadows(VoxelVariables.useShadows);
		entityRenderer.renderEntity(entities, gm);
		shader.stop();
		entities.clear();
	}

	protected void processEntity(AbstractEntity entity) {
		TexturedModel entityModel = entity.getComponent(RendereableComponent.class).model;
		List<AbstractEntity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<AbstractEntity> newBatch = new ArrayList<AbstractEntity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	public static void prepare() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClearColor(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE, 1);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public static void prepare(float r, float g, float b, float a) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
		glClearColor(r, g, b, a);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_DEPTH_TEST);
		glDisable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	public void update(GameResources gm) {
		projectionMatrix = createProjectionMatrix(projectionMatrix, gm.getDisplay().getDisplayWidth(),
				gm.getDisplay().getDisplayHeight(), VoxelVariables.FOV, VoxelVariables.NEAR_PLANE,
				VoxelVariables.FAR_PLANE);
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void setProjectionMatrix(Matrix4f matrix) {
		projectionMatrix = matrix;
	}

	public EntityShader getShader() {
		return shader;
	}

	public static Matrix4f createProjectionMatrix(int width, int height, float fov, float nearPlane, float farPlane) {
		return createProjectionMatrix(new Matrix4f(), width, height, fov, nearPlane, farPlane);
	}

	public static Matrix4f createProjectionMatrix(Matrix4f proj, int width, int height, float fov, float nearPlane,
			float farPlane) {
		float aspectRatio = (float) width / (float) height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))));
		float x_scale = y_scale / aspectRatio;
		float frustrum_length = farPlane - nearPlane;

		proj.setIdentity();
		proj.m00 = x_scale;
		proj.m11 = y_scale;
		proj.m22 = -((farPlane + nearPlane) / frustrum_length);
		proj.m23 = -1;
		proj.m32 = -((2 * nearPlane * farPlane) / frustrum_length);
		proj.m33 = 0;
		return proj;
	}

}
