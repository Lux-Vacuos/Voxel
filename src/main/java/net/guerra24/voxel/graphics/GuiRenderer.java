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

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.List;

import net.guerra24.voxel.graphics.shaders.GuiShader;
import net.guerra24.voxel.resources.Loader;
import net.guerra24.voxel.resources.models.GuiTexture;
import net.guerra24.voxel.resources.models.RawModel;
import net.guerra24.voxel.util.Maths;
import net.guerra24.voxel.util.vector.Matrix4f;

/**
 * Gui Rendering
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class GuiRenderer {

	/**
	 * Gui Data
	 */
	private final RawModel quad;
	private GuiShader shader;

	/**
	 * Constructor, initialize the basic RawModel and the Shader
	 * 
	 * @param loader
	 *            Game Loader
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public GuiRenderer(Loader loader) {
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = loader.loadToVAO(positions, 2);
		shader = new GuiShader();
	}

	/**
	 * Renders the Guis in the List
	 * 
	 * @param guis
	 *            A list of Guis
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void renderGui(List<GuiTexture> guis) {
		glEnable(GL_BLEND);
		shader.start();
		glBindVertexArray(quad.getVaoID());
		glEnableVertexAttribArray(0);
		for (GuiTexture gui : guis) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, gui.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
			shader.loadTransformation(matrix);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
		}
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		shader.stop();
		glDisable(GL_BLEND);
	}

	/**
	 * Clear the Shader
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void cleanUp() {
		shader.cleanUp();
	}
}
