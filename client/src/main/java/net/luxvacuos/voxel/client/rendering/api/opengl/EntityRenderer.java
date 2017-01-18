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

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE10;
import static org.lwjgl.opengl.GL13.GL_TEXTURE11;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE8;
import static org.lwjgl.opengl.GL13.GL_TEXTURE9;
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
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.TexturedModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.EntityShader;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.entities.SunCamera;
import net.luxvacuos.voxel.client.world.entities.components.RendereableComponent;
import net.luxvacuos.voxel.universal.ecs.Components;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;
import net.luxvacuos.voxel.universal.ecs.components.Scale;
import net.luxvacuos.voxel.universal.world.entities.AbstractEntity;

/**
 * Entity Rendering
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class EntityRenderer {
	/**
	 * Entity Shader
	 */
	private EntityShader shader;
	private Map<TexturedModel, List<AbstractEntity>> entities = new HashMap<TexturedModel, List<AbstractEntity>>();

	public EntityRenderer(ResourceLoader loader) {
		shader = new EntityShader();
	}

	public void cleanUp() {
		shader.dispose();
	}

	public void renderEntity(ImmutableArray<Entity> immutableArray, Camera camera, Camera sunCamera, ShadowFBO shadow) {
		for (Entity entity : immutableArray) {
			if (entity instanceof AbstractEntity && entity.getComponent(RendereableComponent.class) != null) {
				processEntity((AbstractEntity) entity);
			}
		}
		renderEntity(camera, sunCamera, shadow);
	}

	private void renderEntity(Camera camera, Camera sunCamera, ShadowFBO shadow) {
		shader.start();
		shader.loadviewMatrix(camera);
		shader.loadLightMatrix(sunCamera);
		shader.useShadows(ClientVariables.useShadows);
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadBiasMatrix(((SunCamera) sunCamera).getProj());
		renderEntity(entities, shadow);
		shader.stop();
		entities.clear();
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

	private void renderEntity(Map<TexturedModel, List<AbstractEntity>> blockEntities, ShadowFBO shadow) {
		for (TexturedModel model : blockEntities.keySet()) {
			prepareTexturedModel(model, shadow);
			List<AbstractEntity> batch = blockEntities.get(model);
			shader.loadMaterial(model.getMaterial());
			for (AbstractEntity entity : batch) {
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}

	}

	private void prepareTexturedModel(TexturedModel model, ShadowFBO shadow) {
		RawModel rawmodel = model.getRawModel();
		Material material = model.getMaterial();
		glBindVertexArray(rawmodel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, material.getDiffuseTexture().getID());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, material.getNormalTexture().getID());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, material.getRoughnessTexture().getID());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, material.getMetallicTexture().getID());
		glActiveTexture(GL_TEXTURE8);
		glBindTexture(GL_TEXTURE_2D, shadow.getShadowMaps()[0]);
		glActiveTexture(GL_TEXTURE9);
		glBindTexture(GL_TEXTURE_2D, shadow.getShadowMaps()[1]);
		glActiveTexture(GL_TEXTURE10);
		glBindTexture(GL_TEXTURE_2D, shadow.getShadowMaps()[2]);
		glActiveTexture(GL_TEXTURE11);
		glBindTexture(GL_TEXTURE_2D, shadow.getShadowMaps()[3]);
	}

	private void unbindTexturedModel() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
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
