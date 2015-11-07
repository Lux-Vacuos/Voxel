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

package net.guerra24.voxel.client.graphics.shaders;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector2f;

/**
 * Post Processing Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class PostProcessingShader extends ShaderProgram {

	/**
	 * Post Processing Shadaer Data
	 */
	private int loc_transformationMatrix;
	private int loc_camUnderWater;
	private int loc_camUnderWaterOffset;
	private int loc_resolution;
	private int loc_texture0;
	private int loc_texture1;
	private int loc_depth0;

	private float time;

	/**
	 * Constructor
	 * 
	 */
	public PostProcessingShader() {
		super(VoxelVariables.VERTEX_FILE_POST, VoxelVariables.FRAGMENT_FILE_POST);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_camUnderWater = super.getUniformLocation("camUnderWater");
		loc_camUnderWaterOffset = super.getUniformLocation("camUnderWaterOffset");
		loc_resolution = super.getUniformLocation("resolution");
		loc_texture0 = super.getUniformLocation("texture0");
		loc_texture1 = super.getUniformLocation("texture1");
		loc_depth0 = super.getUniformLocation("depth0");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	/**
	 * Loads Textures ID
	 * 
	 */
	public void connectTextureUnits() {
		super.loadInt(loc_texture0, 0);
		super.loadInt(loc_texture1, 1);
		super.loadInt(loc_depth0, 2);
	}

	public void loadUnderWater(boolean value) {
		super.loadBoolean(loc_camUnderWater, value);
		super.loadFloat(loc_camUnderWaterOffset, time += 0.1f);
	}

	/**
	 * Load Display Resolution
	 * 
	 * @param res
	 *            Resolution as Vector2f
	 */
	public void loadResolution(Vector2f res) {
		super.load2DVector(loc_resolution, res);
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
