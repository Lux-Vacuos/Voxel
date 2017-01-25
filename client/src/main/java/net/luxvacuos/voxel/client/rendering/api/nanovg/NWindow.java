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
import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.BackgroundStyle;

public class NWindow {

	public enum WindowClose {
		DISPOSE, DO_NOTHING
	};

	private String title, font = "Roboto-Bold";
	private boolean draggable = true, decorations = true, resizable = true, maximized, hidden = false;
	private BackgroundStyle backgroundStyle = BackgroundStyle.SOLID;
	private NVGColor backgroundColor = UIRendering.rgba(0, 0, 0, 255);
	private float x, y, w, h, oldX, oldY, oldW, oldH;
	private WindowClose windowClose = WindowClose.DISPOSE;
	private OnRender onRender;
	private OnUpdate onUpdate;
	protected boolean exit, focus; // TODO: Implement Focus

	public NWindow(float x, float y, float w, float h, String title) {
		this.title = title;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public void render(long windowID, NWM nwm) {
		if (!hidden) {
			NRendering.renderWindow(windowID, title, font, x, WindowManager.getWindow(windowID).getHeight() - y, w, h,
					backgroundStyle, backgroundColor, decorations, resizable);
			onRender.render(windowID);
		}
	}

	public void update(float delta, long windowID, NWM nwm) {
		if (decorations || hidden) {
			if (Mouse.isButtonDown(0)) {
				if (Mouse.getX() > x && Mouse.getY() < y && Mouse.getX() < x + w && Mouse.getY() > y - 32 && draggable
						&& !maximized) {
					x += Mouse.getDX();
					y += Mouse.getDY();
				}
				if (Mouse.getX() > x + w - 31 && Mouse.getY() < y - 2 && Mouse.getX() < x + w - 2
						&& Mouse.getY() > y - 31) {
					switch (windowClose) {
					case DISPOSE:
						exit = true;
						break;
					case DO_NOTHING:
						break;
					}
				}
				if (Mouse.getX() > x + w - 62 && Mouse.getY() < y - 2 && Mouse.getX() < x + w - 33
						&& Mouse.getY() > y - 31 && resizable) {
					maximized = !maximized;
					if (maximized) {
						oldX = x;
						oldY = y;
						oldW = w;
						oldH = h;
						x = 0;
						y = WindowManager.getWindow(windowID).getHeight();
						w = WindowManager.getWindow(windowID).getWidth();
						h = WindowManager.getWindow(windowID).getHeight();
					} else {
						x = oldX;
						y = oldY;
						w = oldW;
						h = oldH;
					}
				}
				if (Mouse.getX() > x + w - 20 && Mouse.getY() < y - h + 20 && Mouse.getX() < x + w + 20
						&& Mouse.getY() > y - h - 20 && resizable) {
					w += Mouse.getDX();
					h -= Mouse.getDY();
				}
			}
		}
		onUpdate.update(delta, windowID, this);
	}

	public void dispose() {
	}

	public boolean insideWindow() {
		return Mouse.getX() > x && Mouse.getX() < x + w && Mouse.getY() > y - h && Mouse.getY() < y;
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	public void setDecorations(boolean decorations) {
		this.decorations = decorations;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
	}

	public void setBackgroundStyle(BackgroundStyle backgroundStyle) {
		this.backgroundStyle = backgroundStyle;
	}

	public BackgroundStyle getBackgroundStyle() {
		return backgroundStyle;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setWindowClose(WindowClose windowClose) {
		this.windowClose = windowClose;
	}

	public void setOnRender(OnRender onRender) {
		this.onRender = onRender;
	}
	
	public void setOnUpdate(OnUpdate onUpdate) {
		this.onUpdate = onUpdate;
	}

	public void setBackgroundColor(float r, float g, float b, float a) {
		backgroundColor.r(r);
		backgroundColor.g(g);
		backgroundColor.b(b);
		backgroundColor.a(a);
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public float getWidth() {
		return w;
	}

	public float getHeight() {
		return h;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public boolean isDecorations() {
		return decorations;
	}

	public boolean isResizable() {
		return resizable;
	}

	public boolean isDraggable() {
		return draggable;
	}

}
