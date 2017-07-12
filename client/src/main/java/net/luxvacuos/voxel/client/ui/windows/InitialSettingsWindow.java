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

package net.luxvacuos.voxel.client.ui.windows;

import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.LANG;
import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.REGISTRY;

import net.luxvacuos.lightengine.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.lightengine.client.rendering.api.glfw.Window;
import net.luxvacuos.lightengine.client.ui.Alignment;
import net.luxvacuos.lightengine.client.ui.Button;
import net.luxvacuos.lightengine.client.ui.ComponentWindow;
import net.luxvacuos.lightengine.client.ui.TextArea;
import net.luxvacuos.lightengine.client.ui.windows.Profiler;
import net.luxvacuos.lightengine.universal.core.TaskManager;
import net.luxvacuos.lightengine.universal.util.registry.Key;

public class InitialSettingsWindow extends ComponentWindow {

	public InitialSettingsWindow(float x, float y, float w, float h) {
		super(x, y, w, h, LANG.getRegistryItem("voxel.initialsettingswindow.name"));
	}

	@Override
	public void initApp(Window window) {
		super.setBackgroundColor(0.4f, 0.4f, 0.4f, 1f);
		super.setResizable(false);
		super.setCloseButton(false);

		TextArea text = new TextArea(LANG.getRegistryItem("voxel.initialsettingswindow.txtinfo"), 100, -100, w - 200);
		text.setWindowAlignment(Alignment.LEFT_TOP);
		super.addComponent(text);

		Button next = new Button(0, 20, 200, 40, LANG.getRegistryItem("voxel.initialsettingswindow.btnnext"));
		next.setAlignment(Alignment.TOP);
		next.setWindowAlignment(Alignment.BOTTOM);
		next.setOnButtonPress(() -> {
			super.closeWindow();
			int ww = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width"));
			int wh = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height"));
			int x = ww / 2 - 512;
			int y = wh / 2 - 300;
			TaskManager.addTask(
					() -> GraphicalSubsystem.getWindowManager().addWindow(new MainWindow(x, wh - y, 1024, 600)));
			TaskManager.addTask(() -> GraphicalSubsystem.getWindowManager().addWindow(new Profiler()));
			TaskManager.addTask(() -> GraphicalSubsystem.getWindowManager().toggleShell());
		});
		super.addComponent(next);

		super.initApp(window);
	}

}
