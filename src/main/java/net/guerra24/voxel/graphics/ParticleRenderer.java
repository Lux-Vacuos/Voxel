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

package net.guerra24.voxel.graphics;

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

import java.util.List;
import java.util.Map;

import net.guerra24.voxel.graphics.shaders.ParticleShader;
import net.guerra24.voxel.particle.Particle;
import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.models.RawModel;
import net.guerra24.voxel.resources.models.TexturedModel;
import net.guerra24.voxel.util.Maths;
import net.guerra24.voxel.util.vector.Matrix4f;

/**
 * Particle Renderer
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class ParticleRenderer {

	/**
	 * ParticleRenderer Data
	 */
	private ParticleShader shader;

	/**
	 * Constructor
	 * 
	 * @param shader
	 *            Particle Shader
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public ParticleRenderer(ParticleShader shader) {
		this.shader = shader;
	}

	/**
	 * Render Particles
	 * 
	 * @param particles
	 *            Batch of particles
	 * @param gm
	 *            GameResources
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void render(Map<TexturedModel, List<Particle>> particles, GameResources gm) {
		for (TexturedModel model : particles.keySet()) {
			prepareTexturedModel(model, gm);
			List<Particle> batch = particles.get(model);
			for (Particle entity : batch) {
				prepareInstance(entity);
				glDrawElements(GL_TRIANGLES, model.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	/**
	 * Prepares the Entity Textured Model and binds the VAOs
	 * 
	 * @param model
	 *            TexturedModel
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	private void prepareTexturedModel(TexturedModel model, GameResources gm) {
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
