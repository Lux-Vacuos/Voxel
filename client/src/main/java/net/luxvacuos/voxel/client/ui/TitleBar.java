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

package net.luxvacuos.voxel.client.ui;

import static net.luxvacuos.voxel.universal.core.subsystems.CoreSubsystem.REGISTRY;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Event;
import net.luxvacuos.voxel.client.rendering.api.nanovg.IWindow;
import net.luxvacuos.voxel.universal.util.registry.Key;

public class TitleBar implements ITitleBar {

	private boolean enabled = true, dragging, pressed;
	private Event drag;
	private IWindow window;
	private RootComponent left, right, center;
	private float time;
	private boolean count;

	public TitleBar(IWindow window) {
		this.window = window;
		left = new RootComponent(this.window.getX(), this.window.getY(), this.window.getWidth(),
				(float) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/WindowManager/titleBarHeight")));
		right = new RootComponent(this.window.getX(), this.window.getY(), this.window.getWidth(),
				(float) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/WindowManager/titleBarHeight")));
		center = new RootComponent(this.window.getX(), this.window.getY(), this.window.getWidth(),
				(float) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/WindowManager/titleBarHeight")));
	}

	@Override
	public void render(Window window) {
		if (enabled) {
			left.render(window);
			right.render(window);
			center.render(window);
		}
	}

	@Override
	public void update(float delta, Window window) {
		if (enabled) {
			if ((Mouse.isButtonDown(0) && canDrag(this.window)) || dragging) {
				dragging = Mouse.isButtonDown(0);
				drag.event(window);
			}
			if (Mouse.isButtonDown(0) && canDrag(this.window) || pressed) {
				if (!pressed) {
					count = true;
					if (time != 0) {
						this.window.toggleMaximize();
						time = 0;
						count = false;
					}
				}
				pressed = Mouse.isButtonDown(0);
			}
			if (count) {
				time += 1 * delta;
				if (time > 0.5f) {
					count = false;
					time = 0;
				}
			}
			left.update(delta, window);
			right.update(delta, window);
			center.update(delta, window);
		}
	}

	@Override
	public void alwaysUpdate(float delta, Window window) {
		if (enabled) {
			float titleBarHeight = (float) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/WindowManager/titleBarHeight"));
			left.alwaysUpdate(delta, window, this.window.getX(), this.window.getY() + titleBarHeight,
					this.window.getWidth(), titleBarHeight);
			right.alwaysUpdate(delta, window, this.window.getX(), this.window.getY() + titleBarHeight,
					this.window.getWidth(), titleBarHeight);
			center.alwaysUpdate(delta, window, this.window.getX(), this.window.getY() + titleBarHeight,
					this.window.getWidth(), titleBarHeight);
		}
	}

	@Override
	public void setOnDrag(Event event) {
		this.drag = event;
	}

	@Override
	public RootComponent getLeft() {
		return left;
	}

	@Override
	public RootComponent getRight() {
		return right;
	}

	@Override
	public RootComponent getCenter() {
		return center;
	}

	private boolean canDrag(IWindow iWindow) {
		return Mouse.getX() > iWindow.getX()
				&& Mouse.getY() < iWindow.getY()
						+ (float) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/WindowManager/titleBarHeight"))
				&& Mouse.getX() < iWindow.getX() + iWindow.getWidth() && Mouse.getY() > iWindow.getY();
	}

	@Override
	public void dispose() {
		left.dispose();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean isDragging() {
		return dragging;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
