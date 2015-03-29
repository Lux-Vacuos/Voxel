package voxel.client.engine.render.shaders;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "assets/shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = "assets/shaders/fragmentShader.txt";

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
