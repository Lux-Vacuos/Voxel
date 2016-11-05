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

package net.luxvacuos.voxel.server.world.block;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

import net.luxvacuos.igl.vector.Vector3d;
import net.luxvacuos.voxel.server.world.items.ItemDrop;

public abstract class BlockBase {
	protected transient boolean affectedByGravity = false;
	protected transient boolean collision = true;
	protected transient boolean fluid = false;
	protected transient boolean transparent = false;

	public abstract byte getId();

	public BoundingBox getBoundingBox(Vector3d pos) {
		return new BoundingBox(new Vector3(pos.x, pos.y, pos.z), new Vector3(pos.x + 1, pos.y + 1, pos.z + 1));
	}

	public ItemDrop getDrop(Vector3d pos) {
		return new ItemDrop(pos, this, 0.2f);
	}

	public boolean isAffectedByGravity() {
		return affectedByGravity;
	}

	public boolean isCollision() {
		return collision;
	}

	public boolean isFluid() {
		return fluid;
	}

	public boolean isTransparent() {
		return transparent;
	}

}
