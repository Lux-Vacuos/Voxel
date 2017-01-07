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

public class UIEditBox extends UIComponent {

	private String text, font = "Poppins-Medium";
	private float fontSize = 20f;

	public UIEditBox(float x, float y, float width, float height, String text) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
	}

	@Override
	public void render(long windowID) {
		if (enabled) {
			UIRendering.renderEditBox(windowID, text, font, rootX + x,
					WindowManager.getWindow(windowID).getHeight() - rootY - y, width, height, fontSize, fadeAlpha);
			super.render(windowID);
		}
		super.render(windowID);
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public void setFont(String font) {
		this.font = font;
	}
	
	public void setText(String text) {
		this.text = text;
	}

}
