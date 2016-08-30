/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.rendering.api.opengl.shaders;

import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.resources.GameResources;
import net.luxvacuos.voxel.client.util.Maths;
import net.luxvacuos.voxel.client.world.entities.Camera;

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

	private int loc_rainFactor;
	private int loc_transparent;

	private TessellatorShader() {
		super(ClientVariables.VERTEX_FILE_TESSELLATOR, ClientVariables.FRAGMENT_FILE_TESSELLATOR);
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
		loc_moveFactor = super.getUniformLocation("moveFactor");
		loc_rainFactor = super.getUniformLocation("rainFactor");
		loc_transparent = super.getUniformLocation("transparent");
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

	public void loadRainFactor(float factor) {
		super.loadFloat(loc_rainFactor, factor);
	}

	public void loadSettings(boolean useShadows, boolean useParallax) {
		super.loadBoolean(loc_useShadows, useShadows);
		super.loadBoolean(loc_useParallax, useParallax);
	}

	public void loadTransparent(boolean trans) {
		super.loadBoolean(loc_transparent, trans);
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
