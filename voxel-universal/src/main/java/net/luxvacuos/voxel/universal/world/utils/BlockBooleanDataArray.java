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

package net.luxvacuos.voxel.universal.world.utils;

public final class BlockBooleanDataArray {
	// [(HEIGHT * WIDTH * Z) + (LENGTH * Y) + X]
	private final boolean[] data;

	public BlockBooleanDataArray() {
		this.data = new boolean[4096]; //16 * 16 * 16 for a ChunkSection
	}

	public BlockBooleanDataArray(boolean[] data) {
		this.data = data;
	}

	public boolean get(int x, int y, int z) {
		return this.data[(16 * 16 * z) + (16 * y) + x];
	}

	public void set(int x, int y, int z, boolean data) {
		this.data[(16 * 16 * z) + (16 * y) + x] = data;
	}

	public final boolean[] getData() {
		return this.data;
	}

}
