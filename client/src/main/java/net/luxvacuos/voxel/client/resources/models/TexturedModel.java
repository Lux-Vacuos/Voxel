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
 * Textured Model
 * 
 * @author Guerra24 <pablo230699@hotmail.com>
 * @category Assets
 */
public class TexturedModel {

	/**
	 * Raw Model
	 */
	private RawModel rawModel;
	/**
	 * Model Texture
	 */
	private ModelTexture texture;

	/**
	 * Constructor, Create a Textured Model
	 * 
	 * @param model
	 *            RawModel
	 * @param texture
	 *            ModelTexture
	 */
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	/**
	 * Get Raw Model
	 * 
	 * @return RawModel
	 */
	public RawModel getRawModel() {
		return rawModel;
	}

	/**
	 * Get Model Texture
	 * 
	 * @return ModelTexture
	 */
	public ModelTexture getTexture() {
		return texture;
	}
}
