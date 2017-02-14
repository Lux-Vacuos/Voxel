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

package net.luxvacuos.voxel.client.rendering.api.nanovg;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.nvgluCreateFramebuffer;
import static org.lwjgl.nanovg.NanoVGGL3.nvgluDeleteFramebuffer;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.nanovg.NVGLUFramebuffer;

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;

public class Composite {

	private NVGLUFramebuffer[] fbos;
	private RawModel quad;
	private List<CompositeEffect> effects;

	public Composite(Window window, int width, int height) {
		effects = new ArrayList<>();
		fbos = new NVGLUFramebuffer[2];
		fbos[0] = nvgluCreateFramebuffer(window.getNVGID(), width, height, 0);
		fbos[1] = nvgluCreateFramebuffer(window.getNVGID(), width, height, 0);
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = window.getResourceLoader().loadToVAO(positions, 2);
	}

	public void render(IWindow window, Window wnd) {
		for (CompositeEffect compositeEffect : effects) {
			NVGLUFramebuffer tmp = fbos[0];
			fbos[0] = fbos[1];
			fbos[1] = tmp;
			compositeEffect.render(fbos, quad, wnd, window);
		}
	}

	public void dispose(Window window) {
		nvgluDeleteFramebuffer(window.getNVGID(), fbos[0]);
		nvgluDeleteFramebuffer(window.getNVGID(), fbos[1]);
		for (CompositeEffect compositeEffect : effects) {
			compositeEffect.dispose();
		}
	}

	public void addEffect(CompositeEffect effect) {
		effects.add(effect);
	}

	public NVGLUFramebuffer[] getFbos() {
		return fbos;
	}
}
