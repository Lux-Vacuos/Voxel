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

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;

public class Container extends Component {

	protected RootComponent comp;

	public Container(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		comp = new RootComponent(x, y - h, w, h);
	}

	@Override
	public void render(Window window) {
		comp.render(window);
	}

	@Override
	public void update(float delta, Window window) {
		comp.update(delta, window);
		super.update(delta, window);
	}

	@Override
	public void alwaysUpdate(float delta, Window window) {
		super.alwaysUpdate(delta, window);
		comp.alwaysUpdate(delta, window, rootComponent.rootX + alignedX, rootComponent.rootY + alignedY + h, w, h);
	}

	@Override
	public void dispose() {
		comp.dispose();
		super.dispose();
	}

	public void setLayout(ILayout layout) {
		comp.setLayout(layout);
	}

	public void addComponent(Component component) {
		comp.addComponent(component);
	}
	
	public void removeComponent(Component component){
		comp.removeComponent(component);
	}

}
