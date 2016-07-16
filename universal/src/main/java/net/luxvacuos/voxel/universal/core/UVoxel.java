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

package net.luxvacuos.voxel.universal.core;

import net.luxvacuos.voxel.universal.resources.UGameResources;

public abstract class UVoxel {

	protected UGameResources gameResources;
	protected String prefix;
	protected boolean client, server;

	public String getPrefix() {
		return prefix;
	}

	public boolean isClient() {
		return client;
	}

	public boolean isServer() {
		return server;
	}

	public UGameResources getGameResources() {
		return gameResources;
	}

}
