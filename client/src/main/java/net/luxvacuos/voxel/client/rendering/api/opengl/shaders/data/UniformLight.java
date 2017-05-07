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

import net.luxvacuos.igl.vector.Vector3f;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.Light;
import static org.lwjgl.opengl.GL20.*;

public class UniformLight extends UniformArray {

	private Light currentValue;
	private boolean used;

	public UniformLight(String name) {
		super(name + ".position", name + ".color", name + ".direction", name + ".radius", name + ".inRadius", name + ".type");
	}

	public void loadLight(Light light) {
		if (!used || !currentValue.equals(light)) {
			Vector3f pos = light.getPosition();
			Vector3f color = light.getColor();
			Vector3f dir = light.getDirection();
			glUniform3f(super.getLocation()[0], pos.getX(), pos.getY(), pos.getZ());
			glUniform3f(super.getLocation()[1], color.getX(), color.getY(), color.getZ());
			if (light.getType() == 1) {
				glUniform3f(super.getLocation()[2], dir.getX(), dir.getY(), dir.getZ());
				glUniform1f(super.getLocation()[3], (float) Math.cos(Math.toRadians(light.getRadius())));
				glUniform1f(super.getLocation()[4], (float) Math.cos(Math.toRadians(light.getInRadius())));
			}
			glUniform1i(super.getLocation()[5], light.getType());
			used = true;
			currentValue = light;
		}
	}
}
