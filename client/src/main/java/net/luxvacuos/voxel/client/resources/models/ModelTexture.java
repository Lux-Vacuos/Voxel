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
 * Model Texture
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class ModelTexture {
	/**
	 * Texture ID
	 */
	private int textureID;

	/**
	 * Constructor, Create a Model Texture
	 * 
	 * @param id
	 *            Texture ID
	 */
	public ModelTexture(int id) {
		this.textureID = id;
	}

	/**
	 * Get Texture ID
	 * 
	 * @return Texture ID
	 */
	public int getID() {
		return textureID;
	}

}
