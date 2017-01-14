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

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.CubeMapTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.DeferredShadingShader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.core.IWorldSimulation;

/**
 * ImagePass, use inside {@link RenderingPipeline} to process different image
 * passes.
 *
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public abstract class ImagePass implements IImagePass {

	/**
	 * Deferred Shader
	 */
	private DeferredShadingShader shader;
	/**
	 * FBO
	 */
	private ImagePassFBO fbo;

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
	public ImagePass(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}

	/**
	 * Initializes the FBO, Shader and loads information to the shader.
	 */
	@Override
	public void init() {
		fbo = new ImagePassFBO(width, height);
		shader = new DeferredShadingShader(name);
		shader.start();
		shader.loadTransformation(Maths.createTransformationMatrix(new Vector2d(0, 0), new Vector2d(1, 1)));
		shader.loadResolution(new Vector2d(width, height));
		shader.loadSkyColor(ClientVariables.skyColor);
		shader.stop();
	}

	@Override
	public void process(Camera camera, Matrix4d previousViewMatrix, Vector3d previousCameraPosition,
			Vector3d lightPosition, Vector3d invertedLightPosition, IWorldSimulation clientWorldSimulation,
			List<Light> lights, ImagePassFBO[] auxs, RenderingPipeline pipe, RawModel quad,
			CubeMapTexture environmentMap, float exposure) {
		fbo.begin();
		shader.start();
		shader.loadUnderWater(false);
		shader.loadMotionBlurData(camera, previousViewMatrix, previousCameraPosition);
		shader.loadLightPosition(lightPosition, invertedLightPosition);
		shader.loadviewMatrix(camera);
		shader.loadSettings(ClientVariables.useDOF, ClientVariables.useFXAA, ClientVariables.useMotionBlur,
				ClientVariables.useVolumetricLight, ClientVariables.useReflections, ClientVariables.useAmbientOcclusion,
				ClientVariables.shadowMapDrawDistance);
		shader.loadSunPosition(Maths.convertTo2F(new Vector3d(lightPosition), camera.getProjectionMatrix(),
				Maths.createViewMatrixRot(camera.getRotation().getX(), camera.getRotation().getY(),
						camera.getRotation().getZ(), tmp),
				width, height));
		shader.loadExposure(exposure);
		shader.loadPointLightsPos(lights);
		shader.loadTime(clientWorldSimulation.getTime());
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		render(auxs, pipe, environmentMap);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
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

	public ImagePassFBO getFbo() {
		return fbo;
	}

}
