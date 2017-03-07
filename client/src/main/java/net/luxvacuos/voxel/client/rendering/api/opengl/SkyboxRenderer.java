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

import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDepthMask;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.SkyboxShader;
import net.luxvacuos.voxel.client.resources.ResourceLoader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.universal.core.IWorldSimulation;

/**
 * Skybox Rendering
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class SkyboxRenderer {

	private RawModel dome;
	private SkyboxShader shader;

	public SkyboxRenderer(ResourceLoader loader) {
		dome = loader.loadObjModel("SkyDome");
		shader = new SkyboxShader();
		shader.start();
		shader.loadTransformationMatrix(Maths.createTransformationMatrix(new Vector3d(), 0, 0, 0, 360));
		shader.stop();
	}

	/**
	 * Render the Skybox
	 * 
	 * @param camera
	 *            A Camera
	 * @param r
	 *            Fog Red Color
	 * @param g
	 *            Fog Green Color
	 * @param b
	 *            Fog Blue Color
	 * @param delta
	 *            Delta
	 */
	public void render(float r, float g, float b, CameraEntity camera, IWorldSimulation clientWorldSimulation,
			Vector3d lightPosition) {
		glDepthMask(false);
		glDisable(GL_CULL_FACE);
		shader.start();
		shader.loadTransformationMatrix(Maths.createTransformationMatrix(camera.getPosition(), 0, 0, 0, 400));
		shader.loadProjectionMatrix(camera.getProjectionMatrix());
		shader.loadViewMatrix(camera);
		shader.loadFog(r, g, b);
		shader.loadTime(clientWorldSimulation.getGlobalTime());
		shader.loadLightPosition(lightPosition);
		glBindVertexArray(dome.getVaoID());
		glEnableVertexAttribArray(0);
		glEnableVertexAttribArray(2);
		glDrawElements(GL_TRIANGLES, dome.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(2);
		glBindVertexArray(0);
		shader.stop();
		glEnable(GL_CULL_FACE);
		glDepthMask(true);
	}

}
