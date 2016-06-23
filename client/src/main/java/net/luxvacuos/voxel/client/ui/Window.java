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

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.nanovg.UIRendering;
import net.luxvacuos.voxel.client.resources.GameResources;

public class Window extends Component {

	private String title, font = "Roboto-Bold";
	private boolean draggable = false;

	public Window(float x, float y, float w, float h, String title) {
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
	}

	@Override
	public void render() {
		if (enabled) {
			UIRendering.renderWindow(title, font, rootX + x,
					GameResources.getInstance().getDisplay().getDisplayHeight() - rootY - y, width, height, fadeAlpha);
			super.render();
		}
	}

	@Override
	public void update(float delta) {
		if (enabled) {
			if (Mouse.isButtonDown(0) && Mouse.getX() > x && Mouse.getY() < y && Mouse.getX() < x + width
					&& Mouse.getY() > y - 32 && draggable) {
				this.x = Mouse.getX() - width / 2;
				this.y = Mouse.getY() + 32 / 2;
			}
			super.update(delta);
		}
	}

	public boolean fadeIn(float time, float delta) {
		if (fadeAlpha < 1) {
			fadeAlpha += time * delta;
			return false;
		}
		return true;
	}

	public boolean fadeOut(float time, float delta) {
		if (fadeAlpha > 0) {
			fadeAlpha -= time * delta;
			return false;
		}
		return true;
	}

	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}

	public boolean isDraggable() {
		return draggable;
	}

	public void setFadeAlpha(float fadeAlpha) {
		this.fadeAlpha = fadeAlpha;
	}

	public void setFont(String font) {
		this.font = font;
	}

}
