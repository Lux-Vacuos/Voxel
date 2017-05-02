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

package net.luxvacuos.voxel.client.resources;

import static org.lwjgl.assimp.Assimp.*;

import java.io.IOException;

import org.lwjgl.assimp.AIScene;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.universal.core.GlobalVariables;

public class AssimpResourceLoader {
	
	private ResourceLoader loader;

	public AssimpResourceLoader(ResourceLoader loader) {
		this.loader = loader;
	}

	public void loadModel(String file) {
		AIScene scene = null;
		try {
			scene = aiImportFile(
					ResourceLoader.ioResourceToByteBuffer(
							"assets/" + GlobalVariables.REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/assets")
									+ "/models/" + file,
							8192),
					aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_GenNormals | aiProcess_SplitLargeMeshes
							| aiProcess_OptimizeMeshes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (scene == null || scene.mFlags() == AI_SCENE_FLAGS_INCOMPLETE || scene.mRootNode() == null) {
			Logger.error(aiGetErrorString());
		}
	}

}
