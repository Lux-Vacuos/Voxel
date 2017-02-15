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

import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_CENTER;
import static org.lwjgl.nanovg.NanoVG.NVG_ALIGN_MIDDLE;

import java.io.File;

import net.luxvacuos.voxel.client.core.ClientVariables;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.EditBox;
import net.luxvacuos.voxel.client.ui.RootComponent;
import net.luxvacuos.voxel.client.ui.Text;

public class CreateWorldMenu extends RootComponent {

	public CreateWorldMenu(float x, float y, float w, float h) {
		super(x, y, w, h, "Create World");
	}

	@Override
	public void initApp(Window window) {
		super.setAlwaysOnTop(true);
		super.setResizable(false);
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);

		EditBox nameB = new EditBox(0, 0, 300, 30, "");
		nameB.setAlignment(Alignment.CENTER);
		nameB.setWindowAlignment(Alignment.CENTER);
		
		Button create = new Button(0, 40, 200, 40, "Create");
		create.setAlignment(Alignment.CENTER);
		create.setWindowAlignment(Alignment.BOTTOM);
		create.setOnButtonPress(() -> {
			new File(ClientVariables.WORLD_PATH + nameB.getText()).mkdirs();
			super.closeWindow();
		});
		
		Text text = new Text("Name", 0, 80);
		text.setAlign(NVG_ALIGN_CENTER | NVG_ALIGN_MIDDLE);
		text.setWindowAlignment(Alignment.CENTER);

		super.addComponent(nameB);
		super.addComponent(create);
		super.addComponent(text);

		window.getKeyboardHandler().enableTextInput();
		super.initApp(window);
	}

	@Override
	public void disposeApp(Window window) {
		super.disposeApp(window);
		window.getKeyboardHandler().disableTextInput();
	}

}
