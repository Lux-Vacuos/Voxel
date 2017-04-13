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

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;

public class ScrollPaneElement {

	private List<Component> components = new ArrayList<>();

	protected Root root;

	public ScrollPaneElement(float x, float y, float w, float h) {
		root = new Root(x, y - h, w, h);
	}

	public void render(Window window) {
		for (Component component : components) {
			component.render(window);
		}
	}

	public void update(float delta, Window window) {
		for (Component component : components) {
			component.update(delta, window);
		}
	}
	
	public void alwaysUpdate(float delta, Window window) {
		for (Component component : components) {
			component.alwaysUpdate(delta, window);
		}
	}

	public void dispose() {
		for (Component component : components) {
			component.dispose();
		}
		components.clear();
	}

	public void addComponent(Component component) {
		component.rootComponent = root;
		component.init();
		components.add(component);
	}

	public void setX(float x) {
		root.rootX = x;
	}

	public void setY(float y) {
		root.rootY = y;
	}

}
