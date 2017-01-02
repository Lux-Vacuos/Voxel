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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.core.Voxel;
import net.luxvacuos.voxel.client.core.exception.IncludeShaderException;
import net.luxvacuos.voxel.client.core.exception.LoadShaderException;

public final class ShaderIncludes {
	
	private static Map<String, String> variablesMap = new HashMap<>();
	private static Map<String, String> functionsMap = new HashMap<>();
	private static Map<String, String> structMap = new HashMap<>();

	private ShaderIncludes() { }
	
	public static void processIncludeFile(String name) {
		StringBuilder source = new StringBuilder();
		String includeName = "";
		InputStream filet = null;
		BufferedReader reader = null;
		try {
			filet = Voxel.class.getClassLoader()
					.getResourceAsStream("assets/" + ClientVariables.assets + "/shaders/includes/" + name);
			reader = new BufferedReader(new InputStreamReader(filet));
			Logger.log("Processing Shader Include File: " + name);
			String line;
			boolean var = false, struct = false, func = false;
			while((line = reader.readLine()) != null) {
				if(line.startsWith("##end") && (var || func || struct)) {
					Logger.log("Parsed ISL Object '"+includeName+"'");
					if(var) {
						var = false;
						variablesMap.put(includeName, source.toString());
						source = new StringBuilder();
						continue;
					} else if(struct) {
						struct = false;
						structMap.put(includeName, source.toString());
						source = new StringBuilder();
						continue;
					} else if(func) {
						func = false;
						functionsMap.put(includeName, source.toString());
						source = new StringBuilder();
						continue;
					}
					continue;
				} else if(line.startsWith("##variable")) { //Process a variable
					var = true;
					String[] cat = line.split(" ");
					includeName = cat[1];
					continue;
				} else if(line.startsWith("##struct")) { //Process a struct
					struct = true;
					String[] cat = line.split(" ");
					includeName = cat[1];
					continue;
				} else if(line.startsWith("##function")) { //Process a function
					func = true;
					String[] cat = line.split(" ");
					includeName = cat[1];
					continue;
				} else if((var || func || struct)) {
					source.append(line).append("//\n");
				}
			}
		} catch(Exception e) {
			throw new LoadShaderException(e);
		} finally {
			try {
				if(filet != null) filet.close();
				if(reader != null) reader.close();
			} catch(Exception e) {
				Logger.error(e.toString());
			}
		}
	}
	
	public static String getFunction(String name) {
		if(!functionsMap.containsKey(name)) throw new IncludeShaderException("ISL Function '"+name+"' does not exist");
		return functionsMap.get(name);
	}
	
	public static String getVariable(String name) {
		if(!variablesMap.containsKey(name)) throw new IncludeShaderException("ISL Variable '"+name+"' does not exist");
		return variablesMap.get(name);
	}
	
	public static String getStruct(String name) {
		if(!structMap.containsKey(name)) throw new IncludeShaderException("ISL Struct '"+name+"' does not exist");
		return structMap.get(name);
	}

}
