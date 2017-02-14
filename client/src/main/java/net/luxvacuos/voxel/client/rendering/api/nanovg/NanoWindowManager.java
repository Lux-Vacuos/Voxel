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

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.effects.Final;
import net.luxvacuos.voxel.client.rendering.api.nanovg.effects.GaussianH;
import net.luxvacuos.voxel.client.rendering.api.nanovg.effects.GaussianV;
import net.luxvacuos.voxel.client.rendering.api.nanovg.effects.MaskBlur;
import net.luxvacuos.voxel.client.rendering.api.opengl.GLUtil;

public class NanoWindowManager implements IWindowManager {

	private List<IWindow> windows;
	private Window window;
	private Composite composite;
	private int width, height;

	public NanoWindowManager(Window window) {
		this.window = window;
		windows = new ArrayList<>();
		width = (int) (window.getWidth() * window.getPixelRatio());
		height = (int) (window.getHeight() * window.getPixelRatio());

		if (width > GLUtil.getTextureMaxSize())
			width = GLUtil.getTextureMaxSize();
		if (height > GLUtil.getTextureMaxSize())
			height = GLUtil.getTextureMaxSize();
		composite = new Composite(window, width, height);
		composite.addEffect(new MaskBlur(width, height));
		composite.addEffect(new GaussianH(width, height));
		composite.addEffect(new GaussianV(width, height));
		composite.addEffect(new Final(width, height));
	}

	@Override
	public void render() {
		for (IWindow window : windows) {
			window.render(this.window, this);
		}
		glDisable(GL_BLEND);
		for (IWindow window : windows) {
			composite.render(window, this.window);
		}
		this.window.beingNVGFrame();
		NRendering.renderImage(this.window.getNVGID(), 0, 0, composite.getFbos()[0].image(), 1f);
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
		composite.dispose(window);
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
