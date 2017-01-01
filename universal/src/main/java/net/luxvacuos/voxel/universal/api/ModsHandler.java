/*
 * This file is part of AbstractVoxel
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

package net.luxvacuos.voxel.universal.api;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.universal.api.mod.IMod;
import net.luxvacuos.voxel.universal.core.AbstractVoxel;

/**
 * @author Guerra24 <pablo230699@hotmail.com>
 */
public class ModsHandler {
	private ModLoader modLoader;
	private IMoltenAPI iMoltenAPI;

	private List<IMod> mods;

	public ModsHandler(AbstractVoxel abstractVoxel) throws Exception {
		modLoader = new ModLoader();
		modLoader.loadMods(this, abstractVoxel.getBootstrap().getPrefix());
		mods = new ArrayList<>();
	}

	public void preInit() {
		Logger.log("Starting Mods Load");
		Logger.log("ModLoader Pre Init phase");
		for (IMod mod : mods) {
			mod.preInit(iMoltenAPI);
		}

	}

	public void init() {
		Logger.log("ModLoader Init phase");
		for (IMod mod : mods) {
			mod.init(iMoltenAPI);
		}

	}

	public void postInit() {
		Logger.log("ModLoader Post Init phase");
		for (IMod mod : mods) {
			mod.postInit(iMoltenAPI);
		}
	}

	public void dispose() {
		for (IMod mod : mods) {
			mod.dispose(iMoltenAPI);
		}
		mods.clear();
	}

	public void setMoltenAPI(IMoltenAPI iMoltenAPI) {
		this.iMoltenAPI = iMoltenAPI;
	}

	public void addMod(IMod mod) {
		this.mods.add(mod);
	}

}
