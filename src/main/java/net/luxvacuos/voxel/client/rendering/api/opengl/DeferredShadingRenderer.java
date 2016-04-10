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

import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.GL_TEXTURE3;
import static org.lwjgl.opengl.GL13.GL_TEXTURE4;
import static org.lwjgl.opengl.GL13.GL_TEXTURE5;
import static org.lwjgl.opengl.GL13.GL_TEXTURE6;
import static org.lwjgl.opengl.GL13.GL_TEXTURE7;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glBlitFramebuffer;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Display;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.DeferredShadingShader;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.resources.models.RawModel;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.PlayerCamera;

public class DeferredShadingRenderer {

	/**
	 * post Processing Data
	 */
	private final DeferredShadingShader shader0;
	private final DeferredShadingShader shader1;
	private final DeferredShadingShader shader2;
	private final DeferredShadingShader shader3;
	private final DeferredShadingShader shader4;
	private final DeferredShadingShader shader5;
	private final DeferredShadingShader shader6;
	private final DeferredShadingShader shaderFinal0;
	private final DeferredShadingShader shaderFinal1;
	private final DeferredShadingFBO postProcessingFBO;
	private final FrameBuffer aux0FBO;
	private final FrameBuffer aux1FBO;
	private final FrameBuffer aux2FBO;
	private final FrameBuffer aux3FBO;
	private final FrameBuffer aux4FBO;
	private final FrameBuffer auxFinal0FBO;
	private final FrameBuffer auxFinal1FBO;
	private final RawModel quad;

	private int width, height;

	private Matrix4f previousViewMatrix;
	private Vector3f previousCameraPosition;

	private Vector3f skyColor = new Vector3f(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE);

	private Display display;

	public DeferredShadingRenderer(GameResources gm) throws Exception {
		this.display = gm.getDisplay();
		width = (int) (display.getDisplayWidth() * display.getPixelRatio());
		height = (int) (display.getDisplayHeight() * display.getPixelRatio());

		if (width > GLUtil.getTextureMaxSize())
			width = GLUtil.getTextureMaxSize();
		if (height > GLUtil.getTextureMaxSize())
			height = GLUtil.getTextureMaxSize();
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = gm.getLoader().loadToVAO(positions, 2);
		shader0 = new DeferredShadingShader("0");
		shader0.start();
		shader0.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader0.connectTextureUnits();
		shader0.loadSkyColor(skyColor);
		shader0.loadResolution(new Vector2f(width, height));
		shader0.stop();
		shader1 = new DeferredShadingShader("1");
		shader1.start();
		shader1.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader1.connectTextureUnits();
		shader1.loadSkyColor(skyColor);
		shader1.loadResolution(new Vector2f(width, height));
		shader1.stop();
		shader2 = new DeferredShadingShader("2");
		shader2.start();
		shader2.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader2.connectTextureUnits();
		shader2.loadSkyColor(skyColor);
		shader2.loadResolution(new Vector2f(width, height));
		shader2.stop();
		shader3 = new DeferredShadingShader("3");
		shader3.start();
		shader3.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader3.connectTextureUnits();
		shader3.loadSkyColor(skyColor);
		shader3.loadResolution(new Vector2f(width, height));
		shader3.stop();
		shader4 = new DeferredShadingShader("4");
		shader4.start();
		shader4.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader4.connectTextureUnits();
		shader4.loadSkyColor(skyColor);
		shader4.loadResolution(new Vector2f(width, height));
		shader4.stop();
		shader5 = new DeferredShadingShader("5");
		shader5.start();
		shader5.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader5.connectTextureUnits();
		shader5.loadResolution(new Vector2f(width, height));
		shader5.loadSkyColor(skyColor);
		shader5.stop();
		shader6 = new DeferredShadingShader("6");
		shader6.start();
		shader6.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader6.connectTextureUnits();
		shader6.loadResolution(new Vector2f(width, height));
		shader6.loadSkyColor(skyColor);
		shader6.stop();
		shaderFinal0 = new DeferredShadingShader("Final0");
		shaderFinal0.start();
		shaderFinal0.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shaderFinal0.connectTextureUnits();
		shaderFinal0.loadResolution(new Vector2f(width, height));
		shaderFinal0.loadSkyColor(skyColor);
		shaderFinal0.stop();
		shaderFinal1 = new DeferredShadingShader("Final1");
		shaderFinal1.start();
		shaderFinal1.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shaderFinal1.connectTextureUnits();
		shaderFinal1.loadResolution(new Vector2f(width, height));
		shaderFinal1.loadSkyColor(skyColor);
		shaderFinal1.stop();
		aux0FBO = new FrameBuffer(false, width, height, display);
		aux1FBO = new FrameBuffer(false, width, height, display);
		aux2FBO = new FrameBuffer(false, width, height, display);
		aux3FBO = new FrameBuffer(false, width, height, display);
		aux4FBO = new FrameBuffer(false, width, height, display);
		auxFinal0FBO = new FrameBuffer(false, width, height, display);
		auxFinal1FBO = new FrameBuffer(false, width, height, display);
		postProcessingFBO = new DeferredShadingFBO(width, height);
		previousViewMatrix = new Matrix4f();
		previousCameraPosition = new Vector3f();
	}

