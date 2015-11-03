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

package net.guerra24.voxel.graphics.shaders;

import net.guerra24.voxel.core.VoxelVariables;
import net.guerra24.voxel.util.vector.Matrix4f;

/**
 * Gui Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class GuiShader extends ShaderProgram {

	/**
	 * Gui Shader Data
	 */
	private int loc_transformationMatrix;

	/**
	 * Constructor, Creastes an GUI Shader
	 */
	public GuiShader() {
		super(VoxelVariables.VERTEX_FILE_GUI, VoxelVariables.FRAGMENT_FILE_GUI);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	/**
	 * Loads Transformation Matrix to the shader
	 * 
	 * @param matrix
	 *            Transformation Matrix
	 */
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(loc_transformationMatrix, matrix);
	}

}
