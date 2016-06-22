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

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.luxvacuos.voxel.client.core.exception.ComponentAdditionException;
import net.luxvacuos.voxel.client.util.Maths;

public abstract class Component {

	protected Queue<Component> childrens;
	protected int x, y;
	protected int rootX, rootY;
	protected int rootW, rootH;
	protected int width, height;
	protected float fadeAlpha = 1;
	protected boolean enabled = true;
	protected boolean positionRelativeToRoot = true;

	public Component() {
		childrens = new ConcurrentLinkedQueue<>();
	}

	public void render() {
		if (enabled)
			for (Component component : childrens) {
				if (component.positionRelativeToRoot) {
					component.rootX = x + rootX;
					component.rootY = y + rootY;
					component.rootW = width;
					component.rootH = height;
				}
				component.fadeAlpha = fadeAlpha;
				component.render();
			}
	}

	public void update() {
		Maths.clamp(fadeAlpha, 0, 1);
		if (enabled)
			for (Component component : childrens) {
				component.update();
			}
	}

	public void addChildren(Component comp) {
		if (comp.equals(this))
			throw new ComponentAdditionException("Can't add the same object");
		childrens.add(comp);
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Queue<Component> getChildrens() {
		return childrens;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setPositionRelativeToRoot(boolean positionRelativeToRoot) {
		this.positionRelativeToRoot = positionRelativeToRoot;
	}

	public boolean isPositionRelativeToRoot() {
		return positionRelativeToRoot;
	}

}
