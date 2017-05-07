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

import static org.lwjgl.assimp.Assimp.AI_SCENE_FLAGS_INCOMPLETE;
import static org.lwjgl.assimp.Assimp.aiGetErrorString;
import static org.lwjgl.assimp.Assimp.aiImportFile;
import static org.lwjgl.assimp.Assimp.aiProcess_FlipUVs;
import static org.lwjgl.assimp.Assimp.aiProcess_GenNormals;
import static org.lwjgl.assimp.Assimp.aiProcess_OptimizeMeshes;
import static org.lwjgl.assimp.Assimp.aiProcess_SplitLargeMeshes;
import static org.lwjgl.assimp.Assimp.*;

import java.io.File;

import org.lwjgl.assimp.AIScene;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Model;
import net.luxvacuos.voxel.universal.core.GlobalVariables;

public class AssimpResourceLoader {

	private ResourceLoader loader;

	public AssimpResourceLoader(ResourceLoader loader) {
		this.loader = loader;
	}

	public Model loadModel(String filePath) {
		String fileName = Thread.currentThread().getContextClassLoader().getResource("assets/"
				+ GlobalVariables.REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/assets") + "/models/" + filePath)
				.getFile();
		File file = new File(fileName);
		AIScene scene = aiImportFile(file.getAbsolutePath(),
				aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_GenNormals | aiProcess_SplitLargeMeshes
						| aiProcess_OptimizeMeshes | aiProcess_CalcTangentSpace);
		if (scene == null || scene.mFlags() == AI_SCENE_FLAGS_INCOMPLETE || scene.mRootNode() == null) {
			Logger.error(aiGetErrorString());
		}
		return new Model(scene);
	}

}
