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

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering;
import net.luxvacuos.voxel.client.util.Maths;

public class ScrollPane extends Component {

	private float scroll = 0f;
	private float cardW, cardH;

	private int colls = 3;
	private List<ScrollPaneElement> elements;

	public ScrollPane(float x, float y, float w, float h, float cardW, float cardH) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.cardW = cardW;
		this.cardH = cardH;
		elements = new ArrayList<>();
	}

	@Override
	public void render(Window window) {
		NRendering.renderScrollPane(window.getNVGID(), rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h, scroll, colls, cardW, cardH, elements,
				window);
	}

	@Override
	public void update(float delta, Window window) {
		if (Mouse.isButtonDown(0)) {
			if (Mouse.getX() > rootComponent.rootX + alignedX + w - 14
					&& Mouse.getX() < rootComponent.rootX + alignedX + w && Mouse.getY() > rootComponent.rootY + alignedY + h - 14 && Mouse.getY() < rootComponent.rootY + alignedY + h) {
				scroll -= 1 * delta * 4;
			}
			if (Mouse.getX() > rootComponent.rootX + alignedX + w - 14
					&& Mouse.getX() < rootComponent.rootX + alignedX + w && Mouse.getY() > rootComponent.rootY + alignedY && Mouse.getY() < rootComponent.rootY + alignedY + 14) {
				scroll += 1 * delta * 4;
			}
		}
		scroll -= Mouse.getDWheel() * delta * 4;
		scroll = Maths.clamp(scroll, 0, 1f);
		for (ScrollPaneElement scrollPaneElement : elements) {
			scrollPaneElement.update(delta, window);
		}
		super.update(delta, window);
	}

	@Override
	public void dispose() {
		for (ScrollPaneElement scrollPaneElement : elements) {
			scrollPaneElement.dispose();
		}
		super.dispose();
	}

	public void setColls(int colls) {
		this.colls = colls;
	}

	public void addElement(ScrollPaneElement component) {
		elements.add(component);
	}

}
