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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.graphics.opengl.Display;
import net.guerra24.voxel.client.graphics.shaders.PostProcessingShader;
import net.guerra24.voxel.client.resources.GameResources;
import net.guerra24.voxel.client.resources.Loader;
import net.guerra24.voxel.client.resources.models.RawModel;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector2f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

public class PostProcessingRenderer {

	/**
	 * post Processing Data
	 */
	private PostProcessingShader shader;
	private FrameBuffer post_fbo;
	private FrameBuffer depth_fbo;
	private final RawModel quad;

	private Matrix4f previousViewMatrix;
	private Vector3f previousCameraPosition;

	/**
	 * Constructor
	 * 
	 * @param loader
	 *            Loader
	 */
	public PostProcessingRenderer(Loader loader, GameResources gm) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(positions, 2);
		shader = new PostProcessingShader();
		shader.start();
		Matrix4f matrix = Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1));
		shader.loadTransformation(matrix);
		shader.connectTextureUnits();
		shader.stop();
		post_fbo = new FrameBuffer(false, true, Display.getWidth(), Display.getHeight());
		depth_fbo = new FrameBuffer(true, false, Display.getWidth(), Display.getHeight());
		previousViewMatrix = Maths.createViewMatrix(gm.getCamera());
		previousCameraPosition = gm.getCamera().getPosition();
	}

	/**
	 * Render the Post Processing Quad
	 * 
	 */
	public void render(GameResources gm) {
		glBindFramebuffer(GL_READ_FRAMEBUFFER, post_fbo.getRenderBuffer());
		glBindFramebuffer(GL_DRAW_FRAMEBUFFER, depth_fbo.getFrameBuffer());
		glBlitFramebuffer(0, 0, Display.getWidth(), Display.getHeight(), 0, 0, Display.getWidth(), Display.getHeight(),
				GL_DEPTH_BUFFER_BIT, GL_NEAREST);
		glBindFramebuffer(GL_FRAMEBUFFER, 0);
		shader.start();
		shader.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		shader.loadUnderWater(gm.getCamera().isUnderWater());
		shader.loadSettings(VoxelVariables.useDOF, VoxelVariables.useFXAA, VoxelVariables.useMotionBlur,
				VoxelVariables.useBloom);
		shader.loadMotionBlurData(gm.getRenderer().getProjectionMatrix(), gm.getCamera(), previousViewMatrix,
				previousCameraPosition);
		previousViewMatrix = Maths.createViewMatrix(gm.getCamera());
		previousCameraPosition = gm.getCamera().getPosition();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, post_fbo.getTexture());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, depth_fbo.getTexture());
		glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
	}

	/**
	 * Clear shader
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void cleanUp() {
		shader.cleanUp();
		depth_fbo.cleanUp();
		post_fbo.cleanUp();
	}

	/**
	 * Post Processing FBO
	 * 
	 * @return FrameBuffer
	 */
	public FrameBuffer getPost_fbo() {
		return post_fbo;
	}

}
