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

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE6;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glBlitFramebuffer;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.ClientWorldSimulation;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.CubeMapTexture;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.DeferredShadingShader;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.core.TaskManager;

/**
 * 
 * This class is responsible for processing and display objects that have been
 * previously rendered. It may contain phases where certain effects are applied
 * to the image created using {@link ImagePass}.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 *
 */
public abstract class RenderingPipeline implements IRenderingPipeline {

	protected RenderingPipelineFBO mainFBO;
	protected int width, height;
	protected List<ImagePass> imagePasses;
	private Matrix4d previousViewMatrix;
	private Vector3d previousCameraPosition;
	private RawModel quad;
	private ImagePassFBO[] auxs;
	private DeferredShadingShader finalShader;
	private String name;

	public RenderingPipeline(String name) {
		this.name = name;
		Logger.log("Using " + name + " Rendering Pipeline");
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		width = (int) (window.getWidth() * window.getPixelRatio());
		height = (int) (window.getHeight() * window.getPixelRatio());

		if (width > GLUtil.getTextureMaxSize())
			width = GLUtil.getTextureMaxSize();
		if (height > GLUtil.getTextureMaxSize())
			height = GLUtil.getTextureMaxSize();
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = window.getResourceLoader().loadToVAO(positions, 2);

		mainFBO = new RenderingPipelineFBO(width, height);
		imagePasses = new ArrayList<>();
		auxs = new ImagePassFBO[3];

		previousCameraPosition = new Vector3d();
		previousViewMatrix = new Matrix4d();
		finalShader = new DeferredShadingShader("Final");
		finalShader.start();
		finalShader.loadTransformation(Maths.createTransformationMatrix(new Vector2d(0, 0), new Vector2d(1, 1)));
		finalShader.loadResolution(new Vector2d(window.getWidth(), window.getHeight()));
		finalShader.loadSkyColor(ClientVariables.skyColor);
		finalShader.stop();
		init();
		for (ImagePass imagePass : imagePasses) {
			TaskManager.addTask(() -> imagePass.init());
		}
	}

	/**
	 * 
	 * Creates the {@link RenderingPipelineFBO} and initializes variables.
	 * 
	 * @param width
	 *            Final UIImage Width, can be higher that the window width.
	 * @param height
	 *            Final UIImage Height, can be higher that the window Height.
	 */
	public RenderingPipeline(String name, int width, int height) throws Exception {
		this.name = name;
		Logger.log("Using " + name + " Rendering Pipeline");
		Window window = ClientInternalSubsystem.getInstance().getGameWindow();

		if (width > GLUtil.getTextureMaxSize())
			width = GLUtil.getTextureMaxSize();
		if (height > GLUtil.getTextureMaxSize())
			height = GLUtil.getTextureMaxSize();
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = window.getResourceLoader().loadToVAO(positions, 2);

		mainFBO = new RenderingPipelineFBO(width, height);
		imagePasses = new ArrayList<>();
		auxs = new ImagePassFBO[3];

		previousCameraPosition = new Vector3d();
		previousViewMatrix = new Matrix4d();
		finalShader = new DeferredShadingShader("Final");
		finalShader.start();
		finalShader.loadTransformation(Maths.createTransformationMatrix(new Vector2d(0, 0), new Vector2d(1, 1)));
		finalShader.loadResolution(new Vector2d(window.getWidth(), window.getHeight()));
		finalShader.loadSkyColor(ClientVariables.skyColor);
		finalShader.stop();
		this.width = width;
		this.height = height;
		init();
		for (ImagePass imagePass : imagePasses) {
			TaskManager.addTask(() -> imagePass.init());
		}
	}

	/**
	 * Begin Rendering
	 */
	@Override
	public void begin() {
		mainFBO.begin();
	}

	/**
	 * End rendering
	 */
	@Override
	public void end() {
		mainFBO.end();
	}

	/**
	 * 
	 * This is used for processing all the stuff
	 * 
	 * @param gm
	 *            {@link ClientInternalSubsystem}
	 */
	public void render(Camera camera, Vector3d lightPosition, Vector3d invertedLightPosition,
			ClientWorldSimulation clientWorldSimulation, List<Light> lights, CubeMapTexture environmentMap,
			float exposure) {
		for (ImagePass imagePass : imagePasses) {
			imagePass.process(camera, previousViewMatrix, previousCameraPosition, lightPosition, invertedLightPosition,
					clientWorldSimulation, lights, auxs, this, quad, environmentMap, exposure);
		}
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		finalShader.start();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, auxs[0].getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		finalShader.stop();

		glBindFramebuffer(GL_READ_FRAMEBUFFER, mainFBO.getFbo());
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL_DEPTH_BUFFER_BIT, GL_LINEAR);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		previousViewMatrix = Maths.createViewMatrix(camera);
		previousCameraPosition = camera.getPosition();
	}

	/**
	 * Internal Dispose
	 */
	@Override
	public void dispose() {
		mainFBO.cleanUp();
		for (ImagePass imagePass : imagePasses) {
			imagePass.dispose();
		}
		finalShader.cleanUp();
	}

	public RenderingPipelineFBO getMainFBO() {
		return mainFBO;
	}

	public String getName() {
		return name;
	}

}
