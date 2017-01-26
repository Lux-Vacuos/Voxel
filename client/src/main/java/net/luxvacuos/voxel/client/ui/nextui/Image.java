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

import net.luxvacuos.voxel.client.rendering.api.glfw.WindowManager;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;

public class Image extends Component {

	private int image;

	public Image(float x, float y, float w, float h, int image) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.image = image;
	}

	@Override
	public void render(long windowID) {
		UIRendering.renderImage(windowID, rootComponent.rootX + alignedX,
				WindowManager.getWindow(windowID).getHeight() - rootComponent.rootY - alignedY - h, w, h, image, 1);
		super.render(windowID);
	}

}
