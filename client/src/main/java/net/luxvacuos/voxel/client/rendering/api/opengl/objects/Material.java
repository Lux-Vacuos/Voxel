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

package net.luxvacuos.voxel.client.rendering.api.opengl.objects;

import net.luxvacuos.igl.vector.Vector4f;

/**
 * Material
 * 
 */
public class Material {

	private Vector4f baseColor;
	private float roughness, metallic, specular;
	private Texture diffuse, normal;

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
	public Material(Vector4f baseColor, float roughness, float metallic, float specular, Texture diffuse,
			Texture normal) {
		this.baseColor = baseColor;
		this.roughness = roughness;
		this.metallic = metallic;
		this.diffuse = diffuse;
		this.normal = normal;
		this.specular = specular;
	}

	public Vector4f getBaseColor() {
		return baseColor;
	}

	public float getMetallic() {
		return metallic;
	}

	public float getRoughness() {
		return roughness;
	}

	public Texture getDiffuse() {
		return diffuse;
	}

	public Texture getNormal() {
		return normal;
	}

	public float getSpecular() {
		return specular;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Material)
			return false;
		Material t = (Material) obj;
		return t.getBaseColor().getX() == baseColor.getX() && t.getBaseColor().getY() == baseColor.getY()
				&& t.getBaseColor().getZ() == baseColor.getZ() && t.getRoughness() == roughness
				&& t.getMetallic() == metallic && t.getSpecular() == specular && t.getDiffuse().equals(diffuse)
				&& t.getNormal().equals(normal);
	}

}
