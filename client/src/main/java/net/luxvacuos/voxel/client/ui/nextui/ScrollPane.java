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

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering;
import net.luxvacuos.voxel.client.util.Maths;

public class ScrollPane extends Component {

	private float scroll;
	private float cardW, cardH;

	private int items = 0, colls = 3;

	public ScrollPane(float x, float y, float w, float h, float cardW, float cardH) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.cardW = cardW;
		this.cardH = cardH;
	}

	@Override
	public void render(Window window) {
		NRendering.renderScrollPane(window.getNVGID(), rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h, items, scroll, colls, cardW, cardH);
	}

	@Override
	public void update(float delta, Window window) {
		scroll += Mouse.getDWheel();
		scroll = Maths.clamp(scroll, 0, 6.25f);

		super.update(delta, window);
	}

	public void setItems(int items) {
		this.items = items;
	}

	public void setColls(int colls) {
		this.colls = colls;
	}

}
