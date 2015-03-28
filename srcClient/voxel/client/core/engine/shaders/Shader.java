package voxel.client.core.engine.shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import voxel.client.core.engine.util.Logger;

public class Shader {

	private String vertexLoc, fragmentLoc;
	private int vShader, fShader;

	public Shader(String vertexLoc, String fragmentLoc) {
		this.vertexLoc = vertexLoc;
		this.fragmentLoc = fragmentLoc;

		loadShader();
	}

	private void loadShader() {
		vShader = glCreateShader(GL_VERTEX_SHADER);
		fShader = glCreateShader(GL_FRAGMENT_SHADER);

		StringBuilder vSource = new StringBuilder();
		StringBuilder fSource = new StringBuilder();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					Shader.class.getResourceAsStream(vertexLoc)));
			String line;
			while ((line = reader.readLine()) != null) {
				vSource.append(line).append('\n');
			}
			reader.close();
		} catch (IOException e) {
			Logger.error("" + vSource);
			Logger.error("Error loading vertex shader source at Shader.java location: "
					+ vertexLoc);
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					Shader.class.getResourceAsStream(fragmentLoc)));
			String line;
			while ((line = reader.readLine()) != null) {
				fSource.append(line).append('\n');
			}
			reader.close();
		} catch (IOException e) {
			System.err.println(fSource);
			System.err
					.println("Error loading fragment shader source at Shader.java location: "
							+ fragmentLoc);
		}

		compileShaders(vSource, fSource);
	}

	private void compileShaders(StringBuilder vSource, StringBuilder fSource) {
		glShaderSource(vShader, vSource);
		glCompileShader(vShader);
		if (glGetShaderi(vShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(vShader, 1024));
			System.err
					.println("Error compiling vertex shader at Shader.java location: "
							+ vertexLoc);
		}

		glShaderSource(fShader, fSource);
		glCompileShader(fShader);
		if (glGetShaderi(fShader, GL_COMPILE_STATUS) == GL_FALSE) {
			System.err.println(glGetShaderInfoLog(fShader, 1024));
			System.err
					.println("Error compiling fragment shader at Shader.java location: "
							+ fragmentLoc);
		}
	}

	public int getvShader() {
		return vShader;
	}

	public int getfShader() {
		return fShader;
	}

	public void dispose() {
		glDeleteShader(vShader);
		glDeleteShader(fShader);
	}
}
