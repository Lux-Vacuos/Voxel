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

package net.luxvacuos.voxel.server.core.subsystems;

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;

import java.io.File;

import net.luxvacuos.voxel.server.bootstrap.Bootstrap;
import net.luxvacuos.voxel.server.core.ServerGameSettings;
import net.luxvacuos.voxel.universal.core.CoreSubsystem;

public class ServerCoreSubsystem extends CoreSubsystem {

	@Override
	public void init() {
		super.init();
		REGISTRY.register("/Voxel/Settings/World/directory", Bootstrap.getPrefix() + "/");
		gameSettings = new ServerGameSettings();
		gameSettings.load(new File((String) REGISTRY.getRegistryItem("/Voxel/Settings/file")));
		gameSettings.read();
	}

	@Override
	public void restart() {
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public void dispose() {
		gameSettings.update();
		gameSettings.save();
	}

}
