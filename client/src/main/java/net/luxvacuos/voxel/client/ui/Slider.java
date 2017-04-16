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

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering;
import net.luxvacuos.voxel.client.util.Maths;

public class Slider extends Component {

	private float pos, precision;
	private OnAction onPress;
	private boolean customPrecision;

	public Slider(float x, float y, float w, float h, float position) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.pos = position;
	}

	@Override
	public void update(float delta, Window window) {
		if (insideSlider())
			if (Mouse.isButtonDown(0)) {
				pos = (Mouse.getX() - rootComponent.rootX - alignedX) / w;
				if (customPrecision)
					pos = (float) (Math.floor(pos * precision) / precision);
				pos = Maths.clamp(pos, 0, 1);
				if (onPress != null)
					onPress.onAction();
			}
		super.update(delta, window);
	}

	@Override
	public void render(Window window) {
		NRendering.renderSlider(window.getNVGID(), pos, rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h);
	}

	public boolean insideSlider() {
		return Mouse.getX() > rootComponent.rootX + alignedX - 6 && Mouse.getY() > rootComponent.rootY + alignedY
				&& Mouse.getX() < rootComponent.rootX + alignedX + w + 6
				&& Mouse.getY() < rootComponent.rootY + alignedY + h;
	}

	public void setOnPress(OnAction onPress) {
		this.onPress = onPress;
	}

	public void useCustomPrecision(boolean customPrecision) {
		this.customPrecision = customPrecision;
	}

	public void setPrecision(float precision) {
		this.precision = precision;
	}

	public float getPosition() {
		return pos;
	}

}
