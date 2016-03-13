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

package net.luxvacuos.voxel.client.resources.models;

/**
 * Water Tile
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class WaterTile {

	/**
	 * Tile Size
	 */
	public static final float TILE_SIZE = 0.5f;
	/**
	 * Y Coord of the Tile
	 */
	private float height;
	/**
	 * X and Z Coords
	 */
	private float x, z;

	/**
	 * Constructor, Create a Water Tile
	 * 
	 * @param centerX
	 *            X Coord
	 * @param centerZ
	 *            Z Coord
	 * @param height
	 *            Y Coord
	 */
	public WaterTile(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
	}

	/**
	 * Get Y Coord
	 * 
	 * @return Y Coord
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Get X Coord
	 * 
	 * @return Z Coord
	 */
	public float getX() {
		return x;
	}

	/**
	 * Get Z Coord
	 * 
	 * @return Z Coord
	 */
	public float getZ() {
		return z;
	}

	public static float getTileSize() {
		return TILE_SIZE;
	}

}
