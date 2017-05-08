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

import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_DIFFUSE;
import static org.lwjgl.assimp.Assimp.AI_MATKEY_COLOR_SPECULAR;
import static org.lwjgl.assimp.Assimp.aiGetMaterialColor;
import static org.lwjgl.assimp.Assimp.aiGetMaterialTexture;
import static org.lwjgl.assimp.Assimp.aiGetMaterialTextureCount;
import static org.lwjgl.assimp.Assimp.aiReturn_SUCCESS;
import static org.lwjgl.assimp.Assimp.aiTextureType_DIFFUSE;
import static org.lwjgl.assimp.Assimp.aiTextureType_NONE;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIString;

import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.core.ClientInternalSubsystem;
import net.luxvacuos.voxel.universal.resources.IDisposable;

/**
 * Material
 * 
 */
public class Material implements IDisposable {

	private Vector4f diffuse;
	private float roughness, metallic;
	private Texture diffuseTexture, normalTexture, roughnessTexture, metallicTexture;

	/**
	 * 
	 * @param baseColor
	 *            Color.
	 * @param roughness
	 *            Material Roughness.
	 * @param metallic
	 *            Material Metallic.
	 * @param specular
	 *            Material Specular.
	 * @param diffuse
	 *            Diffuse texutre.
	 * @param normal
	 *            Normal texture.
	 */
	public Material(Vector4f diffuse, float roughness, float metallic) {
		this.diffuse = diffuse;
		this.roughness = roughness;
		this.metallic = metallic;
		this.diffuseTexture = DefaultData.diffuse;
		this.normalTexture = DefaultData.normal;
		this.roughnessTexture = DefaultData.roughness;
		this.metallicTexture = DefaultData.metallic;
	}

	/**
	 * 
	 * @param material
	 *            Assimp Material
	 */
	public Material(AIMaterial material, String rootPath) {
		this.diffuse = new Vector4f(0.8f, 0.8f, 0.8f, 1);
		this.roughness = 0.5f;
		this.metallic = 0;
		this.diffuseTexture = DefaultData.diffuse;
		this.normalTexture = DefaultData.normal;
		this.roughnessTexture = DefaultData.roughness;
		this.metallicTexture = DefaultData.metallic;

		AIColor4D diffuse = AIColor4D.create();
		AIColor4D data = AIColor4D.create();
		if (aiGetMaterialColor(material, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, diffuse) == aiReturn_SUCCESS) {
			this.diffuse = new Vector4f(diffuse.r(), diffuse.g(), diffuse.b(), diffuse.a());
		}
		if (aiGetMaterialColor(material, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0, data) == aiReturn_SUCCESS) {
			this.roughness = data.r();
			this.metallic = data.g();
		}
		if (aiGetMaterialTextureCount(material, aiTextureType_DIFFUSE) > 0) {
			AIString path = AIString.create();
			if (aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 0, path, (IntBuffer) null, (IntBuffer) null,
					(FloatBuffer) null, (IntBuffer) null, (IntBuffer) null, (IntBuffer) null) == aiReturn_SUCCESS)
				this.diffuseTexture = loadTexture(path, rootPath);

		}
		if (aiGetMaterialTextureCount(material, aiTextureType_DIFFUSE) > 1) {
			AIString path = AIString.create();
			if (aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 1, path, (IntBuffer) null, (IntBuffer) null,
					(FloatBuffer) null, (IntBuffer) null, (IntBuffer) null, (IntBuffer) null) == aiReturn_SUCCESS)
				this.normalTexture = loadTextureMisc(path, rootPath);

		}
		if (aiGetMaterialTextureCount(material, aiTextureType_DIFFUSE) > 2) {
			AIString path = AIString.create();
			if (aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 2, path, (IntBuffer) null, (IntBuffer) null,
					(FloatBuffer) null, (IntBuffer) null, (IntBuffer) null, (IntBuffer) null) == aiReturn_SUCCESS) {
				this.roughnessTexture = loadTextureMisc(path, rootPath);
				this.roughness = 1f;
			}

		}
		if (aiGetMaterialTextureCount(material, aiTextureType_DIFFUSE) > 3) {
			AIString path = AIString.create();
			if (aiGetMaterialTexture(material, aiTextureType_DIFFUSE, 3, path, (IntBuffer) null, (IntBuffer) null,
					(FloatBuffer) null, (IntBuffer) null, (IntBuffer) null, (IntBuffer) null) == aiReturn_SUCCESS) {
				this.metallicTexture = loadTextureMisc(path, rootPath);
				this.metallic = 1f;
			}

		}
	}

	public void setDiffuseTexture(Texture diffuseTexture) {
		this.diffuseTexture = diffuseTexture;
	}

	public void setNormalTexture(Texture normalTexture) {
		this.normalTexture = normalTexture;
	}

	public void setRoughnessTexture(Texture roughnessTexture) {
		this.roughnessTexture = roughnessTexture;
	}

	public void setMetallicTexture(Texture metallicTexture) {
		this.metallicTexture = metallicTexture;
	}

	public Vector4f getDiffuse() {
		return diffuse;
	}

	public float getMetallic() {
		return metallic;
	}

	public float getRoughness() {
		return roughness;
	}

	public Texture getDiffuseTexture() {
		return diffuseTexture;
	}

	public Texture getMetallicTexture() {
		return metallicTexture;
	}

	public Texture getNormalTexture() {
		return normalTexture;
	}

	public Texture getRoughnessTexture() {
		return roughnessTexture;
	}

	@Override
	public void dispose() {
		if (diffuseTexture != null)
			diffuseTexture.dispose();
		if (metallicTexture != null)
			metallicTexture.dispose();
		if (normalTexture != null)
			normalTexture.dispose();
		if (roughnessTexture != null)
			roughnessTexture.dispose();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Material)
			return false;
		Material t = (Material) obj;
		return t.getDiffuse().getX() == diffuse.getX() && t.getDiffuse().getY() == diffuse.getY()
				&& t.getDiffuse().getZ() == diffuse.getZ() && t.getRoughness() == roughness
				&& t.getMetallic() == metallic && t.getDiffuse().equals(diffuse)
				&& t.getNormalTexture().equals(normalTexture) && t.getRoughnessTexture().equals(roughnessTexture)
				&& t.getMetallicTexture().equals(metallicTexture);
	}

	private static Texture loadTexture(AIString path, String rootPath) {
		String file = path.dataString();
		file = file.replace("\\", "/");
		file = file.replace("//", "");
		int count = file.split("\\.").length;
		count--;
		count /= 2;
		for (int i = 0; i < count; i++)
			rootPath = rootPath.substring(0, rootPath.lastIndexOf("/"));
		file = file.substring(2);
		return ClientInternalSubsystem.getInstance().getGameWindow().getResourceLoader().loadTexture(rootPath + file);
	}

	private static Texture loadTextureMisc(AIString path, String rootPath) {
		String file = path.dataString();
		file = file.replace("\\", "/");
		file = file.replace("//", "");
		int count = file.split("\\.").length;
		count--;
		count /= 2;
		for (int i = 0; i < count; i++)
			rootPath = rootPath.substring(0, rootPath.lastIndexOf("/"));
		file = file.substring(2);
		return ClientInternalSubsystem.getInstance().getGameWindow().getResourceLoader()
				.loadTextureMisc(rootPath + file);
	}

}
