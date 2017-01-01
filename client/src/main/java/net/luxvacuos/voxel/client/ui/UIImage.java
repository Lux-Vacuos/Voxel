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

import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;

public class UIImage extends UIComponent {

	private int image;
	private OnAction onUpdate;

	public UIImage(float x, float y, float w, float h, int image) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		this.image = image;
	}

	@Override
	public void render(long windowID) {
		UIRendering.renderImage(windowID, rootX + x, WindowManager.getWindow(windowID).getHeight() - rootY - y,
				width, height, image, fadeAlpha);
		super.render(windowID);
	}

	@Override
	public void update(float delta) {
		if (onUpdate != null)
			onUpdate.onAction(this, delta);
		super.update(delta);
	}

	public void setOnUpdate(OnAction onUpdate) {
		this.onUpdate = onUpdate;
	}

}
