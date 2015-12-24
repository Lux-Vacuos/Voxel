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
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector2f;

public class ParticleShader extends ShaderProgram {

	private int loc_viewMatrix;
	private int loc_transformationMatrix;
	private int loc_projectionMatrix;

	private int loc_texOffset0;
	private int loc_texOffset1;
	private int loc_texCoordInfo;

	public ParticleShader() {
		super(VoxelVariables.VERTEX_FILE_PARTICLE, VoxelVariables.FRAGMENT_FILE_PARTICLE);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_texOffset0 = super.getUniformLocation("texOffset0");
		loc_texOffset1 = super.getUniformLocation("texOffset1");
		loc_texCoordInfo = super.getUniformLocation("texCoordInfo");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadTextureCoordInfo(Vector2f offset0, Vector2f offset1, float numRows, float blend) {
		super.load2DVector(loc_texOffset0, offset0);
		super.load2DVector(loc_texOffset1, offset1);
		super.load2DVector(loc_texCoordInfo, new Vector2f(numRows, blend));
	}

	public void loadViewMatrix(Matrix4f modelView) {
		super.loadMatrix(loc_viewMatrix, modelView);
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(loc_projectionMatrix, projectionMatrix);
	}

	public void loadTransformationMatrix(Matrix4f transformationMatrix) {
		super.loadMatrix(loc_transformationMatrix, transformationMatrix);
	}

}
