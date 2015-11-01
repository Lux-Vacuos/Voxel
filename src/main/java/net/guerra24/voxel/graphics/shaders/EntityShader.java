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
import net.guerra24.voxel.util.Maths;
import net.guerra24.voxel.util.vector.Matrix4f;
import net.guerra24.voxel.util.vector.Vector3f;
import net.guerra24.voxel.world.entities.Camera;

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
	private int loc_viewMatrix;
	private int loc_skyColour;
	private int loc_blockBright;
	private int loc_texture0;
	

	/**
	 * Constructor, creates an Entity Shader
	 * 
	 */
	public EntityShader() {
		super(VoxelVariables.VERTEX_FILE_ENTITY, VoxelVariables.FRAGMENT_FILE_ENTITY);
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
		loc_blockBright = super.getUniformLocation("blockBright");
		loc_texture0 = super.getUniformLocation("texture0");

	}

	/**
	 * Loads Textures ID
	 * 
	 */
	public void connectTextureUnits() {
		super.loadInt(loc_texture0, 0);
	}
	
	public void loadBlockBright(float value){
		super.loadFloat(loc_blockBright, value);
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
	 */
	public void loadSkyColour(float r, float g, float b) {
		super.loadVector(loc_skyColour, new Vector3f(r, g, b));
	}

	/**
	 * Loads Transformation Matrix to the shader
	 * 
	 * @param matrix
	 *            Transformation Matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(loc_transformationMatrix, matrix);
	}

	/**
	 * Loads View Matrix to the shader
	 * 
	 * @param camera
	 *            Camera
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
	 */
	public void loadProjectionMatrix(Matrix4f projection) {
		super.loadMatrix(loc_projectionMatrix, projection);
	}
}