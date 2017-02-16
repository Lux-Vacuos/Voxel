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
import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.igl.vector.Vector4f;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.ShaderProgram;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.Attribute;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformBoolean;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformMatrix;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformSampler;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformVec2;
import net.luxvacuos.voxel.client.rendering.api.opengl.shaders.data.UniformVec4;

public class WindowManagerShader extends ShaderProgram {

	private UniformMatrix transformationMatrix = new UniformMatrix("transformationMatrix");
	private UniformSampler image = new UniformSampler("image");
	private UniformSampler window = new UniformSampler("window");
	private UniformVec2 resolution = new UniformVec2("resolution");
	private UniformVec4 frame = new UniformVec4("frame");
	private UniformBoolean blurBehind = new UniformBoolean("blurBehind");

	public WindowManagerShader(String type) {
		super("wm/V_" + type + ".glsl", "wm/F_" + type + ".glsl", new Attribute(0, "position"));
		super.storeAllUniformLocations(transformationMatrix, image, window, resolution, frame, blurBehind);
		connectTextureUnits();
	}

	private void connectTextureUnits() {
		super.start();
		image.loadTexUnit(0);
		window.loadTexUnit(1);
		super.stop();
	}

	public void loadFrame(Vector4f frame) {
		this.frame.loadVec4(frame);
	}

	public void loadResolution(Vector2f res) {
		resolution.loadVec2(res);
	}

	public void loadBlurBehind(boolean blur) {
		blurBehind.loadBoolean(blur);
	}

	public void loadTransformation(Matrix4d matrix) {
		transformationMatrix.loadMatrix(matrix);
	}

}
