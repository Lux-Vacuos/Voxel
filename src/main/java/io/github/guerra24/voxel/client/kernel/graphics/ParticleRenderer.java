package io.github.guerra24.voxel.client.kernel.graphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;
import java.util.Map;

import io.github.guerra24.voxel.client.kernel.graphics.opengl.VoxelGL33;
import io.github.guerra24.voxel.client.kernel.graphics.shaders.ParticleShader;
import io.github.guerra24.voxel.client.kernel.particle.Particle;
import io.github.guerra24.voxel.client.kernel.resources.GameControllers;
import io.github.guerra24.voxel.client.kernel.resources.models.RawModel;
import io.github.guerra24.voxel.client.kernel.resources.models.TexturedModel;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;

public class ParticleRenderer {

	private ParticleShader shader;

	public ParticleRenderer(ParticleShader shader) {
		this.shader = shader;
	}

	public void render(Map<TexturedModel, List<Particle>> particles, GameControllers gm) {
		for (TexturedModel model : particles.keySet()) {
			prepareTexturedModel(model, gm);
			List<Particle> batch = particles.get(model);
			for (Particle entity : batch) {
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
	private void prepareTexturedModel(TexturedModel model, GameControllers gm) {
		RawModel rawmodel = model.getRawModel();
		glBindVertexArray(rawmodel.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
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
		glBindVertexArray(0);
	}

	/**
	 * Prepares the Textured Model Translation, Rotation and Scale
	 * 
	 * @param entity
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void prepareInstance(Particle particle) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(particle.getPosition(), particle.getRotX(),
				particle.getRotY(), particle.getRotZ(), particle.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
}
