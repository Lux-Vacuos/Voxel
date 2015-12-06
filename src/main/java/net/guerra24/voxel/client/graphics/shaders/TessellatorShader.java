package net.guerra24.voxel.client.graphics.shaders;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Matrix4f;

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

	public TessellatorShader() {
		super(VoxelVariables.VERTEX_FILE_TESSELLATOR, VoxelVariables.FRAGMENT_FILE_TESSELLATOR);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
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
