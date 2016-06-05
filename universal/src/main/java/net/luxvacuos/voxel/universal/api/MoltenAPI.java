/*
 * This file is part of UVoxel
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

package net.luxvacuos.voxel.universal.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.luxvacuos.voxel.universal.api.mod.ModStateLoop;
import net.luxvacuos.voxel.universal.core.UVoxel;

public class MoltenAPI {

	private UVoxel uVoxel;
	private List<ModStateLoop> modStateLoops;
	private final Map<String, APIMethod<?>> methods = new HashMap<String, APIMethod<?>>();
	private String prefix;

	public static final String apiVersion = "0.0.1";
	public static final int apiIntVersion = 1;
	public static final int build = 4;

	public MoltenAPI(UVoxel uVoxel) {
		this.uVoxel = uVoxel;
		if (this.uVoxel.isClient())
			prefix = "Client_";
		else if (this.uVoxel.isServer())
			prefix = "Server_";
		modStateLoops = new ArrayList<>();
		this.uVoxel.registerAPIMethods(this, methods);
	}

	public Object runMethod(String name, Object... objects) {
		APIMethod<?> method = methods.get(prefix + name);
		if (method == null)
			throw new NullPointerException("Method not found: " + prefix + name);

		return method.run(objects);
	}

	public void registerModStateLoop(ModStateLoop modStateLoop) {
		modStateLoops.add(modStateLoop);
	}

	/**
	 * <h1>NOT USE THIS!!!!!!</h1>
	 * 
	 * <p>
	 * This is used internally for calling all modStates
	 * </p>
	 * 
	 * @return
	 */
	public List<ModStateLoop> getModStateLoops() {
		return modStateLoops;
	}

}
