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

package net.luxvacuos.voxel.universal.world.utils;

public final class LightNode {
	private final int x, y, z;
	private int value;
	private final Type type;
	
	public LightNode(int x, int y, int z, Type type) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = type;
	}
	
	public LightNode(int x, int y, int z, int value, Type type) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.value = value;
		this.type = type;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public enum Type {
		ADD, REMOVE
	}

}
