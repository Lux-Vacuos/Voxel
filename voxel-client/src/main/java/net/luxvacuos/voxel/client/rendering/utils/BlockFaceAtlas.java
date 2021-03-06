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

package net.luxvacuos.voxel.client.rendering.utils;

import net.luxvacuos.voxel.universal.world.utils.BlockFace;

public class BlockFaceAtlas {
	private final String up, down, north, south, east, west;
	
	public BlockFaceAtlas(String name) {
		this.up = name;
		this.down = name;
		this.north = name;
		this.south = name;
		this.east = name;
		this.west = name;
	}
	
	public BlockFaceAtlas(String up_down, String sides) {
		this.up = up_down;
		this.down = up_down;
		this.north = sides;
		this.south = sides;
		this.east = sides;
		this.west = sides;
	}
	
	public BlockFaceAtlas(String up, String down, String sides) {
		this.up = up;
		this.down = down;
		this.north = sides;
		this.south = sides;
		this.east = sides;
		this.west = sides;
	}
	
	public BlockFaceAtlas(String top, String bottom, String north, String south, String east, String west) {
		this.up = top;
		this.down = bottom;
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
	}
	
	public String get(BlockFace face) {
		switch(face) {
		case UP:
			return this.up;
		case DOWN:
			return this.down;
		case NORTH:
			return this.north;
		case SOUTH:
			return this.south;
		case EAST:
			return this.east;
		case WEST:
			return this.west;
		default:
			return "Missing";
		}
	}

}
