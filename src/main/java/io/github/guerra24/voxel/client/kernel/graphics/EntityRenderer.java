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

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import io.github.guerra24.voxel.client.kernel.graphics.opengl.VoxelGL33;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.EntityShader;
import io.github.guerra24.voxel.client.kernel.resources.models.RawModel;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;
import io.github.guerra24.voxel.client.kernel.world.block.BlockEntity;
import io.github.guerra24.voxel.client.kernel.world.entities.Entity;

import java.util.List;
import java.util.Map;

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

	/**
	 * Constructor, initializes the shaders and the projection matrix
	 * 
	 * @param shader
	 *            Entity Shader
	 * @param projectionMatrix
	 *            A Matrix4f Projection
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public EntityRenderer(EntityShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	/**
	 * Render the block entity's in the list
	 * 
	 * @param blockEntities
	 *            A List of entity's
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void renderBlockEntity(Map<TexturedModel, List<BlockEntity>> blockEntities) {
		for (TexturedModel model : blockEntities.keySet()) {
			prepareTexturedModel(model);
			List<BlockEntity> batch = blockEntities.get(model);
			for (BlockEntity entity : batch) {
				prepareInstance(entity);
				VoxelGL33.glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	/**
	 * Render the entity's in the list
	 * 
	 * @param blockEntities
	 *            A List of entity's
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void renderEntity(Map<TexturedModel, List<Entity>> blockEntities) {
		for (TexturedModel model : blockEntities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = blockEntities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				VoxelGL33.glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	/**
	 * Prepares the Entity Textured Model and binds the VAOs
	 * 
	 * @param model
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawmodel = model.getRawModel();
		glBindVertexArray(rawmodel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());

	}

	/**
	 * UnBinds the VAOs
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void unbindTexturedModel() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}

	/**
	 * Prepares the Textured Model Translation, Rotation and Scale
	 * 
	 * @param entity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void prepareInstance(BlockEntity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);

	}

	/**
	 * Prepares the Textured Model Translation, Rotation and Scale
	 * 
	 * @param entity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
