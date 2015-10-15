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

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.util.Maths;
import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector3f;
import io.github.guerra24.voxel.client.kernel.world.entities.Camera;
import io.github.guerra24.voxel.client.kernel.world.entities.Light;

/**
 * Entity Shader
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public class EntityShader extends ShaderProgram {

	/**
	 * Entity Shader Data
	 */
	private int loc_transformationMatrix;
	private int loc_projectionMatrix;
	private int loc_lightPosition[];
	private int loc_lightColour[];
	private int loc_attenuations[];
	private int loc_viewMatrix;
	private int loc_skyColour;
	private int loc_directLightDirection;
	private int loc_time;
	private int loc_blendFactor;
	private int loc_texture0;
	private int loc_depth0;

	/**
	 * Constructor, creates an Entity Shader
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public EntityShader() {
		super(KernelConstants.VERTEX_FILE_ENTITY, KernelConstants.FRAGMENT_FILE_ENTITY);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_skyColour = super.getUniformLocation("skyColour");
		loc_directLightDirection = super.getUniformLocation("directLightDirection");
		loc_blendFactor = super.getUniformLocation("blendFactor");
		loc_time = super.getUniformLocation("time");
		loc_texture0 = super.getUniformLocation("texture0");
		loc_depth0 = super.getUniformLocation("depth0");

		loc_lightPosition = new int[KernelConstants.MAX_LIGHTS];
		loc_lightColour = new int[KernelConstants.MAX_LIGHTS];
		loc_attenuations = new int[KernelConstants.MAX_LIGHTS];
		for (int i = 0; i < KernelConstants.MAX_LIGHTS; i++) {
			loc_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			loc_lightColour[i] = super.getUniformLocation("lightColour[" + i + "]");
			loc_attenuations[i] = super.getUniformLocation("attenuations[" + i + "]");
		}
	}

	/**
	 * Loads Textures ID
	 * 
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void connectTextureUnits() {
		super.loadInt(loc_texture0, 0);
		super.loadInt(loc_depth0, 1);
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
		super.loadVector(loc_skyColour, new Vector3f(r, g, b));
	}

	/**
	 * Loads Transformation Matrix to the shader
	 * 
	 * @param matrix
	 *            Transformation Matrix
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(loc_transformationMatrix, matrix);
	}

	/**
	 * Loads The Directional Light Direction to the shader
	 * 
	 * @param direction
	 *            Light Direction
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadDirectLightDirection(Vector3f direction) {
		super.loadVector(loc_directLightDirection, direction);
	}

	/**
	 * Loads the Day/Night blend factor
	 * 
	 * @param time
	 *            Time
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadblendFactor(float factor) {
		super.loadFloat(loc_blendFactor, factor);
	}

	/**
	 * Loads the World Time
	 * 
	 * @param time
	 *            Time
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadTime(float time) {
		super.loadFloat(loc_time, time);
	}

	/**
	 * Loads the List of Lights to the shader
	 * 
	 * @param lights
	 *            List of Lights
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadLights(Queue<Light> lights) {
		List<Light> fixedList = new ArrayList<Light>();
		fixedList.addAll(lights);
		for (int i = 0; i < KernelConstants.MAX_LIGHTS; i++) {
			if (i < lights.size()) {
				super.loadVector(loc_lightPosition[i], fixedList.get(i).getPosition());
				super.loadVector(loc_lightColour[i], fixedList.get(i).getColour());
				super.loadVector(loc_attenuations[i], fixedList.get(i).getAttenuation());
			} else {
				super.loadVector(loc_lightPosition[i], new Vector3f(0, -128, 0));
				super.loadVector(loc_lightColour[i],
						new Vector3f(0.0000000000000000001f, 0.0000000000000000001f, 0.0000000000000000001f));
				super.loadVector(loc_attenuations[i],
						new Vector3f(0.0000000000000000001f, 0.0000000000000000001f, 0.0000000000000000001f));
			}
		}
	}

	/**
	 * Loads View Matrix to the shader
	 * 
	 * @param camera
	 *            Camera
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadviewMatrix(Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(loc_viewMatrix, viewMatrix);
	}

	/**
	 * Loads Projection Matrix to the shader
	 * 
	 * @param projection
	 *            Projection Matrix
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(loc_projectionMatrix, projection);
	}
}