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

package net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data;

import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform4f;

import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Material;

public class UniformMaterial extends UniformArray {

	private Material currentValue;
	private boolean used = false;

	public UniformMaterial(String matName) {
		super(matName + ".diffuse", matName + ".roughness", matName + ".metallic", matName + ".diffuseTex",
				matName + ".normalTex", matName + ".roughnessTex", matName + ".metallicTex");
	}

	public void loadMaterial(Material value) {
		if (!used || !currentValue.equals(value)) {
			glUniform4f(super.getLocation()[0], value.getDiffuse().getX(), value.getDiffuse().getY(),
					value.getDiffuse().getZ(), value.getDiffuse().getW());
			glUniform1f(super.getLocation()[1], value.getRoughness());
			glUniform1f(super.getLocation()[2], value.getMetallic());
			glUniform1i(super.getLocation()[3], 0);
			glUniform1i(super.getLocation()[4], 1);
			glUniform1i(super.getLocation()[5], 2);
			glUniform1i(super.getLocation()[6], 3);
			used = true;
			currentValue = value;
		}
	}

}
