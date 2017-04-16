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

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.Event;
import net.luxvacuos.voxel.client.rendering.api.nanovg.IWindow;

public class TitleBar implements ITitleBar {

	private boolean enabled = true, dragging;
	private Event drag;
	private List<Component> components = new ArrayList<>();
	private IWindow window;
	protected Root root;

	public TitleBar(IWindow window) {
		this.window = window;
		root = new Root(this.window.getX(), this.window.getY(), this.window.getWidth(),
				(float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight"));
	}

	@Override
	public void render(Window window) {
		if (enabled) {
			for (Component component : components) {
				component.render(window);
			}
		}
	}

	@Override
	public void update(float delta, Window window) {
		if (enabled) {
			if ((Mouse.isButtonDown(0) && canDrag(this.window)) || dragging) {
				dragging = Mouse.isButtonDown(0);
				drag.event(window);
			}
			for (Component component : components) {
				component.update(delta, window);
			}
		}
	}

	@Override
	public void alwaysUpdate(float delta, Window window) {
		if (enabled) {
			root.rootX = this.window.getX();
			root.rootY = this.window.getY();
			root.rootW = this.window.getWidth();
			root.rootH = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight");
			for (Component component : components) {
				component.alwaysUpdate(delta, window);
			}
		}
	}

	@Override
	public void addComponent(Component component) {
		component.rootComponent = root;
		component.init();
		components.add(component);
	}

	@Override
	public void setOnDrag(Event event) {
		this.drag = event;
	}

	private boolean canDrag(IWindow iWindow) {
		return Mouse.getX() > iWindow.getX()
				&& Mouse.getY() < iWindow.getY()
						+ (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/titleBarHeight")
				&& Mouse.getX() < iWindow.getX() + iWindow.getWidth() && Mouse.getY() > iWindow.getY();
	}

	@Override
	public void dispose() {
		for (Component component : components) {
			component.dispose();
		}
		components.clear();
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
