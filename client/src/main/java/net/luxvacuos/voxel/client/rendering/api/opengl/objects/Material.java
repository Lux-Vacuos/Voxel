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
import static org.lwjgl.assimp.Assimp.aiGetErrorString;
import static org.lwjgl.assimp.Assimp.aiGetMaterialColor;
import static org.lwjgl.assimp.Assimp.aiTextureType_NONE;

import org.lwjgl.assimp.AIColor4D;
import org.lwjgl.assimp.AIMaterial;

import net.luxvacuos.igl.vector.Vector4f;
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
	 * @param material Assimp Material
	 */
	public Material(AIMaterial material) {
		AIColor4D diffuse = AIColor4D.create();
		if (aiGetMaterialColor(material, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0, diffuse) != 0) {
			throw new IllegalStateException(aiGetErrorString());
		}
		this.diffuse = new Vector4f(diffuse.r(), diffuse.g(), diffuse.b(), diffuse.a());
		this.roughness = roughness;
		this.metallic = metallic;
		this.diffuseTexture = DefaultData.diffuse;
		this.normalTexture = DefaultData.normal;
		this.roughnessTexture = DefaultData.roughness;
		this.metallicTexture = DefaultData.metallic;
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

}
