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

import static org.lwjgl.assimp.Assimp.aiReleaseImport;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;

import net.luxvacuos.voxel.universal.resources.IDisposable;

public class Model implements IDisposable {
	private AIScene scene;
	private List<Mesh> meshes;
	private List<Material> materials;

	public Model(AIScene scene, String rootPath) {
		this.scene = scene;

		int meshCount = scene.mNumMeshes();
		PointerBuffer meshesBuffer = scene.mMeshes();
		meshes = new ArrayList<>();
		for (int i = 0; i < meshCount; ++i) {
			meshes.add(new Mesh(AIMesh.create(meshesBuffer.get(i))));
		}

		int materialCount = scene.mNumMaterials();
		PointerBuffer materialsBuffer = scene.mMaterials();
		materials = new ArrayList<>();
		for (int i = 0; i < materialCount; ++i) {
			materials.add(new Material(AIMaterial.create(materialsBuffer.get(i)), rootPath));
		}
	}

	@Override
	public void dispose() {
		try {
			aiReleaseImport(scene);
		} catch (NullPointerException e) {
			// XXX: Assimp + animations = NPE...
		}
		for (Material material : materials) {
			material.dispose();
		}
		for (Mesh mesh : meshes) {
			mesh.dispose();
		}
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}

}
