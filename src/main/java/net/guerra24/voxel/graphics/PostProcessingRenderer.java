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

package net.guerra24.voxel.graphics;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import net.guerra24.voxel.graphics.opengl.Display;
import net.guerra24.voxel.graphics.shaders.PostProcessingShader;
import net.guerra24.voxel.resources.GameResources;
import net.guerra24.voxel.resources.Loader;
import net.guerra24.voxel.resources.models.RawModel;
import net.guerra24.voxel.util.Maths;
import net.guerra24.voxel.util.vector.Matrix4f;
import net.guerra24.voxel.util.vector.Vector2f;

public class PostProcessingRenderer {

	/**
	 * post Processing Data
	 */
	private PostProcessingShader shader;
	private FrameBuffer post_fbo;
	private final RawModel quad;

	/**
	 * Constructor
	 * 
	 * @param loader
	 *            Loader
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public PostProcessingRenderer(Loader loader) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(positions, 2);
		shader = new PostProcessingShader();
		shader.start();
		Matrix4f matrix = Maths.createTransformationMatrix(new Vector2f(0, 0), new Vector2f(1, 1));
		shader.loadTransformation(matrix);
		shader.connectTextureUnits();
		shader.stop();
		post_fbo = new FrameBuffer(false, true, Display.getWidth(), Display.getHeight());
	}

	/**
	 * Render the Post Processing Quad
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void render(GameResources gm) {
		shader.start();
		shader.loadResolution(new Vector2f(Display.getWidth(), Display.getHeight()));
		shader.loadUnderWater(gm.getCamera().isUnderWater());
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, post_fbo.getTexture());
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
	}

	/**
	 * Post Processing FBO
	 * 
	 * @return FrameBuffer
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public FrameBuffer getPost_fbo() {
		return post_fbo;
	}

}