	/**
	 * Render the Post Processing Quad
	 * 
	 */
	public void render(GameResources gm) {
		aux4FBO.begin(width, height);
		gm.getRenderer().prepare();
		shader6.start();
		shader6.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useVolumetricLight, VoxelVariables.useReflections);
		shader6.loadUnderWater(((PlayerCamera) ((PlayerCamera) gm.getCamera())).isUnderWater());
		shader6.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shader6.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader6.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shader6.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()),
				gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(gm.getCamera()), width, height));
		shader6.loadTime(gm.getWorldSimulation().getTime());
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDiffuseTex());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getPositionTex());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getNormalTex());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDepthTex());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData0Tex());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData1Tex());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader6.stop();
		aux4FBO.end(display);

		aux3FBO.begin(width, height);
		gm.getRenderer().prepare();
		shader5.start();
		shader5.loadUnderWater(((PlayerCamera) gm.getCamera()).isUnderWater());
		shader5.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shader5.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader5.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shader5.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useVolumetricLight, VoxelVariables.useReflections);
		shader5.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()),
				gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(gm.getCamera()), width, height));
		shader5.loadTime(gm.getWorldSimulation().getTime());
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDiffuseTex());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getPositionTex());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getNormalTex());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDepthTex());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData0Tex());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData1Tex());
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, aux4FBO.getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader5.stop();
		aux3FBO.end(display);

		aux2FBO.begin(width, height);
		gm.getRenderer().prepare();
		shader4.start();
		shader4.loadUnderWater(((PlayerCamera) gm.getCamera()).isUnderWater());
		shader4.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shader4.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader4.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shader4.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useVolumetricLight, VoxelVariables.useReflections);
		shader4.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()),
				gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(gm.getCamera()), width, height));
		shader4.loadTime(gm.getWorldSimulation().getTime());
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDiffuseTex());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getPositionTex());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getNormalTex());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDepthTex());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData0Tex());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData1Tex());
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, aux3FBO.getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader4.stop();
		aux2FBO.end(display);

		aux4FBO.begin(width, height);
		gm.getRenderer().prepare();
		shader3.start();
		shader3.loadUnderWater(((PlayerCamera) gm.getCamera()).isUnderWater());
		shader3.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shader3.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader3.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shader3.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useVolumetricLight, VoxelVariables.useReflections);
		shader3.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()),
				gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(gm.getCamera()), width, height));
		shader3.loadTime(gm.getWorldSimulation().getTime());
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDiffuseTex());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getPositionTex());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getNormalTex());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDepthTex());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData0Tex());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData1Tex());
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, aux2FBO.getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader3.stop();
		aux4FBO.end(display);

		aux1FBO.begin(width, height);
		gm.getRenderer().prepare();
		shader2.start();
		shader2.loadUnderWater(((PlayerCamera) gm.getCamera()).isUnderWater());
		shader2.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shader2.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader2.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shader2.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useVolumetricLight, VoxelVariables.useReflections);
		shader2.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()),
				gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(gm.getCamera()), width, height));
		shader2.loadExposure(4f);
		shader2.loadTime(gm.getWorldSimulation().getTime());
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDiffuseTex());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getPositionTex());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getNormalTex());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDepthTex());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData0Tex());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData1Tex());
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, aux4FBO.getTexture());
		glActiveTexture(GL_TEXTURE7);
		glBindTexture(GL_TEXTURE_2D, aux3FBO.getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader2.stop();
		aux1FBO.end(display);

		aux0FBO.begin(width, height);
		gm.getRenderer().prepare();
		shader1.start();
		shader1.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useVolumetricLight, VoxelVariables.useReflections);
		shader1.loadUnderWater(((PlayerCamera) gm.getCamera()).isUnderWater());
		shader1.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shader1.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader1.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shader1.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()),
				gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(gm.getCamera()), width, height));
		shader1.loadTime(gm.getWorldSimulation().getTime());
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDiffuseTex());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getPositionTex());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getNormalTex());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDepthTex());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData0Tex());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData1Tex());
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, aux1FBO.getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader1.stop();
		aux0FBO.end(display);

		auxFinal1FBO.begin(width, height);
		gm.getRenderer().prepare();
		shader0.start();
		shader0.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useVolumetricLight, VoxelVariables.useReflections);
		shader0.loadUnderWater(((PlayerCamera) gm.getCamera()).isUnderWater());
		shader0.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shader0.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader0.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shader0.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()),
				gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(gm.getCamera()), width, height));
		shader0.loadTime(gm.getWorldSimulation().getTime());
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDiffuseTex());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getPositionTex());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getNormalTex());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDepthTex());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData0Tex());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData1Tex());
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, aux0FBO.getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader0.stop();
		auxFinal1FBO.end(display);

		auxFinal0FBO.begin(width, height);
		gm.getRenderer().prepare();
		shaderFinal1.start();
		shaderFinal1.loadUnderWater(((PlayerCamera) gm.getCamera()).isUnderWater());
		shaderFinal1.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shaderFinal1.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shaderFinal1.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shaderFinal1.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()),
				gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(gm.getCamera()), width, height));
		shaderFinal1.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useVolumetricLight, VoxelVariables.useReflections);
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDiffuseTex());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getPositionTex());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getNormalTex());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDepthTex());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData0Tex());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData1Tex());
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, auxFinal1FBO.getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shaderFinal1.stop();
		auxFinal0FBO.end(display);

		gm.getRenderer().prepare();
		shaderFinal0.start();
		shaderFinal0.loadUnderWater(((PlayerCamera) gm.getCamera()).isUnderWater());
		shaderFinal0.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), ((PlayerCamera) gm.getCamera()),
				previousViewMatrix, previousCameraPosition);
		shaderFinal0.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shaderFinal0.loadviewMatrix(((PlayerCamera) gm.getCamera()));
		shaderFinal0.loadSunPosition(Maths.convertTo2F(new Vector3f(gm.getLightPos()),
				gm.getRenderer().getProjectionMatrix(), Maths.createViewMatrix(gm.getCamera()), width, height));
		shaderFinal0.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useVolumetricLight, VoxelVariables.useReflections);
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDiffuseTex());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getPositionTex());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getNormalTex());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getDepthTex());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData0Tex());
		glActiveTexture(GL_TEXTURE5);
		glBindTexture(GL_TEXTURE_2D, postProcessingFBO.getData1Tex());
		glActiveTexture(GL_TEXTURE6);
		glBindTexture(GL_TEXTURE_2D, auxFinal0FBO.getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shaderFinal0.stop();

		glBindFramebuffer(GL_READ_FRAMEBUFFER, postProcessingFBO.getFbo());
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
		glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);

		previousViewMatrix = Maths.createViewMatrix(gm.getCamera());
		previousCameraPosition = ((PlayerCamera) gm.getCamera()).getPosition();
	}

	/**
	 * Clear shader
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void cleanUp() {
		shader0.cleanUp();
		shader1.cleanUp();
		shader2.cleanUp();
		shader3.cleanUp();
		shader4.cleanUp();
		shader5.cleanUp();
		shaderFinal0.cleanUp();
		shaderFinal1.cleanUp();
		aux0FBO.cleanUp();
		aux1FBO.cleanUp();
		aux2FBO.cleanUp();
		aux3FBO.cleanUp();
		aux4FBO.cleanUp();
		auxFinal0FBO.cleanUp();
		auxFinal1FBO.cleanUp();
		postProcessingFBO.cleanUp();
	}

	/**
	 * Post Processing FBO
	 * 
	 * @return FrameBuffer
	 */
	public DeferredShadingFBO getPost_fbo() {
		return postProcessingFBO;
	}

}
