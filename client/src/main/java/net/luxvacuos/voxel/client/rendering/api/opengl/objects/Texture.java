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

package net.luxvacuos.voxel.client.rendering.api.opengl.objects;

import static org.lwjgl.opengl.GL11.glDeleteTextures;

import net.luxvacuos.voxel.universal.resources.IDisposable;

/**
 * Texture
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class Texture implements IDisposable {
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
	public Texture(int id) {
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

	@Override
	public void dispose() {
		glDeleteTextures(textureID);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Texture)
			return false;
		Texture t = (Texture) obj;
		return t.getID() == textureID;
	}

}
