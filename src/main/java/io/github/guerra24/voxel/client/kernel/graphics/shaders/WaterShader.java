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
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;

import org.lwjglx.util.vector.Matrix4f;
import org.lwjglx.util.vector.Vector3f;

/**
 * Water Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @version 0.0.3 Build-59
 * @since 0.0.1 Build-52
 * @category Rendering
 */
public class WaterShader extends ShaderProgram {
	/**
	 * Model View Matrix ID
	 */
	private int location_modelMatrix;
	/**
	 * View Matrix ID
	 */
	private int location_viewMatrix;
	/**
	 * Projection Matrix ID
	 */
	private int location_projectionMatrix;
	/**
	 * DUDVMap Texture ID
	 */
	private int location_dudvMap;
	/**
	 * NormalMap Texture ID
	 */
	private int location_normalMap;
	/**
	 * Move Factor ID
	 */
	private int location_moveFactor;
	/**
	 * Camera Position ID
	 */
	private int location_cameraPosition;
	/**
	 * Directional Light Direction ID
	 */
	private int location_directLightDirection;
	/**
	 * Light Colour ID
	 * 
	 * @deprecated
	 */
	private int location_lightColour;
	/**
	 * Light Position ID
	 * 
	 * @deprecated
	 */
	private int location_lightPosition;
	/**
	 * SkyColour ID
	 */
	private int location_skyColour;

	/**
	 * Constructor, Create a Water Shader
	 */
	public WaterShader() {
		super(KernelConstants.VERTEX_FILE_WATER,
				KernelConstants.FRAGMENT_FILE_WATER);
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
		location_dudvMap = getUniformLocation("dudvMap");
		location_normalMap = getUniformLocation("normalMap");
		location_moveFactor = getUniformLocation("moveFactor");
		location_cameraPosition = getUniformLocation("cameraPosition");
		location_lightColour = getUniformLocation("lightColour");
		location_lightPosition = getUniformLocation("lightPosition");
		location_directLightDirection = super
				.getUniformLocation("directLightDirection");
		location_skyColour = super.getUniformLocation("skyColour");
	}

	/**
	 * Loads the Sky Color to the shader
	 * 
	 * @param r
	 *            Red Value
	 * @param g
	 *            Green Value
	 * @param b
	 *            Blue Value
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(location_skyColour, new Vector3f(r, g, b));
	}

	/**
	 * Loads Textures ID
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void connectTextureUnits() {
		super.loadInt(location_dudvMap, 0);
		super.loadInt(location_normalMap, 1);
	}

	/**
	 * Loads the List of Lights to the shader
	 * 
	 * @param lights
	 *            List of Lights
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadLight(Light light) {
		super.loadVector(location_lightColour, light.getColour());
		super.loadVector(location_lightPosition, light.getPosition());
	}

	/**
	 * Loads the Move Factor
	 * 
	 * @param factor
	 *            Factor
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadMoveFactor(float factor) {
		super.loadFloat(location_moveFactor, factor);
	}

	/**
	 * Loads the Projection Matrix
	 * 
	 * @param projection
	 *            Projection Matrix
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}

	/**
	 * Load Directional Light Direction
	 * 
	 * @param direction
	 *            Light Direction
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadDirectLightDirection(Vector3f direction) {
		super.loadVector(location_directLightDirection, direction);
	}

	/**
	 * Load View Matrix
	 * 
	 * @param camera
	 *            Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		loadMatrix(location_viewMatrix, viewMatrix);
		super.loadVector(location_cameraPosition, camera.getPosition());
	}

	/**
	 * Load Model Matrix
	 * 
	 * @param modelMatrix
	 *            Model Matrix
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadModelMatrix(Matrix4f modelMatrix) {
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
