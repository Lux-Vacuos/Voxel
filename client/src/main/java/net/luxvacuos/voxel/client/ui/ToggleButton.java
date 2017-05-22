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

public class ToggleButton extends Button {

	private boolean status = false;
	private OnAction onToggle;

	public ToggleButton(float x, float y, float w, float h, boolean st) {
		super(x, y, w, h, "");
		onPress = () -> {
			status = !status;
			if (onToggle != null)
				onToggle.onAction();
		};
		this.status = st;
	}

	@Override
	public void render(Window window) {
		if (!enabled)
			return;
		Theme.renderToggleButton(window.getNVGID(), text, font, rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h, fontSize, status);
	}

	@Override
	public void setOnButtonPress(OnAction onPress) {
		this.onToggle = onPress;
	}

	public boolean getStatus() {
		return status;
	}
}
