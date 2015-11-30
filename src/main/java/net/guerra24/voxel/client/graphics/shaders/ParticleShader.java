package net.guerra24.voxel.client.graphics.shaders;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.universal.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram {

	private int loc_viewMatrix;
	private int loc_transformationMatrix;
	private int loc_projectionMatrix;

	public ParticleShader() {
		super(VoxelVariables.VERTEX_FILE_PARTICLE, VoxelVariables.FRAGMENT_FILE_PARTICLE);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
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
