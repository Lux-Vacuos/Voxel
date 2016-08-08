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

package net.luxvacuos.voxel.server.core;

import net.luxvacuos.voxel.universal.core.AbstractGameSettings;

public final class ServerGameSettings extends AbstractGameSettings {

	public ServerGameSettings() { }
	
	@Override
	public void read() {
		VoxelVariables.UPS = Integer.parseInt(getValue("UPS", "60"));
		VoxelVariables.radius = Integer.parseInt(getValue("DrawDistance", "2"));
	}

	public void update() {
		registerValue("SettingsVersion", Integer.toString(ServerGameSettings.VERSION));
		registerValue("UPS", Integer.toString(VoxelVariables.UPS));
		registerValue("DrawDistance", Integer.toString(VoxelVariables.radius));
	}

}
