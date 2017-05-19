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
import net.luxvacuos.voxel.client.rendering.api.nanovg.themes.Theme;

public class Spinner extends Component {

	private float progress = 0;
	private float r;

	public Spinner(float x, float y, float r) {
		this.x = x;
		this.y = y;
		this.r = r;
		this.w = r;
		this.h = r;
	}

	@Override
	public void render(Window window) {
		Theme.renderSpinner(window.getNVGID(), rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, r, progress);
	}

	@Override
	public void update(float delta, Window window) {
		super.update(delta, window);
		progress += 1 * delta;
	}
	
	@Override
	public void setAlignment(Alignment alignment) {
		throw new UnsupportedOperationException("Not Available");
	}

}
