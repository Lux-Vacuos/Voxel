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

import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import net.luxvacuos.voxel.universal.resources.IDisposable;

public class VBO implements IDisposable {

	private final int vboId;
	private final int type;

	private VBO(int vboId, int type) {
		this.vboId = vboId;
		this.type = type;
	}

	public static VBO create(int type) {
		int id = glGenBuffers();
		return new VBO(id, type);
	}

	public void bind() {
		glBindBuffer(type, vboId);
	}

	public void unbind() {
		glBindBuffer(type, 0);
	}

	public void storeData(int[] data, int param) {
		glBufferData(type, data, param);
	}

	public void storeData(float[] data, int param) {
		glBufferData(type, data, param);
	}

	@Override
	public void dispose() {
		glDeleteBuffers(vboId);
	}

}