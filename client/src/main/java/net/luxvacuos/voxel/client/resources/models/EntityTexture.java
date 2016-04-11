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

import java.nio.ByteBuffer;

/**
 * Entity Texture
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class EntityTexture {
	/**
	 * Texture Width
	 */
	private int width;
	/**
	 * Texture Height
	 */
	private int height;
	/**
	 * ByteBuffer
	 */
	private ByteBuffer buffer;

	/**
	 * Constructor, Create an Entity Texutre
	 * 
	 * @param buffer
	 *            ByteBuffer
	 * @param width
	 *            Texture Width
	 * @param height
	 *            Texture Height
	 */
	public EntityTexture(ByteBuffer buffer, int width, int height) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}

	/**
	 * Get the Texture Width
	 * 
	 * @return Texture Width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the Texture Height
	 * 
	 * @return Texture Height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Get Texture ByteBuffer
	 * 
	 * @return ByteBuffer
	 */
	public ByteBuffer getBuffer() {
		return buffer;
	}
}
