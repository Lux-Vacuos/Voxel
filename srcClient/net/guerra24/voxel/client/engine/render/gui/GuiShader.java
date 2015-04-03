package net.guerra24.voxel.client.engine.render.gui;

import net.guerra24.voxel.client.engine.render.shaders.ShaderProgram;

import org.lwjgl.util.vector.Matrix4f;

public class GuiShader extends ShaderProgram {

	private static final String VERTEX_FILE = "assets/shaders/vertexShaderGui.txt";
	private static final String FRAGMENT_FILE = "assets/shaders/fragmentShaderGui.txt";

	private int location_transformationMatrix;

	public GuiShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	public void loadTransformation(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super
				.getUniformLocation("transformationMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
