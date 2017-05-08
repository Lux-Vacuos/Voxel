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
import static org.lwjgl.assimp.Assimp.aiGetErrorString;
import static org.lwjgl.assimp.Assimp.aiImportFileFromMemory;
import static org.lwjgl.assimp.Assimp.aiProcess_CalcTangentSpace;
import static org.lwjgl.assimp.Assimp.aiProcess_FindInvalidData;
import static org.lwjgl.assimp.Assimp.aiProcess_FlipUVs;
import static org.lwjgl.assimp.Assimp.aiProcess_GenNormals;
import static org.lwjgl.assimp.Assimp.aiProcess_ImproveCacheLocality;
import static org.lwjgl.assimp.Assimp.aiProcess_OptimizeMeshes;
import static org.lwjgl.assimp.Assimp.aiProcess_SplitLargeMeshes;
import static org.lwjgl.assimp.Assimp.aiProcess_Triangulate;
import static org.lwjgl.assimp.Assimp.aiProcess_ValidateDataStructure;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.assimp.AIScene;

import net.luxvacuos.igl.Logger;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Model;
import net.luxvacuos.voxel.universal.core.GlobalVariables;

public class AssimpResourceLoader {

	public AssimpResourceLoader() {
	}

	public Model loadModel(String filePath) {
		Logger.log("Loading Model: " + filePath);
		String fileName = "assets/" + GlobalVariables.REGISTRY.getRegistryItem("/Voxel/Settings/Graphics/assets") + "/"
				+ filePath;
		String ext = fileName.split("\\.")[1];
		ByteBuffer bFile = null;
		try {
			bFile = ResourceLoader.ioResourceToByteBuffer(fileName, 512);
		} catch (IOException e) {
			e.printStackTrace();
		}
		AIScene scene = aiImportFileFromMemory(bFile,
				aiProcess_Triangulate | aiProcess_FlipUVs | aiProcess_GenNormals | aiProcess_SplitLargeMeshes
						| aiProcess_OptimizeMeshes | aiProcess_CalcTangentSpace | aiProcess_ImproveCacheLocality
						| aiProcess_ValidateDataStructure | aiProcess_FindInvalidData | aiProcess_FixInfacingNormals,
				ext);
		if (scene == null || scene.mFlags() == AI_SCENE_FLAGS_INCOMPLETE || scene.mRootNode() == null) {
			Logger.error(aiGetErrorString());
		}
		return new Model(scene, filePath.substring(0, filePath.lastIndexOf("/")));
	}

}
