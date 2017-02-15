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

package net.luxvacuos.voxel.client.ui.menus;

import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.opengl.Renderer;
import net.luxvacuos.voxel.client.ui.Image;
import net.luxvacuos.voxel.client.ui.RootComponent;

public class GameWindow extends RootComponent {

	public GameWindow(float x, float y, float w, float h) {
		super(x, y, w, h, "Voxel");
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor(0.0f, 0.0f, 0.0f, 1f);
		super.setResizable(false);
		super.setDecorations(false);
		super.setAsBackground(true);

		Image game = new Image(0, 0, appW, appH, Renderer.getResultTexture());

		super.addComponent(game);

		super.initApp(window);
	}

}
