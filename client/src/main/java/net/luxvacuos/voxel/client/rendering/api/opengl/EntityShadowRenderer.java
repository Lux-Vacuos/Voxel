/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.voxel.client.ecs.ClientComponents;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.ecs.entities.RenderEntity;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Mesh;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Model;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.EntityBasicShader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;
import net.luxvacuos.voxel.universal.ecs.components.Scale;

public class EntityShadowRenderer {

	private EntityBasicShader shader;

	private Map<Model, List<RenderEntity>> entities = new HashMap<Model, List<RenderEntity>>();

	public EntityShadowRenderer() {
		shader = new EntityBasicShader();
	}

	public void cleanUp() {
		shader.dispose();
	}

	public void renderEntity(ImmutableArray<Entity> immutableArray, CameraEntity sunCamera) {
		for (Entity entity : immutableArray) {
			if (entity instanceof RenderEntity) {
				processEntity((RenderEntity) entity);
			}
		}
		renderEntity(sunCamera);
	}

	private void renderEntity(CameraEntity sunCamera) {
		glCullFace(GL_FRONT);
		shader.start();
		shader.loadviewMatrix(sunCamera);
		shader.loadProjectionMatrix(sunCamera.getProjectionMatrix());
		renderEntity(entities);
		shader.stop();
		entities.clear();
		glCullFace(GL_BACK);
	}

	private void processEntity(RenderEntity entity) {
		Model entityModel = ClientComponents.RENDERABLE.get(entity).getModel();
		List<RenderEntity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<RenderEntity> newBatch = new ArrayList<RenderEntity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}

	private void renderEntity(Map<Model, List<RenderEntity>> blockEntities) {
		for (Model model : blockEntities.keySet()) {
			List<RenderEntity> batch = blockEntities.get(model);
			for (RenderEntity entity : batch) {
				prepareInstance(entity);
				for (Mesh mesh : model.getMeshes()) {
					Material mat = model.getMaterials().get(mesh.getAiMesh().mMaterialIndex());
					prepareTexturedModel(mesh, mat);
					glDrawElements(GL_TRIANGLES, mesh.getMesh().getIndexCount(), GL_UNSIGNED_INT, 0);
					unbindTexturedModel(mesh);
				}
			}
		}
	}

	private void prepareTexturedModel(Mesh mesh, Material material) {
		mesh.getMesh().bind(0, 1);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, material.getDiffuseTexture().getID());
	}

	private void unbindTexturedModel(Mesh mesh) {
		mesh.getMesh().unbind(0, 1);
	}

	private void prepareInstance(RenderEntity entity) {
		Position pos = Components.POSITION.get(entity);
		Rotation rot = Components.ROTATION.get(entity);
		Scale scale = Components.SCALE.get(entity);
		Matrix4d transformationMatrix = Maths.createTransformationMatrix(pos.getPosition(), rot.getX(), rot.getY(),
				rot.getZ(), scale.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
