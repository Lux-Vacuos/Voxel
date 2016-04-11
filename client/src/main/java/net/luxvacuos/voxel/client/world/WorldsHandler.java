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

package net.luxvacuos.voxel.client.world;

import java.util.HashMap;
import java.util.Map;

public class WorldsHandler {

	private Map<String, World> worlds;
	private World activeWorld;

	public WorldsHandler() {
		init();
	}

	public void init() {
		worlds = new HashMap<String, World>();
	}

	public void registerWorld(World world) {
		worlds.put(world.getName(), world);
	}

	public void setActiveWorld(String name) {
		activeWorld = worlds.get(name);
	}

	public World getActiveWorld() {
		return activeWorld;
	}

	public void dispose() {
		worlds.clear();
	}

}
