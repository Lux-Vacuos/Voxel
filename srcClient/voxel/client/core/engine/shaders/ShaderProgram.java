package voxel.client.core.engine.shaders;

import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

public class ShaderProgram {

	private int program, vShader, fShader;

	public ShaderProgram(int vShader, int fShader) {
		this.vShader = vShader;
		this.fShader = fShader;

		createShaderProgram();
	}

	private void createShaderProgram() {
		program = glCreateProgram();

		glAttachShader(program, vShader);
		glAttachShader(program, fShader);
		glLinkProgram(program);
		glValidateProgram(program);
	}

	public void use() {
		glUseProgram(program);
	}

	public void release() {
		glUseProgram(0);
	}

	public void dispose() {
		glDeleteProgram(program);
	}

	public int getProgram() {
		return program;
	}

}
