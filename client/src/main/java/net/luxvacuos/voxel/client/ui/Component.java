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
	protected float x, y;
	protected float rootX, rootY;
	protected float rootW, rootH;
	protected float width, height;
	protected float fadeAlpha = 1;
	protected boolean enabled = true;
	protected boolean positionRelativeToRoot = true;

	public Component() {
		childrens = new ConcurrentLinkedQueue<>();
	}

	public void render(long windowID) {
		if (enabled)
			for (Component component : childrens) {
				if (component.positionRelativeToRoot) {
					component.rootX = x + rootX;
					component.rootY = y + rootY;
					component.rootW = width;
					component.rootH = height;
				}
				component.fadeAlpha = fadeAlpha;
				component.render(windowID);
			}
	}

	public void update(float delta) {
		if (enabled) {
			Maths.clamp(fadeAlpha, 0, 1);
			for (Component component : childrens) {
				component.update(delta);
			}
		}
	}

	public void addChildren(Component comp) {
		if (comp.equals(this))
			throw new ComponentAdditionException("Can't add the same object");
		childrens.add(comp);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void addPosition(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}

	public void setSize(float w, float h) {
		this.width = w;
		this.height = h;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
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

	public void setFadeAlpha(float fadeAlpha) {
		this.fadeAlpha = fadeAlpha;
	}

	public float getFadeAlpha() {
		return fadeAlpha;
	}

}
