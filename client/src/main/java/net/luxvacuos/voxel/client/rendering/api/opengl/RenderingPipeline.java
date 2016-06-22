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

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
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
import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.DeferredShadingShader;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.RawModel;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;

/**
 * 
 * This class is responsible for processing and display objects that have been
 * previously rendered. It may contain phases where certain effects are applied
 * to the image created using {@link ImagePass}. This class is used as a
 * replacement {@link DeferredShadingRenderer} since the former has big problems
 * of organization and is very poorly tuned besides that can not change the
 * rendering pipeline.
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 *
 */
public abstract class RenderingPipeline {

	protected RenderingPipelineFBO mainFBO;
	protected int width, height;
	protected List<ImagePass> imagePasses;
	private Matrix4f previousViewMatrix;
	private Vector3f previousCameraPosition;
	private RawModel quad;
	private ImagePassFBO[] auxs;
	private DeferredShadingShader finalShader;
	private String name;

	public RenderingPipeline(String name) {
		this.name = name;
		Logger.log("Using " + name + " Rendering Pipeline");
		GameResources gm = GameResources.getInstance();

		width = (int) (gm.getDisplay().getDisplayWidth() * gm.getDisplay().getPixelRatio());
		height = (int) (gm.getDisplay().getDisplayHeight() * gm.getDisplay().getPixelRatio());

		if (width > GLUtil.getTextureMaxSize())
			width = GLUtil.getTextureMaxSize();
		if (height > GLUtil.getTextureMaxSize())
			height = GLUtil.getTextureMaxSize();
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = gm.getLoader().loadToVAO(positions, 2);

		mainFBO = new RenderingPipelineFBO(width, height);
		imagePasses = new ArrayList<>();
		auxs = new ImagePassFBO[3];

		for (int i = 0; i < auxs.length; i++) {
			auxs[i] = new ImagePassFBO(width, height);
		}
		previousCameraPosition = new Vector3f();
		previousViewMatrix = new Matrix4f();
		finalShader = new DeferredShadingShader("Final");
		finalShader.start();
		finalShader.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		finalShader.connectTextureUnits();
		finalShader.loadResolution(new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth(),
				GameResources.getInstance().getDisplay().getDisplayHeight()));
		finalShader.loadSkyColor(VoxelVariables.skyColor);
		finalShader.stop();
		init(gm);
	}

	/**
	 * 
	 * Creates the {@link RenderingPipelineFBO} and initializes variables.
	 * 
	 * @param width
	 *            Final Image Width, can be higher that the window width.
	 * @param height
	 *            Final Image Height, can be higher that the window Height.
	 */
	public RenderingPipeline(String name, int width, int height) throws Exception {
		this.name = name;
		Logger.log("Using " + name + " Rendering Pipeline");
		GameResources gm = GameResources.getInstance();

		if (width > GLUtil.getTextureMaxSize())
			width = GLUtil.getTextureMaxSize();
		if (height > GLUtil.getTextureMaxSize())
			height = GLUtil.getTextureMaxSize();
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = gm.getLoader().loadToVAO(positions, 2);

		mainFBO = new RenderingPipelineFBO(width, height);
		imagePasses = new ArrayList<>();
		auxs = new ImagePassFBO[3];

		for (int i = 0; i < auxs.length; i++) {
			auxs[i] = new ImagePassFBO(width, height);
		}
		previousCameraPosition = new Vector3f();
		previousViewMatrix = new Matrix4f();
		finalShader = new DeferredShadingShader("Final");
		finalShader.start();
		finalShader.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		finalShader.connectTextureUnits();
		finalShader.loadResolution(new Vector2f(GameResources.getInstance().getDisplay().getDisplayWidth(),
				GameResources.getInstance().getDisplay().getDisplayHeight()));
		finalShader.loadSkyColor(VoxelVariables.skyColor);
		finalShader.stop();
		this.width = width;
		this.height = height;
		init(gm);
	}

	/**
	 * 
	 * Initialize custom objects and variables
	 * 
	 * @param gm
	 *            {@link GameResources}
	 */
	public abstract void init(GameResources gm);

	/**
	 * Begin Rendering
	 */
	public void begin() {
		glDisable(GL_BLEND);

		mainFBO.begin();
	}

	/**
	 * End rendering
	 */
	public void end() {
		mainFBO.end();
		glEnable(GL_BLEND);
	}

	/**
	 * 
	 * This is used for processing all the stuff
	 * 
	 * @param gm
	 *            {@link GameResources}
	 */
	public void render(GameResources gm) {
		for (ImagePass imagePass : imagePasses) {
			imagePass.begin(gm, previousViewMatrix, previousCameraPosition);
			gm.getRenderer().prepare();
			glBindVertexArray(quad.getVaoID());
			glEnableVertexAttribArray(0);
			imagePass.render(auxs, this);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			glDisableVertexAttribArray(0);
			glBindVertexArray(0);
			imagePass.end();
			auxs[0] = imagePass.getFbo();
		}
		gm.getRenderer().prepare();
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
		glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		previousViewMatrix = Maths.createViewMatrix(gm.getCamera());
		previousCameraPosition = ((PlayerCamera) gm.getCamera()).getPosition();
	}

	/**
	 * Internal Dispose
	 */
	public void disposeI() {
		mainFBO.cleanUp();
		for (ImagePassFBO imagePassFBO : auxs) {
			imagePassFBO.cleanUp();
		}
		for (ImagePass imagePass : imagePasses) {
			imagePass.dispose();
		}
		finalShader.cleanUp();
		dispose();
	}

	/**
	 *
	 * Here are disposed all custom objects
	 * 
	 */
	public abstract void dispose();

	public RenderingPipelineFBO getMainFBO() {
		return mainFBO;
	}

	public String getName() {
		return name;
	}

}
