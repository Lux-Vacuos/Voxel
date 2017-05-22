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

package net.luxvacuos.voxel.universal.util.registry;

public class Key implements Comparable<Key> {

	private String key;
	private boolean save;

	public Key(String key) {
		this.key = key;
	}

	public Key(String key, boolean save) {
		this.key = key;
		this.save = save;
	}

	@Override
	public int compareTo(Key o) {
		return key.compareTo(o.getKey());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Key)
			return key.equals(((Key) obj).getKey());
		return false;
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public String toString() {
		return key;
	}

	public String getKey() {
		return key;
	}

	public boolean saveKey() {
		return save;
	}

}
