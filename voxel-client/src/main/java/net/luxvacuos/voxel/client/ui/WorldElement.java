/*
 * This file is part of Voxel
 * 
 * Copyright (C) 2016-2018 Lux Vacuos
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

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_LEFT;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_TOP;

import net.luxvacuos.lightengine.client.rendering.glfw.Window;
import net.luxvacuos.lightengine.client.ui.Alignment;
import net.luxvacuos.lightengine.client.ui.Container;
import net.luxvacuos.lightengine.client.ui.Text;
import net.luxvacuos.voxel.client.core.ClientVariables;

public class WorldElement extends Container {

	private String name;

	public WorldElement(float w, float h, String name) {
		super(0, 0, w, h);
		this.name = name;
	}

	@Override
	public void init(Window window) {
		super.init(window);
		WorldButton btn = new WorldButton(0, 0, w, h, name);
		btn.setOnButtonPress(() -> {
			ClientVariables.worldNameToLoad = name;
		});
		Text txt = new Text(name, 0, 0);
		txt.setAlign(NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		txt.setWindowAlignment(Alignment.LEFT_TOP);
		super.addComponent(btn);
		super.addComponent(txt);
	}

}
