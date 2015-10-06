package io.github.guerra24.voxel.client.kernel.graphics.shaders;

import io.github.guerra24.voxel.client.kernel.core.KernelConstants;
import io.github.guerra24.voxel.client.kernel.util.vector.Matrix4f;
import io.github.guerra24.voxel.client.kernel.util.vector.Vector2f;

public class PostProcessingShader extends ShaderProgram {

	private int loc_transformationMatrix;
	private int loc_resolution;

	public PostProcessingShader() {
		super(KernelConstants.VERTEX_FILE_POST, KernelConstants.FRAGMENT_FILE_POST);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_resolution = super.getUniformLocation("resolution");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadResolution(Vector2f res) {
		super.load2DVector(loc_resolution, res);
	}

	/**
	 * Loads Transformation Matrix to the shader
	 * 
	 * @param matrix
	 *            Transformation Matrix
	 * @author Guerra24 <pablo230699@hotmail.com>
	 */
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(loc_transformationMatrix, matrix);
	}

}
