/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package net.luxvacuos.voxel.client.rendering.api.opengl.shaders;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glBindAttribLocation;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glDetachShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUniform3f;
import static org.lwjgl.opengl.GL20.glUniform4f;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.igl.vector.Matrix4f;
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.rendering.api.glfw.Display;

/**
 * Shader Program, Use to create shaders
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Rendering
 */
public abstract class ShaderProgram {
	/**
	 * Program ID
	 */
	private int programID;
	/**
	 * Vertex Shader ID
	 */
	private int vertexShaderID;
	/**
	 * Fragment Shader ID
	 */
	private int fragmentShaderID;
	/**
	 * Shader bind status
	 */
	private boolean binded = false;
	/**
	 * Used to Load Matrix to shader
	 */
	private FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

	private static Display display;

	public static void setDisplay(Display display) {
		ShaderProgram.display = display;
	}

	/**
	 * Constructor, Creates a Shader Program Using a Vertex Shader and a
	 * Fragment Shader
	 * 
	 * @param vertexFile
	 *            Vertex Shader Path
	 * @param fragmentFile
	 *            Fragment Shader Path
	 */
	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
		programID = glCreateProgram();
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		glLinkProgram(programID);
		glValidateProgram(programID);
		getAllUniformLocations();
	}

	/**
	 * Loads all Uniform Locations
	 */
	protected abstract void getAllUniformLocations();

	/**
	 * Gets The Uniform Location
	 * 
	 * @param uniformName
	 *            Uniform Namer
	 * @return ID of the Uniform Location
	 */
	protected int getUniformLocation(String uniformName) {
		return glGetUniformLocation(programID, uniformName);
	}

	/**
	 * Starts the Shader
	 * 
	 */
	public void start() {
		if (binded)
			throw new RuntimeException("A Shader is already binded");
		glUseProgram(programID);
		binded = true;
	}

	/**
	 * Stops the Shader
	 * 
	 */
	public void stop() {
		glUseProgram(0);
		binded = false;
	}

	/**
	 * Clear all the shader loaded data
	 * 
	 */
	public void cleanUp() {
		stop();
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
		glDeleteProgram(programID);
	}

	/**
	 * Binds the Shader Attributes
	 */
	protected abstract void bindAttributes();

	/**
	 * Binds the variable to an uniform id
	 * 
	 * @param attribute
	 *            Uniform ID
	 * @param variableName
	 *            Variable Name
	 */
	protected void bindAttribute(int attribute, String variableName) {
		glBindAttribLocation(programID, attribute, variableName);
	}

	/**
	 * Load a float
	 * 
	 * @param location
	 *            Uniform ID
	 * @param value
	 *            Value
	 */
	protected void loadFloat(int location, float value) {
		glUniform1f(location, value);
	}

	/**
	 * Load an int
	 * 
	 * @param location
	 *            Uniform ID
	 * @param value
	 *            Value
	 */
	protected void loadInt(int location, int value) {
		glUniform1i(location, value);
	}

	/**
	 * Load a Vector4f
	 * 
	 * @param location
	 *            Uniform ID
	 * @param vector
	 *            Vector4f
	 */
	protected void loadVector(int location, Vector4f vector) {
		glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
	}

	/**
	 * Load a Vector3f
	 * 
	 * @param location
	 *            Uniform ID
	 * @param vector
	 *            Vector3f
	 */
	protected void loadVector(int location, Vector3f vector) {
		glUniform3f(location, vector.x, vector.y, vector.z);
	}

	/**
	 * Load a Vector2f
	 * 
	 * @param location
	 *            Uniform ID
	 * @param vector
	 *            Vector2f
	 */
	protected void load2DVector(int location, Vector2f vector) {
		glUniform2f(location, vector.x, vector.y);
	}

	/**
	 * Load a Boolean
	 * 
	 * @param location
	 *            Uniform ID
	 * @param value
	 *            Value
	 */
	protected void loadBoolean(int location, boolean value) {
		int toLoad = 0;
		if (value) {
			toLoad = 1;
		}
		glUniform1i(location, toLoad);
	}

	/**
	 * Load a Matrix4f
	 * 
	 * @param location
	 *            Uniform ID
	 * @param matrix
	 *            Matrix4f
	 */
	protected void loadMatrix(int location, Matrix4f matrix) {
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		glUniformMatrix4fv(location, false, matrixBuffer);
	}

	/**
	 * Load a Shader File
	 * 
	 * @param file
	 *            Shader Path
	 * @param type
	 *            Type of Shader
	 * @return Shader ID
	 * 
	 * @throws IllegalStateException
	 *             in case of compilation error
	 */
	private int loadShader(String file, int type) throws IllegalStateException {
		StringBuilder shaderSource = new StringBuilder();
		InputStream filet = getClass().getClassLoader().getResourceAsStream("assets/shaders/" + file);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(filet));
			Logger.log("Loading Shader: " + file);
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("//\n");
			}
			reader.close();
			if (display.isNvidia())
				shaderSource.insert(0, "#define NVIDIA//\n");// TEMPORAL FIX
			else if (display.isAmd())
				shaderSource.insert(0, "#define AMD//\n");// TEMPORAL FIX
		} catch (IOException e) {
			Logger.warn("Shader file not found: " + file);
			e.printStackTrace();
			System.exit(-1);
		}
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			Logger.error(glGetShaderInfoLog(shaderID, 500));
			throw new IllegalStateException("Syntax Error in " + file);
		}
		return shaderID;
	}

}