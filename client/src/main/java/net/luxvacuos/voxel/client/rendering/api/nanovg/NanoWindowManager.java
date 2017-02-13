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

import static org.lwjgl.nanovg.NanoVG.NVG_IMAGE_PREMULTIPLIED;
import static org.lwjgl.nanovg.NanoVGGL3.nvgluBindFramebuffer;
import static org.lwjgl.nanovg.NanoVGGL3.nvgluCreateFramebuffer;
import static org.lwjgl.nanovg.NanoVGGL3.nvgluDeleteFramebuffer;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.nanovg.NVGLUFramebuffer;

import net.luxvacuos.igl.vector.Vector2d;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.shaders.WindowManagerShader;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.rendering.api.opengl.objects.RawModel;
import net.luxvacuos.voxel.client.util.Maths;

public class NanoWindowManager implements IWindowManager {

	private List<IWindow> windows;
	private Window window;
	private NVGLUFramebuffer[] fbos;
	private WindowManagerShader shader;
	private RawModel quad;

	public NanoWindowManager(Window window) {
		this.window = window;
		windows = new ArrayList<>();
		fbos = new NVGLUFramebuffer[2];
		fbos[0] = nvgluCreateFramebuffer(window.getNVGID(), (int) (window.getWidth() * window.getPixelRatio()),
				(int) (window.getHeight() * window.getPixelRatio()), NVG_IMAGE_PREMULTIPLIED);
		fbos[1] = nvgluCreateFramebuffer(window.getNVGID(), (int) (window.getWidth() * window.getPixelRatio()),
				(int) (window.getHeight() * window.getPixelRatio()), NVG_IMAGE_PREMULTIPLIED);
		float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1 };
		quad = window.getResourceLoader().loadToVAO(positions, 2);
		shader = new WindowManagerShader();
		shader.start();
		shader.loadTransformation(Maths.createTransformationMatrix(new Vector2d(0, 0), new Vector2d(1, 1)));
		shader.stop();
	}

	@Override
	public void render() {

		// TODO: OPTIMIZE THIS!
		for (IWindow window : windows) {
			window.render(this.window, this);
		}
		glDisable(GL_BLEND);
		for (IWindow window : windows) {
			NVGLUFramebuffer tmp = fbos[0];
			fbos[0] = fbos[1];
			fbos[1] = tmp;
			nvgluBindFramebuffer(this.window.getNVGID(), fbos[0]);
			shader.start();
			glBindVertexArray(quad.getVaoID());
			glEnableVertexAttribArray(0);
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, fbos[1].texture());
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, window.getFBO().texture());
			glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getVertexCount());
			glDisableVertexAttribArray(0);
			glBindVertexArray(0);
			shader.stop();
			nvgluBindFramebuffer(this.window.getNVGID(), null);
		}
		this.window.beingNVGFrame();
		NRendering.renderImage(this.window.getNVGID(), 0, 0, fbos[0].image(), 1f);
		this.window.endNVGFrame();
	}

	@Override
	public void update(float delta) {
		List<IWindow> toRemove = new ArrayList<>();
		IWindow toTop = null;
		for (IWindow window : windows) {
			if (window.shouldClose()) {
				window.dispose(this.window);
				toRemove.add(window);
				continue;
			}
			if (window.insideWindow() && Mouse.isButtonDown(0))
				toTop = window;
		}
		windows.removeAll(toRemove);
		if (toTop != null) {
			IWindow top = windows.get(windows.size() - 1);
			if (top != toTop)
				if (!top.isAlwaysOnTop()) {
					windows.remove(toTop);
					windows.add(toTop);
				} else
					toTop.update(delta, window, this);
		}

		if (!windows.isEmpty()) {
			windows.get(windows.size() - 1).update(delta, window, this);
		}

	}

	@Override
	public void dispose() {
		nvgluDeleteFramebuffer(window.getNVGID(), fbos[0]);
		nvgluDeleteFramebuffer(window.getNVGID(), fbos[1]);
		shader.dispose();
		for (IWindow window : windows) {
			window.dispose(this.window);
		}
	}

	public List<IWindow> getWindows() {
		return windows;
	}

	@Override
	public void addWindow(IWindow window) {
		window.init(this.window);
		window.update(0, this.window, this);
		this.windows.add(window);
	}

	@Override
	public void removeWindow(IWindow window) {
		window.dispose(this.window);
		this.windows.remove(window);
	}

}
