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

package net.luxvacuos.voxel.client.core.states;

import static net.luxvacuos.lightengine.universal.core.subsystems.CoreSubsystem.REGISTRY;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import net.luxvacuos.lightengine.client.core.subsystems.GraphicalSubsystem;
import net.luxvacuos.lightengine.client.rendering.api.opengl.Renderer;
import net.luxvacuos.lightengine.client.ui.windows.BackgroundWindow;
import net.luxvacuos.lightengine.universal.core.TaskManager;
import net.luxvacuos.lightengine.universal.core.states.AbstractState;
import net.luxvacuos.lightengine.universal.core.states.StateMachine;
import net.luxvacuos.lightengine.universal.util.registry.Key;
import net.luxvacuos.voxel.client.core.subsystems.WorldSubsystem;
import net.luxvacuos.voxel.client.ui.windows.InitialSettingsWindow;

/**
 * Main Menu State, this is the menu show after the splash screen fade out.
 * 
 * @author danirod
 */
public class MainMenuState extends AbstractState {
	
	private WorldSubsystem subsystem;

	public MainMenuState() {
		super(StateNames.MAIN_MENU);
	}
	
	@Override
	public void init() {
		subsystem =new WorldSubsystem();
		subsystem.init();
		TaskManager.addTask(() -> {
			StateMachine.registerState(new SPWorldState());
		});
		super.init();
	}
	
	@Override
	public void start() {
		super.start();
		GraphicalSubsystem.getWindowManager().toggleShell();
		GraphicalSubsystem.getWindowManager().addWindow(0,
				new BackgroundWindow(0, (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height")),
						(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width")),
						(int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height"))));
		int ww = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/width"));
		int wh = (int) REGISTRY.getRegistryItem(new Key("/Light Engine/Display/height"));
		int x = ww / 2 - 512;
		int y = wh / 2 - 300;
		GraphicalSubsystem.getWindowManager().addWindow(new InitialSettingsWindow(x, wh - y, 1024, 600));
	}
	
	@Override
	public void dispose() {
		subsystem.dispose();
		super.dispose();
	}

	@Override
	public void render(float delta) {
		Renderer.clearBuffer(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		Renderer.clearColors(1, 1, 1, 1);
		GraphicalSubsystem.getWindowManager().render();
	}

	@Override
	public void update(float delta) {
		GraphicalSubsystem.getWindowManager().update(delta);
	}

}
