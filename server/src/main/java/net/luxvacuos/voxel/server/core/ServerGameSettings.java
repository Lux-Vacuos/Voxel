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

import net.luxvacuos.voxel.universal.core.AbstractGameSettings;

public class ServerGameSettings extends AbstractGameSettings {

	@Override
	public void read() {
		ServerVariables.UPS = Integer.parseInt(getValue("UPS", "60"));
		ServerVariables.chunk_radius = Integer.parseInt(getValue("chunkDistance", "6"));
		ServerVariables.worldName = getValue("worldName", "world");
		ServerVariables.port = Integer.parseInt(getValue("port", "44454"));
	}

	@Override
	public void update() {
		registerValue("UPS", Integer.toString(ServerVariables.UPS));
		registerValue("chunkDistance", Integer.toString(ServerVariables.chunk_radius));
		registerValue("worldName", ServerVariables.worldName);
		registerValue("port", Integer.toString(ServerVariables.port));
	}

}
