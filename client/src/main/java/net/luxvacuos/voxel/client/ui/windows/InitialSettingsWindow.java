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

import static net.luxvacuos.voxel.universal.core.subsystems.CoreSubsystem.LANG;
import static net.luxvacuos.voxel.universal.core.subsystems.CoreSubsystem.REGISTRY;

import net.luxvacuos.voxel.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.ui.Alignment;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.RootComponentWindow;
import net.luxvacuos.voxel.client.ui.TextArea;
import net.luxvacuos.voxel.universal.core.TaskManager;
import net.luxvacuos.voxel.universal.util.registry.Key;

public class InitialSettingsWindow extends RootComponentWindow {

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
			Shell shell = new Shell(0, 0, (int) REGISTRY.getRegistryItem(new Key("/Voxel/Display/width")), 30);
			TaskManager.addTask(() -> GraphicalSubsystem.getWindowManager().addWindow(shell));
			TaskManager.addTask(() -> GraphicalSubsystem.getWindowManager().setShell(shell));
			float borderSize = (float) REGISTRY.getRegistryItem(new Key("/Voxel/Settings/WindowManager/borderSize"));
			float titleBarHeight = (float) REGISTRY
					.getRegistryItem(new Key("/Voxel/Settings/WindowManager/titleBarHeight"));
			int height = (int) REGISTRY.getRegistryItem(new Key("/Voxel/Display/height"));
			RootComponentWindow mainMenu = new MainWindow(borderSize + 10, height - titleBarHeight - 10,
					(int) REGISTRY.getRegistryItem(new Key("/Voxel/Display/width")) - borderSize * 2f - 20,
					height - titleBarHeight - borderSize - 50);
			TaskManager.addTask(() -> GraphicalSubsystem.getWindowManager().addWindow(mainMenu));
			TaskManager.addTask(() -> GraphicalSubsystem.getWindowManager().toggleShell());
		});
		super.addComponent(next);

		super.initApp(window);
	}

}
