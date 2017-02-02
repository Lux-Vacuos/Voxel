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

import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.BackgroundStyle;

public abstract class NanoWindow implements IWindow {

	private boolean draggable = true, decorations = true, resizable = true, maximized, hidden = false, exit,
			alwaysOnTop;
	private BackgroundStyle backgroundStyle = BackgroundStyle.SOLID;
	private NVGColor backgroundColor = NRendering.rgba(0, 0, 0, 255);
	private float x, y, w, h;
	protected float appX, appY, appW, appH;
	private float oldX, oldY, oldW, oldH;
	private WindowClose windowClose = WindowClose.DISPOSE;
	private ITitleBar titleBar;

	public NanoWindow(float x, float y, float w, float h, String title) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		titleBar = new TitleBar(title, 30);
		titleBar.setOnMaximize((window) -> {
			if (resizable) {
				maximized = !maximized;
				if (maximized) {
					oldX = this.x;
					oldY = this.y;
					oldW = this.w;
					oldH = this.h;
					this.x = 0;
					this.y = window.getHeight();
					this.w = window.getWidth();
					this.h = window.getHeight();
				} else {
					this.x = oldX;
					this.y = oldY;
					this.w = oldW;
					this.h = oldH;
				}
			}
		});
		titleBar.setOnExit((window) -> {
			onClose();
			closeWindow();
		});
		titleBar.setOnDrag((window) -> {
			if (draggable && !maximized) {
				this.x += Mouse.getDX();
				this.y += Mouse.getDY();
			}
		});
	}

	@Override
	public void render(Window window, IWindowManager nanoWindowManager) {
		if (!hidden) {
			NRendering.renderWindow(window.getNVGID(), x, window.getHeight() - y, w, h, backgroundStyle,
					backgroundColor, decorations, titleBar.isEnabled());
			if (decorations)
				titleBar.render(window, this);
			nvgSave(window.getNVGID());
			nvgScissor(window.getNVGID(), appX, window.getHeight() - appY, appW, appH);
			renderApp(window);
			nvgRestore(window.getNVGID());
		}
	}

	@Override
	public void update(float delta, Window window, IWindowManager nanoWindowManager) {
		if (decorations && !hidden) {
			titleBar.update(window, this);
			if (Mouse.isButtonDown(0)) {
				if (Mouse.getX() > x + w - 20 && Mouse.getY() < y - h + 20 && Mouse.getX() < x + w + 20
						&& Mouse.getY() > y - h - 20 && resizable && !maximized) {
					w += Mouse.getDX();
					h -= Mouse.getDY();
				}
			}
		}
		if (titleBar.isEnabled()) {
			appX = x + 2;
			appY = y - 4 - titleBar.getH();
			appW = w - 4;
			appH = h - 4 - titleBar.getH() - 2;
		} else {
			appX = x + 2;
			appY = y - 2;
			appW = w - 4;
			appH = h - 4;
		}
		updateApp(delta, window);
	}

	@Override
	public void dispose(Window window) {
		disposeApp(window);
	}

	@Override
	public void onClose() {
	}

	@Override
	public boolean insideWindow() {
		return Mouse.getX() > x && Mouse.getX() < x + w && Mouse.getY() > y - h && Mouse.getY() < y;
	}

	@Override
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	@Override
	public void setDecorations(boolean decorations) {
		this.decorations = decorations;
	}

	@Override
	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	@Override
	public void setBackgroundStyle(BackgroundStyle backgroundStyle) {
		this.backgroundStyle = backgroundStyle;
	}

	@Override
	public void setWindowClose(WindowClose windowClose) {
		this.windowClose = windowClose;
	}

	@Override
	public void setBackgroundColor(float r, float g, float b, float a) {
		backgroundColor.r(r);
		backgroundColor.g(g);
		backgroundColor.b(b);
		backgroundColor.a(a);
	}

	@Override
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	@Override
	public void setAlwaysOnTop(boolean alwaysOnTop) {
		this.alwaysOnTop = alwaysOnTop;
	}

	@Override
	public void toggleTitleBar() {
		this.titleBar.setEnabled(!this.titleBar.isEnabled());
	}

	@Override
	public BackgroundStyle getBackgroundStyle() {
		return backgroundStyle;
	}

	@Override
	public float getWidth() {
		return w;
	}

	@Override
	public float getHeight() {
		return h;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public boolean hasDecorations() {
		return decorations;
	}

	@Override
	public boolean isResizable() {
		return resizable;
	}

	@Override
	public boolean isDraggable() {
		return draggable;
	}

	@Override
	public boolean shouldClose() {
		return exit;
	}

	@Override
	public boolean isAlwaysOnTop() {
		return alwaysOnTop;
	}

	@Override
	public void closeWindow() {
		switch (windowClose) {
		case DISPOSE:
			exit = true;
			break;
		case DO_NOTHING:
			break;
		case HIDE:
			hidden = true;
			break;
		}
	}

}
