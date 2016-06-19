/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016 Lux Vacuos
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

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;

import net.luxvacuos.igl.vector.Vector2f;
import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.VectorsRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

public class Button {
	private Vector2f pos, renderPos;
	private Vector2f size, renderSize;

	public Button(Vector2f pos, Vector2f size) {
		this.pos = new Vector2f(pos.x, pos.y);
		this.size = new Vector2f((pos.x + size.x), (pos.y + size.y));
		renderPos = pos;
		renderSize = size;
	}

	public boolean insideButton() {
		if (Mouse.getX() > pos.getX() && Mouse.getY() > pos.getY() && Mouse.getX() < size.getX()
				&& Mouse.getY() < size.getY())
			return true;
		else
			return false;
	}

	public void render(String text) {
		this.render(text, "Roboto-Regular", "Entypo", null,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
	}

	public void render(String text, ByteBuffer preicon) {
		this.render(text, "Roboto-Regular", "Entypo", preicon,
				VectorsRendering.rgba(255, 255, 255, 255, VectorsRendering.colorA));
	}

	public void render(String text, NVGColor color) {
		this.render(text, "Roboto-Regular", "Entypo", null, color);
	}

	public void render(String text, ByteBuffer preicon, NVGColor color) {
		this.render(text, "Roboto-Regular", "Entypo", preicon, color);
	}

	public void render(String text, String font, String entypo, ByteBuffer preicon, NVGColor color) {
		VectorsRendering.renderButton(preicon, text, font, entypo, (float) (renderPos.x),
				(float) (GameResources.getInstance().getDisplay().getDisplayHeight() - renderPos.y - renderSize.y), (float) (renderSize.x),
				(float) (renderSize.y), color, this.insideButton());
	}

	public boolean pressed() {
		if (insideButton())
			if (Mouse.isButtonDown(0))
				return true;
			else
				return false;
		else
			return false;
	}
}
