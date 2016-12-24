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

package net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data;

import static org.lwjgl.opengl.GL20.glUniform2f;

import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.igl.vector.Vector2f;

public class UniformVec2 extends Uniform {

	private float currentX;
	private float currentY;
	private boolean used = false;

	public UniformVec2(String name) {
		super(name);
	}

	public void loadVec2(Vector2d vector) {
		loadVec2((float) vector.x, (float) vector.y);
	}

	public void loadVec2(Vector2f vector) {
		loadVec2(vector.x, vector.y);
	}

	public void loadVec2(float x, float y) {
		if (!used || x != currentX || y != currentY) {
			this.currentX = x;
			this.currentY = y;
			used = true;
			glUniform2f(super.getLocation(), x, y);
		}
	}

}
