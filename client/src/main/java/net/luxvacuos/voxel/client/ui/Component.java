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

public abstract class Component implements IComponent {

	protected Root rootComponent;
	protected Alignment alignment = Alignment.RIGHT_TOP, windowAlignment = Alignment.LEFT_BOTTOM;
	protected float alignedX, alignedY, x, y, w, h;

	@Override
	public void init() {
	}
	
	@Override
	public void update(float delta, Window window) {
	}

	@Override
	public void alwaysUpdate(float delta, Window window) {
		// TODO: Optimize this Sh*t
		switch (alignment) {
		case LEFT:
			alignedX = x;
			alignedY = y - h / 2;
			break;
		case CENTER:
			alignedX = x - w / 2;
			alignedY = y - h / 2;
			break;
		case RIGHT:
			alignedX = x - w;
			alignedY = y - h / 2;
			break;
		case BOTTOM:
			alignedX = x - w / 2;
			alignedY = y - h;
			break;
		case TOP:
			alignedX = x - w / 2;
			alignedY = y;
			break;
		case LEFT_BOTTOM:
			alignedX = x - w;
			alignedY = y - h;
			break;
		case LEFT_TOP:
			alignedX = x - w;
			alignedY = y;
			break;
		case RIGHT_BOTTOM:
			alignedX = x;
			alignedY = y - h;
			break;
		case RIGHT_TOP:
			alignedX = x;
			alignedY = y;
			break;
		default:
			throw new UnsupportedOperationException("Invalid Alignment: " + alignment.name());
		}
		switch (windowAlignment) {
		case LEFT:
			alignedX += 2;
			alignedY += rootComponent.rootH / 2;
			break;
		case CENTER:
			alignedX += rootComponent.rootW / 2;
			alignedY += rootComponent.rootH / 2;
			break;
		case RIGHT:
			alignedX += rootComponent.rootW;
			alignedY += rootComponent.rootH / 2;
			break;
		case BOTTOM:
			alignedX += rootComponent.rootW / 2;
			alignedY += 0;
			break;
		case TOP:
			alignedX += rootComponent.rootW / 2;
			alignedY += rootComponent.rootH;
			break;
		case LEFT_BOTTOM:
			alignedX += 0;
			alignedY += 0;
			break;
		case LEFT_TOP:
			alignedX += 0;
			alignedY += rootComponent.rootH;
			break;
		case RIGHT_BOTTOM:
			alignedX += rootComponent.rootW;
			alignedY += 0;
			break;
		case RIGHT_TOP:
			alignedX += rootComponent.rootW;
			alignedY += rootComponent.rootH;
			break;
		default:
			throw new UnsupportedOperationException("Invalid Alignment: " + windowAlignment.name());
		}
	}

	@Override
	public void dispose() {
	}

	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}

	public void setWindowAlignment(Alignment windowAlignment) {
		this.windowAlignment = windowAlignment;
	}

}
