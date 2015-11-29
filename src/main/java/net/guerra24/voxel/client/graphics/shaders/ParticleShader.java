package net.guerra24.voxel.client.graphics.shaders;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.universal.util.vector.Matrix4f;

public class ParticleShader extends ShaderProgram {

	private int location_modelViewMatrix;
	private int location_projectionMatrix;

	public ParticleShader() {
		super(VoxelVariables.VERTEX_FILE_PARTICLE, VoxelVariables.FRAGMENT_FILE_PARTICLE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
