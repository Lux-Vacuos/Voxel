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

import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;

import net.luxvacuos.igl.vector.Matrix4d;

public class UniformMatrix extends Uniform {

	private DoubleBuffer matrixBuffer = BufferUtils.createDoubleBuffer(16);

	public UniformMatrix(String name) {
		super(name);
	}

	public void loadMatrix(Matrix4d matrix) {
		matrixBuffer.clear();
		matrix.store(matrixBuffer);
		matrixBuffer.flip();

		double[] dm = new double[16];
		matrixBuffer.get(dm);
		float[] fm = new float[16];
		fm[0] = (float) dm[0];
		fm[1] = (float) dm[1];
		fm[2] = (float) dm[2];
		fm[3] = (float) dm[3];
		fm[4] = (float) dm[4];
		fm[5] = (float) dm[5];
		fm[6] = (float) dm[6];
		fm[7] = (float) dm[7];
		fm[8] = (float) dm[8];
		fm[9] = (float) dm[9];
		fm[10] = (float) dm[10];
		fm[11] = (float) dm[11];
		fm[12] = (float) dm[12];
		fm[13] = (float) dm[13];
		fm[14] = (float) dm[14];
		fm[15] = (float) dm[15];
		glUniformMatrix4fv(super.getLocation(), false, fm);
	}

}
