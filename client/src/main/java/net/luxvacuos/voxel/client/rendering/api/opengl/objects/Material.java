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

import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.resources.ResourceLoader;

/**
 * Material
 * 
 */
public class Material {

	private Vector4f diffuse;
	private float roughness, metallic;
	private Texture diffuseTexture, normalTexture, roughnessTexture, metallicTexture;
	private static Texture dDiffuse, dNormal, dRoughness, dMetallic;

	public static void init(ResourceLoader loader) {
		dDiffuse = loader.loadTextureMisc("def/d");
		dNormal = loader.loadTextureMisc("def/d_n");
		dRoughness = loader.loadTextureMisc("def/d_r");
		dMetallic = loader.loadTextureMisc("def/d_m");
	}

	/**
	 * 
	 * @param baseColor
	 *            Color, set alpha to -1 when using textures.
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
	public Material(Vector4f diffuse, float roughness, float metallic, Texture diffuseTexture, Texture normalTexture,
			Texture roughnessTexture, Texture metallicTexture) {
		this.diffuse = diffuse;
		this.roughness = roughness;
		this.metallic = metallic;
		if (diffuseTexture == null)
			this.diffuseTexture = dDiffuse;
		else
			this.diffuseTexture = diffuseTexture;
		if (normalTexture == null)
			this.normalTexture = dNormal;
		else
			this.normalTexture = normalTexture;
		if (roughnessTexture == null)
			this.roughnessTexture = dRoughness;
		else
			this.roughnessTexture = roughnessTexture;
		if (metallicTexture == null)
			this.metallicTexture = dMetallic;
		else
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
