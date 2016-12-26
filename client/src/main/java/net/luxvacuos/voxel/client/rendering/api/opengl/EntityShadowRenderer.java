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
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.utils.ImmutableArray;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.TexturedModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.EntityBasicShader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.AbstractEntity;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.entities.components.RendereableComponent;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;
import net.luxvacuos.voxel.universal.ecs.components.Scale;

public class EntityShadowRenderer {

	private EntityBasicShader shader;

	private Map<TexturedModel, List<AbstractEntity>> entities = new HashMap<TexturedModel, List<AbstractEntity>>();

	public EntityShadowRenderer(Matrix4d shadowProjectionMatrix) {
		shader = new EntityBasicShader();
		shader.start();
		shader.loadProjectionMatrix(shadowProjectionMatrix);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	public void renderEntity(ImmutableArray<Entity> immutableArray, Camera sunCamera) {
		for (Entity entity : immutableArray) {
			if (entity instanceof AbstractEntity && entity.getComponent(RendereableComponent.class) != null) {
				processEntity((AbstractEntity) entity);
			}
		}
		renderEntity(sunCamera);
	}

	private void renderEntity(Camera sunCamera) {
		glCullFace(GL_FRONT);
		shader.start();
		shader.loadviewMatrix(sunCamera);
		renderEntity(entities);
		shader.stop();
		entities.clear();
		glCullFace(GL_BACK);
	}

	private void processEntity(AbstractEntity entity) {
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

	private void renderEntity(Map<TexturedModel, List<AbstractEntity>> blockEntities) {
		for (TexturedModel model : blockEntities.keySet()) {
			prepareTexturedModel(model);
			List<AbstractEntity> batch = blockEntities.get(model);
			for (AbstractEntity entity : batch) {
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawmodel = model.getRawModel();
		glBindVertexArray(rawmodel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getMaterial().getDiffuseTexture().getID());

	}

	private void unbindTexturedModel() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glBindVertexArray(0);
	}

	private void prepareInstance(AbstractEntity entity) {
		Position pos = Components.POSITION.get(entity);
		Rotation rot = Components.ROTATION.get(entity);
		Scale scale = Components.SCALE.get(entity);
		Matrix4d transformationMatrix = Maths.createTransformationMatrix(pos.getPosition(), rot.getX(), rot.getY(),
				rot.getZ(), scale.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
