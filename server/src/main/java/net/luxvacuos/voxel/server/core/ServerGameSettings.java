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

package net.luxvacuos.voxel.server.core;

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.universal.core.AbstractGameSettings;

public class ServerGameSettings extends AbstractGameSettings {

	@Override
	public void read() {
		Logger.log("Loading data to registry...");
		
		REGISTRY.register("/Voxel/Settings/Core/ups", 			Integer.parseInt(getValue("UPS", "20")));
		
		REGISTRY.register("/Voxel/Server/port", 				Integer.parseInt(getValue("port", "44454")));
		
		REGISTRY.register("/Voxel/Settings/World/chunkRadius", 	Integer.parseInt(getValue("chunkRadius", "6")));
		REGISTRY.register("/Voxel/Settings/World/chunkManagerThreads", 		Integer.parseInt(getValue("chunkManagerThreads", "3")));
		
		REGISTRY.register("/Voxel/Simulation/World/name", 		getValue("worldName", "world"));
		
		Logger.log("Load completed");
	}

	@Override
	public void update() {
		Logger.log("Updating registry...");
		registerValue("SettingsVersion", 		Integer.toString(ServerGameSettings.VERSION));
		
		registerValue("UPS", 					Integer.toString((int) REGISTRY.getRegistryItem("/Voxel/Settings/Core/ups")));
		
		registerValue("port", 					Integer.toString((int) REGISTRY.getRegistryItem("/Voxel/Server/port")));
		
		registerValue("chunkRadius", 			Integer.toString((int) REGISTRY.getRegistryItem("/Voxel/Settings/World/chunkRadius")));
		registerValue("chunkManagerThreads", 	Integer.toString((int) REGISTRY.getRegistryItem("/Voxel/Settings/World/chunkManagerThreads")));
		
		registerValue("worldName", 				(String) REGISTRY.getRegistryItem("/Voxel/Simulation/World/name"));
		
		Logger.log("Updated completed");
	}

}
