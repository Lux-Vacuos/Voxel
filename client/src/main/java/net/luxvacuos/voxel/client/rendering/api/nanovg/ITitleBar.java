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

package net.luxvacuos.voxel.client.rendering.api.nanovg;

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;

public interface ITitleBar {

	public void render(Window window, IWindow iWindow);

	public void update(Window window, IWindow iWindow);

	public void setOnExit(Event onclick);

	public void setOnMaximize(Event onclick);

	public void setOnDrag(Event event);

	public boolean isEnabled();

	public void setEnabled(boolean enabled);
	
	public void setTitle(String title);

	public String getTitle();

}
