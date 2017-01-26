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

import net.luxvacuos.voxel.client.rendering.api.nanovg.NRendering.BackgroundStyle;
import net.luxvacuos.voxel.universal.resources.IDisposable;

public interface INWindow extends IDisposable {

	public enum WindowClose {
		DISPOSE, DO_NOTHING
	};

	public void initApp();

	public void renderApp(long windowID);

	public void updateApp(float delta);

	public void disposeApp();

	public void render(long windowID, NWM nwm);

	public void update(float delta, long windowID, NWM nwm);

	public boolean insideWindow();

	public void setDraggable(boolean draggable);

	public void setDecorations(boolean decorations);

	public void setResizable(boolean resizable);

	public void setBackgroundStyle(BackgroundStyle backgroundStyle);

	public void setTitle(String title);

	public void setWindowClose(WindowClose windowClose);

	public void setBackgroundColor(float r, float g, float b, float a);

	public void setHidden(boolean hidden);

	public void setAlwaysOnTop(boolean alwaysOnTop);

	public BackgroundStyle getBackgroundStyle();

	public float getWidth();

	public float getHeight();

	public float getX();

	public float getY();

	public boolean hasDecorations();

	public boolean isResizable();

	public boolean isDraggable();

	public boolean shouldClose();

	public void closeWindow();

	public boolean isAlwaysOnTop();

}
