/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

package net.luxvacuos.voxel.universal.world.utils;

import com.badlogic.gdx.math.Vector3;

public final class ChunkNode {

	private final int x, y, z;

	public ChunkNode(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return this.z;
	}

	public Vector3 asVector3() {
		return new Vector3(this.x * 16, this.y * 16, this.z * 16);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		result = prime * result + z;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ChunkNode))
			return false;
		ChunkNode other = (ChunkNode) obj;
		if (x != other.x)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	public static ChunkNode getFromBlockCoords(int x, int y, int z) {
		int cx = x >> 4;
		int cy = y >> 4;
		int cz = z >> 4;

		return new ChunkNode(cx, cy, cz);
	}

	public static ChunkNode getFromBlockNode(BlockNode block) {
		int cx = block.getX() >> 4;
		int cy = block.getY() >> 4;
		int cz = block.getZ() >> 4;

		return new ChunkNode(cx, cy, cz);
	}
}
