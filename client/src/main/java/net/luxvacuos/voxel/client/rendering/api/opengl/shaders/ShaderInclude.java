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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.exception.LoadShaderException;

public class ShaderInclude {

	private String file, shaderSource;

	public ShaderInclude(String file) {
		this.file = file;
	}

	void loadShader() {
		StringBuilder shaderSource = new StringBuilder();
		InputStream filet = getClass().getClassLoader()
				.getResourceAsStream("assets/" + ClientVariables.assets + "/shaders" + file);
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(filet));
			Logger.log("Loading Shader to GL-VFS: " + file);
			String line;
			while ((line = reader.readLine()) != null) {
				shaderSource.append(line).append("//\n");
			}
			reader.close();
		} catch (IOException e) {
			throw new LoadShaderException(e);
		}
		this.shaderSource = shaderSource.toString();
	}

	public String getShaderSource() {
		return shaderSource;
	}

	public String getFile() {
		return file;
	}

}
