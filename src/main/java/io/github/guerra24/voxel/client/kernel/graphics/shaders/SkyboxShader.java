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
import io.github.guerra24.voxel.client.kernel.graphics.opengl.DisplayManager;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;

/**
 * Skybox Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.3 Build-60
 * @since 0.0.1 Build-52
 * @category Rendering
 */
public class SkyboxShader extends ShaderProgram {
	/**
	 * Projection Matrix ID
	 */
	private int location_projectionMatrix;
	/**
	 * View Matrix ID
	 */
	private int location_viewMatrix;
	/**
	 * Fog Colour ID
	 */
	private int location_fogColour;
	/**
	 * Skybox Texture1 ID
	 */
	private int location_cubeMap;
	/**
	 * Skybox Texture2 ID
	 */
	private int location_cubeMap2;
	/**
	 * Skybox Blend Factor ID
	 */
	private int location_blendFactor;
	/**
	 * Skybox Rotation
	 */
	private float rotation = 0;

	/**
	 * Constructor, Create a Skybox Shader
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public SkyboxShader() {
		super(KernelConstants.VERTEX_FILE_SKYBOX,
				KernelConstants.FRAGMENT_FILE_SKYBOX);
	}

	/**
	 * Loads a Projection Matrix
	 * 
	 * @param matrix
	 *            Projection Matrix
	 */
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}

	/**
	 * Loads View Matrix
	 * 
	 * @param camera
	 *            Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		rotation += KernelConstants.ROTATE_SPEED
				* DisplayManager.getFrameTimeSeconds();
		Matrix4f.rotate((float) Math.toRadians(rotation),
				new Vector3f(0, 1, 0), matrix, matrix);
		super.loadMatrix(location_viewMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = super
				.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
		location_fogColour = super.getUniformLocation("fogColour");
		location_blendFactor = super.getUniformLocation("blendFactor");
		location_cubeMap = super.getUniformLocation("cubeMap");
		location_cubeMap2 = super.getUniformLocation("cubeMap2");
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
		super.loadVector(location_fogColour, new Vector3f(r, g, b));
	}

	/**
	 * Loads Textures ID
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void connectTextureUnits() {
		super.loadInt(location_cubeMap, 0);
		super.loadInt(location_cubeMap2, 1);
	}

	/**
	 * Loads Blend Factor
	 * 
	 * @param blend
	 *            Value
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadBlendFactor(float blend) {
		super.loadFloat(location_blendFactor, blend);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
