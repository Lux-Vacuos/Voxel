package voxel.client.engine.render.shaders;

import org.lwjgl.util.vector.Matrix4f;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "assets/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "assets/shaders/fragmentShader.txt";

	private int location_transformationMatrix;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super
				.getUniformLocation("transformationMatrix");
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
}
