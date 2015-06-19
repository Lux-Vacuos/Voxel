package io.github.guerra24.voxel.client.kernel.render.types;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import io.github.guerra24.voxel.client.kernel.Kernel;
import io.github.guerra24.voxel.client.kernel.render.MasterRenderer;
import io.github.guerra24.voxel.client.kernel.render.shaders.types.EntityShader;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.resources.models.RawModel;
import io.github.guerra24.voxel.client.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.world.entities.Entity;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

public class EntityRenderer {

	private EntityShader shader;
	private int viewDistanceX = 8, viewDistanceZ = 8;

	public EntityRenderer(EntityShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				viewCull(entity);
				if (entity.isVisible()) {
					prepareInstance(entity);
					glDrawElements(GL_TRIANGLES, model.getRawModel()
							.getVertexCount(), GL_UNSIGNED_INT, 0);
				}
			}
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawmodel = model.getRawModel();
		glBindVertexArray(rawmodel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, model.getTexture().getID());

	}

	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
	}

	private void viewCull(Entity entity) {
		if ((int) Kernel.gameResources.camera.getPosition().x < (int) entity
				.getPosition().x + viewDistanceX) {
			if ((int) Kernel.gameResources.camera.getPosition().z < (int) entity
					.getPosition().z + viewDistanceX) {
				if ((int) Kernel.gameResources.camera.getPosition().x > (int) entity
						.getPosition().x - viewDistanceZ) {
					if ((int) Kernel.gameResources.camera.getPosition().z > (int) entity
							.getPosition().z - viewDistanceZ) {
						entity.setVisible(true);
					} else {
						entity.setVisible(false);
					}
				} else {
					entity.setVisible(false);
				}
			} else {
				entity.setVisible(false);
			}
		} else {
			entity.setVisible(false);
		}
	}

	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(
				entity.getPosition(), entity.getRotX(), entity.getRotY(),
				entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}

}
