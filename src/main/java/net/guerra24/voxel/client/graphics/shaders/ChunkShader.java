package net.guerra24.voxel.client.graphics.shaders;

import net.guerra24.voxel.client.core.VoxelVariables;

public class ChunkShader extends ShaderProgram {
	
	private int loc_projectionMatrix;
	private int loc_viewMatrix;

	public ChunkShader() {
		super(VoxelVariables.VERTEX_FILE_CHUNK, VoxelVariables.FRAGMENT_FILE_CHUNK);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "texCoords");
		super.bindAttribute(2, "texCoordsOffset");
		super.bindAttribute(3, "color");
	}

}
