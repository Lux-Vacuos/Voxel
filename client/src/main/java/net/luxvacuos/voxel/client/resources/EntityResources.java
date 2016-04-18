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

package net.luxvacuos.voxel.client.resources;

import net.luxvacuos.voxel.client.core.exception.LoadTextureException;
import net.luxvacuos.voxel.client.resources.models.ModelTexture;
import net.luxvacuos.voxel.client.resources.models.TexturedModel;

public class EntityResources {

	private static TexturedModel guineaPig;

	static void loadEntityResources(Loader loader) throws LoadTextureException, Exception {
		guineaPig = new TexturedModel(loader.getObjLoader().loadObjModel("GuineaPig"),
				new ModelTexture(loader.loadTextureEntity("GuineaPig")));
	}

	public static TexturedModel getGuineaPig() {
		return guineaPig;
	}

}
