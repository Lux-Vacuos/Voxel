package net.guerra24.voxel.client.graphics.shaders;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.universal.util.vector.Matrix4f;
import net.guerra24.voxel.universal.util.vector.Vector2f;

public class CompositeShader extends ShaderProgram {

	private int loc_transformationMatrix;
	private int loc_sunPositionInScreen;

	public CompositeShader() {
		super(VoxelVariables.VERTEX_FILE_COMPOSITE1, VoxelVariables.FRAGMENT_FILE_COMPOSITE1);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_sunPositionInScreen = super.getUniformLocation("sunPositionInScreen");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadSunPosition(Vector2f pos) {
		super.load2DVector(loc_sunPositionInScreen, pos);
	}

	/**
	 * Loads Transformation Matrix to the shader
	 * 
	 * @param matrix
	 *            Transformation Matrix
	 */
	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(loc_transformationMatrix, matrix);
	}

}
