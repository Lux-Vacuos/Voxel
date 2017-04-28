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

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;

import java.util.List;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.ecs.entities.CameraEntity;
import net.luxvacuos.voxel.client.ecs.entities.Sun;
import net.luxvacuos.voxel.client.ecs.entities.SunCamera;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.CubeMapTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Texture;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.DeferredShadingShader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.universal.core.IWorldSimulation;

public abstract class DeferredPass implements IDeferredPass {

	/**
	 * Deferred Shader
	 */
	private DeferredShadingShader shader;
	/**
	 * FBO
	 */
	private FBO fbo;

	/**
	 * Width and Height of the FBO
	 */
	private int width, height;
	/**
	 * Name
	 */
	private String name;

	private static Matrix4d tmp;

	/**
	 * 
	 * @param width
	 *            Width
	 * @param height
	 *            Height
	 */
	public DeferredPass(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	/**
	 * Initializes the FBO, Shader and loads information to the shader.
	 */
	@Override
	public void init() {
		fbo = new FBO(width, height);
		shader = new DeferredShadingShader(name);
		shader.start();
		shader.loadResolution(new Vector2d(width, height));
		shader.loadSkyColor(ClientVariables.skyColor);
		shader.stop();
	}

	@Override
	public void process(CameraEntity camera, Sun sun, Matrix4d previousViewMatrix, Vector3d previousCameraPosition,
			IWorldSimulation clientWorldSimulation, List<Light> lights, FBO[] auxs, IDeferredPipeline pipe,
			RawModel quad, CubeMapTexture irradianceCapture, CubeMapTexture environmentMap, Texture brdfLUT,
			ShadowFBO shadowFBO, float exposure) {
		fbo.begin();
		shader.start();
		shader.loadUnderWater(false);
		shader.loadMotionBlurData(camera, previousViewMatrix, previousCameraPosition);
		shader.loadLightPosition(sun.getSunPosition(), sun.getInvertedSunPosition());
		shader.loadviewMatrix(camera);
		shader.loadSettings(false, false, false,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/volumetricLight"),
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/reflections"),
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/ambientOcclusion"),
				(int) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/shadowsDrawDistance"), false,
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/lensFlares"),
				(boolean) REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/shadows"));
		shader.loadSunPosition(Maths.convertTo2F(new Vector3d(sun.getSunPosition()), camera.getProjectionMatrix(),
				Maths.createViewMatrixRot(camera.getRotation().getX(), camera.getRotation().getY(),
						camera.getRotation().getZ(), tmp),
				width, height));
		shader.loadExposure(exposure);
		shader.loadPointLightsPos(lights);
		shader.loadTime(clientWorldSimulation.getTime());
		shader.loadLightMatrix(sun.getCamera().getViewMatrix());
		shader.loadBiasMatrix(((SunCamera) sun.getCamera()).getProjectionArray());
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		render(auxs, pipe, irradianceCapture, environmentMap, brdfLUT, shadowFBO);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		shader.stop();
		fbo.end();
		auxs[0] = getFbo();
	}

	/**
	 * Dispose shader and FBO
	 */
	@Override
	public void dispose() {
		shader.dispose();
		fbo.cleanUp();
	}

	public FBO getFbo() {
		return fbo;
	}

}
