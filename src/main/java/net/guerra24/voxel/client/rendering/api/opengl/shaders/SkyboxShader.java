/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Guerra24
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

package net.guerra24.voxel.client.rendering.api.opengl.shaders;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector3f;

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
	private int loc_transformationMatrix;
	private int loc_viewMatrix;
	private int loc_time;
	private int loc_lightPosition;
	private int loc_fogColour;

	/**
	 * Constructor, Create a Skybox Shader
	 * 
	 */
	public SkyboxShader() {
		super(VoxelVariables.VERTEX_FILE_SKYBOX, VoxelVariables.FRAGMENT_FILE_SKYBOX);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_fogColour = super.getUniformLocation("fogColour");
		loc_time = super.getUniformLocation("time");
		loc_lightPosition = super.getUniformLocation("lightPosition");
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
	 */
	public void loadViewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		super.loadMatrix(loc_viewMatrix, matrix);
	}

	public void loadTransformationMatrix(Matrix4f mat) {
		super.loadMatrix(loc_transformationMatrix, mat);
	}

	public void loadLightPosition(Vector3f pos) {
		super.loadVector(loc_lightPosition, pos);
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
	 */
	public void loadFog(float r, float g, float b) {
		super.loadVector(loc_fogColour, new Vector3f(r, g, b));
	}

	public void loadTime(float time) {
		super.loadFloat(loc_time, time);
	}

}
