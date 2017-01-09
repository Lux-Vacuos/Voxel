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

package net.luxvacuos.voxel.server.world.entities;

import com.badlogic.ashley.core.Entity;

import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.universal.ecs.components.AABB;
import net.luxvacuos.voxel.universal.ecs.components.Health;
import net.luxvacuos.voxel.universal.ecs.components.Player;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Rotation;
import net.luxvacuos.voxel.universal.ecs.components.Scale;
import net.luxvacuos.voxel.universal.ecs.components.Velocity;

public class PlayerEntity extends Entity {
	
	private String id;

	public PlayerEntity(String id) {
		this.id = id;
		this.add(new Position());
		this.add(new Rotation());
		this.add(new Player());
		this.add(new Velocity());
		this.add(new Scale());
		this.add(new AABB(new Vector3d(-0.25f, -1.4f, -0.25f), new Vector3d(0.25f, 0.2f, 0.25f))
				.setBoundingBox(new Vector3d(-0.25f, -1.4f, -0.25f), new Vector3d(0.25f, 0.2f, 0.25f)));
		super.add(new Health(20));
	}
	
	public String getId() {
		return id;
	}

}
