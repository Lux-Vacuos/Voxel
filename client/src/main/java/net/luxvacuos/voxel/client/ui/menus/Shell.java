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

import java.util.HashMap;
import java.util.Map;

import net.luxvacuos.voxel.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.voxel.client.rendering.api.glfw.Window;
import net.luxvacuos.voxel.client.rendering.api.nanovg.IShell;
import net.luxvacuos.voxel.client.rendering.api.nanovg.IWindow;
import net.luxvacuos.voxel.client.ui.Button;
import net.luxvacuos.voxel.client.ui.Component;
import net.luxvacuos.voxel.client.ui.Container;
import net.luxvacuos.voxel.client.ui.Direction;
import net.luxvacuos.voxel.client.ui.FlowLayout;
import net.luxvacuos.voxel.client.ui.RootComponentWindow;
import net.luxvacuos.voxel.universal.core.subsystems.CoreSubsystem;
import net.luxvacuos.voxel.universal.util.registry.Key;

public class Shell extends RootComponentWindow implements IShell {

	private Map<Integer, Component> buttons;
	private Container apps;

	public Shell(float x, float y, float w, float h) {
		super(x, y, w, h, "Shell");
		CoreSubsystem.REGISTRY.register(new Key("/Voxel/Settings/WindowManager/shellHeight"), y);
	}

	@Override
	public void initApp(Window window) {
		buttons = new HashMap<>();
		super.setDecorations(false);
		super.initApp(window);
		super.setBackgroundColor("#1F1F1F78");
		super.setLayout(new FlowLayout(Direction.RIGHT, 0, 0));
		super.setAsBackground(true);
		Container left = new Container(0, 0, 82, 30);
		Button btn = new Button(0, 0, 80, 30, "Start");
		// btn.setColor("#00000000");
		// btn.setHighlightColor("#FFFFFF64");
		// btn.setTextColor("#FFFFFFFF");
		btn.setOnButtonPress(() -> {
		});
		left.addComponent(btn);
		// super.addComponent(left);
		apps = new Container(0, 0, super.w - 100, 30);
		apps.setLayout(new FlowLayout(Direction.RIGHT, 0, 0));
		super.addComponent(apps);
	}

	@Override
	public void disposeApp(Window window) {
		super.disposeApp(window);
		buttons.clear();
		CoreSubsystem.REGISTRY.register(new Key("/Voxel/Settings/WindowManager/shellHeight"), 0f);
	}

	@Override
	public void notifyAdd(IWindow window) {
		if (!(window.hasDecorations() && !window.isHidden()))
			return;
		Button btn = new Button(0, 0, 100, 30, window.getTitle());
		// btn.setColor("#00000000");
		// btn.setHighlightColor("#FFFFFF64");
		// btn.setTextColor("#FFFFFFFF");
		btn.setOnButtonPress(() -> {
			if (!GraphicalSubsystem.getWindowManager().isOnTop(window) && !window.isMinimized()) {
				GraphicalSubsystem.getWindowManager().bringToFront(window);
				return;
			}
			if (window.isMinimized())
				GraphicalSubsystem.getWindowManager().bringToFront(window);
			window.toggleMinimize();
		});
		apps.addComponent(btn);
		buttons.put(window.hashCode(), btn);
	}

	@Override
	public void notifyClose(IWindow window) {
		if (!(window.hasDecorations() && !window.isHidden()))
			return;
		apps.removeComponent(buttons.get(window.hashCode()));
		buttons.remove(window.hashCode());
	}

}
