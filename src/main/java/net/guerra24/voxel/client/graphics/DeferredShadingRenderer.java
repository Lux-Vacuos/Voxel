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

package net.guerra24.voxel.client.graphics;

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
import static org.lwjgl.opengl.GL13.GL_TEXTURE8;
import static org.lwjgl.opengl.GL13.GL_TEXTURE9;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.GL_DRAW_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.GL_READ_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glBlitFramebuffer;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.graphics.shaders.DeferredShadingShader;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.Loader;
import net.guerra24.voxel.client.resources.models.RawModel;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

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
	private final DeferredShadingShader shaderFinal0;
	private final DeferredShadingFBO postProcessingFBO;
	private final FrameBuffer aux0FBO;
	private final FrameBuffer aux1FBO;
	private final FrameBuffer aux2FBO;
	private final FrameBuffer aux3FBO;
	private final FrameBuffer aux4FBO;
	private final FrameBuffer auxFinal0FBO;
	private final RawModel quad;

	private Matrix4f previousViewMatrix;
	private Vector3f previousCameraPosition;

	private Vector3f skyColor = new Vector3f(VoxelVariables.RED, VoxelVariables.GREEN, VoxelVariables.BLUE);

	/**
	 * Constructor
	 * 
	 * @param loader
	 *            Loader
	 */
	public DeferredShadingRenderer(Loader loader, GameResources gm) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(positions, 2);
		shader0 = new DeferredShadingShader("0");
		shader0.start();
		shader0.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader0.connectTextureUnits();
		shader0.loadSkyColor(skyColor);
		shader0.stop();
		shader1 = new DeferredShadingShader("1");
		shader1.start();
		shader1.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader1.connectTextureUnits();
		shader1.loadSkyColor(skyColor);
		shader1.stop();
		shader2 = new DeferredShadingShader("2");
		shader2.start();
		shader2.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader2.connectTextureUnits();
		shader2.loadSkyColor(skyColor);
		shader2.stop();
		shader3 = new DeferredShadingShader("3");
		shader3.start();
		shader3.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader3.connectTextureUnits();
		shader3.loadSkyColor(skyColor);
		shader3.stop();
		shader4 = new DeferredShadingShader("4");
		shader4.start();
		shader4.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader4.connectTextureUnits();
		shader4.loadSkyColor(skyColor);
		shader4.stop();
		shader5 = new DeferredShadingShader("5");
		shader5.start();
		shader5.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shader5.connectTextureUnits();
		shader5.loadSkyColor(skyColor);
		shader5.stop();
		shaderFinal0 = new DeferredShadingShader("Final0");
		shaderFinal0.start();
		shaderFinal0.loadTransformation(Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1)));
		shaderFinal0.connectTextureUnits();
		shaderFinal0.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		shaderFinal0.loadSkyColor(skyColor);
		shaderFinal0.stop();
		aux0FBO = new FrameBuffer(false, Display.getWidth(), Display.getHeight());
		aux1FBO = new FrameBuffer(false, Display.getWidth(), Display.getHeight());
		aux2FBO = new FrameBuffer(false, Display.getWidth(), Display.getHeight());
		aux3FBO = new FrameBuffer(false, Display.getWidth(), Display.getHeight());
		aux4FBO = new FrameBuffer(false, Display.getWidth(), Display.getHeight());
		auxFinal0FBO = new FrameBuffer(false, Display.getWidth(), Display.getHeight());
		postProcessingFBO = new DeferredShadingFBO(Display.getWidth(), Display.getHeight());
		previousViewMatrix = Maths.createViewMatrix(gm.getCamera());
		previousCameraPosition = gm.getCamera().getPosition();
	}

	/**
	 * Render the Post Processing Quad
	 * 
	 */
	public void render(GameResources gm) {
		aux4FBO.begin(Display.getWidth(), Display.getHeight());
		gm.getRenderer().prepare();
		shader5.start();
		shader5.loadSettings();
		shader5.loadUnderWater(gm.getCamera().isUnderWater());
		shader5.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), gm.getCamera(), previousViewMatrix,
				previousCameraPosition);
		shader5.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		shader5.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader5.loadviewMatrix(gm.getCamera());
		shader5.loadSunPosition(
				Maths.convertTo2F(new Vector3f(gm.getLightPos()), gm.getRenderer().getProjectionMatrix(),
						Maths.createViewMatrix(gm.getCamera()), Display.getWidth(), Display.getHeight()));
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
		shader5.stop();
		aux4FBO.end();

		aux3FBO.begin(Display.getWidth(), Display.getHeight());
		gm.getRenderer().prepare();
		shader4.start();
		shader4.loadUnderWater(gm.getCamera().isUnderWater());
		shader4.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), gm.getCamera(), previousViewMatrix,
				previousCameraPosition);
		shader4.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader4.loadviewMatrix(gm.getCamera());
		shader4.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		shader4.loadSettings();
		shader4.loadSunPosition(
				Maths.convertTo2F(new Vector3f(gm.getLightPos()), gm.getRenderer().getProjectionMatrix(),
						Maths.createViewMatrix(gm.getCamera()), Display.getWidth(), Display.getHeight()));
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
		shader4.stop();
		aux3FBO.end();

		aux2FBO.begin(Display.getWidth(), Display.getHeight());
		gm.getRenderer().prepare();
		shader3.start();
		shader3.loadUnderWater(gm.getCamera().isUnderWater());
		shader3.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), gm.getCamera(), previousViewMatrix,
				previousCameraPosition);
		shader3.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader3.loadviewMatrix(gm.getCamera());
		shader3.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		shader3.loadSettings();
		shader3.loadSunPosition(
				Maths.convertTo2F(new Vector3f(gm.getLightPos()), gm.getRenderer().getProjectionMatrix(),
						Maths.createViewMatrix(gm.getCamera()), Display.getWidth(), Display.getHeight()));
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
		shader3.stop();
		aux2FBO.end();

		aux1FBO.begin(Display.getWidth(), Display.getHeight());
		gm.getRenderer().prepare();
		shader2.start();
		shader2.loadUnderWater(gm.getCamera().isUnderWater());
		shader2.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), gm.getCamera(), previousViewMatrix,
				previousCameraPosition);
		shader2.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader2.loadviewMatrix(gm.getCamera());
		shader2.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		shader2.loadSettings();
		shader2.loadSunPosition(
				Maths.convertTo2F(new Vector3f(gm.getLightPos()), gm.getRenderer().getProjectionMatrix(),
						Maths.createViewMatrix(gm.getCamera()), Display.getWidth(), Display.getHeight()));
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
		glActiveTexture(GL_TEXTURE7);
		glBindTexture(GL_TEXTURE_2D, aux3FBO.getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader2.stop();
		aux1FBO.end();

		aux0FBO.begin(Display.getWidth(), Display.getHeight());
		gm.getRenderer().prepare();
		shader1.start();
		shader1.loadSettings();
		shader1.loadUnderWater(gm.getCamera().isUnderWater());
		shader1.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), gm.getCamera(), previousViewMatrix,
				previousCameraPosition);
		shader1.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader1.loadviewMatrix(gm.getCamera());
		shader1.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		shader1.loadSunPosition(
				Maths.convertTo2F(new Vector3f(gm.getLightPos()), gm.getRenderer().getProjectionMatrix(),
						Maths.createViewMatrix(gm.getCamera()), Display.getWidth(), Display.getHeight()));
		shader1.loadSkyBoxBlending(gm.getSkyboxRenderer().getBlendFactor());
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
		glActiveTexture(GL_TEXTURE8);
		glBindTexture(GL_TEXTURE_CUBE_MAP, gm.getSkyboxRenderer().getDayTexture());
		glActiveTexture(GL_TEXTURE9);
		glBindTexture(GL_TEXTURE_CUBE_MAP, gm.getSkyboxRenderer().getNightTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader1.stop();
		aux0FBO.end();

		auxFinal0FBO.begin(Display.getWidth(), Display.getHeight());
		gm.getRenderer().prepare();
		shader0.start();
		shader0.loadSettings();
		shader0.loadUnderWater(gm.getCamera().isUnderWater());
		shader0.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), gm.getCamera(), previousViewMatrix,
				previousCameraPosition);
		shader0.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shader0.loadviewMatrix(gm.getCamera());
		shader0.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		shader0.loadSunPosition(
				Maths.convertTo2F(new Vector3f(gm.getLightPos()), gm.getRenderer().getProjectionMatrix(),
						Maths.createViewMatrix(gm.getCamera()), Display.getWidth(), Display.getHeight()));
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
		auxFinal0FBO.end();

		shaderFinal0.start();
		shaderFinal0.loadUnderWater(gm.getCamera().isUnderWater());
		shaderFinal0.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), gm.getCamera(), previousViewMatrix,
				previousCameraPosition);
		shaderFinal0.loadLightPosition(gm.getLightPos(), gm.getInvertedLightPosition());
		shaderFinal0.loadviewMatrix(gm.getCamera());
		shaderFinal0.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		shaderFinal0.loadSunPosition(
				Maths.convertTo2F(new Vector3f(gm.getLightPos()), gm.getRenderer().getProjectionMatrix(),
						Maths.createViewMatrix(gm.getCamera()), Display.getWidth(), Display.getHeight()));
		shaderFinal0.loadSettings();
		previousViewMatrix = Maths.createViewMatrix(gm.getCamera());
		previousCameraPosition = gm.getCamera().getPosition();
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
		glBlitFramebuffer(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, Display.getWidth(), Display.getHeight(),
				GL_DEPTH_BUFFER_BIT, GL_NEAREST);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
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
		aux0FBO.cleanUp();
		aux1FBO.cleanUp();
		aux2FBO.cleanUp();
		aux3FBO.cleanUp();
		aux4FBO.cleanUp();
		auxFinal0FBO.cleanUp();
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
