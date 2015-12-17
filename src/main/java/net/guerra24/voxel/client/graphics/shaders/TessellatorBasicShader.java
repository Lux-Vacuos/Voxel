package net.guerra24.voxel.client.graphics.shaders;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.util.Maths;
import net.guerra24.voxel.client.world.entities.Camera;
import net.guerra24.voxel.universal.util.vector.Matrix4f;

public class TessellatorBasicShader extends ShaderProgram {

	private static TessellatorBasicShader instance = null;

	public static TessellatorBasicShader getInstance() {
		if (instance == null) {
			instance = new TessellatorBasicShader();
		}
		return instance;
	}

	private int loc_projectionMatrix;
	private int loc_viewMatrix;
	private int loc_cameraPos;

	public TessellatorBasicShader() {
		super(VoxelVariables.VERTEX_FILE_TESSELLATOR_BASIC, VoxelVariables.FRAGMENT_FILE_TESSELLATOR_BASIC);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
		loc_viewMatrix = super.getUniformLocation("viewMatrix");
		loc_cameraPos = super.getUniformLocation("cameraPos");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
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
