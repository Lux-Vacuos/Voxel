/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015-2016 Lux Vacuos
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

package net.luxvacuos.voxel.client.rendering.api.opengl.shaders;

import net.luxvacuos.voxel.client.core.VoxelVariables;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;
import net.luxvacuos.voxel.universal.util.vector.Matrix4f;

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
	private int loc_projectionLightMatrix;
	private int loc_viewLightMatrix;
	private int loc_biasMatrix;
	private int loc_entityLight;
	private int loc_texture0;
	private int loc_depth0;

	private int loc_useShadows;

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
		loc_biasMatrix = super.getUniformLocation("biasMatrix");
		loc_projectionLightMatrix = super.getUniformLocation("projectionLightMatrix");
		loc_viewLightMatrix = super.getUniformLocation("viewLightMatrix");
		loc_entityLight = super.getUniformLocation("entityLight");
		loc_texture0 = super.getUniformLocation("texture0");
		loc_depth0 = super.getUniformLocation("depth0");
		loc_useShadows = super.getUniformLocation("useShadows");
	}

	/**
	 * Loads Textures ID
	 * 
	 */
	public void connectTextureUnits() {
		super.loadInt(loc_texture0, 0);
		super.loadInt(loc_depth0, 1);
	}
	
	public void loadEntityLight(float light){
		super.loadFloat(loc_entityLight, light);
	}
	
	public void useShadows(boolean value) {
		super.loadBoolean(loc_useShadows, value);
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
	
	public void loadBiasMatrix(GameResources gm){
		Matrix4f biasMatrix = new Matrix4f();
		biasMatrix.m00 = 0.5f;
		biasMatrix.m01 = 0;
		biasMatrix.m02 = 0;
		biasMatrix.m03 = 0;
		biasMatrix.m10 = 0;
		biasMatrix.m11 = 0.5f;
		biasMatrix.m12 = 0;
		biasMatrix.m13 = 0;
		biasMatrix.m20 = 0;
		biasMatrix.m21 = 0;
		biasMatrix.m22 = 0.5f;
		biasMatrix.m23 = 0;
		biasMatrix.m30 = 0.5f;
		biasMatrix.m31 = 0.5f;
		biasMatrix.m32 = 0.5f;
		biasMatrix.m33 = 1f;
		super.loadMatrix(loc_biasMatrix, biasMatrix);
		super.loadMatrix(loc_projectionLightMatrix, gm.getMasterShadowRenderer().getProjectionMatrix());
	}

	public void loadLightMatrix(GameResources gm) {
		super.loadMatrix(loc_viewLightMatrix, Maths.createViewMatrix(gm.getSun_Camera()));
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