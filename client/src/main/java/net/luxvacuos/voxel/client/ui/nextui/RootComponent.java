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

package net.luxvacuos.voxel.client.ui.nextui;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.client.rendering.api.nanovg.NWindow;

public class RootComponent extends NWindow {

	private List<Component> components = new ArrayList<>();

	protected float rootX, rootY, rootW, rootH;

	public RootComponent(float x, float y, float w, float h, String title) {
		super(x, y, w, h, title);
		rootX = x + 2;
		rootY = y - h + 2;
		rootW = w - 4;
		rootH = h - 35;
	}

	@Override
	public void initApp() {
		for (Component component : components) {
			component.init();
		}
	}

	@Override
	public void renderApp(long windowID) {
		for (Component component : components) {
			component.render(windowID);
		}
	}

	@Override
	public void updateApp(float delta) {
		rootX = x + 2;
		rootY = y - h + 2;
		rootW = w - 4;
		rootH = h - 35;
		for (Component component : components) {
			component.update(delta);
		}
	}

	@Override
	public void disposeApp() {
		for (Component component : components) {
			component.dispose();
		}
		components.clear();
	}

	public void addComponent(Component component) {
		component.rootComponent = this;
		components.add(component);
	}

}
