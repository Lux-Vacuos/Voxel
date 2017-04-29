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

package net.luxvacuos.voxel.universal.ecs.entities;

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;

import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.universal.ecs.components.AABB;
import net.luxvacuos.voxel.universal.ecs.components.ChunkLoader;
import net.luxvacuos.voxel.universal.ecs.components.Health;
import net.luxvacuos.voxel.universal.ecs.components.Player;

public class PlayerEntity extends BasicEntity {

	public PlayerEntity(String name) {
		super(name);
		this.add(new Player());
		this.add(new AABB(new Vector3d(-0.25f, -1.5f, -0.25f), new Vector3d(0.25f, 0.2f, 0.25f)));
		this.add(new Health(20));
		this.add(new ChunkLoader((int) REGISTRY.getRegistryItem("/Voxel/Settings/World/chunkRadius")));
	}

	public PlayerEntity(String name, String uuid) {
		super(name, uuid);
		this.add(new Player());
		this.add(new AABB(new Vector3d(-0.25f, -1.5f, -0.25f), new Vector3d(0.25f, 0.2f, 0.25f)));
		this.add(new Health(20));
		this.add(new ChunkLoader((int) REGISTRY.getRegistryItem("/Voxel/Settings/World/chunkRadius")));
	}

}
