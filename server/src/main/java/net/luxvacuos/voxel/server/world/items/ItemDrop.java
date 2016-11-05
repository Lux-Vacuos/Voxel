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

package net.luxvacuos.voxel.server.world.items;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector3;

import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.server.world.block.BlockBase;
import net.luxvacuos.voxel.universal.ecs.components.AABB;
import net.luxvacuos.voxel.universal.ecs.components.Position;
import net.luxvacuos.voxel.universal.ecs.components.Velocity;

public class ItemDrop extends Entity {

	private BlockBase block;

	public ItemDrop(Vector3d pos, BlockBase block, float scale) {
		Vector3 min = new Vector3(-0.2f, -0.2f, -0.2f);
		Vector3 max = new Vector3(0.2f, 0.2f, 0.2f);
		this.add(new Position(pos));
		this.add(new Velocity());
		this.add(new AABB(min, max).setBoundingBox(min, max));
		this.block = block;
	}

	public ItemDrop(BlockBase block) {
		this(new Vector3d(), block, 0f);
	}

	public BlockBase getBlock() {
		return block;
	}

}
