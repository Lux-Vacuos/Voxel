package net.guerra24.voxel.client.graphics.shaders;

import net.guerra24.voxel.client.core.VoxelVariables;
import net.guerra24.voxel.client.resources.GameResources;
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
	private int loc_cameraPos;
	private int loc_projectionLightMatrix;
	private int loc_viewLightMatrix;
	private int loc_biasMatrix;

	private int loc_texture;
	private int loc_depth;

	private int loc_useShadows;

	public TessellatorShader() {
		super(VoxelVariables.VERTEX_FILE_TESSELLATOR, VoxelVariables.FRAGMENT_FILE_TESSELLATOR);
	}

	public void conectTextureUnits() {
		super.loadInt(loc_texture, 0);
		super.loadInt(loc_depth, 1);
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
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
		super.bindAttribute(3, "data");
	}

	public void loadSettings(boolean useShadows) {
		super.loadBoolean(loc_useShadows, useShadows);
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
	
	public void loadBiasMatrix(GameResources gm){
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
