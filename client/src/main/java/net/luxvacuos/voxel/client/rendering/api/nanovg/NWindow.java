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

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.BackgroundStyle;

public abstract class NWindow implements INWindow {

	private String title, font = "Roboto-Bold";
	private boolean draggable = true, decorations = true, resizable = true, maximized, hidden = false, exit,
			alwaysOnTop;
	private BackgroundStyle backgroundStyle = BackgroundStyle.SOLID;
	private NVGColor backgroundColor = NRendering.rgba(0, 0, 0, 255);
	protected float x, y, w, h;
	private float oldX, oldY, oldW, oldH;
	private WindowClose windowClose = WindowClose.DISPOSE;

	public NWindow(float x, float y, float w, float h, String title) {
		this.title = title;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	@Override
	public void render(Window window, NWM nwm) {
		if (!hidden) {
			NRendering.renderWindow(window.getNVGID(), title, font, x, window.getHeight() - y, w, h, backgroundStyle,
					backgroundColor, decorations, WM.invertWindowButtons, resizable);
			renderApp(window);
		}
	}

	@Override
	public void update(float delta, Window window, NWM nwm) {
		if (decorations || hidden) {
			if (Mouse.isButtonDown(0)) {
				if (WM.invertWindowButtons) {
					if (Mouse.getX() > x + 33 && Mouse.getY() < y - 2 && Mouse.getX() < x + 62
							&& Mouse.getY() > y - 31 && resizable) {
						maximized = !maximized;
						if (maximized) {
							oldX = x;
							oldY = y;
							oldW = w;
							oldH = h;
							x = 0;
							y = window.getHeight();
							w = window.getWidth();
							h = window.getHeight();
						} else {
							x = oldX;
							y = oldY;
							w = oldW;
							h = oldH;
						}
					}
					if (Mouse.getX() > x + 2 && Mouse.getY() < y - 2 && Mouse.getX() < x + 32
							&& Mouse.getY() > y - 31) {
						closeWindow();
					}
				} else {
					if (Mouse.getX() > x + w - 62 && Mouse.getY() < y - 2 && Mouse.getX() < x + w - 33
							&& Mouse.getY() > y - 31 && resizable) {
						maximized = !maximized;
						if (maximized) {
							oldX = x;
							oldY = y;
							oldW = w;
							oldH = h;
							x = 0;
							y = window.getHeight();
							w = window.getWidth();
							h = window.getHeight();
						} else {
							x = oldX;
							y = oldY;
							w = oldW;
							h = oldH;
						}
					}
					if (Mouse.getX() > x + w - 31 && Mouse.getY() < y - 2 && Mouse.getX() < x + w - 2
							&& Mouse.getY() > y - 31) {
						closeWindow();
					}
				}
				if (Mouse.getX() > x && Mouse.getY() < y && Mouse.getX() < x + w && Mouse.getY() > y - 32 && draggable
						&& !maximized) {
					x += Mouse.getDX();
					y += Mouse.getDY();
				}
				if (Mouse.getX() > x + w - 20 && Mouse.getY() < y - h + 20 && Mouse.getX() < x + w + 20
						&& Mouse.getY() > y - h - 20 && resizable) {
					w += Mouse.getDX();
					h -= Mouse.getDY();
				}
			}
		}
		updateApp(delta, window);
	}

	@Override
	public void dispose(Window window) {
		disposeApp(window);
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
	public void setTitle(String title) {
		this.title = title;
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
		}
	}

}
