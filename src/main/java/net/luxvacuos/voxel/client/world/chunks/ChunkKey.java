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

import java.util.ArrayList;

public class ChunkKey implements Cloneable {
	public int dim, cx, cy, cz;

	public ChunkKey(int dim, int cx, int cy, int cz) {
		this.dim = dim;
		this.cx = cx;
		this.cy = cy;
		this.cz = cz;
	}

	@Override
	public int hashCode() {
		return (dim << 16) ^ (cx << 8) ^ (cy << 4) ^ cz;
	}

	@Override
	public boolean equals(Object obj) {
		ChunkKey key = (ChunkKey) obj;
		if (key.dim != dim)
			return false;
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
		return new ChunkKey(dim, cx, cy, cz);
	}

	private static ArrayList<ChunkKey> pool = new ArrayList<ChunkKey>();
	public static int cnt;

	public static ChunkKey alloc(int dim, int cx, int cy, int cz) {
		ChunkKey c;
		synchronized (pool) {
			int size = pool.size();
			if (size == 0) {
				cnt++;
				return new ChunkKey(dim, cx, cy, cz);
			}
			c = pool.remove(size - 1);
		}
		c.dim = dim;
		c.cx = cx;
		c.cy = cy;
		c.cz = cz;
		return c;
	}

	private static void free(ChunkKey c) {
		synchronized (pool) {
			pool.add(c);
		}
	}

	public void free() {
		free(this);
	}

}