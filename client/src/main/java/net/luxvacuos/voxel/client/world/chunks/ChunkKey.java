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

package net.luxvacuos.voxel.client.world.chunks;

public class ChunkKey implements Cloneable {
	public int cx, cy, cz;

	public ChunkKey(int cx, int cy, int cz) {
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
	}

	@Override
	public int hashCode() {
		return (cx << 8) ^ (cy << 4) ^ cz;
	}

	@Override
	public boolean equals(Object obj) {
		ChunkKey key = (ChunkKey) obj;
		if (key.cx != cx)
			return false;
		if (key.cy != cy)
			return false;
		if (key.cz != cz)
			return false;
		return true;
	}

	@Override
	public ChunkKey clone() {
		return new ChunkKey(cx, cy, cz);
	}

}