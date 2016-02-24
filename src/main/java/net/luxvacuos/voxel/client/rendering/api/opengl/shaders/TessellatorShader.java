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

public class TessellatorShader extends ShaderProgram {

	private static TessellatorShader instance = null;

	public static TessellatorShader getInstance() {
		if (instance == null) {
			instance = new TessellatorShader();
		}
		return instance;
	}

	private int loc_projectionMatrix;
	private int loc_viewMatrix;
	private int loc_cameraPos;
	private int loc_projectionLightMatrix;
	private int loc_viewLightMatrix;
	private int loc_biasMatrix;
	private int loc_moveFactor;

	private int loc_texture;
	private int loc_depth;
	private int loc_normalMap;
	private int loc_heightMap;
	private int loc_specularMap;

	private int loc_useShadows;
	private int loc_useParallax;

	public TessellatorShader() {
		super(VoxelVariables.VERTEX_FILE_TESSELLATOR, VoxelVariables.FRAGMENT_FILE_TESSELLATOR);
	}

	public void conectTextureUnits() {
		super.loadInt(loc_texture, 0);
		super.loadInt(loc_depth, 1);
		super.loadInt(loc_normalMap, 2);
		super.loadInt(loc_heightMap, 3);
		super.loadInt(loc_specularMap, 4);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_biasMatrix = super.getUniformLocation("biasMatrix");
		loc_projectionLightMatrix = super.getUniformLocation("projectionLightMatrix");
		loc_viewLightMatrix = super.getUniformLocation("viewLightMatrix");
		loc_cameraPos = super.getUniformLocation("cameraPos");
		loc_texture = super.getUniformLocation("texture0");
		loc_depth = super.getUniformLocation("depth");
		loc_useShadows = super.getUniformLocation("useShadows");
		loc_normalMap = super.getUniformLocation("normalMap");
		loc_heightMap = super.getUniformLocation("heightMap");
		loc_specularMap = super.getUniformLocation("specularMap");
		loc_useParallax = super.getUniformLocation("useParallax");
		loc_moveFactor = getUniformLocation("moveFactor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "data");
		super.bindAttribute(4, "tangent");
		super.bindAttribute(5, "bitangent");
	}

	/**
	 * Loads View Matrix to the shader
	 * 
	 * @param camera
	 *            Camera
	 */
	public void loadviewMatrix(Camera camera) {
		Matrix4f matrix = Maths.createViewMatrix(camera);
		matrix.m30 = 0;
		matrix.m31 = 0;
		matrix.m32 = 0;
		super.loadMatrix(loc_viewMatrix, matrix);
		super.loadVector(loc_cameraPos, camera.getPosition());
	}

	public void loadBiasMatrix(GameResources gm) {
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
	
	public void loadMoveFactor(float factor) {
		super.loadFloat(loc_moveFactor, factor);
	}

	public void loadSettings(boolean useShadows, boolean useParallax) {
		super.loadBoolean(loc_useShadows, useShadows);
		super.loadBoolean(loc_useParallax, useParallax);
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
