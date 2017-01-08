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

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawArraysInstanced;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.ParticleTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.ParticleShader;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.client.world.particles.Particle;

public class ParticleRenderer {

	private static final float[] VERTICES = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };
	private static final int MAX_INSTANCES = 10000;
	private static final int INSTANCE_DATA_LENGHT = 21;
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_INSTANCES * INSTANCE_DATA_LENGHT);

	private RawModel quad;
	private ParticleShader shader;

	private ResourceLoader loader;
	private int vbo;
	private int pointer = 0;

	public ParticleRenderer(ResourceLoader loader) {
		this.loader = loader;
		this.vbo = loader.createEmptyVBO(INSTANCE_DATA_LENGHT * MAX_INSTANCES);
		quad = loader.loadToVAO(VERTICES, 2);
		loader.addInstacedAttribute(quad.getVaoID(), vbo, 1, 4, INSTANCE_DATA_LENGHT, 0);
		loader.addInstacedAttribute(quad.getVaoID(), vbo, 2, 4, INSTANCE_DATA_LENGHT, 4);
		loader.addInstacedAttribute(quad.getVaoID(), vbo, 3, 4, INSTANCE_DATA_LENGHT, 8);
		loader.addInstacedAttribute(quad.getVaoID(), vbo, 4, 4, INSTANCE_DATA_LENGHT, 12);
		loader.addInstacedAttribute(quad.getVaoID(), vbo, 5, 4, INSTANCE_DATA_LENGHT, 16);
		loader.addInstacedAttribute(quad.getVaoID(), vbo, 6, 1, INSTANCE_DATA_LENGHT, 20);

		shader = new ParticleShader();
	}

	public void render(Map<ParticleTexture, List<Particle>> particles, Camera camera) {
		prepare();
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		for (ParticleTexture texture : particles.keySet()) {
			bindTexture(texture);
			List<Particle> particleList = particles.get(texture);
			pointer = 0;
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGHT];

			for (Particle particle : particles.get(texture)) {
				updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(),
						camera.getViewMatrix(), vboData);
				updateTexCoordInfo(particle, vboData);
			}
			loader.updateVBO(vbo, vboData, buffer);
			glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
		}
		finishRendering();
	}

	private void bindTexture(ParticleTexture texture) {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture.getTexture().getID());
		shader.loadNumberOfRows(texture.getNumbreOfRows());
	}

	private void updateTexCoordInfo(Particle particle, float[] vboData) {
		vboData[pointer++] = (float) particle.getTexOffset0().x;
		vboData[pointer++] = (float) particle.getTexOffset0().y;
		vboData[pointer++] = (float) particle.getTexOffset1().x;
		vboData[pointer++] = (float) particle.getTexOffset1().y;
		vboData[pointer++] = particle.getBlend();
	}

	private void updateModelViewMatrix(Vector3d position, float rotation, float scale, Matrix4d viewMatrix,
			float[] vboData) {
		Matrix4d modelMatrix = new Matrix4d();
		Matrix4d.translate(position, modelMatrix, modelMatrix);
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		Matrix4d modelViewMatrix = Matrix4d.mul(viewMatrix, modelMatrix, null);
		Matrix4d.rotate((float) Math.toRadians(rotation), new Vector3d(0, 0, 1), modelMatrix, modelMatrix);
		Matrix4d.scale(new Vector3d(scale, scale, scale), modelMatrix, modelMatrix);
		storeMatrixData(modelViewMatrix, vboData);
	}

	private void storeMatrixData(Matrix4d matrix, float[] vboData) {
		vboData[pointer++] = (float) matrix.m00;
		vboData[pointer++] = (float) matrix.m01;
		vboData[pointer++] = (float) matrix.m02;
		vboData[pointer++] = (float) matrix.m03;
		vboData[pointer++] = (float) matrix.m10;
		vboData[pointer++] = (float) matrix.m11;
		vboData[pointer++] = (float) matrix.m12;
		vboData[pointer++] = (float) matrix.m13;
		vboData[pointer++] = (float) matrix.m20;
		vboData[pointer++] = (float) matrix.m21;
		vboData[pointer++] = (float) matrix.m22;
		vboData[pointer++] = (float) matrix.m23;
		vboData[pointer++] = (float) matrix.m30;
		vboData[pointer++] = (float) matrix.m31;
		vboData[pointer++] = (float) matrix.m32;
		vboData[pointer++] = (float) matrix.m33;
	}

	private void prepare() {
		shader.start();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glEnableVertexAttribArray(6);
		glDepthMask(false);
		glEnable(GL_BLEND);
	}

	private void finishRendering() {
		glDisable(GL_BLEND);
		glDepthMask(true);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glDisableVertexAttribArray(6);
		glBindVertexArray(0);
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}

}
