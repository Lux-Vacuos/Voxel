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

package net.luxvacuos.voxel.client.rendering.api.nanovg.shaders;

import net.luxvacuos.igl.vector.Matrix4d;
import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.ShaderProgram;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.Attribute;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformMatrix;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformSampler;

public class WindowManagerShader extends ShaderProgram {

	private UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	private UniformSampler image = new UniformSampler("image");
	private UniformSampler window = new UniformSampler("window");

	public WindowManagerShader() {
		super(ClientVariables.VERTEX_WINDOW_MANAGER, ClientVariables.FRAGMENT_WINDOW_MANAGER,
				new Attribute(0, "position"));
		super.storeAllUniformLocations(transformationMatrix, image, window);
		connectTextureUnits();
	}

	private void connectTextureUnits() {
		super.start();
		image.loadTexUnit(0);
		window.loadTexUnit(1);
		super.stop();
	}

	public void loadTransformation(Matrix4d matrix) {
		transformationMatrix.loadMatrix(matrix);
	}

}
