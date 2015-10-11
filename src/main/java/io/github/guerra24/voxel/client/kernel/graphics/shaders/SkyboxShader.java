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

package io.github.guerra24.voxel.client.kernel.graphics.shaders;

import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;

/**
 * Skybox Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class SkyboxShader extends ShaderProgram {
	/**
	 * Skybox Shader Data
	 */
	private int loc_projectionMatrix;
	private int loc_viewMatrix;
	private int loc_fogColour;
	private int loc_cubeMap;
	private int loc_cubeMap2;
	private int loc_blendFactor;
	private float rotation = 0;

	/**
	 * Constructor, Create a Skybox Shader
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public SkyboxShader() {
		super(KernelConstants.VERTEX_FILE_SKYBOX, KernelConstants.FRAGMENT_FILE_SKYBOX);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_fogColour = super.getUniformLocation("fogColour");
		loc_blendFactor = super.getUniformLocation("blendFactor");
		loc_cubeMap = super.getUniformLocation("cubeMap");
		loc_cubeMap2 = super.getUniformLocation("cubeMap2");
	}

	/**
	 * Loads a Projection Matrix
	 * 
	 * @param matrix
	 *            Projection Matrix
	 */
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(loc_projectionMatrix, matrix);
	}

	/**
	 * Loads View Matrix
	 * 
	 * @param camera
	 *            Camera
	 * @param delta
	 *            Delta
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadViewMatrix(Camera camera, float delta) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += KernelConstants.ROTATE_SPEED * delta;
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), matrix, matrix);
		super.loadMatrix(loc_viewMatrix, matrix);
	}

	/**
	 * Loads Fog Color
	 * 
	 * @param r
	 *            Red Value
	 * @param g
	 *            Green Value
	 * @param b
	 *            Blue Value
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadFog(float r, float g, float b) {
		super.loadVector(loc_fogColour, new Vector3f(r, g, b));
	}

	/**
	 * Loads Textures ID
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void connectTextureUnits() {
		super.loadInt(loc_cubeMap, 0);
		super.loadInt(loc_cubeMap2, 1);
	}

	/**
	 * Loads Blend Factor
	 * 
	 * @param blend
	 *            Value
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadBlendFactor(float blend) {
		super.loadFloat(loc_blendFactor, blend);
	}

}
