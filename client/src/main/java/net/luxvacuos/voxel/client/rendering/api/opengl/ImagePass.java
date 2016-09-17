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

import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.DeferredShadingShader;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.RawModel;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;

/**
 * ImagePass, use inside {@link RenderingPipeline} to process diferent image
 * passes.
 *
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public abstract class ImagePass {

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
	public void init() {
		fbo = new ImagePassFBO(width, height);
		shader = new DeferredShadingShader(name);
		shader.start();
		shader.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader.connectTextureUnits();
		shader.loadResolution(new Vector2f(width, height));
		shader.loadSkyColor(ClientVariables.skyColor);
		shader.stop();
	}

	public void process(GameResources gm, Matrix4f previousViewMatrix, Vector3f previousCameraPosition,
			ImagePassFBO[] auxs, RenderingPipeline pipe, RawModel quad) {
		begin(gm, previousViewMatrix, previousCameraPosition);
		MasterRenderer.prepare();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		render(auxs, pipe);
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		end();
		auxs[0] = getFbo();
	}

	/**
	 * Begin ImagePass processing, binds the FBO, starts the shader, loads
	 * dynamic data and leaves the state for rendering.
	 * 
	 * @param gm
	 * @param previousViewMatrix
	 *            Previous View Matrix
	 * @param previousCameraPosition
	 *            Previous Camera Position
	 */
	public void begin(GameResources gm, Matrix4f previousViewMatrix, Vector3f previousCameraPosition) {
		fbo.begin();
		shader.start();
		shader.loadUnderWater(((PlayerCamera) gm.getCamera()).isUnderWater());
		shader.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shader.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shader.loadSettings(ClientVariables.useDOF, ClientVariables.useFXAA, ClientVariables.useMotionBlur,
				ClientVariables.useVolumetricLight, ClientVariables.useReflections);
		shader.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()), gm.getRenderer().getProjectionMatrix(),
				Maths.createViewMatrixRot(gm.getCamera().getPitch(), gm.getCamera().getYaw(), gm.getCamera().getRoll(),
						null),
				width, height));
		shader.loadExposure(2f);
		shader.loadPointLightsPos(gm.getLightRenderer().getLights());
		shader.loadTime(gm.getWorldSimulation().getTime());
		MasterRenderer.prepare();
	}

	/**
	 * Runs the render code.
	 * 
	 * @param auxs
	 *            Auxiliary FBOs
	 * @param pipe
	 *            Rendering Pipline
	 */
	public abstract void render(ImagePassFBO[] auxs, RenderingPipeline pipe);

	/**
	 * End the render, disables shader and ends FBO.
	 */
	public void end() {
		shader.stop();
		fbo.end();
	}

	/**
	 * Dispose shader and FBO
	 */
	public void dispose() {
		shader.cleanUp();
		fbo.cleanUp();
	}

	public ImagePassFBO getFbo() {
		return fbo;
	}

}
