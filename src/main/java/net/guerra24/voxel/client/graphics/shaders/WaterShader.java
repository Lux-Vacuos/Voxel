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
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Matrix4f;

/**
 * Water Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class WaterShader extends ShaderProgram {
	/**
	 * Water Shader Data
	 */
	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	private int location_moveFactor;
	/**
	 * Constructor
	 * 
	 */
	public WaterShader() {
		super(VoxelVariables.VERTEX_FILE_WATER, VoxelVariables.FRAGMENT_FILE_WATER);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_moveFactor = getUniformLocation("moveFactor");
	}


	/**
	 * Loads the Move Factor
	 * 
	 * @param factor
	 *            Factor
	 */
	public void loadMoveFactor(float factor) {
		super.loadFloat(location_moveFactor, factor);
	}

	/**
	 * Loads the Projection Matrix
	 * 
	 * @param projection
	 *            Projection Matrix
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}

	/**
	 * Load View Matrix
	 * 
	 * @param camera
	 *            Camera
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
	}

	/**
	 * Load Model Matrix
	 * 
	 * @param modelMatrix
	 *            Model Matrix
	 */
	public void loadModelMatrix(Matrix4f modelMatrix) {
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}