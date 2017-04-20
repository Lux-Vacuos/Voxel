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

import static net.luxvacuos.voxel.universal.core.GlobalVariables.REGISTRY;
import static org.lwjgl.nanovg.NanoVG.*;

import java.util.ArrayList;
import java.util.List;

import net.luxvacuos.voxel.client.input.Mouse;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering;
import net.luxvacuos.voxel.client.util.Maths;

public class ScrollArea extends Component {

	private List<Component> components = new ArrayList<>();

	protected Root root;
	protected float maxW, maxH, scrollW, scrollH;

	private boolean moveV;

	public ScrollArea(float x, float y, float w, float h, float maxW, float maxH) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.maxW = maxW;
		this.maxH = maxH;
		root = new Root(x, y - h, w, h);
	}

	@Override
	public void render(Window window) {
		NRendering.renderBox(window.getNVGID(), rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h,
				NRendering.rgba(0.6f, 0.6f, 0.6f, 0f, NRendering.colorB));
		nvgSave(window.getNVGID());
		nvgScissor(window.getNVGID(), rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h);
		for (Component component : components) {
			component.render(window);
		}
		nvgRestore(window.getNVGID());
		NRendering.renderScrollBarV(window.getNVGID(), rootComponent.rootX + alignedX,
				window.getHeight() - rootComponent.rootY - alignedY - h, w, h, scrollH / maxH, maxH);
	}

	@Override
	public void update(float delta, Window window) {
		if (Mouse.isButtonDown(0)) {
			float scrollBarSize = (float) REGISTRY.getRegistryItem("/Voxel/Settings/WindowManager/scrollBarSize");
			if (Mouse.getX() > rootComponent.rootX + alignedX + w - scrollBarSize
					&& Mouse.getX() < rootComponent.rootX + alignedX + w
					&& Mouse.getY() > rootComponent.rootY + alignedY + h - scrollBarSize
					&& Mouse.getY() < rootComponent.rootY + alignedY + h) {
				scrollH -= 200 * delta;
			}
			if (Mouse.getX() > rootComponent.rootX + alignedX + w - scrollBarSize
					&& Mouse.getX() < rootComponent.rootX + alignedX + w
					&& Mouse.getY() > rootComponent.rootY + alignedY
					&& Mouse.getY() < rootComponent.rootY + alignedY + scrollBarSize) {
				scrollH += 200 * delta;
			}
			if ((Mouse.isButtonDown(0) && scrollBarV(scrollBarSize)) || moveV) {
				moveV = Mouse.isButtonDown(0);
				scrollH -= Mouse.getDY() * 2f;
			}
		}
		scrollH -= Mouse.getDWheel() * 16;
		scrollH = Maths.clamp(scrollH, 0, maxH);
		for (Component component : components) {
			component.update(delta, window);
		}
		super.update(delta, window);
	}

	@Override
	public void alwaysUpdate(float delta, Window window) {
		super.alwaysUpdate(delta, window);
		root.rootX = rootComponent.rootX + alignedX;
		root.rootY = rootComponent.rootY - alignedY - h + h + scrollH;
		root.rootW = w;
		root.rootH = h;
		for (Component component : components) {
			component.alwaysUpdate(delta, window);
		}
	}

	@Override
	public void dispose() {
		for (Component component : components) {
			component.dispose();
		}
		components.clear();
		super.dispose();
	}

	private boolean scrollBarV(float scrollBarSize) {
		float scrollv = (h / maxH) * (h / 2);
		return Mouse.getX() > rootComponent.rootX + alignedX + w - scrollBarSize
				&& Mouse.getX() < rootComponent.rootX + alignedX + w - scrollBarSize + scrollBarSize
				&& Mouse.getY() > rootComponent.rootY + alignedY + scrollBarSize
						+ (h - 8 - scrollv) * (1 - (scrollH / maxH))
				&& Mouse.getY() < rootComponent.rootY + alignedY + scrollBarSize
						+ (h - 8 - scrollv) * (1 - (scrollH / maxH)) + scrollv - scrollBarSize * 2f + 8;
	}

	public void addComponent(Component component) {
		component.rootComponent = root;
		component.init();
		components.add(component);
	}

}
