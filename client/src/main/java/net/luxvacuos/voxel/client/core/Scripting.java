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

package net.luxvacuos.voxel.client.core;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.core.exception.CompileGroovyException;

/**
 * 
 * Groovy scripting system
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 *
 */
public class Scripting {

	private ScriptEngineManager scriptEngineManager;
	private ScriptEngine scriptEngine;
	private Compilable compilableEngine;

	public Scripting() {
		scriptEngineManager = new ScriptEngineManager();
		scriptEngine = scriptEngineManager.getEngineByName("groovy");
		if (scriptEngine == null)
			throw new NullPointerException("Can't setup Groovy engine");
		if (scriptEngine instanceof Compilable) {
			compilableEngine = (Compilable) scriptEngine;
		}
	}

	public CompiledScript compile(String file) {
		Logger.log("Compiling: " + file);
		InputStream filet = getClass().getClassLoader().getResourceAsStream("assets/voxel/scripts/" + file + ".groovy");
		BufferedReader reader = new BufferedReader(new InputStreamReader(filet));
		try {
			return compilableEngine.compile(reader);
		} catch (ScriptException e) {
			throw new CompileGroovyException("Unable to compile: " + file, e);
		}
	}

}
