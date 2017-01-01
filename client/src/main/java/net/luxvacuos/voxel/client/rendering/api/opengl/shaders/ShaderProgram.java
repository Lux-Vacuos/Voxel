/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2017 Lux Vacuos
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

import static org.lwjgl.opengl.ARBShadingLanguageInclude.GL_SHADER_INCLUDE_ARB;
import static org.lwjgl.opengl.ARBShadingLanguageInclude.glNamedStringARB;
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
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.exception.CompileShaderException;
import net.luxvacuos.voxel.client.core.exception.LoadShaderException;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.Attribute;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.IUniform;

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
	 * Shader bind status
	 */
	private static boolean binded = false;

	/**
	 * Load includes to VFS
	 */
	public static void loadToVFS(List<ShaderInclude> shaders) {
		for (ShaderInclude shader : shaders) {
			shader.loadShader();
			glNamedStringARB(GL_SHADER_INCLUDE_ARB, shader.getFile(), shader.getShaderSource());
		}
	}

	public static void loadToVFS(ShaderInclude shader) {
		shader.loadShader();
		glNamedStringARB(GL_SHADER_INCLUDE_ARB, shader.getFile(), shader.getShaderSource());
	}

	/**
	 * Constructor, Creates a Shader Program Using a Vertex Shader and a
	 * Fragment Shader
	 * 
	 * @param vertexFile
	 *            Vertex Shader Path
	 * @param fragmentFile
	 *            Fragment Shader Path
	 * @throws Exception
	 */
	public ShaderProgram(String vertexFile, String fragmentFile, Attribute... inVariables) {
		int vertexShaderID = loadShader(vertexFile, GL_VERTEX_SHADER);
		int fragmentShaderID = loadShader(fragmentFile, GL_FRAGMENT_SHADER);
		programID = glCreateProgram();
		glAttachShader(programID, vertexShaderID);
		glAttachShader(programID, fragmentShaderID);
		bindAttributes(inVariables);
		glLinkProgram(programID);
		glDetachShader(programID, vertexShaderID);
		glDetachShader(programID, fragmentShaderID);
		glDeleteShader(vertexShaderID);
		glDeleteShader(fragmentShaderID);
	}

	/**
	 * Loads All Uniforms and validate the program.
	 * 
	 * @param uniforms
	 *            Array of Uniforms
	 */
	protected void storeAllUniformLocations(IUniform... uniforms) {
		for (IUniform uniform : uniforms) {
			uniform.storeUniformLocation(programID);
		}
		glValidateProgram(programID);
	}

	/**
	 * Loads All Uniforms.
	 * 
	 * @param uniforms
	 */
	protected void storeUniformArray(IUniform... uniforms) {
		for (IUniform uniform : uniforms) {
			uniform.storeUniformLocation(programID);
		}
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
		glDeleteProgram(programID);
	}

	/**
	 * Bind array of attributes
	 * 
	 * @param inVariables
	 *            Array
	 */
	private void bindAttributes(Attribute[] att) {
		for (int i = 0; i < att.length; i++) {
			glBindAttribLocation(programID, att[i].getId(), att[i].getName());
		}
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
	 */
	private int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
		InputStream filet = getClass().getClassLoader()
				.getResourceAsStream("assets/" + ClientVariables.assets + "/shaders/" + file);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(filet));
			Logger.log("Loading Shader: " + file);
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		} catch (IOException e) {
			throw new LoadShaderException(e);
		}
		int shaderID = glCreateShader(type);
		glShaderSource(shaderID, shaderSource);
		glCompileShader(shaderID);
		if (glGetShaderi(shaderID, GL_COMPILE_STATUS) == GL_FALSE) {
			Logger.error(glGetShaderInfoLog(shaderID, 500));
			throw new CompileShaderException(glGetShaderInfoLog(shaderID, 500));
		}
		return shaderID;
	}

}